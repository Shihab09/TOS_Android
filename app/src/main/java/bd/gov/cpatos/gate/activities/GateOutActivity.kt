package bd.gov.cpatos.gate.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints.BASE_URL
import bd.gov.cpatos.utils.EndPoints.URL_GATEOUT_DATAGET
import bd.gov.cpatos.utils.EndPoints.URL_GATEOUT_DATASAVE
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_gate_out.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import android.widget.Toast as Toast1


class GateOutActivity : AppCompatActivity() {
    var URL_ALARGREEN = ""
    var URL_ALARMRED = ""
    var scanID = ""
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null

    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private  var AlarmIp:String? = null

    private var TITLE:String? =null
    private var imgvDriver: ImageView? = null; private  var imgvAssistant:ImageView? = null; private  var imgTruckId:ImageView? = null

    private var etTrvno: TextInputEditText? = null; private  var etTruckNo:TextInputEditText? = null; private  var etAsstype:TextInputEditText? = null; private  var etvessel:TextInputEditText? = null; private  var etRotation:TextInputEditText? = null; private  var etBlNo:TextInputEditText? = null; private  var etShedYardNo:TextInputEditText? = null; private  var etContainerNo:TextInputEditText? = null; private  var etGoodsDetails:TextInputEditText? = null
    private var etGateNO: TextInputEditText? = null; private  var etCfName:TextInputEditText? = null; private  var etActualDelvPack:TextInputEditText? = null; private  var etActualDelvUnit:TextInputEditText? = null; private  var etDriverName:TextInputEditText? = null; private  var etIdCardDriver:TextInputEditText? = null; private  var etAssistantName:TextInputEditText? = null; private  var etIdCardAssistant:TextInputEditText? = null
    private var textView: TextView? = null; private  var tvPaymentStatus:TextView? = null
    private val SERVER_URL = "http://122.152.54.179/"
    //FOr Camara Activity
    private val PERMISSION_CODE = 2000
    private val IMAGE_CAPTURE_CODE = 2001
    var bitmap: Bitmap? = null
    internal var qrScanIntegrator: IntentIntegrator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_out)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            TITLE = bundle.getString("TITLE") // 1
            supportActionBar?.title = TITLE

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            this.onBackPressed()


        }

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
        AlarmIp = mPreferences?.getString("AlarmIp", "null")

        URL_ALARGREEN = "http://"+AlarmIp+"/id/1"
        URL_ALARMRED = "http://"+AlarmIp+"/id/2"




        textView = findViewById(R.id.textView)
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
        etTrvno = findViewById(R.id.etTrvno)
        etTruckNo=findViewById(R.id.etTruckNo)
        etAsstype = findViewById(R.id.etAsstype)
        etvessel = findViewById(R.id.etvessel)
        etRotation = findViewById(R.id.etRotation)
        etBlNo = findViewById(R.id.etBlNo)
        etShedYardNo = findViewById(R.id.etShedYardNo)
        etContainerNo = findViewById(R.id.etContainerNo)
        etGoodsDetails = findViewById(R.id.etGoodsDetails)
        etActualDelvPack = findViewById(R.id.etActualDelvPack)
        etActualDelvUnit = findViewById(R.id.etActualDelvUnit)
        etGateNO=findViewById(R.id.etGateNO)
        etCfName=findViewById(R.id.etCfName)
        etDriverName = findViewById(R.id.etDriverName)
        etIdCardDriver = findViewById(R.id.etIdCardDriver)
        etAssistantName = findViewById(R.id.etAssistantName)
        etIdCardAssistant = findViewById(R.id.etIdCardAssistant)
        imgvDriver =findViewById(R.id.imgDriver)
        imgvAssistant=findViewById(R.id.imgAssistant)
        imgTruckId=findViewById(R.id.imgTruckId)
        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setOrientationLocked(false)

