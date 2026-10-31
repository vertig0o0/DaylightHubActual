package dev.daylight.hub.reading

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.webkit.*
import androidx.webkit.WebViewAssetLoader
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.util.UUID
import java.util.zip.ZipFile

data class ReaderAppearance(val theme:String="Light",val font:String="serif",val fontSize:Int=18,
    val lineHeight:Float=1.6f,val scrolling:Boolean=false,val reducedMotion:Boolean=false,val fixedZoom:String="fit-page") {
    fun json()=JSONObject().put("theme",theme).put("font",font).put("fontSize",fontSize)
        .put("lineHeight",lineHeight).put("scrolling",scrolling).put("reducedMotion",reducedMotion).put("fixedZoom",fixedZoom)
}
data class ReaderTocItem(val title:String,val locator:String,val depth:Int)
data class ReaderSearchResult(val title:String,val locator:String,val excerpt:String)
sealed interface ReaderEvent {
    data class Location(val position:BookPosition):ReaderEvent
    data class Opened(val title:String,val author:String,val contents:List<ReaderTocItem>,val fixedLayout:Boolean=false,val sections:Int=0):ReaderEvent
    data class Search(val results:List<ReaderSearchResult>):ReaderEvent
    data class Selection(val text:String):ReaderEvent
    data class Error(val message:String):ReaderEvent
    data object Interaction:ReaderEvent
}

/** Construct on an IO dispatcher. Resources are confined to one immutable private copy. */
class ReflowAsset(val book:Book,val source:File):Closeable {
    val token=UUID.randomUUID().toString()
    val size=source.length()
    private val zip=if(book.format=="EPUB") ZipFile(source) else null
    private val manifest:String=try { JSONObject().put("entries",JSONObject().apply {
        zip?.entries()?.toList()?.filter{!it.isDirectory}?.forEach{entry->
            BookFiles.requireSafeEntry(entry.name);put(entry.name,entry.size)
        }
    }).toString() } catch(error:Throwable){zip?.close();throw error}
    private fun response(mime:String,data:InputStream,status:Int=200)=WebResourceResponse(mime,"UTF-8",status,if(status==200)"OK" else "Unavailable",mapOf("Cache-Control" to "no-store","X-Content-Type-Options" to "nosniff"),data)
    fun respond(uri:Uri):WebResourceResponse = try {
        val operation=uri.path?.substringAfter("/book/$token/") ?: ""
        when(operation) {
            "manifest"->response("application/json",manifest.byteInputStream())
            "entry"->{
                val name=uri.getQueryParameter("name") ?: error("Missing resource")
                BookFiles.requireSafeEntry(name)
                val archive=zip ?: error("Not an EPUB")
                val entry=archive.getEntry(name)
                if(entry==null) response("text/plain",ByteArrayInputStream(byteArrayOf()),404)
                else {
                    require(entry.size in 0..BookFiles.MAX_RESOURCE){"Resource too large"}
                    val mime=when(name.substringAfterLast('.').lowercase()) {
                        "xhtml","html","htm"->"application/xhtml+xml";"xml","opf","ncx"->"application/xml"
                        "css"->"text/css";"svg"->"image/svg+xml";"jpg","jpeg"->"image/jpeg";"png"->"image/png"
                        "woff"->"font/woff";"woff2"->"font/woff2";"ttf"->"font/ttf";else->"application/octet-stream"
                    }
                    response(mime,BufferedInputStream(BookFiles.boundedStream(archive.getInputStream(entry),BookFiles.MAX_RESOURCE)))
                }
            }
            "range"->{
                val start=uri.getQueryParameter("start")?.toLongOrNull() ?: error("Missing range")
                val end=uri.getQueryParameter("end")?.toLongOrNull() ?: error("Missing range")
                require(start>=0 && end>=start && end<=source.length() && end-start<=BookFiles.MAX_RESOURCE)
                val bytes=ByteArray((end-start).toInt())
                RandomAccessFile(source,"r").use{it.seek(start);it.readFully(bytes)}
                response("application/octet-stream",ByteArrayInputStream(bytes))
            }
            "file"->{require(source.length()<=BookFiles.MAX_TEXT);response("application/xml",BufferedInputStream(source.inputStream()))}
            else->response("text/plain",ByteArrayInputStream(byteArrayOf()),404)
        }
    }catch(_:Exception){response("text/plain","Book resource unavailable".byteInputStream(),403)}
    override fun close(){zip?.close()}
}

