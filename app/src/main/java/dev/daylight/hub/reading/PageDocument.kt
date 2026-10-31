package dev.daylight.hub.reading

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.ParcelFileDescriptor
import io.legere.pdfiumandroid.PdfDocument
import io.legere.pdfiumandroid.PdfiumCore
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.io.Closeable
import java.io.File
import java.util.zip.ZipFile
import kotlin.math.*

/** All operations, including close, run serially on the reader's IO dispatcher. */
interface PageDocument:Closeable {
    val pageCount:Int
    val title:String
    val author:String
    val contents:List<ReaderTocItem>
    fun render(index:Int,width:Int):Bitmap
    suspend fun search(query:String):List<ReaderSearchResult>
}

class PdfPages(context:Context,file:File,password:String?=null):PageDocument {
    private val descriptor=ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY)
    private val document:PdfDocument=try { PdfiumCore(context).newDocument(descriptor,password) } catch(error:Throwable){descriptor.close();throw IllegalArgumentException("This PDF could not be opened. Password-protected PDFs need an unlocked copy.",error)}
    override val pageCount:Int
    override val title:String
    override val author:String
    override val contents:List<ReaderTocItem>
    init { try {
        pageCount=document.getPageCount().also{require(it in 1..100000){"This PDF has an unsupported page count."}}
        val metadata=document.getDocumentMeta()
        title=metadata.title.orEmpty().take(512)
        author=metadata.author.orEmpty().take(512)
        contents=buildList {
        fun add(items:List<io.legere.pdfiumandroid.api.Bookmark>,depth:Int) {
            if(depth>12)return
            for(item in items) {
                if(size>=2000)break
                if(item.pageIdx in 0 until pageCount.toLong())add(ReaderTocItem(item.title.orEmpty().take(200),item.pageIdx.toString(),depth))
                add(item.children,depth+1)
            }
        }
        add(document.getTableOfContents(),0)
        }
    } catch(error:Throwable){try{document.close()}finally{descriptor.close()};throw error} }
    fun pageText(index:Int):String {
        require(index in 0 until pageCount)
        return document.openPage(index)?.use { page -> page.openTextPage().use { text ->
            text.textPageGetText(0,text.textPageCountChars().coerceIn(0,200000)).orEmpty()
        } }.orEmpty()
    }
    override fun render(index:Int,width:Int):Bitmap {
        require(index in 0 until pageCount)
        return (document.openPage(index) ?: error("This page is unavailable.")).use { page ->
            val ratio=page.getPageHeightPoint().toDouble()/page.getPageWidthPoint().coerceAtLeast(1)
            require(ratio.isFinite() && ratio in .01..100.0){"The page dimensions are invalid."}
            val w=minOf(width.coerceIn(320,3072),sqrt(6_000_000.0/ratio).toInt()).coerceAtLeast(1)
            val h=(w*ratio).roundToInt().coerceAtLeast(1)
            val bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
            try { page.renderPageBitmap(bitmap,0,0,w,h,true);bitmap } catch(error:Throwable){bitmap.recycle();throw error}
        }
    }
    override suspend fun search(query:String):List<ReaderSearchResult> = buildList {
        if(query.isBlank())return@buildList
        for(index in 0 until pageCount) {
            currentCoroutineContext().ensureActive()
            document.openPage(index)?.use { page -> page.openTextPage().use { text ->
                val count=text.textPageCountChars().coerceIn(0,200000)
                val value=text.textPageGetText(0,count).orEmpty()
                val found=value.indexOf(query,ignoreCase=true)
                if(found>=0)add(ReaderSearchResult("Page ${index+1}",index.toString(),value.substring((found-60).coerceAtLeast(0),(found+query.length+100).coerceAtMost(value.length))))
            } }
            if(size>=100)break
        }
    }
    override fun close(){try{document.close()}finally{descriptor.close()}}
}

class ComicPages(file:File):PageDocument {
    private val archive=ZipFile(file)
    private val pages=try { archive.entries().toList().filter{!it.isDirectory && BookFiles.isImage(it.name)}.onEach {
        BookFiles.requireSafeEntry(it.name);require(it.size in 0..BookFiles.MAX_RESOURCE){"A comic page is too large."}
    }.sortedWith { a,b -> naturalCompare(a.name,b.name) }.also{require(it.size in 1..50000){"The comic has no readable pages."}} } catch(error:Throwable){archive.close();throw error}
    override val pageCount=pages.size
    override val title=""
    override val author=""
    override val contents=emptyList<ReaderTocItem>()
    override fun render(index:Int,width:Int):Bitmap {
        val entry=pages.getOrNull(index) ?: error("This page is unavailable.")
        val bytes=archive.getInputStream(entry).use{BookFiles.boundedBytes(it)}
        val bounds=BitmapFactory.Options().apply{inJustDecodeBounds=true}
        BitmapFactory.decodeByteArray(bytes,0,bytes.size,bounds)
        require(bounds.outWidth>0 && bounds.outHeight>0){"The comic contains an invalid image."}
        var sample=1
        val target=width.coerceIn(320,3072)
        while(bounds.outWidth/sample>target*2 || bounds.outWidth.toLong()/sample*(bounds.outHeight/sample)>6_000_000)sample*=2
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size,BitmapFactory.Options().apply{inSampleSize=sample}) ?: error("This comic page could not be decoded.")
    }
    override suspend fun search(query:String)=emptyList<ReaderSearchResult>()
    override fun close()=archive.close()
}

internal fun naturalCompare(left:String,right:String):Int {
    var a=0;var b=0
    while(a<left.length && b<right.length) {
        if(left[a].isDigit() && right[b].isDigit()) {
            val startA=a;val startB=b
            while(a<left.length && left[a].isDigit())a++
            while(b<right.length && right[b].isDigit())b++
            val first=left.substring(startA,a).trimStart('0');val second=right.substring(startB,b).trimStart('0')
            val size=first.length.compareTo(second.length);if(size!=0)return size
            val number=first.compareTo(second);if(number!=0)return number
        } else {
            val order=left[a].lowercaseChar().compareTo(right[b].lowercaseChar());if(order!=0)return order
            a++;b++
        }
    }
    val length=(left.length-a).compareTo(right.length-b)
    return if(length!=0)length else left.compareTo(right)
}
