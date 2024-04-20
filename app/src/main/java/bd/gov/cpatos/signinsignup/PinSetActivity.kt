package bd.gov.cpatos.signinsignup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.util.*


class PinSetActivity : AppCompatActivity() {
    private var etNewPin: TextInputEditText? =null
    private var etReTypeNewPin: TextInputEditText? =null
    private var btnSavePin: Button? =null
    private var MOBILE_NO:String? = null
    private var DEVICE_ID:String? = null
    private var ACTIVITY:String?=null
    private var URL:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_set)
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

        etNewPin = findViewById(R.id.etNewPin)
        etReTypeNewPin = findViewById(R.id.etReTypeNewPin)
        btnSavePin = findViewById(R.id.btnSavePin)

        etNewPin?.addTextChangedListener(object : TextWatcher {
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
                if(etNewPin?.text.toString().length >=6) {
                    etNewPin?.error = null
                } else {
                    etNewPin?.error = "Minimum PIN/Password length 6"
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        etReTypeNewPin?.addTextChangedListener(object : TextWatcher {
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
                if(etNewPin?.text.toString() !="" && p0?.toString() ==etNewPin?.text.toString()) {
                    etReTypeNewPin?.error = null
                } else {
                    etReTypeNewPin?.error = "Not matched"
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })


        btnSavePin?.setOnClickListener {

           // nextActivity()
            if(etNewPin?.text.toString().length >=6)
            PinVerifiCation()
            else
                Toast.makeText(applicationContext, "Minimum PIN/Password length 6", Toast.LENGTH_LONG).show()

        }

    }



    private fun  PinVerifiCation() {
        val dialog = ProgressDialog.progressDialog(this@PinSetActivity)

        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_SIGNUP,
            Response.Listener { response ->
                Log.e("anyText", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {
                        dialog.dismiss()
                        if(ACTIVITY =="registration") {
                            val intent =
                                Intent(this@PinSetActivity, MoreUsersDataActivity::class.java)
                            intent.putExtra("TITLE", "ADD OTHER INFORMATIONS")
                            intent.putExtra("USER_TYPE", "GatePass")
                            intent.putExtra("MOBILE_NO", MOBILE_NO.toString())
                            intent.putExtra("DEVICE_ID", DEVICE_ID.toString())
                            startActivity(intent)
                            finish()
                        }else{

                            finish()
                        }
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
                params["step_id"] = "3"
                params["mobile_no"] =MOBILE_NO.toString()
                params["password"] = etReTypeNewPin?.text.toString()
                params["device_id"] = DEVICE_ID.toString()
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}