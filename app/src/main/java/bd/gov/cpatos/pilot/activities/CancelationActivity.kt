package bd.gov.cpatos.pilot.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CancelationActivity : AppCompatActivity() {
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var isSignedIn: String? = null
    private var SignedInId: String? = null
    private var SignedInLoginId: String? = null
    private var SignedInSection: String? = null
    private var SignedInUserRole: String? = null
    //----//
    private var VVD_GKEY = ""
    private var ROTATION = ""
    private var ACTIVITY_FOR = ""
    private var relLayPOB: RelativeLayout? = null
    private var relLayDOP: RelativeLayout? = null
    private var relLayCancelAt: RelativeLayout? = null
    private var relLayCancellMovement: RelativeLayout? = null
    private var relLayCancelReason: RelativeLayout? = null
    private var linLayoutNextButton: LinearLayout? = null
    private var etOnBoardDate: TextInputEditText? = null
    private var etOnBoardTime: TextInputEditText? = null
    private var etOffBordDate: TextInputEditText? = null
    private var etOffBordTime: TextInputEditText? = null
    private var etCancelledDate: TextInputEditText? = null
    private var etCancelledTime: TextInputEditText? = null
    private var actvBerthFrom: AutoCompleteTextView? = null
    private var actvBerthTo: AutoCompleteTextView? = null
    private var swGearProblem: SwitchMaterial? = null
    private var swEngineProblem: SwitchMaterial? = null
    private var swVesselNotReady: SwitchMaterial? = null
    private var swOthers: SwitchMaterial? = null
    private var txtInputLayoutRemarks: TextInputLayout? = null
    private var etRemarks: TextInputEditText? = null
    private var Berth: ArrayList<String> = arrayListOf<String>()
    private var btnNext: Button? = null
    private var mContext: Context? = null
    private val calander: Calendar = Calendar.getInstance()
    private val date_format = SimpleDateFormat("yyyy-MM-dd")
    companion object {
        const val START_ACTIVITY_REPORT = 707
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancelation)
        mContext = this
        init()
        findViewById()
        getBerth()
        btnNext?.setOnClickListener {
            vesselEventAction()
        }
    }

    private fun init() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = this.title
            VVD_GKEY = bundle.getString("VVD_GKEY")!!
            ROTATION = bundle.getString("ROTATION")!!
            ACTIVITY_FOR = bundle.getString("ACTIVITY_FOR")!!
