package bd.gov.cpatos.pilot.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.print.PrintJob
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import es.voghdev.pdfviewpager.library.util.FileUtil
import kotlinx.android.synthetic.main.activity_report_view.*
import kotlinx.android.synthetic.main.activity_report_view.progressBar
import kotlinx.android.synthetic.main.layout_loading_dialog.*


private var remotePDFViewPager: RemotePDFViewPager? = null

private var pdfPagerAdapter: PDFPagerAdapter? = null

private var url: String? = null
private var pdfLayout: LinearLayout? = null
private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
var mPreferences: SharedPreferences? = null
private var preferencesEditor: SharedPreferences.Editor? = null
private  var isSignedIn:String? = null
private  var SignedInId:String? = null
private  var SignedInLoginId:String? = null
private  var SignedInSection:String? = null
private  var SignedInUserRole:String? = null
lateinit var context: Context
lateinit var activity: ReportViewActivity
var printJob: PrintJob? = null
var printBtnPressed = false
private var VVD_GKEY = ""
private var ROTATION = ""
private var ACTIVITY_FOR = ""
private var downlaod: FloatingActionButton? = null
private var ProgressBar: ProgressBar?= null

class ReportViewActivity : AppCompatActivity(), DownloadFile.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_view)

        init()

        var urlRot = ROTATION.replace("/", "_")
        url =
            "http://cpatos.gov.bd/PcsOracle/index.php/pilotageApp/ReportController/Report/" + urlRot + "/" + VVD_GKEY + "/" + ACTIVITY_FOR + "/" + SignedInLoginId.toString()
        ProgressBar = findViewById(R.id.progressBar)
        downlaod = findViewById(R.id.Downlaod)
        ProgressBar?.setVisibility(View.VISIBLE);
        pdfLayout = findViewById<LinearLayout>(R.id.layOut)

        remotePDFViewPager = RemotePDFViewPager(this, url, this)
    }

    private fun init() {
        val toolbar: Toolbar = findViewById(bd.gov.cpatos.R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = "Report"
            VVD_GKEY = bundle.getString("VVD_GKEY")!!
            ROTATION = bundle.getString("ROTATION")!!
            ACTIVITY_FOR = bundle.getString("ACTIVITY_FOR")!!

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        isSignedIn = mPreferences?.getString("issignedin", "false")
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
    }

    override fun onSuccess(url: String?, destinationPath: String?) {
        pdfPagerAdapter = PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url))
        remotePDFViewPager!!.adapter = pdfPagerAdapter
        updateLayout()
        progressBar!!.visibility = View.GONE
    }

    private fun updateLayout() {
        pdfLayout?.addView(
            remotePDFViewPager,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
        )
    }

    override fun onFailure(e: java.lang.Exception?) {
        TODO("Not yet implemented")
    }

    override fun onProgressUpdate(progress: Int, total: Int) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (pdfPagerAdapter != null) {
            pdfPagerAdapter!!.close()
        }
    }
}
