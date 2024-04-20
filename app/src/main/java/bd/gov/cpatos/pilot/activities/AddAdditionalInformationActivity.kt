package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Button
import android.widget.Toast
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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddAdditionalInformationActivity : AppCompatActivity() {
    companion object {
        const val START_ACTIVITY_SIGNATUREVIEW_REQUEST_CODE = 704
    }
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var isSignedIn:String? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null



    private var VVD_GKEY = ""
    private var ROTATION = ""
    private var REQ_TYPE = ""
    private var ACTIVITY_FOR = ""
    private var BERTH_FROM = ""
    private var DEPURTURE_OF_PILOT = ""

    private var etEditionalTug: TextInputEditText? =null
    private var etAdditionalPilot: TextInputEditText? =null
    private var etRemarks: TextInputEditText? =null
    private var etDraught: TextInputEditText? =null

    private var swEngine: SwitchMaterial? =null
    private var swAnchor: SwitchMaterial? =null
    private var swRudder: SwitchMaterial? =null
    private var swRpm: SwitchMaterial? =null
    private var swBowthruster: SwitchMaterial? =null
    private var swSoalConvention: SwitchMaterial? =null
    private var swdayNight: SwitchMaterial? =null
    private var swHoliday: SwitchMaterial? =null
    private var btnSaveAndNext: Button? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_additional_information)
        init()
        getUI()
        //getVesselEvent()
        btnSaveAndNext!!.setOnClickListener {

            if(etAdditionalPilot?.text.toString() !="" && etEditionalTug?.text.toString() !="") {
                vesselEventAction()
            }else{
                Toast.makeText( applicationContext,"Fill up  Empty Field",Toast.LENGTH_LONG).show()
            }

        }



    }
    private fun init (){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = this.title
            VVD_GKEY = bundle.getString("VVD_GKEY").toString()
            ROTATION = bundle.getString("ROTATION").toString()
            ACTIVITY_FOR = bundle.getString("ACTIVITY_FOR").toString()
            BERTH_FROM = bundle.getString("BERTH_FROM").toString()
            DEPURTURE_OF_PILOT = bundle.getString("DEPURTURE_OF_PILOT").toString()
           // Log.e("ROTATION-2", ROTATION)


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
    private fun getUI(){

        etEditionalTug = findViewById(R.id.etEditionalTug)
        etAdditionalPilot = findViewById(R.id.etAdditionalPilot)
        etRemarks = findViewById(R.id.etRemarks)
        etDraught = findViewById(R.id.etDraught)
        swEngine = findViewById(R.id.swEngine)
        swAnchor = findViewById(R.id.swAnchor)
        swRudder = findViewById(R.id.swRudder)
        swRpm = findViewById(R.id.swRpm)
        swBowthruster = findViewById(R.id.swBowthruster)
        swSoalConvention = findViewById(R.id.swSoalConvention)
        swdayNight = findViewById(R.id.swdayNight)
        swHoliday = findViewById(R.id.swHoliday)
        btnSaveAndNext = findViewById(R.id.btnSaveAndNext)

        etEditionalTug?.filters = arrayOf<InputFilter>(MinMaxFilter( 0, 5))
        etAdditionalPilot?.filters = arrayOf<InputFilter>(MinMaxFilter(0, 5))
        etDraught?.filters = arrayOf<InputFilter>(InputFilterMinMax( 1.0F, 10.0F))




    }
    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        // Initialized
        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        // Check if input c is in between min a and max b and
        // returns corresponding boolean
        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }


    inner class InputFilterMinMax(min:Float, max:Float): InputFilter {
        private var min:Float = 0.0F
        private var max:Float = 0.0F

        init{
            this.min = min
            this.max = max
        }

        override fun filter(source:CharSequence, start:Int, end:Int, dest: Spanned, dstart:Int, dend:Int): CharSequence? {
            try
            {
                val input = (dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length)).toFloat()
                if (isInRange(min, max, input))
                    return null
            }
            catch (nfe:NumberFormatException) {}
            return ""
        }

        private fun isInRange(a:Float, b:Float, c:Float):Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
    private fun isHoliday():Boolean{
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = formatter.parse(DEPURTURE_OF_PILOT)
        var calendar = Calendar.getInstance()
        calendar.time = date
        var day:Int = calendar[Calendar.DAY_OF_WEEK]
        var holiday:Boolean =false
        if(day==6 || day ==7 )
            holiday = true
        return holiday
    }
    private fun isNight():Boolean{

        return true
    }

    private fun additionalDataButtonAction(){
     //   vesselEventAction()
        btnSaveAndNext!!.setOnClickListener {

            if(etAdditionalPilot?.text.toString() !="" && etEditionalTug?.text.toString() !="") {
                var intent = Intent(this@AddAdditionalInformationActivity, FinalSubmitPilotOperationActivity::class.java)
                if (ACTIVITY_FOR == "incoming")
                    intent.putExtra("TITLE", "Incoming Vessel:(" + ROTATION + ")")
                else if (ACTIVITY_FOR == "shifting")
                    intent.putExtra("TITLE", "Shifting Vessel:(" + ROTATION + ")")
                else
                    intent.putExtra("TITLE", "Outgoing Vessel:(" + ROTATION + ")")
                // Pass the values to next activity (StationActivity)
                intent.putExtra("VVD_GKEY", VVD_GKEY)
                intent.putExtra("ROTATION", ROTATION)
                intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
                intent.putExtra("BERTH_FROM", BERTH_FROM)

                startActivityForResult(intent, START_ACTIVITY_SIGNATUREVIEW_REQUEST_CODE)

            }else{
                Toast.makeText( applicationContext,"Fill up  Empty Field",Toast.LENGTH_LONG).show()
            }

        }

    }
    private fun getVesselEvent() {
        var dialog = ProgressDialog.progressDialog(this@AddAdditionalInformationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                  //  Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")

                        if (data.length() > 0) {
                            var dataInner: JSONObject = data.getJSONObject(0)

                            val aditional_pilot = dataInner.getString("aditional_pilot")
                            val aditional_tug = dataInner.getString("aditional_tug")
                            val remarks = dataInner.getString("remarks")
                            val draught = dataInner.getString("draught")

                            etAdditionalPilot?.setText(aditional_pilot)
                            etEditionalTug?.setText(aditional_tug)
                            etRemarks?.setText(if(remarks=="") "N/A" else remarks)
                            etDraught?.setText(if(draught=="null") "" else draught)

                        }
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
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@AddAdditionalInformationActivity)
        queue?.add(stringRequest)

    }
    private fun vesselEventAction() {

        var dialog = ProgressDialog.progressDialog(this@AddAdditionalInformationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_SET_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject: JSONObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                   // Log.e("success", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        var intent = Intent(this@AddAdditionalInformationActivity, FinalSubmitPilotOperationActivity::class.java)
                        if (ACTIVITY_FOR == "incoming")
                            intent.putExtra("TITLE", "Incoming Vessel:(" + ROTATION + ")")
                        else if (ACTIVITY_FOR == "shifting")
                            intent.putExtra("TITLE", "Shifting Vessel:(" + ROTATION + ")")
                        else
                            intent.putExtra("TITLE", "Outgoing Vessel:(" + ROTATION + ")")
                        // Pass the values to next activity (StationActivity)
                        intent.putExtra("VVD_GKEY", VVD_GKEY)
                        intent.putExtra("ROTATION", ROTATION)
                        intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
                        intent.putExtra("BERTH_FROM", BERTH_FROM)

                        startActivityForResult(intent, START_ACTIVITY_SIGNATUREVIEW_REQUEST_CODE)



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
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                var engine:Int = 0
                var anchore:Int = 0
                var rudder:Int = 0
                var rpm:Int = 0
                var bowthurster:Int = 0
                var soalconvention:Int = 0
                var daynight:Int = 0
                var holiday:Int = 0
                if(swEngine?.isChecked!!){
                    engine = 1
                }
                if(swAnchor?.isChecked!!){
                    anchore = 1
                }
                if(swRudder ?.isChecked!!){
                    rudder = 1
                }
                if(swRpm?.isChecked!!){
                    rpm = 1
                }
                if(swBowthruster?.isChecked!!){
                    bowthurster = 1
                }
                if(swSoalConvention?.isChecked!!){
                    soalconvention = 1
                }
                if(swdayNight?.isChecked!!){
                    daynight = 1
                }
                if(swHoliday?.isChecked!!){
                    holiday = 1
                }
                val params = HashMap<String, String>()
                params.put("rotation", ROTATION)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("pilot_name", SignedInLoginId.toString())
                params.put("request_type", "additional")
                params.put("request_for", ACTIVITY_FOR)
                params.put("draught", etDraught?.text.toString())
                params.put("addiPilot", etAdditionalPilot?.text.toString())
                params.put("addiTug", etEditionalTug?.text.toString())
                params.put("remarks", etRemarks?.text.toString())
                params.put("is_main_engine_ok",engine.toString())
                params.put("is_acnchors_ok",anchore.toString())
                params.put("is_rudder_indicator_ok", rudder.toString())
                params.put("is_rpm_indicator_ok", rpm.toString())
                params.put("is_bow_therster_available", bowthurster.toString())
                params.put("is_complying_soal_convention", soalconvention.toString())
                params.put("is_night", daynight.toString())
                params.put("is_holiday", holiday.toString())


                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@AddAdditionalInformationActivity)
        queue?.add(stringRequest)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_SIGNATUREVIEW_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val message = data!!.getStringExtra("message")
                // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                sendDataBackToPreviousActivity()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendDataBackToPreviousActivity() {
        val intent = Intent().apply {
            putExtra("message", "This is a message from Activity3")
            // Put your data here if you want.
            //finish()
        }
        setResult(Activity.RESULT_OK, intent)
    }


}


