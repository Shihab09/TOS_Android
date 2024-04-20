package bd.gov.cpatos.gate.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints.BASE_URL
import bd.gov.cpatos.utils.EndPoints.URL_GATEIN_DATAGET
import bd.gov.cpatos.utils.EndPoints.URL_GATEIN_DATASAVE
import bd.gov.cpatos.utils.EndPoints.URL_SERVER
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_gate_in.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class GateInActivity : AppCompatActivity() {
    var URL_ALARGREEN = ""
    var URL_ALARMRED = ""
    var scanID = ""
    var NO_OF_PERSION = 0
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var radioDriverGroup: RadioGroup? = null
    private var radioDriverButton: RadioButton? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private  var AlarmIp:String? = null
    private var title:String? =null

    private var etTrvno: TextInputEditText? = null
    private var etAsstype: TextInputEditText? = null
    private var etFromDate: TextInputEditText? = null
    private var etToDate: TextInputEditText? = null
    private var etvessel: TextInputEditText? = null
    private var etRotation: TextInputEditText? = null
    private var etBlNo: TextInputEditText? = null
    private var etShedYardNo: TextInputEditText? = null
    private var etJettySarkarName: TextInputEditText? = null
    private var etContainerNo: TextInputEditText? = null
    private var etGoodsDetails: TextInputEditText? = null
    private var etGateNO: TextInputEditText? = null
    private var etTruckNo: TextInputEditText? = null
    private var etDriverName: TextInputEditText? = null
    private var etIdCardDriver: TextInputEditText? = null
    private var etUnionMemberShipNoDriver: TextInputEditText? = null
    private var etAssistantName: TextInputEditText? = null
    private var etIdCardAssistant: TextInputEditText? = null
    private var etUnionMembNoAssistan: TextInputEditText? = null
    private var etTransportAgencyName: TextInputEditText? = null
    private var textView: TextView? = null
    private var tvPaymentStatus: TextView? = null

    private var context: Context? = null
    private var imgvDriver: ImageView? = null
    private var imgvAssistant: ImageView? = null
    private var imgTruck: ImageView? = null
     //FOr Camara Activity
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var bitmap: Bitmap? = null
    internal var qrScanIntegrator: IntentIntegrator? = null
    private var mUri: Uri? = null
    private lateinit var photoFile: File
    private val FILE_NAME = "photo.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_in)
        context = this
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
        AlarmIp = mPreferences?.getString("AlarmIp", "null")

        URL_ALARGREEN = "http://"+AlarmIp+"/id/1"
        URL_ALARMRED = "http://"+AlarmIp+"/id/2"
        radioDriverGroup = findViewById(R.id.radioGroup)
        //  radioDriverButton = findViewById(selectedId);
        textView = findViewById(R.id.textView)
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
        etTrvno = findViewById(R.id.etTrvno)
        etAsstype = findViewById(R.id.etAsstype)
        etFromDate = findViewById(R.id.etFromDate)
        etToDate = findViewById(R.id.etToDate)
        etvessel = findViewById(R.id.etvessel)
        etRotation = findViewById(R.id.etRotation)
        etBlNo = findViewById(R.id.etBlNo)
        etShedYardNo = findViewById(R.id.etShedYardNo)
        etJettySarkarName = findViewById(R.id.etJettySarkarName)
        etContainerNo = findViewById(R.id.etContainerNo)
        etGoodsDetails = findViewById(R.id.etGoodsDetails)

        etGateNO = findViewById(R.id.etGateNO)
        etTruckNo = findViewById(R.id.etTruckNo)
        etDriverName = findViewById(R.id.etDriverName)
        etIdCardDriver = findViewById(R.id.etIdCardDriver)
        etUnionMemberShipNoDriver = findViewById(R.id.etUnionMemberShipNoDriver)
        etAssistantName = findViewById(R.id.etAssistantName)
        etIdCardAssistant = findViewById(R.id.etIdCardAssistant)
        etUnionMembNoAssistan = findViewById(R.id.etUnionMembNoAssistan)
        etTransportAgencyName = findViewById(R.id.etTransportAgencyName)
        imgvDriver = findViewById(R.id.imgDriver)
        imgvAssistant = findViewById(R.id.imgAssistant)
        imgTruck = findViewById(R.id.imgTruck)

        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setOrientationLocked(false)
        NO_OF_PERSION = 1
        radioDriverGroup!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioDriverGroup, i ->
          //  textView.text = "option "+i+" is selected"
            //var selectedId:Int = radioDriverGroup!!.getCheckedRadioButtonId();
            val radio:RadioButton = findViewById(i)
            if(radio.text == "Both"){
                NO_OF_PERSION = 2
            }
           // Toast.makeText(this," option "+radio.text+" is selected", Toast.LENGTH_LONG).show()
        })
