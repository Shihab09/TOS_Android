package bd.gov.cpatos.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.EndPoints.BASE_URL

class MainWebView : AppCompatActivity() {
    var mWebView: WebView? = null
    var mTitle:String? = null
    var mVisitId:String? = null
    var mURL:String? = null
    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_web_view)
        mWebView = findViewById(R.id.webViewMain)
        mContext = this
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            mTitle = bundle.getString("TITLE") // 1
            mVisitId = bundle.getString("VISIT_ID") // 1
            mURL = EndPoints.URL_OPEN_PAYMENT_GATEPASS+"?visit_id="+mVisitId
            supportActionBar?.title = mTitle
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        mWebView?.apply {
            // Configure related browser settings
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            mWebView?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            // Configure the client to use when opening URLs
            mWebView?.webViewClient = WebViewClient()
            // Load the initial URL
            mWebView?.loadUrl(mURL.toString())
//            val path: String = Uri.parse("file:///android_asset/index.html").toString()
//            mWebView?.loadUrl(path)

        }
        // Enable responsive layout
        mWebView?.settings?.useWideViewPort = true
        // Zoom out if the content width is greater than the width of the viewport
        mWebView?.settings?.loadWithOverviewMode = true
        mWebView?.settings?.setSupportZoom(true)
        mWebView?.settings?.builtInZoomControls = true // allow pinch to zooom
        mWebView?.settings?.displayZoomControls = false
    }
}