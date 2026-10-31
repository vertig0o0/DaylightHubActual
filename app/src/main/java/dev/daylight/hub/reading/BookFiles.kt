package dev.daylight.hub.reading

import java.io.*
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource

data class PublicationInfo(val format:String,val title:String,val author:String="",val coverEntry:String?=null,val fb2Entry:String?=null)

object BookFiles {
    const val MAX_RESOURCE=32L*1024*1024
    const val MAX_TEXT=16L*1024*1024
    const val MAX_IMPORT=2L*1024*1024*1024
    fun boundedStream(source:InputStream,limit:Long):InputStream=object:FilterInputStream(source) {
        init { require(limit>=0) }
        private var consumed=0L
        private fun count(amount:Int):Int { if(amount>0){consumed+=amount;if(consumed>limit)throw IOException("Book resource exceeds its reading limit.")};return amount }
        override fun read():Int { val value=super.read();if(value>=0)count(1);return value }
        override fun read(buffer:ByteArray,offset:Int,length:Int):Int {
            require(offset>=0 && length>=0 && offset<=buffer.size-length)
            if(length==0)return 0
            return count(`in`.read(buffer,offset,minOf(length.toLong(),(limit-consumed).coerceAtLeast(0)+1).toInt()))
        }
        override fun skip(amount:Long):Long { if(amount<=0)return 0;val skipped=`in`.skip(minOf(amount,(limit-consumed).coerceAtLeast(0)));consumed+=skipped;return skipped }
        override fun markSupported()=false
        override fun reset():Unit=throw IOException("This bounded resource cannot be reset.")
    }
    private val images=setOf("jpg","jpeg","png","webp","gif","bmp")
    fun isImage(name:String)=name.substringAfterLast('.').lowercase(Locale.ROOT) in images
    fun requireSafeEntry(name:String) {
        require(name.length<=1024 && !name.startsWith('/') && !name.contains('\\') && name.split('/').none{it==".."}) { "The archive contains an unsafe entry name." }
    }
    fun boundedBytes(input:InputStream,limit:Long=MAX_RESOURCE):ByteArray {
        val output=ByteArrayOutputStream();val buffer=ByteArray(16384);var total=0L
        while(true) { val count=input.read(buffer);if(count<0) break;total+=count;require(total<=limit){"A book resource is too large to read safely."};output.write(buffer,0,count) }
        return output.toByteArray()
    }
    fun zipBytes(zip:ZipFile,name:String,limit:Long=MAX_RESOURCE):ByteArray? {
        requireSafeEntry(name)
        val entry=zip.getEntry(name) ?: return null
        require(entry.size in 0..limit) { "A book resource is too large to read safely." }
        return zip.getInputStream(entry).use{boundedBytes(it,limit)}
    }
    private fun xml(bytes:ByteArray):org.w3c.dom.Document {
        require(bytes.size<=1024*1024)
        val signature=bytes.toString(Charsets.UTF_8).replace("\u0000","")
        require(!Regex("<!DOCTYPE|<!ENTITY",RegexOption.IGNORE_CASE).containsMatchIn(signature)) { "Unsupported XML declarations in book metadata." }
        val factory=DocumentBuilderFactory.newInstance().apply{isNamespaceAware=true;isExpandEntityReferences=false}
        return factory.newDocumentBuilder().apply{setEntityResolver{_,_->InputSource(StringReader(""))}}.parse(ByteArrayInputStream(bytes))
    }
    fun inspect(file:File,name:String,mime:String?):PublicationInfo {
        require(file.length() in 1..MAX_IMPORT) { "Choose a nonempty book up to 2 GB." }
        val prefix=file.inputStream().use{stream->val bytes=ByteArray(minOf(1024L,file.length()).toInt());val count=stream.read(bytes);bytes.copyOf(count.coerceAtLeast(0))}
        val fallback=name.substringBeforeLast('.').ifBlank{"Untitled book"}.take(512)
        if(prefix.toString(Charsets.ISO_8859_1).contains("%PDF-")) return PublicationInfo("PDF",fallback)
        if(prefix.size>=4 && prefix[0]==0x50.toByte() && prefix[1]==0x4b.toByte()) {
            ZipFile(file).use { zip ->
                val entries=zip.entries().toList();require(entries.size<=50000){"This archive has too many entries."}
                var total=0L
                entries.forEach{entry->requireSafeEntry(entry.name);require(entry.size>=0){"Unknown archive entry size."};require(entry.size<=MAX_IMPORT-total){"Expanded book exceeds the 2 GB limit."};total+=entry.size}
                if(zip.getEntry("META-INF/container.xml")!=null) {
                    val container=xml(zipBytes(zip,"META-INF/container.xml",1024*1024)!!)
                    val roots=container.getElementsByTagNameNS("*","rootfile")
                    require(roots.length>0){"The EPUB package is missing."}
                    val path=(roots.item(0) as org.w3c.dom.Element).getAttribute("full-path");requireSafeEntry(path)
                    val opf=xml(zipBytes(zip,path,1024*1024) ?: error("The EPUB package is missing."))
                    zipBytes(zip,"META-INF/encryption.xml",1024*1024)?.let { encrypted ->
                        val doc=xml(encrypted);val methods=doc.getElementsByTagNameNS("*","EncryptionMethod")
                        repeat(methods.length){index->val algorithm=(methods.item(index) as org.w3c.dom.Element).getAttribute("Algorithm")
                            require(algorithm in listOf("http://www.idpf.org/2008/embedding","http://ns.adobe.com/pdf/enc#RC")){"DRM-protected e-books are not supported. Import a DRM-free copy."}}
                    }
                    require(zip.getEntry("META-INF/license.lcpl")==null){"Readium LCP protected books are not supported."}
                    fun text(tag:String)=opf.getElementsByTagNameNS("*",tag).item(0)?.textContent?.trim()?.take(512).orEmpty()
                    val items=opf.getElementsByTagNameNS("*","item")
                    var coverId:String?=null;var cover:String?=null
                    val meta=opf.getElementsByTagNameNS("*","meta")
                    repeat(meta.length){index->val item=meta.item(index) as org.w3c.dom.Element;if(item.getAttribute("name")=="cover")coverId=item.getAttribute("content")}
                    repeat(items.length){index->val item=items.item(index) as org.w3c.dom.Element
                        if(item.getAttribute("id")==coverId || "cover-image" in item.getAttribute("properties").split(' ')) {
                            cover=runCatching { val base=java.net.URI("https","book.invalid","/$path",null);val target=base.resolve(item.getAttribute("href")).normalize();require(target.host=="book.invalid");target.path.removePrefix("/").also(::requireSafeEntry) }.getOrNull()
                        }}
                    return PublicationInfo("EPUB",text("title").ifBlank{fallback},text("creator"),cover)
                }
                val fb2=entries.filter{!it.isDirectory && it.name.endsWith(".fb2",true)}
                if(fb2.size==1) return PublicationInfo("FB2_ZIP",fallback,fb2Entry=fb2.single().name)
                require(fb2.isEmpty()){ "This archive contains multiple FB2 books. Import them separately." }
                if(entries.any{!it.isDirectory && isImage(it.name)}) return PublicationInfo("CBZ",fallback)
                error("This ZIP is not a supported e-book or comic archive.")
            }
        }
        if(prefix.size>=68 && prefix.copyOfRange(60,68).toString(Charsets.US_ASCII)=="BOOKMOBI") {
            RandomAccessFile(file,"r").use { input ->
                input.seek(78);val first=input.readInt().toLong() and 0xffffffffL
                require(first>=78 && first+16<=file.length()){ "Invalid MOBI record table." }
                input.seek(first+4);val textBytes=input.readInt().toLong() and 0xffffffffL
                require(textBytes<=MAX_TEXT){"This MOBI has more than 16 MB of text. Convert it to EPUB for bounded reading."}
                input.seek(first+12);require(input.readUnsignedShort()==0){"Encrypted MOBI/AZW books are not supported."}
            }
            return PublicationInfo("MOBI",fallback)
        }
        val text=prefix.toString(Charsets.UTF_8).trimStart('\uFEFF',' ','\r','\n','\t')
        val extension=name.substringAfterLast('.').lowercase(Locale.ROOT)
        if(prefix.size>=4 && prefix.copyOfRange(0,4).toString(Charsets.US_ASCII)=="Rar!") return PublicationInfo("CBR",fallback)
        if(text.startsWith("{\\rtf")) return PublicationInfo("RTF",fallback)
        if(extension=="fb2" || Regex("<([\\w-]+:)?FictionBook[\\s>]",RegexOption.IGNORE_CASE).containsMatchIn(text)) return PublicationInfo("FB2",fallback)
        if(extension in listOf("html","htm","xhtml") || mime in listOf("text/html","application/xhtml+xml")) return PublicationInfo("HTML",fallback)
        if(extension=="txt" || mime=="text/plain") return PublicationInfo("TXT",fallback)
        error("This format is unsupported. Choose a DRM-free EPUB, PDF, MOBI/AZW3, FB2, TXT, HTML, RTF, or comic book.")
    }

