package bd.gov.cpatos.gatepass

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.layout_loading_dialog.*


class PaymentActivity : AppCompatActivity() {
    var mywebview: WebView? = null
    private var webView: String? = null
    var PAGE_URL:String? = null
    var mContext:Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        mywebview = findViewById(R.id.webviewPayment)
        mContext = this
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            var Param:String? = bundle.getString("PARAM")
            PAGE_URL =EndPoints.URL_CNF_TRUCK_PAYMENT+Param



        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        Log.e("PAGE_URL:-", PAGE_URL.toString())
        mywebview?.apply {
            // Configure related browser settings
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            mywebview?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            // Configure the client to use when opening URLs
            mywebview?.webViewClient = WebViewClient()
            // Load the initial URL
            mywebview?.loadUrl(PAGE_URL.toString())
//            val path: String = Uri.parse("file:///android_asset/index.html").toString()
//            mywebview?.loadUrl(path)

        }
        // Enable responsive layout
        mywebview?.settings?.useWideViewPort = true
        // Zoom out if the content width is greater than the width of the viewport
        mywebview?.settings?.loadWithOverviewMode = true
        mywebview?.settings?.setSupportZoom(true)
        mywebview?.settings?.builtInZoomControls = true // allow pinch to zooom
        mywebview?.settings?.displayZoomControls = false // disable the default zoom controls on the page

//        var progressDialog = ProgressDialog.progressDialog(this@PaymentActivity)
//        progressDialog.show();
//        mywebview?.setWebViewClient(object : WebViewClient() {
//            override fun onPageStarted(view: WebView,url: String,favicon: Bitmap){
//                super.onPageStarted(view, url, favicon)
//            }
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//            override fun onPageCommitVisible(view: WebView,url: String) {
//                super.onPageCommitVisible(view, url)
//                if (progressDialog != null) {
//                    progressDialog.dismiss()
//                }
//            }
//        })




}

}
