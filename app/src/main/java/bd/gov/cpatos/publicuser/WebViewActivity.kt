package bd.gov.cpatos.publicuser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import bd.gov.cpatos.R


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WebViewActivity : AppCompatActivity() {
    private  var webview: WebView? = null



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        webview = findViewById(R.id.webView)
       // webview?.settings?.javaScriptEnabled   = true
      //  webview?.webViewClient = MyWebViewClient()
       // webview?.loadUrl("www.cpatos.gov.bd");

        val webSettings: WebSettings? = webview?.settings
        webSettings?.javaScriptEnabled = true

        val webViewClient = MyWebViewClient(this)
        webview?.webViewClient = webViewClient

        webview?.loadUrl("www.cpatos.gov.bd")

    }
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && this.webview!!.canGoBack()) {
//            this.webview!!.goBack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }


    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString()
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            //Toast.makeText(activity, "Got Error!", Toast.LENGTH_SHORT).show()
        }
    }

}