    fun xmlEscape(value:String)=value.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;")
    /** Preserve readable HTML structure while removing active content and unavailable images. */
    fun htmlToEpub(source:File,destination:File,fallbackTitle:String,checkCancelled:()->Unit={}) {
        require(source.length()<=MAX_TEXT){"This HTML file is too large. Import a chaptered EPUB instead."}
        checkCancelled()
        val document=org.jsoup.Jsoup.parse(source,null)
        document.select("script,style,iframe,object,embed,form").remove()
        document.select("img").forEach{it.replaceWith(org.jsoup.nodes.TextNode(it.attr("alt")))}
        val safe=org.jsoup.safety.Cleaner(org.jsoup.safety.Safelist.relaxed().removeTags("img").addAttributes(":all","id")).clean(document)
        safe.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml).escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml).charset(Charsets.UTF_8)
        val title=document.title().trim().take(512).ifBlank{fallbackTitle}
        checkCancelled()
        ZipOutputStream(BufferedOutputStream(destination.outputStream())).use{zip->
            fun put(name:String,text:String){checkCancelled();zip.putNextEntry(ZipEntry(name));zip.write(text.toByteArray(Charsets.UTF_8));zip.closeEntry()}
            val mime="application/epub+zip".toByteArray(Charsets.US_ASCII)
            zip.putNextEntry(ZipEntry("mimetype").apply{method=ZipEntry.STORED;size=mime.size.toLong();compressedSize=size;crc=java.util.zip.CRC32().apply{update(mime)}.value});zip.write(mime);zip.closeEntry()
            put("META-INF/container.xml","""<container xmlns="urn:oasis:names:tc:opendocument:xmlns:container"><rootfiles><rootfile full-path="book.opf" media-type="application/oebps-package+xml"/></rootfiles></container>""")
            put("section-0.xhtml","""<html xmlns="http://www.w3.org/1999/xhtml"><head><title>${xmlEscape(title)}</title></head><body>${safe.body().html()}</body></html>""")
            put("nav.xhtml","""<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops"><head><title>Contents</title></head><body><nav epub:type="toc"><ol><li><a href="section-0.xhtml">${xmlEscape(title)}</a></li></ol></nav></body></html>""")
            put("book.opf","""<package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="id"><metadata xmlns:dc="http://purl.org/dc/elements/1.1/"><dc:identifier id="id">urn:prospacehub:html</dc:identifier><dc:title>${xmlEscape(title)}</dc:title><dc:language>und</dc:language><meta property="dcterms:modified">2000-01-01T00:00:00Z</meta></metadata><manifest><item id="s0" href="section-0.xhtml" media-type="application/xhtml+xml"/><item id="nav" href="nav.xhtml" media-type="application/xhtml+xml" properties="nav"/></manifest><spine><itemref idref="s0"/></spine></package>""")
        }
    }
    /** Streaming plain-text packaging; each chapter stays small regardless of total file size. */
    fun textEncoding(source:File):String=source.inputStream().use {
        val prefix=ByteArray(3);val count=it.read(prefix)
        when { count>=2 && prefix[0]==0xff.toByte() && prefix[1]==0xfe.toByte()->"UTF-16LE"
            count>=2 && prefix[0]==0xfe.toByte() && prefix[1]==0xff.toByte()->"UTF-16BE"
            else->"UTF-8" }
    }
    fun textToEpub(source:File,destination:File,title:String,encoding:String=textEncoding(source),checkCancelled:()->Unit={}) {
        ZipOutputStream(BufferedOutputStream(destination.outputStream())).use { zip ->
            fun put(name:String,text:String){zip.putNextEntry(ZipEntry(name));zip.write(text.toByteArray(Charsets.UTF_8));zip.closeEntry()}
            val mimetype="application/epub+zip".toByteArray(Charsets.US_ASCII)
            zip.putNextEntry(ZipEntry("mimetype").apply{method=ZipEntry.STORED;size=mimetype.size.toLong();compressedSize=size;crc=java.util.zip.CRC32().apply{update(mimetype)}.value});zip.write(mimetype);zip.closeEntry()
            put("META-INF/container.xml","""<?xml version="1.0"?><container xmlns="urn:oasis:names:tc:opendocument:xmlns:container" version="1.0"><rootfiles><rootfile full-path="book.opf" media-type="application/oebps-package+xml"/></rootfiles></container>""")
            val names=mutableListOf<String>();var part=StringBuilder();val buffer=CharArray(8192)
            fun flush(){if(part.isEmpty())return;val name="section-${names.size}.xhtml";names.add(name)
                put(name,"""<?xml version="1.0" encoding="utf-8"?><html xmlns="http://www.w3.org/1999/xhtml"><head><title>${xmlEscape(title)}</title></head><body><div style="white-space:pre-wrap">${xmlEscape(part.toString())}</div></body></html>""");part=StringBuilder()}
            source.reader(charset(encoding)).buffered().use { reader ->
                var first=true
                while(true){checkCancelled();val count=reader.read(buffer);if(count<0)break;val start=if(first && count>0 && buffer[0]=='\uFEFF')1 else 0;first=false;part.append(buffer,start,count-start);if(part.length>=49152 && !part.last().isHighSurrogate())flush()}
            }
            flush();require(names.isNotEmpty()){ "The file contains no readable text." }
            val manifest=names.mapIndexed{index,name->"<item id=\"s$index\" href=\"$name\" media-type=\"application/xhtml+xml\"/>"}.joinToString("")
            val spine=names.indices.joinToString(""){"<itemref idref=\"s$it\"/>"}
            put("nav.xhtml","""<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops"><head><title>Contents</title></head><body><nav epub:type="toc"><ol>${names.mapIndexed{index,name->"<li><a href=\"$name\">Part ${index+1}</a></li>"}.joinToString("")}</ol></nav></body></html>""")
            put("book.opf","""<?xml version="1.0"?><package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="id"><metadata xmlns:dc="http://purl.org/dc/elements/1.1/"><dc:identifier id="id">urn:prospacehub:converted</dc:identifier><dc:title>${xmlEscape(title)}</dc:title><dc:language>und</dc:language><meta property="dcterms:modified">2000-01-01T00:00:00Z</meta></metadata><manifest>$manifest<item id="nav" href="nav.xhtml" media-type="application/xhtml+xml" properties="nav"/></manifest><spine>$spine</spine></package>""")
        }
    }
}
