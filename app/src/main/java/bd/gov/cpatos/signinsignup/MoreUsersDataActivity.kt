package bd.gov.cpatos.signinsignup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class MoreUsersDataActivity : AppCompatActivity() {
    private var etName: TextInputEditText? =null
    private var etEmail: TextInputEditText? =null
    private var imgNidFront: ShapeableImageView? =null
    private var imgNidBack: ShapeableImageView? =null
    private var imgUser: ShapeableImageView? =null
    private var btnUploadNidFront: MaterialButton? =null
    private var btnUploadNidBack: MaterialButton? =null
    private var btnUplaodImage: MaterialButton? =null
    private var btnCompleteLater: Button? =null
    private var btnFinished: Button? =null
    private val PERMISSION_CODE = 1000
    private val NID_FRONT_CAPTURE_CODE = 1001
    private val NID_BACK_CAPTURE_CODE = 1002
    private val MY_IMAGE_CAPTURE_CODE = 1003
    var bitmapimgNidFront: Bitmap? = null
    var bitmapimgNidBack: Bitmap? = null
    var bitmapimgUser: Bitmap? = null


    private var MOBILE_NO:String? = null
    private var DEVICE_ID:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_users_data)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            MOBILE_NO = bundle.getString("MOBILE_NO")
            DEVICE_ID = bundle.getString("DEVICE_ID")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        imgNidFront = findViewById(R.id.imgNidFront)
        imgNidBack = findViewById(R.id.imgNidBack)
        imgUser = findViewById(R.id.imgUser)
        btnUploadNidFront = findViewById(R.id.btnUploadNidFront)
        btnUploadNidBack = findViewById(R.id.btnUploadNidBack)
        btnUplaodImage = findViewById(R.id.btnUplaodImage)
        btnCompleteLater = findViewById(R.id.btnCompleteLater)
        btnFinished = findViewById(R.id.btnFinished)

        btnCompleteLater?.setOnClickListener {
            nextActivity()
        }
        btnFinished?.setOnClickListener {
          //  nextActivity()
            if(etName?.text.toString() ==""){
                Toast.makeText(this,"Please Enter Name",Toast.LENGTH_LONG).show()
            }else if(bitmapimgNidFront ==null){
                Toast.makeText(this,"Please Add NID Front",Toast.LENGTH_LONG).show()
            }else if(bitmapimgNidBack ==null){
                Toast.makeText(this,"Please Add NID Back",Toast.LENGTH_LONG).show()
            }else if(bitmapimgUser ==null){
                Toast.makeText(this,"Please Add Your Image",Toast.LENGTH_LONG).show()
            }else {
                updateMoreInformation()
            }
        }
        btnUploadNidFront?.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            permissionCheckAndTaskStart(NID_FRONT_CAPTURE_CODE)
        }
        btnUploadNidBack?.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            permissionCheckAndTaskStart(NID_BACK_CAPTURE_CODE)
        }
        btnUplaodImage?.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            permissionCheckAndTaskStart(MY_IMAGE_CAPTURE_CODE)
        }

    }
    private fun permissionCheckAndTaskStart(PERMISSION_TYPE: Int?){

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
                openCamera(PERMISSION_TYPE)
            }
        } else {
            //system os is < marshmallow
            openCamera(PERMISSION_TYPE)
        }

    }

    private fun nextActivity(){
//        val intent = Intent(this@MoreUsersDataActivity, GatePassHomeActivity::class.java)
//        intent.putExtra("TITLE", "Gate Pass Panel")
//        intent.putExtra("USER_TYPE", "GatePass")
//        startActivity(intent)
        finish()
    }

    private fun openCamera(PERMISSION_TYPE: Int?) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, PERMISSION_TYPE!!)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.e("ASIF", "requestCode $requestCode resultCode $resultCode ")
        if (requestCode == NID_FRONT_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            bitmapimgNidFront = extras?.get("data") as Bitmap
            imgNidFront?.setImageBitmap(bitmapimgNidFront)
        }else if (requestCode == NID_BACK_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            bitmapimgNidBack = extras?.get("data") as Bitmap
            imgNidBack?.setImageBitmap(bitmapimgNidBack)
        }else if (requestCode == MY_IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
             bitmapimgUser = extras?.get("data") as Bitmap
            imgUser?.setImageBitmap(bitmapimgUser)
        }else{
//            if(intentResult !=null){
//                if(intentResult.contents ==null){
//                    textView?.setText("")
//                }else{
//                    textView?.setText(intentResult.contents)
//                    if(intentResult.contents.isEmpty()){
//                        Toast.makeText(this, "This is not valid!", Toast.LENGTH_LONG).show()
//                    }else{
//                        getGateData(intentResult.contents)
//                    }
//                }
//            }
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

        private fun updateMoreInformation() {
        val dialog = ProgressDialog.progressDialog(this@MoreUsersDataActivity)
        dialog.show()

        val stringRequest: StringRequest =
            object : StringRequest(
                Request.Method.POST, EndPoints.URL_SIGNUP,
                object : Response.Listener<String?> {
                    override fun onResponse(response: String?) {
                        Log.e("anyText", response.toString())
                        try {
                            val jsonObject = JSONObject(response)
                            val success = jsonObject.getString("success")
                            val message = jsonObject.getString("message")
                            if (success == "1") {
                                dialog.dismiss()
//                                val intent = Intent(this@MoreUsersDataActivity, LoginActivity::class.java)
//                                intent.putExtra("TITLE", "Gate Pass Panel")
//                                intent.putExtra("USER_TYPE", "GatePass")
//                                intent.putExtra("MOBILE_NO", MOBILE_NO.toString())
//                                intent.putExtra("DEVICE_ID", DEVICE_ID.toString())
//                                startActivity(intent)
                                finish()

                            }
                            if (success == "0") {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
                                    .show()
                                dialog.dismiss()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            dialog.dismiss()
                            Toast.makeText(applicationContext, " Error !1$e", Toast.LENGTH_LONG)
                                .show()

                        }
                    }
                }, Response.ErrorListener { error ->
                    dialog.dismiss()
                    Toast.makeText(applicationContext, " Error !2$error", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String?> {
                    val bitmapimgNidFrontStr: String = bitmapimgNidFront?.let { getStringImage(it) }!!
                    val bitmapimgNidBackStr: String = bitmapimgNidBack?.let { getStringImage(it) }!!
                    val bitmapimgUserStr: String = bitmapimgUser?.let { getStringImage(it) }!!

                    val params: MutableMap<String, String?> = HashMap()
                    params["step_id"] = "4"
                    params["mobile_no"] = MOBILE_NO.toString()
                    params["name"] = etName?.text.toString()
                    params["email"] = etEmail?.text.toString()
                    params["user_img"] = bitmapimgUserStr
                    params["nid_img_front"] = bitmapimgNidFrontStr
                    params["nid_img_back"] = bitmapimgNidBackStr
                    return params

                    return params
                }
            }
        //        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        val encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        println("Encoded Image: $encodedImage")
        return encodedImage
    }



}