//        btnCaptureTruckImg?.setOnClickListener {
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
        btnScanButton2?.setOnClickListener {
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
//        btnSaveGateOutButton?.setOnClickListener{
//            if(scanID.equals("")){
//                android.widget.Toast.makeText(this, "You are not allowed to gate out", android.widget.Toast.LENGTH_LONG).show()
//            }else{
//                this.updateGateOutData()
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
            imgTruckId?.setImageBitmap(bitmap)

        }else{
            if(intentResult !=null){
                if(intentResult.contents ==null){
                    textView?.text = ""
                }else{
                    textView?.text = intentResult.contents
                    if(intentResult.contents.isEmpty()){
                        android.widget.Toast.makeText(this, "This is not valid!", android.widget.Toast.LENGTH_LONG).show()
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
                    android.widget.Toast.makeText(this, "Permission Granted", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    //permission from popup was denied
                    android.widget.Toast.makeText(this, "Permission denied", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun getGateData(id: String) {

        val pdDialog = ProgressDialog.progressDialog(this@GateOutActivity)
        pdDialog.show()
        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, URL_GATEOUT_DATAGET,
                object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        Log.e("anyText", response.toString())
                        try {
                            val jsonObject = JSONObject(response)
                            val success = jsonObject.getString("success")
                            val message = jsonObject.getString("message")
                            if (success == "1") {
                                Toast1.makeText(applicationContext, "Success", Toast1.LENGTH_LONG).show()
                                pdDialog.dismiss()
                                //val id = jsonObject.getString("id")
                                val import_rotation = jsonObject.getString("import_rotation")
                                val cont_no = jsonObject.getString("cont_no")
                                val truck_id = jsonObject.getString("truck_id")
                                //val delv_pack = jsonObject.getString("delv_pack")
                                val actual_delv_pack = jsonObject.getString("actual_delv_pack")
                                val actual_delv_unit = jsonObject.getString("actual_delv_unit")
                                val driver_name = jsonObject.getString("driver_name")
                                val driver_gate_pass = jsonObject.getString("driver_gate_pass")
                                val assistant_name = jsonObject.getString("assistant_name")
                                val assistant_gate_pass = jsonObject.getString("assistant_gate_pass")
                                val traffic_chk_st = jsonObject.getString("traffic_chk_st")
                                val yard_security_chk_st = jsonObject.getString("yard_security_chk_st")
                                val cnf_chk_st = jsonObject.getString("cnf_chk_st")
                                val Vessel_Name = jsonObject.getString("Vessel_Name")
                                val Description_of_Goods = jsonObject.getString("Description_of_Goods")
                                val Bill_of_Entry_No = jsonObject.getString("Bill_of_Entry_No")
                                val cnf = jsonObject.getString("cnf")
                                //val cnf_lic = jsonObject.getString("cnf_lic")
                                //val cnf_addr = jsonObject.getString("cnf_addr")
                                val Yard_No = jsonObject.getString("Yard_No")
                                val assignmentType = jsonObject.getString("assignmentType")
                                val driver_photo = jsonObject.getString("driver_photo")
                                val assistant_photo = jsonObject.getString("assistant_photo")
                                val gate_out_status = jsonObject.getString("gate_out_status")
                                val gate_out_time = jsonObject.getString("gate_out_time")
                                Picasso.get().load(BASE_URL + driver_photo).into(imgvDriver)
                                Picasso.get().load(BASE_URL + assistant_photo).into(imgvAssistant)



                                if (gate_out_status == "1") {
                                    Alarm(URL_ALARMRED)
                                    textView?.text = "You Already used this ticket To Gate Out at: $gate_out_time"
                                    textView?.setTextColor(Color.rgb(255, 0, 0))
                                } else if (traffic_chk_st == "0") {
                                    Alarm(URL_ALARMRED)
                                    textView?.text = "Traffic did not confirmed yet"
                                    textView?.setTextColor(Color.rgb(255, 0, 0))
                                } else if (cnf_chk_st == "0") {
                                    Alarm(URL_ALARMRED)
                                    textView?.text = "C&F did not confirmed yet"
                                    textView?.setTextColor(Color.rgb(255, 0, 0))
                                } else if (yard_security_chk_st == "0") {
                                    Alarm(URL_ALARMRED)
                                    textView?.text = "Security did not confirmed yet"
                                    textView?.setTextColor(Color.rgb(255, 0, 0))
                                } else if (traffic_chk_st == "1" && traffic_chk_st == "1" && cnf_chk_st == "1") {
                                    Alarm(URL_ALARGREEN)
                                    textView?.text = "Your all information is correct and payment is done now please gate out"
                                    textView?.setTextColor(Color.rgb(0, 255, 0))
                                    scanID = id
                                } else {
                                    Alarm(URL_ALARMRED)
                                    textView?.text = "Please try again after your processing done "
                                    textView?.setTextColor(Color.rgb(255, 0, 0))
                                }
                                etAsstype?.setText(assignmentType)
                                etShedYardNo?.setText(Yard_No)
                                etvessel?.setText(Vessel_Name)
                                etGoodsDetails?.setText(Description_of_Goods)
                                etBlNo?.setText(Bill_of_Entry_No)
                                etTrvno?.setText(id)
                                etTruckNo?.setText(truck_id)
                                etGateNO?.setText(SignedInSection)
                                etCfName?.setText(cnf)
                                etActualDelvPack?.setText(actual_delv_pack)
                                etActualDelvUnit?.setText(actual_delv_unit)
                                etAssistantName?.setText(assistant_name)
                                etContainerNo?.setText(cont_no)
                                etDriverName?.setText(driver_name)
                                etIdCardDriver?.setText(driver_gate_pass)
                                etAssistantName?.setText(assistant_name)
                                etIdCardAssistant?.setText(assistant_gate_pass)
                                etRotation?.setText(import_rotation)
                            }
                            if (success == "0") {
                                Toast1.makeText(applicationContext, message, Toast1.LENGTH_LONG).show()
                                pdDialog.dismiss()
                                Alarm(URL_ALARMRED)
                            }
                            if (success == "3") {
                                Toast1.makeText(applicationContext, message, Toast1.LENGTH_LONG).show()
                                pdDialog.dismiss()
                                Alarm(URL_ALARMRED)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            pdDialog.dismiss()
                            Toast1.makeText(applicationContext, " Error !1$e", Toast1.LENGTH_LONG).show()
                        }
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                pdDialog.dismiss()
                Toast1.makeText(applicationContext, " Error !2$error", Toast1.LENGTH_LONG).show()
            }
        }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["id"] = id
                params["gate_outBy"] = SignedInLoginId.toString()
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
                object : Response.Listener<JSONObject?>{
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

//    private fun updateGateOutData() {
//        val pdDialog = ProgressDialog.progressDialog(this@GateOutActivity)
//        pdDialog.show()
//        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, URL_GATEOUT_DATASAVE,
//                object : Response.Listener<String?> {
//                    override fun onResponse(response: String?) {
//                        Log.e("anyText", response)
//                        try {
//                            val jsonObject = JSONObject(response)
//                            val success = jsonObject.getString("success")
//                            val message = jsonObject.getString("message")
//                            if (success == "1") {
//                                Toast1.makeText(applicationContext, "Success", Toast1.LENGTH_LONG).show()
//                                pdDialog.dismiss()
//                                textView?.text = "Gate Out success at Gate No: $SignedInSection"
//                                tvPaymentStatus?.text = ""
//                                etAsstype?.setText("")
//                                etShedYardNo?.setText("")
//                                etCfName?.setText("")
//                                etvessel?.setText("")
//                                etGoodsDetails?.setText("")
//                                etBlNo?.setText("")
//                                etTrvno?.setText("")
//                                etActualDelvPack?.setText("")
//                                etActualDelvUnit?.setText("")
//                                etAssistantName?.setText("")
//                                etContainerNo?.setText("")
//                                etDriverName?.setText("")
//                                etIdCardDriver?.setText("")
//                                etAssistantName?.setText("")
//                                etIdCardAssistant?.setText("")
//                                etRotation?.setText("")
//                                scanID == ""
//                                etTruckNo?.setText("")
//                                etGateNO?.setText("")
//                                imgvDriver?.setImageResource(R.drawable.dft_img)
//                                imgvAssistant?.setImageResource(R.drawable.dft_img)
//                                imgTruckId?.setImageResource(R.drawable.truck_demo)
//                                textView?.setTextColor(Color.rgb(0, 0, 0))
//                            }
//                            if (success == "0") {
//                                Toast1.makeText(applicationContext, message, Toast1.LENGTH_LONG).show()
//                                pdDialog.dismiss()
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                            pdDialog.dismiss()
//                            Toast1.makeText(applicationContext, " Error !1$e", Toast1.LENGTH_LONG).show()
//                        }
//                    }
//                }, object : Response.ErrorListener {
//                override fun onErrorResponse(error: VolleyError) {
//                    pdDialog.dismiss()
//                    Toast1.makeText(applicationContext, " Error !2$error", Toast1.LENGTH_LONG).show()
//            }
//        }) {
//            override fun getParams(): Map<String, String> {
//                val image: String = bitmap?.let { getStringImage(it) }!!
//
//                val driver_id = etIdCardDriver?.text.toString()
//                val helper_id = etIdCardAssistant?.text.toString()
//
//                val params: MutableMap<String, String> = HashMap()
//                params["id"] = scanID
//                params["gate_out_by"] = SignedInLoginId!!
//                params["gate_no"] = SignedInSection!!
//                params["truck_image"] = image
//                params["driver_id"] = driver_id
//                params["helper_id"] = helper_id
//
//                return params
//            }
//        }
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