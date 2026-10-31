package dev.daylight.hub

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.data.HubDatabase
import dev.daylight.hub.reading.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.*
import java.security.MessageDigest
import java.util.UUID
import java.util.zip.*

@RunWith(AndroidJUnit4::class)
class ReadingIntegrationTest {
    private val context=ApplicationProvider.getApplicationContext<Context>()
    private val resolver=context.contentResolver
    private fun uri(name:String,bytes:ByteArray):Uri=Uri.parse(resolver.call(Uri.parse("content://dev.daylight.hub.test.books"),"create",name,Bundle().apply{putByteArray("bytes",bytes)})!!.getString("uri"))
    private fun removeSource(uri:Uri){resolver.call(Uri.parse("content://dev.daylight.hub.test.books"),"delete",uri.lastPathSegment,null)}
    private fun database()=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
    private fun clean(repository:ReadingRepository,book:Book){val directory=repository.file(book.fileName).parentFile!!;directory.listFiles()?.forEach{if(it.isFile)it.delete()};directory.delete()}

    @Test fun openWithIncludesAzw3AndExcludesUnsupportedDjvu() {
        fun resolves(mime:String)=context.packageManager.resolveActivity(android.content.Intent(android.content.Intent.ACTION_VIEW)
            .setPackage(context.packageName).setDataAndType(Uri.parse("content://fixture/book"),mime),android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
        assertNotNull(resolves("application/vnd.amazon.mobi8-ebook"));assertNotNull(resolves("application/epub+zip"));assertNull(resolves("image/vnd.djvu"))
    }

    @Test fun privateCopyDuplicatesBookmarksRepairAndSourceRemoval()=runBlocking {
        val db=database();val repo=ReadingRepository(context,db);val name="copy-${UUID.randomUUID()}.txt"
        val source=uri(name,"Private import ${UUID.randomUUID()}\nA short readable chapter.".toByteArray())
        var item:Book?=null
        try {
            val first=repo.import(source);item=first
            assertEquals("EPUB",first.format);assertTrue(repo.file(first.fileName).isFile);assertTrue(repo.file(first.normalizedFile!!).isFile)
            val location=BookPosition(first.id,locator="epubcfi(/6/2!/4/2:12)",fraction=.25)
            repo.savePosition(location);repo.bookmark(location,"Keep this")
            val duplicate=repo.import(source)
            assertEquals(first.id,duplicate.id);assertEquals(.25,db.reading().position(first.id)!!.fraction,0.0)
            assertEquals("Keep this",db.reading().bookmarks(first.id).first().single().label)
            repo.file(first.normalizedFile).delete()
            val repaired=repo.import(source);assertEquals(first.id,repaired.id);assertTrue(repo.file(repaired.normalizedFile!!).isFile)
            removeSource(source)
            assertEquals("EPUB",BookFiles.inspect(repo.file(repaired.normalizedFile),"book.epub",null).format)
            val current=db.reading().book(first.id)!!;repo.remove(current);repo.restore(first.id,current.revision+1)
            assertNull(db.reading().book(first.id)!!.deletedAt);assertEquals(.25,db.reading().position(first.id)!!.fraction,0.0)
        }finally{removeSource(source);item?.let{clean(repo,it)};db.close()}
    }
    @Test fun failedAndCancelledImportsLeaveNoNewPrivateCopies()=runBlocking {
        val db=database();val repo=ReadingRepository(context,db)
        val bytes="Unsupported ${UUID.randomUUID()}".toByteArray();val source=uri("invalid-${UUID.randomUUID()}.bin",bytes)
        val hash=MessageDigest.getInstance("SHA-256").digest(bytes).joinToString(""){"%02x".format(it)}
        val largeName="cancel-${UUID.randomUUID()}.txt"
        val large=Uri.parse(resolver.call(Uri.parse("content://dev.daylight.hub.test.books"),"generate",largeName,Bundle().apply{putInt("bytes",4*1024*1024)})!!.getString("uri"))
        try {
            assertTrue(runCatching{repo.import(source)}.isFailure);assertNull(db.reading().byHash(hash));assertFalse(repo.file("books/$hash/original").exists())
            lateinit var importJob:Job
            importJob=launch(start=CoroutineStart.LAZY){repo.import(large){if(it.bytesCopied>=1024*1024)importJob.cancel()}}
            importJob.start();importJob.join();assertTrue(importJob.isCancelled)
            db.openHelper.readableDatabase.query("SELECT COUNT(*) FROM books").use{it.moveToFirst();assertEquals(0,it.getInt(0))}
            assertTrue(File(context.filesDir,"books").listFiles().orEmpty().none{it.name.startsWith("import-") && it.name.endsWith(".part")})
        } finally{removeSource(source);removeSource(large);db.close()}
    }
    @Test fun interruptedImportsCleanOnlyStagingAndRetainCompleteOriginals()=runBlocking {
        val root=File(context.cacheDir,"recovery-${UUID.randomUUID()}").apply{mkdirs()}
        val isolated=object:android.content.ContextWrapper(context){override fun getFilesDir()=root}
        val db=database();val repo=ReadingRepository(isolated,db)
        val id=UUID.randomUUID().toString();val hash="a".repeat(64)
        val staging=repo.file("books/import-$id.part").apply{parentFile!!.mkdirs();writeText("Partial")}
        val original=repo.file("books/$hash/original").apply{parentFile!!.mkdirs();writeText("Original bytes")}
        val conversion=repo.file("books/$hash/normalized-v1.epub.part").apply{writeText("Partial conversion")}
        try {
            db.reading().saveImport(BookImport(id,"content://fixture","book.epub"))
            repo.recoverImports()
            assertFalse(staging.exists());assertFalse(conversion.exists());assertEquals("Original bytes",original.readText())
            assertEquals("INTERRUPTED",db.reading().imports().first().single().stage)
        }finally{root.walkBottomUp().forEach{it.delete()};db.close()}
    }
    @Test fun rtfAndLargeFb2NormalizationRunEntirelyOffline()=runBlocking {
        val db=database();val repo=ReadingRepository(context,db);val sources=mutableListOf<Uri>();val books=mutableListOf<Book>()
        try {
            val rtf=uri("rtf-${UUID.randomUUID()}.rtf","{\\rtf1\\ansi Hello \\b bold\\b0.\\par Second paragraph.}".toByteArray());sources.add(rtf)
            val rtfBook=repo.import(rtf);books.add(rtfBook)
            ZipFile(repo.file(rtfBook.normalizedFile!!)).use{zip->val text=zip.getInputStream(zip.getEntry("section-0.xhtml")).reader().readText();assertTrue(text.contains("Hello bold."));assertTrue(text.contains("Second paragraph."))}
            val fb2=File(context.cacheDir,"stream-${UUID.randomUUID()}.fb2")
            val text=File(context.cacheDir,"stream-${UUID.randomUUID()}.txt")
            try {
                fb2.writer().use{writer->writer.write("<?xml version=\"1.0\"?><FictionBook xmlns=\"http://www.gribuser.ru/xml/fictionbook/2.0\"><body><section>");repeat(400000){writer.write("<p>A streaming paragraph keeps memory bounded.</p>")};writer.write("</section></body></FictionBook>")}
                assertTrue(fb2.length()>BookFiles.MAX_TEXT)
                withContext(Dispatchers.IO){Fb2Normalizer.toText(fb2,text){ensureActive()}}
                assertTrue(text.length()>1000000);assertTrue(text.bufferedReader().use{it.readLine();it.readLine();it.readLine()}.orEmpty().contains("streaming"))
            } finally{fb2.delete();text.delete()}
        }finally{sources.forEach(::removeSource);books.forEach{clean(repo,it)};db.close()}
    }
    @Test fun pdfRendersAndSearchesActualPageNumbers()=runBlocking(Dispatchers.IO) {
        val file=File(context.cacheDir,"pages-${UUID.randomUUID()}.pdf")
        try {
            val document=android.graphics.pdf.PdfDocument()
            try {
                repeat(3){index->val page=document.startPage(android.graphics.pdf.PdfDocument.PageInfo.Builder(300,400,index+1).create());page.canvas.drawText("Acceptance page ${index+1}",20f,70f,Paint().apply{color=Color.BLACK;textSize=18f});document.finishPage(page)}
                file.outputStream().use(document::writeTo)
            } finally{document.close()}
            PdfPages(context,file).use{pages->
                assertEquals(3,pages.pageCount);val image=pages.render(2,600)
                try { assertEquals(600,image.width);assertEquals(800,image.height);assertTrue(image.allocationByteCount<=24_000_000) }finally{image.recycle()}
                val found=pages.search("page 3");assertEquals("2",found.single().locator)
            }
        }finally{file.delete()}
    }
    @Test fun comicPagesUseNaturalOrderAndBoundedBitmaps()=runBlocking(Dispatchers.IO) {
        val file=File(context.cacheDir,"comic-${UUID.randomUUID()}.cbz")
        try {
            ZipOutputStream(file.outputStream()).use{zip->listOf(10 to Color.BLUE,2 to Color.GREEN,1 to Color.RED).forEach{(number,color)->
                zip.putNextEntry(ZipEntry("page$number.png"));val bitmap=Bitmap.createBitmap(40,60,Bitmap.Config.ARGB_8888);bitmap.eraseColor(color);bitmap.compress(Bitmap.CompressFormat.PNG,100,zip);bitmap.recycle();zip.closeEntry()
            }}
            ComicPages(file).use{pages->assertEquals(3,pages.pageCount);pages.render(0,400).let{try{assertEquals(Color.RED,it.getPixel(10,10))}finally{it.recycle()}};pages.render(2,400).let{try{assertEquals(Color.BLUE,it.getPixel(10,10))}finally{it.recycle()}}}
        }finally{file.delete()}
    }
    @Test fun nativeRarStoredComicConvertsAndRejectsTraversal()=runBlocking(Dispatchers.IO) {
        val source=File(context.cacheDir,"rar-${UUID.randomUUID()}.cbr");val target=File(context.cacheDir,"rar-${UUID.randomUUID()}.cbz")
        try {
            val image=ByteArrayOutputStream();Bitmap.createBitmap(20,30,Bitmap.Config.ARGB_8888).let{it.eraseColor(Color.RED);it.compress(Bitmap.CompressFormat.PNG,100,image);it.recycle()}
            source.writeBytes(storedRar("001.png",image.toByteArray()));assertTrue(NativeArchive.toZip(source,target)>0)
            ComicPages(target).use{assertEquals(1,it.pageCount);it.render(0,400).recycle()}
            source.writeBytes(storedRar("../001.png",image.toByteArray()));assertTrue(runCatching{NativeArchive.toZip(source,target)}.isFailure)
        }finally{source.delete();target.delete()}
    }
    private fun storedRar(name:String,data:ByteArray):ByteArray {
        val output=ByteArrayOutputStream()
        fun little(value:Long,bytes:Int)=ByteArray(bytes){index->(value shr (index*8)).toByte()}
        fun header(type:Int,flags:Int,body:ByteArray) {
            val payload=byteArrayOf(type.toByte())+little(flags.toLong(),2)+little((7+body.size).toLong(),2)+body
            output.write(little(CRC32().apply{update(payload)}.value and 65535,2));output.write(payload)
        }
        output.write(byteArrayOf(0x52,0x61,0x72,0x21,0x1a,0x07,0x00))
        header(0x73,0,ByteArray(6))
        val filename=name.toByteArray()
        val body=little(data.size.toLong(),4)+little(data.size.toLong(),4)+byteArrayOf(3)+little(CRC32().apply{update(data)}.value,4)+little(0,4)+byteArrayOf(20,0x30)+little(filename.size.toLong(),2)+little(0x81a4,4)+filename
        header(0x74,0x8000,body);output.write(data);header(0x7b,0,byteArrayOf());return output.toByteArray()
    }
}
