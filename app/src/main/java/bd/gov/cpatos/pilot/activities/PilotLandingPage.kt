package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.main.*
import bd.gov.cpatos.pilot.activities.reports.ReportActivity
import bd.gov.cpatos.signinsignup.ResetPassword
import bd.gov.cpatos.utils.*
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class PilotLandingPage : AppCompatActivity(), DatePickerDialog.OnDateSetListener,View.OnClickListener {
    companion object {
        const val START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE = 700
    }

    private var issignedIn: String?=null
    private var SignedInId:String? = null

    private var SignedInLoginId:String? = null
    private var SignedInSection:String? = null
    private var SignedInUserRole:String? = null
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var btnIncoming:Button? = null
    private var btnShifting:Button? = null
    private var btnOutgoing:Button? = null
    private var btnCancel:Button? = null
    private var btnReportView:Button? = null
    private var tvTotalIncoming: TextView?= null
    private var tvTotalShifting: TextView?= null
    private var tvTotalOutGoing: TextView?= null
    private var tvTotalCancel: TextView?= null
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
    private var etToDate:TextInputEditText?=null
    private var ServiceType:AutoCompleteTextView?=null
    private var mContext: Context?=null

  //  private val sharedPreference: SharedPreference =SharedPreference(this)
    private val type = arrayOf("Incoming", "Shifting", "Outgoing","Cancel", "All")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_landing_page)
        mContext = this
        val networkConnection= NetworkConnection(mContext as PilotLandingPage)

        init()
        getUserPreferenceData()
        getUI()
        dateTimeFieldIconButtonAction()
        geSummery()
       // Log.e(sharedPreference.toString(),"pref")

    }

    private fun init(){
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

//          private fun getUserData(){
//
//
//          UserData.init(getApplicationContext());
//          UserData.getString("LATITUDE", "")
//          UserData.putString("LATITUDE", "")
//gc
//    }


    private fun getUserPreferenceData() {

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor =mPreferences?.edit()
        issignedIn =mPreferences?.getString("issignedin", "false")
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")

//        sharedPreference.getValueString("isSignedin","False")
//        sharedPreference.getValueString("SignedInId","null" )
//        sharedPreference.getValueString("isSignedin","False")
//        sharedPreference.getValueString("isSignedin","False")
//        sharedPreference.getValueString("isSignedin","False")

          }


    private fun getUI(){
        tvTotalIncoming = findViewById(R.id.tvTotalIncoming)
        tvTotalShifting = findViewById(R.id.tvTotalShifting)
        tvTotalOutGoing = findViewById(R.id.tvTotalOutGoing)
        tvTotalCancel = findViewById(R.id.tvTotalCancel)
        btnFromDate = findViewById(R.id.btnFromDate)
        btnToDate = findViewById(R.id.btnToDate)
        etFormDate = findViewById(R.id.etFormDate)
        etToDate = findViewById(R.id.etToDate)
        ServiceType =findViewById(R.id.ServiceType)
        btnIncoming =findViewById(R.id.btnIncoming)
        btnShifting =findViewById(R.id.btnShifting)
        btnOutgoing =findViewById(R.id.btnOutgoing)
        btnCancel =findViewById(R.id.btnCancel)
        btnReportView =findViewById(R.id.btnReportView)

        btnIncoming?.setOnClickListener(this)
        btnShifting?.setOnClickListener(this)
        btnOutgoing?.setOnClickListener(this)
        btnCancel?.setOnClickListener(this)
        btnReportView?.setOnClickListener(this)

        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item,type)
        ServiceType?.setAdapter(adapter)



   }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnIncoming ->{
                val intent = Intent(this@PilotLandingPage, SearchActivity::class.java)
                intent.putExtra("TITLE", "Incoming Vessel List")
                intent.putExtra("ACTIVITY_FOR", "incoming")
                startActivityForResult(intent, START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE)
            }
            R.id.btnShifting ->{
                val intent = Intent(this@PilotLandingPage, VesselListTypeWiseActivity::class.java)
                intent.putExtra("TITLE", "Shifting Vessel List")
                intent.putExtra("ACTIVITY_FOR", "shifting")
                startActivityForResult(intent,START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE)
            }
            R.id.btnOutgoing ->{
                val intent = Intent(this@PilotLandingPage, VesselListTypeWiseActivity::class.java)
                intent.putExtra("TITLE", "Outgoing Vessel List")
                intent.putExtra("ACTIVITY_FOR", "outgoing")
                startActivityForResult(intent,START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE)
            }
            R.id.btnCancel ->{
                val intent = Intent(this@PilotLandingPage, VesselListTypeWiseActivity::class.java)
                intent.putExtra("TITLE", "Vessel List")
                intent.putExtra("ACTIVITY_FOR", "cancel")
                startActivityForResult(intent,START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE)
            }
            R.id.btnReportView ->{
                var strFormDate = etFormDate?.text.toString()
                var strToDate = etToDate?.text.toString()
                var strServiceType = ServiceType?.text.toString()
                if (strFormDate != "" && strToDate != "" && strServiceType != "") {
                    var intent = Intent(this@PilotLandingPage, ReportActivity::class.java)
                    intent.putExtra("TITLE","Report On " + strServiceType + "(" + strFormDate + " To " + strToDate + ")")
                    intent.putExtra("FROMDATE", strFormDate)
                    intent.putExtra("TODATE", strToDate)
                    intent.putExtra("SERVICE", strServiceType)
                    intent.putExtra("USER_ID", SignedInLoginId.toString())
                    startActivityForResult(intent,START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE)
                }else{
                    Toast.makeText(mContext,"From date or Todate or Service type should Not be empty",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkNetworkConnection() {

//        cld.observe(this, { isConnected ->
//            if (isConnected){
//
//            }else{
//            }
//        })
    }
    private fun geSummery() {
        var dialog = ProgressDialog.progressDialog(this@PilotLandingPage)
        dialog.show()
          val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_SUMMERY,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    if (success.equals("1")) {
                        dialog.dismiss()
                        //  val dStations = JSONArray(jsonObject.getString("data"))
                        val dStations: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until dStations.length()) {
                            var dataInner: JSONObject = dStations.getJSONObject(i)

                            tvTotalIncoming?.text= dataInner.getString("total_vsl_arrival")
                            tvTotalOutGoing?.text= dataInner.getString("total_vsl_depart")
                            tvTotalShifting?.text= dataInner.getString("total_vsl_shift")
                            tvTotalCancel?.text= dataInner.getString("total_vsl_cancel")

                        }

                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("user_id", SignedInLoginId.toString())
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@PilotLandingPage)
        queue?.add(stringRequest)
    }

    private fun dateTimeFieldIconButtonAction(){
        btnFromDate!!.setEndIconOnClickListener {
         //   Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(this@PilotLandingPage,this@PilotLandingPage,year,month,day)
            datePickerDialog.show()
            SELECTED_BOX = 1
        }
        btnToDate!!.setEndIconOnClickListener {
           // Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(this@PilotLandingPage,this@PilotLandingPage,year,month,day)
            datePickerDialog.show()
            SELECTED_BOX = 2
        }

    }


    override fun onBackPressed() {

            AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage("Do you want to Logout?")
                .setPositiveButton("YES") { _, _ ->

                    preferencesEditor?.putString("issignedin", "false")
                    preferencesEditor?.remove("issignedin")
                    finish()
                    preferencesEditor?.clear()
                }.setNegativeButton("NO", null)
                .setNeutralButton("Remember Pass") { _, _ ->
                    finish()
                }.show()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        myYear = year
        myday = dayOfMonth
        myMonth = month
        val myadatetime = myYear.toString() + "-" + String.format("%02d", myMonth + 1) + "-" + String.format("%02d",myday)
        when (SELECTED_BOX) {
            1 -> etFormDate?.setText(myadatetime)
            2 -> etToDate?.setText(myadatetime)
            else ->  SELECTED_BOX=0
        }
        SELECTED_BOX=0

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data!!.getStringExtra("message")
                //    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                geSummery()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_resetpass -> {
                val intent = Intent(this@PilotLandingPage, ResetPassword::class.java)
                intent.putExtra("TITLE", "Reset Password")
                 // ContextCompat.startActivity(this, intent, Bundle())
                startActivityForResult(intent,
                    START_ACTIVITY_REPORT_OR_VESSELLIST_REQUEST_CODE
                )
                true
            }
            R.id.action_logout ->{

                AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.app_name)
                    .setMessage("Do you want to Logout?")
                    .setPositiveButton("YES") { _, _ ->

                        preferencesEditor?.putString("issignedin","false")
                        preferencesEditor?.remove("issignedin")
//                        sharedPreference?.apply()
                        preferencesEditor?.commit()
                        finish()
                        preferencesEditor?.clear()
                    }.setNegativeButton("NO", null)
                    .setNeutralButton("Remember Pass") { _, _ ->
                        finish()
                    }.show()

                return true

            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}