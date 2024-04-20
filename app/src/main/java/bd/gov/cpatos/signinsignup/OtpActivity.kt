package bd.gov.cpatos.signinsignup

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import bd.gov.cpatos.utils.SmsBroadcastReceiver
import bd.gov.cpatos.utils.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.util.HashMap

import java.util.regex.Matcher
import java.util.regex.Pattern


class OtpActivity : AppCompatActivity() {
    private var etOtp: TextInputEditText? = null
    private var btnVarifyOtp:Button? = null
    private var MOBILE_NO:String? = null
    private var DEVICE_ID:String? = null
    private var ACTIVITY:String?=null
    private var URL:String? =null
    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: SmsBroadcastReceiver? = null
  //  var verifyOTP: Button? = null
   // var textViewMessage: TextView? = null
  //  var otpText: EditText? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            MOBILE_NO = bundle.getString("MOBILE_NO")
            DEVICE_ID = bundle.getString("DEVICE_ID")
            ACTIVITY =  bundle.getString("ACTIVITY")
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        if(ACTIVITY=="registration"){
            URL = EndPoints.URL_SIGNUP

        }else{
            URL = EndPoints.URL_RESET
        }



        etOtp = findViewById(R.id.etOtp)
        btnVarifyOtp= findViewById(R.id.btnVarifyOtp)
        btnVarifyOtp?.setOnClickListener {
            /*var intent = Intent(this@OtpActivity, PinSetActivity::class.java)
            intent.putExtra("TITLE", "PIN Setup")
            intent.putExtra("USER_TYPE", "GatePass")
            startActivity(intent)*/
            if(etOtp?.text.toString() !="")
                PinVerifiCation()
            else
                Toast.makeText(applicationContext,"OTP is empty",Toast.LENGTH_LONG ).show()
        }

        startSmsUserConsent()
    }
    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {
            Toast.makeText(
                applicationContext,
                "On Success",
                Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "On OnFailure", Toast.LENGTH_LONG).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                textViewMessage?.setText(
//                    String.format(
//                        "%s - %s",
//                        getString(R.string.received_message),
//                        message
//                    )
//                )
                getOtpFromMessage(message.toString())
            }
        }
    }
    private fun getOtpFromMessage(message: String) {
        // This will match any 6 digit number in the message
        val pattern: Pattern = Pattern.compile("(|^)\\d{4}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            etOtp?.setText(matcher.group(0))
        }
    }
    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver?.smsBroadcastReceiverListener = object : SmsBroadcastReceiverListener {
            override fun onSuccess(intent: Intent?) {
                startActivityForResult(intent, REQ_USER_CONSENT)
            }

            override fun onFailure() {}
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }
    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

    private fun  PinVerifiCation() {
        val dialog = ProgressDialog.progressDialog(this@OtpActivity)

        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, URL,
            Response.Listener { response ->
                Log.e("anyText", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {

                        dialog.dismiss()
                        val intent = Intent(this@OtpActivity, PinSetActivity::class.java)
                        intent.putExtra("TITLE", "PIN/PASSWORD SETUP")
                        intent.putExtra("USER_TYPE", "GatePass")
                        intent.putExtra("MOBILE_NO", MOBILE_NO.toString())
                        intent.putExtra("DEVICE_ID", DEVICE_ID.toString())
                        intent.putExtra("ACTIVITY", ACTIVITY.toString())
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
                params["step_id"] = "2"
                params["mobile_no"] = MOBILE_NO.toString()
                params["otp"] = etOtp?.text.toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }


}