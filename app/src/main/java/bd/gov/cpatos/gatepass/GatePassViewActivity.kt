package bd.gov.cpatos.gatepass

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.activities.ReportViewActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GatePassViewActivity : AppCompatActivity() {
    private var webviewGatePass:WebView?=null
    private var printWeb: WebView? = null
    private var btnDownlaod: FloatingActionButton? = null
    var PAGE_URL:String? = null
    var trucVisitId:String? =null
    var printJob: PrintJob? = null
    lateinit var activity: GatePassViewActivity
    var printBtnPressed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_pass_view)
        webviewGatePass = findViewById(R.id.webviewGatePass)
        webviewGatePass?.setBackgroundColor(Color.WHITE)
        btnDownlaod = findViewById(R.id.btnDownlaod)
        activity =this

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            trucVisitId = bundle.getString("trucVisitId")

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        PAGE_URL ="http://cpatos.gov.bd/pcs/index.php/ShedBillController/printGatePassForMobileApp/R/"+trucVisitId

        val final_url = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + PAGE_URL
        webviewGatePass?.apply {
            // Configure related browser settings
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            webviewGatePass?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            // Configure the client to use when opening URLs
            webviewGatePass?.webViewClient = WebViewClient()
            // Load the initial URL
            webviewGatePass?.loadUrl(final_url)
//            val path: String = Uri.parse("file:///android_asset/index.html").toString()
//            webviewGatePass?.loadUrl(path)

        }
        // Enable responsive layout
        webviewGatePass?.settings?.useWideViewPort = true
        // Zoom out if the content width is greater than the width of the viewport
        webviewGatePass?.settings?.loadWithOverviewMode = true
        webviewGatePass?.settings?.setSupportZoom(true)
        webviewGatePass?.settings?.builtInZoomControls = true // allow pinch to zooom
        webviewGatePass?.settings?.displayZoomControls = false
        printWeb = webviewGatePass
        btnDownlaod?.setOnClickListener( {
            if (printWeb != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    PrintTheWebPage(printWeb!!)
                }else {
                    Toast.makeText( activity,"Not available for device below Android LOLLIPOP",Toast.LENGTH_SHORT).show(); }
            }else{
                Toast.makeText(activity, "WebPage not fully loaded", Toast.LENGTH_SHORT).show()
            }
        })
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun PrintTheWebPage(webView: WebView) {

        // set printBtnPressed true
        printBtnPressed = true

        // Creating  PrintManager instance
        val printManager = this.getSystemService(PRINT_SERVICE) as PrintManager

        // setting the name of job
        val jobName = trucVisitId

        // Creating  PrintDocumentAdapter instance
        val printAdapter = webView.createPrintDocumentAdapter(jobName!!)
        assert(printManager != null)
        printJob = printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()
        if (printJob != null && printBtnPressed) {
            if (printJob!!.isCompleted) {
                // Showing Toast Message
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isStarted) {
                // Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isBlocked) {
                // Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isCancelled) {
                // Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isFailed) {
                // Showing Toast Message
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isQueued) {
                // Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show()
            }
            // set printBtnPressed false
            printBtnPressed = false
        }
    }
}