//            Log.e("ROTATION-2", ROTATION)
//            Log.e("ROTATION-2", VVD_GKEY)
//            Log.e("ROTATION-2", ACTIVITY_FOR)

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
    private fun findViewById() {
        relLayPOB = findViewById(R.id.relLayPOB)
        relLayDOP = findViewById(R.id.relLayDOP)
        relLayCancelAt = findViewById(R.id.relLayCancelAt)
        relLayCancellMovement = findViewById(R.id.relLayCancellMovement)
        relLayCancelReason = findViewById(R.id.relLayCancelReason)
        linLayoutNextButton = findViewById(R.id.liLay05)

        etOnBoardDate = findViewById(R.id.etOnBoardDate)
        etOnBoardTime = findViewById(R.id.etOnBoardTime)
        etOffBordDate = findViewById(R.id.etOffBordDate)
        etOffBordTime = findViewById(R.id.etOffBordTime)
        etCancelledDate = findViewById(R.id.etCancelledDate)
        etCancelledTime = findViewById(R.id.etCancelledTime)
        actvBerthFrom = findViewById(R.id.actvBerthFrom)
        actvBerthTo = findViewById(R.id.actvBerth)
        swGearProblem = findViewById(R.id.swGearProblem)
        swEngineProblem = findViewById(R.id.swEngineProblem)
        swVesselNotReady = findViewById(R.id.swVesselNotReady)
        swOthers = findViewById(R.id.swOthers)
        txtInputLayoutRemarks = findViewById(R.id.txtInputLayoutRemarks)
        etRemarks = findViewById(R.id.etRemarks)
        btnNext = findViewById(R.id.btnNext)

        val date_time: String = date_format.format(calander.time)
        etOnBoardDate?.setText(date_time)
        etOffBordDate?.setText(date_time)
        etCancelledDate?.setText(date_time)


        swOthers?.setOnCheckedChangeListener { _, isChecked -> if (isChecked) {
            txtInputLayoutRemarks?.visibility = View.VISIBLE
        }else {
            txtInputLayoutRemarks?.visibility = View.GONE}

        }

    }
    private fun getBerth() {
        var dialog = ProgressDialog.progressDialog(this@CancelationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_BERTH_LIST,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                  //  Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")
                        //val berthFrom = jsonObject.getString("berthFrom")
                        //  actvBerthFrom?.setText(berthFrom)
                        // actvBerth?.setText(berthFrom)

                        for (i in 0 until data.length()) {
                            var dataInner: JSONObject = data.getJSONObject(i)
                            val gkey = dataInner.getString("gkey")
                            val berth_name = dataInner.getString("berth_name")
                            if (berth_name != "") {
                                Berth.add(berth_name)
                            }
                        }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Berth)
                        actvBerthFrom?.setAdapter(adapter)
                        actvBerthTo?.setAdapter(adapter)
                    } else {
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    // Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            })
        {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@CancelationActivity)
        queue?.add(stringRequest)
    }

    private fun vesselEventAction() {
        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("CANCEL")
            .setMessage("Do you want to cancel?")
            .setPositiveButton("YES") { dialog, which ->
                postCancelData()
            }.setNegativeButton("NO", null)
            .show()
    }
    private fun postCancelData(){
        var dialog = ProgressDialog.progressDialog(this@CancelationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_SET_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject:JSONObject= JSONObject(response)
                    val success = jsonObject.getString("success")
                  //  Log.e("Message", jsonObject.toString())

                    if (success.equals("1")) {
                        dialog.dismiss()
                        Toast.makeText(applicationContext, "Successfully Cancelled", Toast.LENGTH_LONG).show()

                        var intent = Intent(this@CancelationActivity, ReportViewActivity::class.java)
                        intent.putExtra("TITLE", "Cancel Vessel:(" + ROTATION + ")")
                        intent.putExtra("VVD_GKEY", VVD_GKEY)
                        intent.putExtra("ROTATION", ROTATION)
                        intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)

                        startActivityForResult(intent, START_ACTIVITY_REPORT)

                        Log.e("VVD:",VVD_GKEY)
                        Log.e("VVD:",ROTATION)
                        Log.e("VVD:",ACTIVITY_FOR)



                    }else {
                        dialog.dismiss()
                        Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                    }
                }catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    // Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
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

                var reason = ""
                var firstLine = ""
                var lastLine = ""
                if(swGearProblem?.isChecked!!){
                    reason = "Gear Problem "
                }
                if(swEngineProblem?.isChecked!!){
                    reason = "Engine Problem"
                }
                if(swVesselNotReady?.isChecked!!){
                    reason ="Vessel Not Ready"
                }
                if(swOthers?.isChecked!!){
//                    reason = reason+", "+etRemarks?.text.toString()
                    reason="Others"
                }


                val params = HashMap<String, String>()
                params.put("rotation", ROTATION)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("pilot_name", SignedInLoginId.toString())
                params.put("pob",
                    etOnBoardDate?.text.toString().trim()+" "+setTime(etOnBoardTime?.text.toString())
                        .trim()
                )
                params.put("dop",
                    etOffBordDate?.text.toString().trim()+" "+setTime(etOffBordTime?.text.toString())
                        .trim()
                )
                params.put("cancel_at",
                    etCancelledDate?.text.toString().trim()+" "+setTime(etCancelledTime?.text.toString())
                        .trim()
                )
                params.put("cancel_from", actvBerthFrom?.text.toString())
                params.put("cancel_to", actvBerthTo?.text.toString())
                params.put("firstLine", firstLine)
                params.put("lastLine", lastLine)
                params.put("remarks", reason)
                params.put("request_type", "cancel")
                params.put("request_for", ACTIVITY_FOR)
                params.put("final_submit", "0")
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@CancelationActivity)
        queue?.add(stringRequest)

    }
    private fun setTime(myTime:String):String{

        val newTime = myTime.substring(0,2)+":"+myTime.substring(2,myTime.length)

        return newTime
    }
}