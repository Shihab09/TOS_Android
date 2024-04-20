package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.log


class FinalSubmitPilotOperationActivity : AppCompatActivity() {
    companion object {
        const val START_ACTIVITY_REPORTVIEW_REQUEST_CODE = 705
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
    private var ACTIVITY_FOR = ""
    private var BERTH_FROM = ""

    var rotation: String? =null
    var vvd_gkey:String? = null
    var pilot_name:String? = null
    var vsl_type:String? = null
    var remarks:String? = null
    var berth_name:String? = null
    var signString:String? = null
    //var loginDataModels: LoginDataModel? = null
    var mContext: Context?=null
    var txtLabel: TextView? =null
    var txtVslName: TextInputEditText? = null
    var txtCallSign:TextInputEditText? = null
    var txtGt:TextInputEditText? = null
    var txtNt:TextInputEditText? = null
    var txtDeckCargo:TextInputEditText? = null
    var txtLoa:TextInputEditText? = null
    var txtFlag:TextInputEditText? = null
    var txtAgent:TextInputEditText? = null
    var txtRotation:TextInputEditText? = null
    var txtPilotName:TextInputEditText? = null
    var txtOnBoard: TextInputEditText? =null
    var txtOffBoard:TextInputEditText? = null
    var txtFirstLine:TextInputEditText? = null
    var txtLastLine:TextInputEditText? = null
    var txtAdditionalPilot:TextInputEditText? = null
    var txtAdditionalTug:TextInputEditText? = null
    var txtRemarks:TextInputEditText? = null
   // var txtBerth:TextInputEditText? = null
    //var btnRptUrl:Button? = null
    private var mSignaturePad: SignaturePad? = null
    private var mClearButton: Button? = null
    private var btnEdit: Button?=null
    private var mSaveButton: Button? = null
    var bitmap: Bitmap? = null

    private var tvLlHeading: TextView? = null
    private var llOnBoard: LinearLayout? = null
    private var llOffBoard: LinearLayout? = null
    private var llFirstLine: LinearLayout? = null
    private var llLastLine: LinearLayout? = null
  //  private var btnEdit: ImageButton?=null
    private var buttonPressCount:Int? =0
//    private var linLayoutOnboard: LinearLayout? =null
//    private var linLayoutLastLine: LinearLayout? =null
//    private var linLayoutFirstLine: LinearLayout? =null
//    private var linLayoutOffBoard: LinearLayout? =null
//    private var linLayoutNextButton: LinearLayout? =null
    private var linLayoutShiftFrom: LinearLayout? =null //
    private var relLayPOB: RelativeLayout? =null
    private var relLayLastLine: RelativeLayout? =null
    private var relLayFirstLine: RelativeLayout? =null
    private var relLayDOP: RelativeLayout? =null
    private var etShiftingFrom: AutoCompleteTextView? =null
    private var etOnBoardDate: TextInputEditText? =null
    private var etOnBoardTime: TextInputEditText? =null
    private var etLastLineDate: TextInputEditText? =null
    private var etLastLineTime: TextInputEditText? =null
    private var etLastLineTitle: TextView? =null
    private var etFirstLineDate: TextInputEditText? =null
    private var etFirstLineTime: TextInputEditText? =null
    private var toBertTitle: TextInputLayout?=null
    private var etOffBordDate: TextInputEditText? =null
    private var etOffBordTime: TextInputEditText? =null
    private var Berth : ArrayList<String> = arrayListOf<String>()
    private var actvBerth: AutoCompleteTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature_view)
        init()
        findViewById()
        getBerth()
        ShowAndHideData()
        getAllFinalData()
        addButtonAction()

    }
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        mContext = this
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            VVD_GKEY = bundle.getString("VVD_GKEY")!!
            ROTATION = bundle.getString("ROTATION")!!
            ACTIVITY_FOR = bundle.getString("ACTIVITY_FOR")!!
            BERTH_FROM = bundle.getString("BERTH_FROM")!!
            title = bundle.getString("TITLE") // 1

           // supportActionBar?.title = this.title
            supportActionBar?.title = "Review("+ROTATION+")"

         //   Log.e("ROTATION-2", ROTATION)
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
    private fun findViewById(){
       // txtLabel =findViewById(R.id.txtLabel);
        txtVslName =findViewById(R.id.etVesselName)
        txtCallSign =findViewById(R.id.etCallSign)
        txtGt =findViewById(R.id.etGt)
        txtNt =findViewById(R.id.etNt)
        txtDeckCargo =findViewById(R.id.etDeckCargo)
        txtLoa =findViewById(R.id.etLoa)
        txtFlag =findViewById(R.id.etFlag)
        txtAgent =findViewById(R.id.etAgent)
        txtRotation =findViewById(R.id.etRotation)
        txtPilotName =findViewById(R.id.etPilotName)
        btnEdit = findViewById(R.id.btnEdit)
//        txtOnBoard =findViewById(R.id.txtOnBoard);
//        txtOffBoard =findViewById(R.id.txtOffBoard);
//        txtFirstLine =findViewById(R.id.txtFirstLine);
//        txtLastLine =findViewById(R.id.txtLastLine);
        txtAdditionalPilot =findViewById(R.id.txtAdditionalPilot)
        txtAdditionalTug =findViewById(R.id.txtAdditionalTug)
        txtRemarks =findViewById(R.id.txtRemarks)
        // txtBerth =findViewById(R.id.txtBerth);
       // btnRptUrl =findViewById(R.id.btnRptUrl);
      //  mClearButton = findViewById(R.id.btnClear)
        mSaveButton = findViewById(R.id.btnSaveSign)
      //  mSignaturePad = findViewById(R.id.signature_pad)
        etOnBoardDate = findViewById(R.id.etOnBoardDate)
        etOnBoardTime = findViewById(R.id.etOnBoardTime)
        etShiftingFrom= findViewById(R.id.etShiftingFrom)
        etLastLineDate = findViewById(R.id.etLastLineDate)
        etLastLineTime = findViewById(R.id.etLastLineTime)
        etLastLineTitle = findViewById(R.id.etLastLineTitle)
        etFirstLineDate = findViewById(R.id.etFirstLineDate)
        etFirstLineTime = findViewById(R.id.etFirstLineTime)
        toBertTitle = findViewById(R.id.txtInputLayoutToBerth)
        etOffBordDate = findViewById(R.id.etOffBordDate)
        etOffBordTime = findViewById(R.id.etOffBordTime)
//        linLayoutOnboard = findViewById(R.id.liLay01)
//        linLayoutLastLine = findViewById(R.id.liLay02)
//        linLayoutFirstLine = findViewById(R.id.liLay03)
//        linLayoutOffBoard = findViewById(R.id.liLay04)
        linLayoutShiftFrom = findViewById(R.id.liLay01New)
        relLayPOB = findViewById(R.id.relLayPOB)
        relLayLastLine = findViewById(R.id.relLayLastLine)
        relLayFirstLine = findViewById(R.id.relLayFirstLine)
        relLayDOP = findViewById(R.id.relLayDOP)
        actvBerth = findViewById(R.id.actvBerth)
        if(ACTIVITY_FOR =="outgoing"){
            etLastLineTitle?.text = "CAST OFF"
            toBertTitle?.hint = "Type And Select Berth"
            etShiftingFrom?.setText(BERTH_FROM)
        }else if(ACTIVITY_FOR =="shifting"){
            toBertTitle?.hint = "Shifting To Berth"
            etLastLineTitle?.text = "LAST LINE CAST OFF"
            etShiftingFrom?.setText(BERTH_FROM)
        }else{
            etLastLineTitle?.text = "LAST LINE"
            toBertTitle?.hint = "Type And Select Berth"
            etShiftingFrom?.setText(BERTH_FROM)
        }

        //   txtRotation?.text = ROTATION
      //  tvLlHeading= findViewById(R.id.tvLlHeading);
//        llOnBoard= findViewById(R.id.llOnBoard);
//        llOffBoard= findViewById(R.id.llOffBoard);
//        llFirstLine= findViewById(R.id.llFirstLine);
//        llLastLine= findViewById(R.id.llLastLine);






//        mSignaturePad!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
//            override fun onStartSigning() {
//                //Toast.makeText(this@SignatureActivity, "OnStartSigning", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onSigned() {
//                mSaveButton!!.isEnabled = true
//                mClearButton!!.isEnabled = true
//            }
//
//            override fun onClear() {
//                mSaveButton!!.isEnabled = false
//                mClearButton!!.isEnabled = false
//            }
//        })
        btnEdit?.setOnClickListener({
            if(buttonPressCount==0){
                //btnEdit?.setImageResource(R.drawable.tick)
                btnEdit?.text = "UPDATE"
                Toast.makeText(mContext,"Edit",Toast.LENGTH_LONG).show()
                buttonPressCount=1
                activeorInactiveViews(1)
            }else if(buttonPressCount==1){
               // btnEdit?.setImageResource(R.drawable.edit_icon)
                btnEdit?.text = "EDIT"
                Toast.makeText(mContext,"Updated",Toast.LENGTH_LONG).show()
                buttonPressCount=0
                activeorInactiveViews(2)
                vesselEventEdit()
            }
        })
    }
    private fun activeorInactiveViews(a:Int){
        if(a==1){
            txtAdditionalPilot?.isEnabled = true
            txtAdditionalTug?.isEnabled = true
            txtRemarks?.isEnabled = true
          //  txtBerth?.isEnabled = true
            actvBerth?.isEnabled = true
            etOnBoardDate?.isEnabled = true
            etOnBoardTime?.isEnabled = true
            etLastLineDate?.isEnabled = true
            etLastLineTime?.isEnabled = true
            etFirstLineDate?.isEnabled = true
            etFirstLineTime?.isEnabled = true
            etOffBordDate?.isEnabled = true
            etOffBordTime?.isEnabled = true
        }else{
            txtAdditionalPilot?.isEnabled = false
            txtAdditionalTug?.isEnabled = false
            txtRemarks?.isEnabled = false
           // txtBerth?.isEnabled = false
            actvBerth?.isEnabled = false
            etOnBoardDate?.isEnabled = false
            etOnBoardTime?.isEnabled = false
            etLastLineDate?.isEnabled = false
            etLastLineTime?.isEnabled = false
            etFirstLineDate?.isEnabled = false
            etFirstLineTime?.isEnabled = false
            etOffBordDate?.isEnabled = false
            etOffBordTime?.isEnabled = false
        }
    }
    private fun ShowAndHideData(){
        if(ACTIVITY_FOR == "incoming") {
            linLayoutShiftFrom?.visibility = View.GONE
            relLayPOB?.visibility = View.VISIBLE
            relLayLastLine?.visibility = View.GONE
            relLayFirstLine?.visibility = View.VISIBLE
            relLayDOP?.visibility = View.VISIBLE
        }else if(ACTIVITY_FOR == "shifting"){
            linLayoutShiftFrom?.visibility = View.VISIBLE
            relLayPOB?.visibility = View.VISIBLE
            relLayLastLine?.visibility = View.VISIBLE
            relLayFirstLine?.visibility = View.VISIBLE
            relLayDOP?.visibility = View.VISIBLE
        }else if(ACTIVITY_FOR == "outgoing"){
            linLayoutShiftFrom?.visibility = View.GONE
            relLayPOB?.visibility = View.VISIBLE
            relLayLastLine?.visibility = View.VISIBLE
            relLayFirstLine?.visibility = View.GONE
            relLayDOP?.visibility = View.VISIBLE
        }else{
            linLayoutShiftFrom?.visibility = View.GONE
            relLayPOB?.visibility = View.GONE
            relLayLastLine?.visibility = View.GONE
            relLayFirstLine?.visibility = View.GONE
            relLayDOP?.visibility = View.GONE
        }
    }
    private fun  addButtonAction(){
        mSaveButton?.setOnClickListener {
          //  Log.e("FINAL SUBMIT PRESSED","")
            final_submit();
        }

       // mClearButton!!.setOnClickListener { mSignaturePad!!.clear() }
//        mSaveButton?.setOnClickListener {
//            var intent = Intent(this@FinalSubmitPilotOperationActivity, ReportViewActivity::class.java)
//            if (ACTIVITY_FOR == "incoming")
//                intent.putExtra("TITLE", "Incoming Vessel:(" + ROTATION + ")")
//            else if (ACTIVITY_FOR == "shifting")
//                intent.putExtra("TITLE", "Shifting Vessel:(" + ROTATION + ")")
//            else
//                intent.putExtra("TITLE", "Outgoing Vessel:(" + ROTATION + ")")
//            // Pass the values to next activity (StationActivity)
//            intent.putExtra("VVD_GKEY", VVD_GKEY)
//            intent.putExtra("ROTATION", ROTATION)
//            intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
//            startActivityForResult(intent, START_ACTIVITY_REPORTVIEW_REQUEST_CODE)
//        }
    }
    private fun final_submit() {
      //  Log.e("Final Submit","++++++")
        var dialog = ProgressDialog.progressDialog(this@FinalSubmitPilotOperationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_SET_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                     // Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()

                        var intent = Intent(this@FinalSubmitPilotOperationActivity, ReportViewActivity::class.java)
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

                        startActivityForResult(intent, START_ACTIVITY_REPORTVIEW_REQUEST_CODE)

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
                    Toast.makeText(applicationContext, "Try Again final", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                var pob= ""
                var firstLine = ""
                var lastLine = ""
                var dop = ""

                val pobDate= etOnBoardDate?.text.toString().trim()
                val pobTime=etOnBoardTime?.text.toString().trim()
                val lastLineDate= etLastLineDate?.text.toString().trim()
                val lastLineTime=etLastLineTime?.text.toString().trim()
                val firstLineDate= etFirstLineDate?.text.toString().trim()
                val firstLineTime=etFirstLineTime?.text.toString().trim()
                val dopDate= etOffBordDate?.text.toString().trim()
                val dopTime=etOffBordTime?.text.toString().trim()
                if(ACTIVITY_FOR == "incoming"){
                    pob = pobDate+" "+ setTime(pobTime)
                    lastLine = lastLineDate+" "+setTime("0000")
                    firstLine = firstLineDate+" "+setTime(firstLineTime)
                    dop = dopDate+" "+setTime(dopTime)
                }else if(ACTIVITY_FOR == "shifting"){
                    pob = pobDate+" "+ setTime(pobTime)
                    lastLine = lastLineDate+" "+setTime(lastLineTime)
                    firstLine = firstLineDate+" "+setTime(firstLineTime)
                    dop = dopDate+" "+setTime(dopTime)
                }else{
                    pob = pobDate+" "+ setTime(pobTime)
                    lastLine = lastLineDate+" "+setTime(lastLineTime)
                    firstLine = firstLineDate+" "+setTime("0000")
                    dop = dopDate+" "+setTime(dopTime)
                }
                params.put("rotation", ROTATION)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("addition_pilot", txtAdditionalPilot?.text.toString())
                params.put("tug", txtAdditionalTug?.text.toString())
                params.put("pob", pob)
                params.put("remarks", VVD_GKEY)
                params.put("berth", actvBerth?.text.toString())
                params.put("first_line", firstLine)
                params.put("lastline", lastLine)
                params.put("dop", dop)
                params.put("request_for", ACTIVITY_FOR)
                params.put("request_type", ACTIVITY_FOR)
                params.put("final_submit", "1")
                 return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@FinalSubmitPilotOperationActivity)
        queue?.add(stringRequest)
    }



    private fun vesselEventEdit() {
        var dialog = ProgressDialog.progressDialog(this@FinalSubmitPilotOperationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_EDIT_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                   // Log.e("Message", jsonObject.toString())
                    if(success.equals("1")) {
                        dialog.dismiss()
                    }else{
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again e-1", Toast.LENGTH_LONG).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again e-2", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                var pob= ""
                var firstLine = ""
                var lastLine = ""
                var dop = ""

                val pobDate= etOnBoardDate?.text.toString().trim()
                val pobTime=etOnBoardTime?.text.toString().trim()
                val lastLineDate= etLastLineDate?.text.toString().trim()
                val lastLineTime=etLastLineTime?.text.toString().trim()
                val firstLineDate= etFirstLineDate?.text.toString().trim()
                val firstLineTime=etFirstLineTime?.text.toString().trim()
                val dopDate= etOffBordDate?.text.toString().trim()
                val dopTime=etOffBordTime?.text.toString().trim()
                if(ACTIVITY_FOR == "incoming"){
                    pob = pobDate+" "+ setTime(pobTime)
                    lastLine = lastLineDate+" "+setTime("0000")
                    firstLine = firstLineDate+" "+setTime(firstLineTime)
                    dop = dopDate+" "+setTime(dopTime)
                }else if(ACTIVITY_FOR == "shifting"){
                    pob = pobDate+" "+ setTime(pobTime)
                    lastLine = lastLineDate+" "+setTime(lastLineTime)
                    firstLine = firstLineDate+" "+setTime(firstLineTime)
                    dop = dopDate+" "+setTime(dopTime)
                }else{
                    pob = pobDate+" "+ setTime(pobTime)
                    lastLine = lastLineDate+" "+setTime(lastLineTime)
                    firstLine = firstLineDate+" "+setTime("0000")
                    dop = dopDate+" "+setTime(dopTime)
                }
                params.put("rotation", ROTATION)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("addition_pilot", txtAdditionalPilot?.text.toString())
                params.put("tug", txtAdditionalTug?.text.toString())
                params.put("pob", pob)
                params.put("remarks", VVD_GKEY)
                params.put("berth", actvBerth?.text.toString())
                params.put("first_line", firstLine)
                params.put("lastline", lastLine)
                params.put("dop", dop)
                params.put("request_for", ACTIVITY_FOR)
                params.put("final_submit", "0")
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@FinalSubmitPilotOperationActivity)
        queue?.add(stringRequest)
    }
//    private fun upLoadSignatureDate() {
//        val dialog = ProgressDialog.progressDialog(this@FinalSubmitPilotOperationActivity)
//        dialog.show()
//
//        val stringRequest: StringRequest =
//            object : StringRequest(
//                Request.Method.POST, EndPoints.URL_SET_INCOMING_VESSEL_EVENT,
//                object : Response.Listener<String?> {
//                    override fun onResponse(response: String?) {
//                        response?.let { Log.e("anyText", it) }
//                        try {
//                            val jsonObject = JSONObject(response)
//                            val success = jsonObject.getString("success")
//                            val message = jsonObject.getString("message")
//                            if (success == "1") {
//                                dialog.dismiss()
//                                var intent = Intent(this@FinalSubmitPilotOperationActivity, ReportViewActivity::class.java)
//                                if (ACTIVITY_FOR == "incoming")
//                                    intent.putExtra("TITLE", "Incoming Vessel:(" + ROTATION + ")")
//                                else if (ACTIVITY_FOR == "shifting")
//                                    intent.putExtra("TITLE", "Shifting Vessel:(" + ROTATION + ")")
//                                else
//                                    intent.putExtra("TITLE", "Outgoing Vessel:(" + ROTATION + ")")
//                                // Pass the values to next activity (StationActivity)
//                                intent.putExtra("VVD_GKEY", VVD_GKEY)
//                                intent.putExtra("ROTATION", ROTATION)
//                                intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
//                                startActivityForResult(intent, START_ACTIVITY_REPORTVIEW_REQUEST_CODE)
//                            }
//                            if (success == "0") {
//                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                                dialog.dismiss()
//                            }
//                        } catch (e: java.lang.Exception) {
//                            e.printStackTrace()
//                            dialog.dismiss()
//                            Toast.makeText(applicationContext, " Error !1$e", Toast.LENGTH_LONG)
//                                .show()
//
//                        }
//                    }
//                }, Response.ErrorListener { error ->
//                    dialog.dismiss()
//                    Toast.makeText(applicationContext, " Error !2$error", Toast.LENGTH_LONG).show()
//                }) {
//                override fun getParams(): Map<String, String?> {
//                    val image: String = bitmap?.let { getStringImage(it) }!!
//                    val params: MutableMap<String, String?> = HashMap()
//                    params["signature"] = image
//                    params.put("rotation", ROTATION)
//                    params.put("vvd_gkey", VVD_GKEY)
//                    params.put("pilot_name", SignedInLoginId.toString())
//                    params.put("request_type", ACTIVITY_FOR)
//                    params.put("request_for", "SIGNATURE")
//                    return params
//                }
//            }
//        val requestQueue = Volley.newRequestQueue(this)
//        requestQueue.add(stringRequest)
//    }

    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        val encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        println("Encoded Image: $encodedImage")
        return encodedImage
    }
    override fun onSaveInstanceState(outState:Bundle ) {
        super.onSaveInstanceState(outState)
        outState.putString("VVD_GKEY", VVD_GKEY)
        outState.putString("ROTATION", ROTATION)
        outState.putString("ACTIVITY_FOR", ACTIVITY_FOR)
        outState.clear()
    }
    private fun getAllFinalData() {
        var dialog = ProgressDialog.progressDialog(this@FinalSubmitPilotOperationActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_ALL_FINAL_DATA,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                  //  Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")
                         val heading_n4data: JSONArray = jsonObject.getJSONArray("heading_n4data")

                        if (data.length() > 0) {
                            var dataInner: JSONObject = data.getJSONObject(0)
                            val pilot_on_board = dataInner.getString("pilot_on_board")
                            val pilot_off_board = dataInner.getString("pilot_off_board")
                            val mooring_frm_time = dataInner.getString("mooring_frm_time")
                            val mooring_to_time = dataInner.getString("mooring_to_time")
                            val aditional_pilot = dataInner.getString("aditional_pilot")
                            val aditional_tug = dataInner.getString("aditional_tug")
                            val remarks = dataInner.getString("remarks")
                            val draught = dataInner.getString("draught")
                            val berth = dataInner.getString("berth")
                            if (heading_n4data.length() > 0) {
                                var heading_n4dataInner: JSONObject = heading_n4data.getJSONObject(0)
                                val radio_call_sign = heading_n4dataInner.getString("radio_call_sign")
                                val loa_cm = heading_n4dataInner.getString("loa_cm")
                                val gross_registered_ton = heading_n4dataInner.getString("gross_registered_ton")
                                val net_registered_ton = heading_n4dataInner.getString("net_registered_ton")
                                val localagent = heading_n4dataInner.getString("localagent")
                                val Vessel_Name = heading_n4dataInner.getString("vsl_name")
                                val flag = heading_n4dataInner.getString("flag")
                                val beam_cm = heading_n4dataInner.getString("beam_cm")

                               // Log.e("ACT-", "INCOMING")

                                txtCallSign?.setText(radio_call_sign)
                                txtGt?.setText(gross_registered_ton)
                                txtNt?.setText(net_registered_ton)
                                txtVslName?.setText(Vessel_Name)
                                if(beam_cm=="null")
                                    txtDeckCargo?.setText("Nil")
                                else
                                    txtDeckCargo?.setText(beam_cm)

                                txtLoa?.setText(loa_cm)
                                txtFlag?.setText(flag)
                                txtAgent?.setText(localagent)
                                txtRotation?.setText(ROTATION)
                                //txtPilotName?.setText(pilot_name);
                            }
                            etOnBoardDate?.setText(getDate(pilot_on_board))
                            etOnBoardTime?.setText(getTime(pilot_on_board))
                            etOffBordDate?.setText(getDate(pilot_off_board))
                            etOffBordTime?.setText(getTime(pilot_off_board))
                            txtAdditionalPilot?.setText(aditional_pilot)
                            txtAdditionalTug?.setText(aditional_tug)
                            txtRemarks?.setText(remarks)
                            actvBerth?.setText(berth)
                            if (ACTIVITY_FOR == "incoming") {
                                txtLastLine?.visibility = View.GONE
                                etFirstLineDate?.setText(getDate(mooring_frm_time))
                                etFirstLineTime?.setText(getTime(mooring_frm_time))
                                etLastLineDate?.setText(getDate(mooring_to_time))
                                etLastLineTime?.setText(getTime(mooring_to_time))

                            } else if (ACTIVITY_FOR == "shifting") {
                                etLastLineDate?.setText(getDate(mooring_frm_time))
                                etLastLineTime?.setText(getTime(mooring_frm_time))
                                etFirstLineDate?.setText(getDate(mooring_to_time))
                                etFirstLineTime?.setText(getTime(mooring_to_time))
                            } else {
                                txtFirstLine?.visibility = View.GONE
                                etFirstLineDate?.setText(getDate(mooring_frm_time))
                                etFirstLineTime?.setText(getTime(mooring_frm_time))
                                etLastLineDate?.setText(getDate(mooring_to_time))
                                etLastLineTime?.setText(getTime(mooring_to_time))
                            }
                        }
                    } else {
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again to load", Toast.LENGTH_LONG).show()
                }
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
//                Log.e("REQUEST FOR::",ACTIVITY_FOR);
//                Log.e("vvd_gkey FOR::",VVD_GKEY);
//                Log.e("rotation FOR::",ROTATION);


                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("rotation", ROTATION)
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@FinalSubmitPilotOperationActivity)
        queue?.add(stringRequest)

    }
    private fun getBerth() {
        var dialog = ProgressDialog.progressDialog(this@FinalSubmitPilotOperationActivity)
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
                //    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            var dataInner: JSONObject = data.getJSONObject(i)
                            val gkey = dataInner.getString("gkey")
                            val berth_name = dataInner.getString("berth_name")
                            if (berth_name != "") {
                                Berth.add(berth_name)
                            }
                        }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Berth)
                        actvBerth?.setAdapter(adapter)
                        etShiftingFrom?.setAdapter(adapter)
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
                    Toast.makeText(applicationContext, "Try Again to load berth", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@FinalSubmitPilotOperationActivity)
        queue?.add(stringRequest)
    }
    private fun getDate(datetime: String):String {
        if(datetime.length>6) {
            val parts: List<String> = datetime.split(" ")
            return parts[0]
        }else{
            return "0000-00-00"
        }
    }

    private fun getTime(datetime: String):String {
        if(datetime.length>6) {
            val parts: List<String> = datetime.split(" ")
            return parts[1].replace(":","")
        }else{
            return  "00:00:00"
        }
    }
//    private fun getDate(datetime: String):String {
//    val parts: List<String> = datetime.split(" ")
//    return parts[0]
//}
//    private fun getTime(datetime: String):String {
//        val parts: List<String> = datetime.split(" ")
//        return parts[1].replace(":","")
//    }
    private fun setTime(myTime:String):String{
        val newTime = myTime.substring(0,2)+":"+myTime.substring(2,myTime.length)
        return newTime
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_REPORTVIEW_REQUEST_CODE) {
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