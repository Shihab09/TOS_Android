package bd.gov.cpatos.gate.activities



import android.Manifest
import android.app.Activity
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
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.*
import bd.gov.cpatos.R
import bd.gov.cpatos.R.layout.activity_gatein_new
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.EndPoints.BASE_URL
import bd.gov.cpatos.utils.EndPoints.URL_GATEIN_SCAN_DATA_RESONSE
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_gate_in.*
import kotlinx.android.synthetic.main.item_container.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File


class GateinNewActivity : AppCompatActivity() {
    var URL_ALARGREEN = ""
    var URL_ALARMRED = ""
    var scanID = ""
    var NO_OF_PERSION = 0
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private var title:String? =null

    private var SCAN_TYPE=""

    private var etVisitIdCheck: TextInputEditText? = null
    private var findButton:Button? = null

    private var etTrvno: TextInputEditText? = null
    private var etPaymentStatus: TextInputEditText? = null

    private var etDriverName: TextInputEditText? = null
    private var etIdCardDriver: TextInputEditText? = null
    private var imgvDriver: ImageView? = null

    private var etAssistantName: TextInputEditText? = null
    private var etIdCardAssistant: TextInputEditText? = null
    private var imgvAssistant: ImageView? = null

    private var etTruckNo: TextInputEditText? = null
    private var etTruckCompany: TextInputEditText? = null


    private var mgateInButton:Button? =null
    private var mScanButton:Button? = null


    private var textView: TextView? = null
    private var tvPaymentStatus: TextView? = null




    private var context: Context? = null
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
        setContentView(activity_gatein_new)
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

        UI()
        BUTTONACTION()



        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setOrientationLocked(true)
        NO_OF_PERSION = 1


    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action: Int = event.getAction()
        Log.e("action-", action.toString())
        val keyCode: Int = event.getKeyCode()
        Log.e("keyCode-", keyCode.toString())

