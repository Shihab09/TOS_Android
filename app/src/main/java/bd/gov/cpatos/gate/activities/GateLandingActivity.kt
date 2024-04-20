package bd.gov.cpatos.gate.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.activities.PilotLandingPage
import bd.gov.cpatos.pilot.activities.VesselListTypeWiseActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class GateLandingActivity : AppCompatActivity() {
    companion object {
        const val START_ACTIVITY_GATE_IN_LANDING_PAGE = 700
    }
    private var SignedInId:String? = null
    private var SignedInLoginId:String? = null
    private var SignedInSection:String? = null
    private var SignedInUserRole:String? = null
    private var btnGateIn: Button? = null
    private var btnGateOut: Button? = null
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var isSignedIn:String? = null
    private var tvTotalGateIn: TextView?= null
    private var tvTotalGateOut: TextView?= null
    private var btnFromDate: TextInputLayout? =null
    private var btnToDate: TextInputLayout? = null
    private var day = 0
    private var month:Int = 0
    private var year:Int = 0
    private var SELECTED_BOX = 0
    private var myday = 0
    private var myMonth:Int = 0
    private var myYear:Int = 0
    private var etFormDate: TextInputEditText? =null
    private var etToDate: TextInputEditText?=null
    private var ServiceType: AutoCompleteTextView?=null
    private var btnReportView: Button?=null
    private var mContext: Context?=null
    private val type = arrayOf("Gate In","Gate Out","All")




    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_landing)
        USER_DATA()
        UI()
        BUTTON_ACTIONS()



    }
    fun USER_DATA(){
        mContext = this
        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor =mPreferences?.edit()
        isSignedIn =mPreferences?.getString("issignedin", "false")
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")

    }

    fun UI(){
        tvTotalGateIn = findViewById(R.id.tvTotalGateIn)
        tvTotalGateOut = findViewById(R.id.tvTotalGateOut)
        btnGateIn= findViewById(R.id.btnGateIn)
        btnGateOut= findViewById(R.id.btnGateOut)
        btnFromDate = findViewById(R.id.btnFromDate)
        btnToDate = findViewById(R.id.btnToDate)
        etFormDate = findViewById(R.id.etFormDate)
        etToDate = findViewById(R.id.etToDate)
        btnReportView=findViewById(R.id.btnReportView)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = this.title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
    fun BUTTON_ACTIONS(){
        btnGateIn?.setOnClickListener {
            val intent = Intent(this@GateLandingActivity, GateinNewActivity::class.java)
            intent.putExtra("TITLE", "Gate In")
            startActivityForResult(
                intent,
                GateLandingActivity.START_ACTIVITY_GATE_IN_LANDING_PAGE
            )
        }

        btnGateOut?.setOnClickListener {
            val intent = Intent(this@GateLandingActivity, GateOutActivity::class.java)
            intent.putExtra("TITLE", "Gate Out")
            startActivityForResult(
                intent,
                GateLandingActivity.START_ACTIVITY_GATE_IN_LANDING_PAGE
            )
        }


    }
    override fun onBackPressed() {


        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.app_name)
            .setMessage("Do you want to Logout?")
            .setPositiveButton("YES", { dialog, which ->

                preferencesEditor?.putString("issignedin", "false")
                preferencesEditor?.apply()

                finish()

            }).setNegativeButton("NO", null).show()



    }

}