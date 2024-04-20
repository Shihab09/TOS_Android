package bd.gov.cpatos.signinsignup

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.util.*

class MobileNoAddActivity : AppCompatActivity() {
    private var etUserMobileNo: TextInputEditText? =null
    private var btnSaveMobileNo: Button? =null
    private var IMEINumber:String?  =null
    private val PERMISSIONS_READ_PHONE_STATE = 1
    private var ACTIVITY:String?=null
    private var URL:String?=null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_no_add)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            ACTIVITY= bundle.getString("ACTIVITY")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        if(ACTIVITY=="registration"){
            URL = EndPoints.URL_SIGNUP

        }else{
            URL = EndPoints.URL_RESET
        }


        etUserMobileNo = findViewById(R.id.etUserMobileNo)
        btnSaveMobileNo = findViewById((R.id.btnSaveMobileNo))

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSIONS_READ_PHONE_STATE
            )
        }

        var deviceId: String? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            deviceId = Settings.Secure.getString(
                applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (telephonyManager.imei != null) {
                deviceId = telephonyManager.imei
            } else if (telephonyManager.meid != null) {
                deviceId = telephonyManager.meid
            }
        }
        IMEINumber = deviceId.toString()
        Log.d("MyApp", "Device ID: $deviceId")



        etUserMobileNo?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int, p2: Int, p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int, p2: Int, p3: Int
            ) {
                // check inputted characters is a valid phone number or not
                if (p0.isValidPhoneNumber() && p0?.length == 11) {
                    etUserMobileNo?.error = null
                } else {
                    etUserMobileNo?.error = "Invalid phone number."
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })



        btnSaveMobileNo?.setOnClickListener{


             AddMobileNo()
        }


    }
    // extension function to validate edit text inputted phone number
    fun CharSequence?.isValidPhoneNumber():Boolean{
        return !isNullOrEmpty() && Patterns.PHONE.matcher(this).matches()
    }

    private fun  AddMobileNo() {
        val dialog = ProgressDialog.progressDialog(this@MobileNoAddActivity)

        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, URL,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {

                        dialog.dismiss()
                        val intent = Intent(this@MobileNoAddActivity, OtpActivity::class.java)
                        intent.putExtra("TITLE", "OTP")
                        intent.putExtra("USER_TYPE", "GatePass")
                        intent.putExtra("MOBILE_NO",  etUserMobileNo?.text.toString())
                        intent.putExtra("DEVIE_ID", IMEINumber.toString())
                        intent.putExtra("ACTIVITY",ACTIVITY.toString())
                        startActivity(intent)
                        finish()

                    }
                    if (success == "0") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Faild$e", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                dialog.dismiss()
                Toast.makeText(applicationContext, "Faild Error$error", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["step_id"] = "1"
                params["mobile_no"] = etUserMobileNo?.text.toString()
                params["device_id"] = IMEINumber.toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}