        if (keyCode.toString() == "10036" || keyCode.toString() == "103") {
           var data =  etVisitIdCheck?.text.toString()
            if(data !=""){

                getScanDataResponse(data)
            }
        }
        return true
    }
    fun UI(){
        textView = findViewById(R.id.textView)
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
        etVisitIdCheck = findViewById(R.id.etVisitIdCheck)
        findButton = findViewById(R.id.findButton)

        etTrvno = findViewById(R.id.etTrvno)
        etPaymentStatus = findViewById(R.id.etPaymentStatus)


        etDriverName = findViewById(R.id.etDriverName)
        etIdCardDriver = findViewById(R.id.etIdCardDriver)
        imgvDriver = findViewById(R.id.imgDriver)

        etAssistantName = findViewById(R.id.etAssistantName)
        etIdCardAssistant = findViewById(R.id.etIdCardAssistant)
        imgvAssistant = findViewById(R.id.imgAssistant)

        etTruckNo = findViewById(R.id.etTruckNo)
        etTruckCompany = findViewById(R.id.etTruckCompany)


        mgateInButton = findViewById(R.id.gateInButton)
        mScanButton = findViewById(R.id.ScanButton)
    }
    fun  BUTTONACTION(){
        findButton?.setOnClickListener{
            var data =  etVisitIdCheck?.text.toString()
            if(data !=""){

                getScanDataResponse(data)
            }
        }
        mScanButton?.setOnClickListener {

          //  SCAN_TYPE = "PAYMENT"
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
        mgateInButton?.setOnClickListener { ScanDataGateIn() }


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
                        getScanDataResponse(intentResult.contents)
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
    private fun getScanDataResponse(id: String) {
        val dialog = ProgressDialog.progressDialog(this@GateinNewActivity)
        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, URL_GATEIN_SCAN_DATA_RESONSE,
            Response.Listener<String?> { response ->
                Log.e("anyText", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        val response_type = jsonObject.getString("response_type")
                        if(response_type.toString().uppercase() == "PAYMENT"){
                            var myid = jsonObject.getString("id")
                            var paid_status = jsonObject.getString("paid_status")
                            if(paid_status.toString() =="1"){
                                etTrvno?.setText(myid.toString())
                                etPaymentStatus?.setText("PAID")
                                textView?.text = "VALID"
                                textView?.setTextColor(Color.rgb(0, 255, 0))

                            }else{
                                etPaymentStatus?.setText("NOT PAID")
                                textView?.text = "INVALID"
                                textView?.setTextColor(Color.rgb(255, 0, 0))
                            }

                        }else if(response_type.uppercase() == "DRIVER"){
                            var myid = jsonObject.getString("id")
                            var card_number = jsonObject.getString("card_number")
                            var agent_name = jsonObject.getString("agent_name")
                            var agent_type = jsonObject.getString("agent_type")
                            etDriverName?.setText(agent_name)
                            etIdCardDriver?.setText(card_number)
                            var agent_photo = jsonObject.getString("agent_photo")
                            var img_array =agent_photo.split(".").toTypedArray()
                            var img_folder =img_array[0]
                            Picasso.get().load(BASE_URL +"biometricPhoto/"+ img_folder+"/"+agent_photo).into(imgvDriver)
                            textView?.text = "VALID"
                            textView?.setTextColor(Color.rgb(0, 255, 0))

                        }else if(response_type.toString().uppercase() == "HELPER"){
                            var myid = jsonObject.getString("id")
                            var card_number = jsonObject.getString("card_number")
                            var agent_name = jsonObject.getString("agent_name")
                            etAssistantName?.setText(agent_name)
                            etIdCardAssistant?.setText(card_number)
                            var agent_photo = jsonObject.getString("agent_photo")
                            var img_array =agent_photo.split(".").toTypedArray()
                            var img_folder =img_array[0]
                            Picasso.get().load(BASE_URL +"biometricPhoto/"+  img_folder+"/"+agent_photo).into(imgvAssistant)
                            textView?.text = "VALID"
                            textView?.setTextColor(Color.rgb(0, 255, 0))
                        }else if(response_type.toString().uppercase() == "TRUCK"){
                            var myid = jsonObject.getString("id")
                            var truck_id = jsonObject.getString("truck_id")
                            var truck_agency_name = jsonObject.getString("truck_agency_name")
                            var truck_agency_phone = jsonObject.getString("truck_agency_phone")
                            etTruckNo?.setText(truck_id)
                            etTruckCompany?.setText(truck_agency_name)
                            textView?.text = "VALID"
                            textView?.setTextColor(Color.rgb(0, 255, 0))
                        }else{


                        }
                        etVisitIdCheck?.setText("")
                   }
                    if (success == "0") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        textView?.text = "INVALID"
                        textView?.setTextColor(Color.rgb(255, 0, 0))
                        dialog.dismiss()
                        etVisitIdCheck?.setText("")
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, " Error !1$e", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    etVisitIdCheck?.setText("")
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
                var tr_type:String?=null
                var tr_id:String? = null
                if(id.contains("---")){
                    tr_type = "NONPAYMENT"
                    tr_id = id
                }else if(id.contains("===")){
                    tr_type = "TRUCK"
                    tr_id = id.replace("===", "")

                }else{
                    tr_type = "PAYMENT"
                    tr_id = id
                }


                params["id"] = tr_id
                params["tr_type"] = tr_type
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun ScanDataGateIn() {
        val dialog = ProgressDialog.progressDialog(this@GateinNewActivity)
        dialog.show()

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_GATEIN_SCAN_DATA,
            Response.Listener<String?> { response ->
                Log.e("anyText", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {
                        dialog.dismiss()
                        textView?.text = message.toString()
                        textView?.setTextColor(Color.rgb(0, 255, 0))
                        clearFielddata()


                    }
                    if (success == "0") {
                         dialog.dismiss()
                        textView?.text = message.toString()
                        textView?.setTextColor(Color.rgb(255, 0, 0))

                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, " Error !1$e", Toast.LENGTH_LONG).show()
                    dialog.dismiss()

                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, " Error !2$error", Toast.LENGTH_LONG).show()
                }
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["visit_id"] = etTrvno?.text.toString()
                params["payment_status"] = etPaymentStatus?.text.toString()
                params["driver_id"] = etIdCardDriver?.text.toString()
                params["driver_name"] = etDriverName?.text.toString()
                params["assistent_id"] = etIdCardAssistant?.text.toString()
                params["assistent_name"] = etAssistantName?.text.toString()
                params["truck_id"] = etTruckNo?.text.toString()
                params["truck_company"] = etTruckCompany?.text.toString()
                params["gate_in_by"] = SignedInLoginId.toString()
                params["gate_no"] = SignedInSection.toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    fun clearFielddata(){
        etVisitIdCheck?.setText("")
        etTrvno?.setText("")
        etPaymentStatus?.setText("")
        etIdCardDriver?.setText("")
        etDriverName?.setText("")
        etIdCardAssistant?.setText("")
        etAssistantName?.setText("")
        etTruckNo?.setText("")
        etTruckCompany?.setText("")

        imgvDriver?.setImageResource(R.drawable.dft_img)
        imgvAssistant?.setImageResource(R.drawable.dft_img)

    }
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

}