package dev.daylight.hub.reading

import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.*
import javax.xml.parsers.SAXParserFactory

/** Large FB2 files use bounded streaming text normalization; the original retains illustrations. */
object Fb2Normalizer {
    fun toText(source:File,destination:File,checkCancelled:()->Unit) {
        var body=0;var binary=0;var written=0L
        destination.writer(Charsets.UTF_8).buffered().use { output ->
            fun write(text:CharArray,start:Int,length:Int) {
                checkCancelled();written+=length;require(written<=BookFiles.MAX_IMPORT){"Expanded FB2 text exceeds its reading limit."};output.write(text,start,length)
            }
            val handler=object:DefaultHandler() {
                override fun resolveEntity(publicId:String?,systemId:String?):InputSource=throw SAXException("External XML entities are unsupported.")
                override fun startElement(uri:String?,localName:String?,qName:String?,attributes:org.xml.sax.Attributes?) {
                    checkCancelled();val name=localName?.ifBlank{null} ?: qName?.substringAfter(':')
                    if(name=="body")body++;if(name=="binary")binary++
                    if(body>0 && binary==0 && name in listOf("p","v","subtitle","title","empty-line","section"))output.write("\n")
                }
                override fun endElement(uri:String?,localName:String?,qName:String?) {
                    val name=localName?.ifBlank{null} ?: qName?.substringAfter(':')
                    if(body>0 && binary==0 && name in listOf("p","v","subtitle","title"))output.write("\n")
                    if(name=="body")body--;if(name=="binary")binary--
                }
                override fun characters(chars:CharArray,start:Int,length:Int){if(body>0 && binary==0)write(chars,start,length)}
                override fun error(error:org.xml.sax.SAXParseException):Unit=throw error
                override fun fatalError(error:org.xml.sax.SAXParseException):Unit=throw error
            }
            source.inputStream().buffered().use { raw ->
                val guarded=object:FilterInputStream(raw) {
                    var tail=""
                    fun inspect(buffer:ByteArray,offset:Int,count:Int) {
                        checkCancelled();if(count<=0)return
                        val text=tail+String(buffer,offset,count,Charsets.ISO_8859_1).replace("\u0000","").uppercase(java.util.Locale.ROOT)
                        require(!text.contains("<!DOCTYPE") && !text.contains("<!ENTITY")){"Unsupported XML declarations in FB2."};tail=text.takeLast(12)
                    }
                    override fun read():Int { val value=`in`.read();if(value>=0)inspect(byteArrayOf(value.toByte()),0,1);return value }
                    override fun read(buffer:ByteArray,offset:Int,length:Int):Int { val count=`in`.read(buffer,offset,length);inspect(buffer,offset,count);return count }
                }
                val parser=SAXParserFactory.newInstance().apply{isNamespaceAware=true}.newSAXParser().xmlReader
                parser.contentHandler=handler;parser.entityResolver=handler;parser.errorHandler=handler
                parser.parse(InputSource(guarded))
            }
        }
    }
}