//        //button click
//        btnTruckImgCapture?.setOnClickListener {
//            //if system os is Marshmallow or Above, we need to request runtime permission
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.CAMERA)
//                    == PackageManager.PERMISSION_DENIED ||
//                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_DENIED
//                ) {
//                    //permission was not enabled
//                    val permission = arrayOf(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//                    //show popup to request permission
//                    requestPermissions(permission, PERMISSION_CODE)
//                } else {
//                    //permission already granted
//                    openCamera()
//                }
//            } else {
//                //system os is < marshmallow
//                openCamera()
//            }
//        }
        scanButton?.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    performAction()
                }
            } else {
                //system os is < marshmallow
                performAction()
            }
        }
//        buttonGateIn?.setOnClickListener{
//            if(scanID.equals("")){
//                Toast.makeText(this, "You are not allowed to gate in", Toast.LENGTH_LONG).show()
//            }else{
//                this.updateGateInData()
//            }
//        }
    }


    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }
    private fun openCamera() {
       val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.e("ASIF", "requestCode $requestCode resultCode $resultCode ")
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            bitmap = extras?.get("data") as Bitmap
            imgTruck?.setImageBitmap(bitmap)
        }else{
            if(intentResult !=null){
                if(intentResult.contents ==null){
                    textView?.text = ""
                }else{
                    textView?.text = intentResult.contents
                    if(intentResult.contents.isEmpty()){
                        Toast.makeText(this, "This is not valid!", Toast.LENGTH_LONG).show()
                    }else{
                        getGateData(intentResult.contents)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getGateData(id: String) {
        val dialog = ProgressDialog.progressDialog(this@GateInActivity)
        dialog.show()

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, URL_GATEIN_DATAGET,
            Response.Listener<String?> { response ->
                Log.e("anyText", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        //val id = jsonObject.getString("id")
                        //String verify_info_fcl_id = jsonObject.getString("verify_info_fcl_id");
                        // String verify_number = jsonObject.getString("verify_number");
                        val import_rotation = jsonObject.getString("import_rotation")
                        val cont_no = jsonObject.getString("cont_no")
                        val truck_id = jsonObject.getString("truck_id")
                        //String delv_pack = jsonObject.getString("delv_pack");
                        //String actual_delv_pack = jsonObject.getString("actual_delv_pack");
                        //String actual_delv_unit = jsonObject.getString("actual_delv_unit");
                        val gate_no = jsonObject.getString("gate_no")
                        val driver_name = jsonObject.getString("driver_name")
                        val driver_gate_pass = jsonObject.getString("driver_gate_pass")
                        val assistant_name = jsonObject.getString("assistant_name")
                        val assistant_gate_pass = jsonObject.getString("assistant_gate_pass")
                        val gate_in_status = jsonObject.getString("gate_in_status")
                        val gate_out_status = jsonObject.getString("gate_out_status")
                        val visit_time_slot_start =
                            jsonObject.getString("visit_time_slot_start")
                        val visit_time_slot_end = jsonObject.getString("visit_time_slot_end")
                        val Vessel_Name = jsonObject.getString("Vessel_Name")
                        val Description_of_Goods = jsonObject.getString("Description_of_Goods")
                        val Bill_of_Entry_No = jsonObject.getString("Bill_of_Entry_No")
                        val gate_in_time = jsonObject.getString("gate_in_time")
                        val gate_out_time = jsonObject.getString("gate_out_time")
                        //val gate_in_by = jsonObject.getString("gate_in_by")
                        //val gate_out_by = jsonObject.getString("gate_out_by")

                        //
                        //val cnf = jsonObject.getString("cnf")
                        //  val cnf_lic = jsonObject.getString("cnf_lic")
                        // val cnf_addr = jsonObject.getString("cnf_addr")
                        val Yard_No = jsonObject.getString("Yard_No")
                        val assignmentType = jsonObject.getString("assignmentType")
                        val paid_status = jsonObject.getString("paid_status")
                        val dateinouttimej = jsonObject.getString("dateinouttime")
                        val driver_photo = jsonObject.getString("driver_photo")
                        val assistant_photo = jsonObject.getString("assistant_photo")
                        val driver_pass_validity = jsonObject.getString("driver_pass_validity")
                        val assistant_pass_validity = jsonObject.getString("assistant_pass_validity")

                        Picasso.get().load(BASE_URL + driver_photo).into(imgvDriver)
                        Picasso.get().load(BASE_URL + assistant_photo).into(imgvAssistant)



                        // private ImageView imgvDriver,imgvAssistant;
                        if (paid_status == "1" && gate_in_status == "1" && gate_out_status == "1") {
                            Alarm(URL_ALARMRED)
                            textView?.text =
                                "FAILED!! You already used this ticket to Gate in at: $gate_in_time and Gate out at $gate_out_time In Gate No: $gate_no"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                        } else if (paid_status == "1" && gate_in_status == "1") {
                            Alarm(URL_ALARMRED)
                            textView?.text =
                                "FAILED!! You already used this ticket to Gate in at: $gate_in_time In Gate No: $gate_no"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                        } else if (paid_status == "0") {
                            Alarm(URL_ALARMRED)
                            textView?.text = "Your payment is not Paid"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                        } else if (!(gate_no == "Gate 3" || gate_no == "Gate 5")) {
                            Alarm(URL_ALARMRED)
                            textView?.text =
                                "SUCCESS!! Your Assigned Gate is: Gate 3 && gate 5 But you are In $SignedInSection"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                            scanID = id
                        } else if (dateinouttimej == "0") {
                            Alarm(URL_ALARMRED)
                            textView?.text =
                                "SUCCESS!! Your have missed your time  slot which was: $visit_time_slot_start to $visit_time_slot_end"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                            scanID = id
                        } else if (dateinouttimej == "2") {
                            Alarm(URL_ALARMRED)
                            textView?.text =
                                "SUCCESS!! Your time  slot will start from: $visit_time_slot_start to $visit_time_slot_end"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                            scanID = id
                        } else if (paid_status == "1") {
                            Alarm(URL_ALARGREEN)
                            textView?.text =
                                "SUCCESS!! Your all information is correct and payment is done now please gate in"
                            textView?.setTextColor(Color.rgb(0, 255, 0))
                            scanID = id
                        } else {
                            Alarm(URL_ALARMRED)
                            textView?.text =
                                "FAILED!! SSSSPlease try again after your payment done or You are using wrong format"
                            textView?.setTextColor(Color.rgb(255, 0, 0))
                        }
                        etAsstype?.setText(assignmentType)
                        etShedYardNo?.setText(Yard_No)
                        etvessel?.setText(Vessel_Name)
                        etGoodsDetails?.setText(Description_of_Goods)
                        etBlNo?.setText(Bill_of_Entry_No)
                        etTrvno?.setText(id)
                        etTruckNo?.setText(truck_id)
                        etAssistantName?.setText(assistant_name)
                        etFromDate?.setText(visit_time_slot_start)
                        etToDate?.setText(visit_time_slot_end)
                        etContainerNo?.setText(cont_no)
                        etGateNO?.setText(gate_no)
                        etDriverName?.setText(driver_name)
                        etIdCardDriver?.setText(driver_gate_pass)
                        etAssistantName?.setText(assistant_name)
                        etIdCardAssistant?.setText(assistant_gate_pass)
                        etRotation?.setText(import_rotation)
                    }
                    if (success == "0") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        Alarm(URL_ALARMRED)
                    }
                    if (success == "3") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        Alarm(URL_ALARMRED)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, " Error !1$e", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    //   Alarm(URL_ALARMRED)
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, " Error !2$error", Toast.LENGTH_LONG).show()
                }
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = id
                params["NO_OF_PERSION"] = NO_OF_PERSION.toString()
                params["gate_in_by"] = SignedInLoginId.toString()
                params["gate_no"] = SignedInSection.toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun Alarm(URL: String) {


// prepare the Request
        val getRequest = JsonObjectRequest(Request.Method.GET, URL, null,
            object : Response.Listener<JSONObject?> {


                override fun onResponse(response: JSONObject?) {
                    Log.d("Response", response.toString())
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Log.d("Error.Response", error.toString())
                }
            }
        )

// add it to the RequestQueue
        val queue = Volley.newRequestQueue(this)
        queue.add(getRequest)
    }

//    private fun updateGateInData() {
//        val dialog = ProgressDialog.progressDialog(this@GateInActivity)
//        dialog.show()
//
//        val stringRequest: StringRequest =
//            object : StringRequest(Request.Method.POST, URL_GATEIN_DATASAVE,
//                object : Response.Listener<String?> {
//                    override fun onResponse(response: String?) {
//                        Log.e("anyText", response)
//                        try {
//                            val jsonObject = JSONObject(response)
//                            val success = jsonObject.getString("success")
//                            val message = jsonObject.getString("message")
//                            if (success == "1") {
//                                Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG)
//                                    .show()
//                                dialog.dismiss()
//                                textView?.text = "Gate In SUCCESS"
//                                tvPaymentStatus?.text = ""
//                                etAsstype?.setText("")
//                                etShedYardNo?.setText("")
//                                etvessel?.setText("")
//                                etGoodsDetails?.setText("")
//                                etBlNo?.setText("")
//                                etTrvno?.setText("")
//                                etTruckNo?.setText("")
//                                etAssistantName?.setText("")
//                                etFromDate?.setText("")
//                                etToDate?.setText("")
//                                etContainerNo?.setText("")
//                                etGateNO?.setText("")
//                                etDriverName?.setText("")
//                                etIdCardDriver?.setText("")
//                                etAssistantName?.setText("")
//                                etIdCardAssistant?.setText("")
//                                etRotation?.setText("")
//                                scanID == ""
//                                imgvDriver?.setImageResource(R.drawable.dft_img)
//                                imgvAssistant?.setImageResource(R.drawable.dft_img)
//                                imgTruck?.setImageResource(R.drawable.truck_demo)
//                                textView?.setTextColor(Color.rgb(0, 0, 0))
//                            }
//                            if (success == "0") {
//                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
//                                    .show()
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
//                    val driver_id = etIdCardDriver?.text.toString()
//                    val helper_id = etIdCardAssistant?.text.toString()
//                    val params: MutableMap<String, String?> = HashMap()
//                    params["id"] = scanID
//                    params["gate_in_by"] = SignedInLoginId
//                    params["gate_no"] = SignedInSection
//                    params["truck_image"] = image
//                    params["driver_id"] = driver_id
//                    params["helper_id"] = helper_id
//                    params["NO_OF_PERSION"] = NO_OF_PERSION.toString()
//
//                    return params
//                }
//            }
//        //        int socketTimeout = 30000;
////        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////        stringRequest.setRetryPolicy(policy);
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
    fun StringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
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