/** Main-frame-only message port. Publication iframes are sandboxed without scripts. */
@SuppressLint("SetJavaScriptEnabled")
@Suppress("DEPRECATION")
class ReflowReader(context:Context,private val asset:ReflowAsset,private val position:BookPosition?,
    private var appearance:ReaderAppearance,private val onEvent:(ReaderEvent)->Unit):WebView(context) {
    companion object { private const val ORIGIN="https://appassets.androidplatform.net";private const val PAGE="$ORIGIN/assets/reader/reader.html" }
    private var port:WebMessagePort?=null
    private var released=false
    init {
        settings.javaScriptEnabled=true
        settings.allowFileAccess=false;settings.allowContentAccess=false
        settings.allowFileAccessFromFileURLs=false
        settings.allowUniversalAccessFromFileURLs=false
        settings.mixedContentMode=WebSettings.MIXED_CONTENT_NEVER_ALLOW
        settings.javaScriptCanOpenWindowsAutomatically=false;settings.setSupportMultipleWindows(false)
        settings.textZoom=(resources.configuration.fontScale*100).toInt().coerceIn(50,300)
        setRendererPriorityPolicy(RENDERER_PRIORITY_BOUND,true)
        val loader=WebViewAssetLoader.Builder().addPathHandler("/assets/",WebViewAssetLoader.AssetsPathHandler(context)).build()
        webViewClient=object:WebViewClient() {
            override fun shouldInterceptRequest(view:WebView,request:WebResourceRequest):WebResourceResponse? {
                val uri=request.url
                if(uri.scheme!="https" || uri.host!="appassets.androidplatform.net") return denied()
                if(uri.path?.startsWith("/book/${asset.token}/")==true) return asset.respond(uri)
                return loader.shouldInterceptRequest(uri) ?: denied()
            }
            override fun shouldOverrideUrlLoading(view:WebView,request:WebResourceRequest):Boolean =
                if(request.isForMainFrame) request.url.toString()!=PAGE else request.url.scheme!="blob" && !(request.url.scheme=="https" && request.url.host=="appassets.androidplatform.net")
            override fun onPageFinished(view:WebView,url:String) {
                if(released || url!=PAGE || port!=null)return
                val channels=createWebMessageChannel();port=channels[0]
                channels[0].setWebMessageCallback(object:WebMessagePort.WebMessageCallback() {
                    override fun onMessage(messagePort:WebMessagePort,message:WebMessage?) {
                        if(released)return
                        val raw=message?.data ?: return
                        if(raw.length>65536)return
                        try { handle(JSONObject(raw)) }catch(_:Exception){onEvent(ReaderEvent.Error("The reader returned an invalid response. Your saved position was kept."))}
                    }
                })
                postWebMessage(WebMessage("prospacehub-reader-channel",arrayOf(channels[1])),Uri.parse(ORIGIN))
            }
            override fun onRenderProcessGone(view:WebView,detail:RenderProcessGoneDetail):Boolean {
                if(!released) onEvent(ReaderEvent.Error("The reader stopped. Reopen the book to resume from its saved position."))
                return true
            }
            private fun denied()=WebResourceResponse("text/plain","UTF-8",403,"Offline",emptyMap(),ByteArrayInputStream(byteArrayOf()))
        }
        loadUrl(PAGE)
    }
    private fun handle(message:JSONObject) {
        when(message.getString("type")) {
            "ready"->send(JSONObject().put("type","open").put("format",asset.book.format)
                .put("base","$ORIGIN/book/${asset.token}").put("size",asset.size).put("name",asset.book.originalName)
                .put("title",asset.book.title)
                .put("locator",position?.locator.orEmpty()).put("preferences",appearance.json()))
            "location","bookmark"->{
                val next=BookPosition(asset.book.id,locator=message.getString("locator"),section=message.optInt("section"),
                    quote=message.optString("quote").take(160),chapter=message.optString("chapter").take(512),fraction=message.getDouble("fraction"))
                next.validate();onEvent(ReaderEvent.Location(next))
            }
            "opened"->{
                val contents=mutableListOf<ReaderTocItem>()
                fun add(items:JSONArray,depth:Int) {
                    if(depth>12)return
                    for(index in 0 until minOf(items.length(),2000)) {
                        if(contents.size>=2000)break
                        val item=items.getJSONObject(index)
                        val href=item.optString("href")
                        if(href.length in 1..4096) contents.add(ReaderTocItem(item.optString("label").take(200),href,depth))
                        item.optJSONArray("subitems")?.let{add(it,depth+1)}
                    }
                }
                message.optJSONArray("toc")?.let{add(it,0)}
                onEvent(ReaderEvent.Opened(message.optString("title").take(512),message.optString("author").take(512),contents,message.optBoolean("fixedLayout"),message.optInt("sections").coerceIn(0,50000)))
            }
            "search"->{
                val values=message.optJSONArray("results") ?: JSONArray()
                val results=(0 until minOf(values.length(),100)).map{index->values.getJSONObject(index)}.map{item->
                    ReaderSearchResult(item.optString("label").take(200),item.optString("locator").take(4096),item.optString("excerpt").take(500))
                }
                onEvent(ReaderEvent.Search(results))
            }
            "selection"->onEvent(ReaderEvent.Selection(message.optString("text").take(1600)))
            "interact"->onEvent(ReaderEvent.Interaction)
            "error"->onEvent(ReaderEvent.Error(message.optString("message","Could not open the book.").take(500)))
        }
    }
    fun send(message:JSONObject){if(!released)port?.postMessage(WebMessage(message.toString()))}
    fun command(type:String,locator:String?=null,query:String?=null)=send(JSONObject().put("type",type).apply{locator?.let{put("locator",it)};query?.let{put("query",it.take(200))}})
    fun appearance(value:ReaderAppearance){if(appearance==value)return;appearance=value;send(JSONObject().put("type","appearance").put("preferences",value.json()))}
    fun release() {
        if(released)return
        command("close");released=true;port?.close();port=null;stopLoading();loadUrl("about:blank");removeAllViews();destroy()
    }
}
