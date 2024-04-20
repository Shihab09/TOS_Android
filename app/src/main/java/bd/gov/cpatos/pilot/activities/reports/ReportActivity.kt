package bd.gov.cpatos.pilot.activities.reports

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.adapters.VesselReportListAdapter
import bd.gov.cpatos.pilot.models.VesselReport
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_vessel_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ReportActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var isSignedIn:String? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null

    private var ACTIVITY_FOR = ""
    private var SERVICE = ""
    private var FROMDATE = ""
    private var TODATE = ""
    private var USER_ID=""
    private var searchVessel: SearchView? = null
    private var mContext: Context? = null
    private var adapter: VesselReportListAdapter? =null
    lateinit var reportActivity: ReportActivity
    companion object {
        const val START_ACTIVITY_VESSELEDETAILS_REQUEST_CODE = 701
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        init()
        getReport()
    }
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        searchVessel = findViewById(R.id.searchVessel)
        mContext = this
        reportActivity =this
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            FROMDATE = bundle.getString("FROMDATE").toString()
            TODATE = bundle.getString("TODATE").toString()
            SERVICE = bundle.getString("SERVICE").toString()
            USER_ID = bundle.getString("USER_ID").toString()
            if(SERVICE=="Incoming")
                ACTIVITY_FOR="incoming"
            else if(SERVICE=="Shifting")
                ACTIVITY_FOR="shifting"
            else if(SERVICE=="Outgoing")
                ACTIVITY_FOR="outgoing"
            else if(SERVICE=="Cancel")
                ACTIVITY_FOR="cancel"
            else
                ACTIVITY_FOR="all"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        isSignedIn = mPreferences?.getString("issignedin", "false")
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun getReport() {
        var dialog = ProgressDialog.progressDialog(this@ReportActivity)
        dialog.show()
        val vList = arrayListOf<VesselReport>()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_REPORT_LIST,
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
                            Log.d(">>>>>>", dataInner.getString("Vessel_Name"))
                            vList.add(
                                VesselReport( dataInner.getString("vvd_gkey"),dataInner.getString("Import_Rotation_No"),
                                    dataInner.getString("Vessel_Name"),dataInner.getString("Name_of_Master"),dataInner.getString("input_type"))
                            )
                        }
                        searchVessel?.setOnQueryTextListener(this)


                        adapter = VesselReportListAdapter(reportActivity,this, vList,ACTIVITY_FOR)
                        ReportList.adapter = adapter
//                        vesselList.setOnItemClickListener { _, _, position, _ ->
//                            val intent = Intent(this@VesselListTypeWiseActivity, VesselDetailsActivity::class.java)
//                            // Pass the values to next activity (StationActivity)
//                            intent.putExtra("TITLE", "Vessle Details")
//                            intent.putExtra("VVD_GKEY", vList[position].vvdGkey)
//                            intent.putExtra("ROTATION", vList[position].ibVvg)
//                            intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
//                            Log.e("ROTATION", vList[position].ibVvg)
//                            startActivity(intent)
//                        }
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
                params.put("user_id", USER_ID)
                params.put("report_type", SERVICE)
                params.put("start_date", FROMDATE)
                params.put("end_date", TODATE)
                params.put("pilot_name", SignedInLoginId.toString())
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@ReportActivity)
        queue?.add(stringRequest)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        val text = newText!!
        System.out.println("TEXT"+text)
        adapter?.filter!!.filter(newText.toString())
        return false
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    override fun onBackPressed() {
        sendDataBackToPreviousActivity()
    }
    private fun sendDataBackToPreviousActivity() {
        val intent = Intent().apply {
            putExtra("message", "This is a message from Activity3")
            // Put your data here if you want.
            finish()
        }
        setResult(Activity.RESULT_OK, intent)
    }
}




