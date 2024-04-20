package bd.gov.cpatos.signinsignup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R.*
import bd.gov.cpatos.gate.activities.GateInActivity
import bd.gov.cpatos.gate.activities.GateLandingActivity
import bd.gov.cpatos.gate.activities.GateOutActivity
import bd.gov.cpatos.gatepass.GatePassHomeActivity
import bd.gov.cpatos.main.MainActivity
import bd.gov.cpatos.pilot.activities.PilotLandingPage
import bd.gov.cpatos.reeferwater.WaterSupplyActivity
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {


    private var mPreferences: SharedPreferences?=null
    //edittext and spinner
    private var lusername: TextInputEditText? = null
    private var lpassword: TextInputEditText? = null
   // private var requiredDocTxtView: TextView? = null
    private var ActivityTitle: TextView? = null
    private var versionis: TextView? = null
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var is_signed_in  = ""
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var NEXT_ACTIVTY:String? =null
    private var loginbutton:Button? = null
    private var signUpButton:TextView? = null
    private var btnReset:TextView? = null
//    private val sharedPreference: SharedPreference = SharedPreference(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)
        ActivityTitle= findViewById(id.ActivityTitle)
        lusername = findViewById(id.lusername)
        lpassword = findViewById(id.lpassword)
        versionis =  findViewById(id.version)
      //  requiredDocTxtView=  findViewById(id.requiredDocTxtView)
         //versionis = findViewById(id.version)
        loginbutton = findViewById(id.loginbutton)
        signUpButton = findViewById(id.signUp)
        btnReset = findViewById(id.forgot_passwd_tv)

        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            NEXT_ACTIVTY = bundle.getString("NEXT_ACTIVTY") // 1
            ActivityTitle?.text = bundle.getString("TITLE")
        }

        mPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        is_signed_in = mPreferences?.getString("issignedin","false").toString()
        Log.e("ASIF DATA", is_signed_in)
        if(is_signed_in.equals("true")){
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        var versionName: String? = ""
        try {
            versionName = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        versionis?.text = "Version: $versionName"
//        Log.e("Version:",versionName)

        loginbutton?.setOnClickListener{

           if(lusername?.text.toString().isEmpty()) {
               lusername?.setError("Enter User Name please");
               lusername?.requestFocus();
               makeText(this@LoginActivity, string.toast_login_activity_user, LENGTH_SHORT).show()
           }else if(lpassword?.text.toString().isEmpty()) {
               makeText(this@LoginActivity, string.toast_login_activity_password, LENGTH_SHORT).show()
           }else {
               login()
           }
        }
//        if(NEXT_ACTIVTY=="TosGatePass"){
//
//            requiredDocTxtView?.setVisibility(TextView.VISIBLE);
//        }else{
//            requiredDocTxtView?.setVisibility(TextView.INVISIBLE);
//        }


        signUpButton?.setOnClickListener{
            signUp()
        }
        btnReset?.setOnClickListener{
            passwordReset()
        }
    }

    //adding a new record to database
    private fun login() {
        var dialog = ProgressDialog.progressDialog(this@LoginActivity)
        dialog.show()
        //getting the record values
        val username = lusername?.text.toString()
        val password = lpassword?.text.toString()



        //creating volley string request
        val stringRequest = object : StringRequest(Request.Method.POST, EndPoints.URL_LOGIN,
                Response.Listener<String> { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        //  val message = jsonObject.getString("message")
                        if (success.equals("1")) {
                            dialog.dismiss()
                            val SignedInId: String = jsonObject.getString("id")
                            val SignedInLoginId: String = jsonObject.getString("login_id")
                            val SignedInSection: String = jsonObject.getString("section")
                            val SignedInUserRole: String = jsonObject.getString("user_role")

                            mPreferences?.getString("issignedin", "true")
                            mPreferences?.getString("SignedInId", SignedInId)
                            mPreferences?.getString("SignedInLoginId", SignedInLoginId)
                            mPreferences?.getString("SignedInSection", SignedInSection)
                            mPreferences?.getString("SignedInSection", SignedInSection)
                            mPreferences?.getString("SignedInUserRole", SignedInUserRole)
                            //preferencesEditor?.putString("AlarmIp", "XXX.XXX.X.XXX")
                            preferencesEditor?.commit()

                            if(NEXT_ACTIVTY=="PilotLandingPage" && SignedInUserRole =="64") {
                                Toast.makeText( applicationContext,"PilotLandingPage",
                                    Toast.LENGTH_LONG).show()
                                val i = Intent(this@LoginActivity, PilotLandingPage::class.java)
                                startActivity(i)
                                finish()

                            }else if(NEXT_ACTIVTY=="WaterByMarin" && SignedInUserRole =="64"){
                                val intent = Intent(this@LoginActivity, WaterSupplyActivity::class.java)

                                intent.putExtra("TITLE", "Water By Boat")
                                intent.putExtra("USER_TYPE", "Marin")
                                startActivity(intent)
                                finish()

                            }else if(NEXT_ACTIVTY=="WaterByMarin" && SignedInUserRole =="78"){
                                val intent = Intent(this@LoginActivity, WaterSupplyActivity::class.java)
                                intent.putExtra("TITLE", "Water By Line")
                                intent.putExtra("USER_TYPE", "Electric")
                                startActivity(intent)
                                finish()

                            }else if(NEXT_ACTIVTY=="TosGatePass" && SignedInUserRole =="79"){
                                val intent = Intent(this@LoginActivity, GatePassHomeActivity::class.java)
                                intent.putExtra("TITLE", "DeshBoard")
                                startActivity(intent)
                                finish()

                            }else if(NEXT_ACTIVTY=="GATEIN" && SignedInUserRole =="67"){
                                val intent = Intent(this@LoginActivity, GateInActivity::class.java)
                                intent.putExtra("TITLE", "Gate In")
                                startActivity(intent)
                                finish()

                            }else if(NEXT_ACTIVTY=="GATEOUT" && SignedInUserRole =="67"){
                                val intent = Intent(this@LoginActivity, GateOutActivity::class.java)
                                intent.putExtra("TITLE", "Gate Out")
                                startActivity(intent)
                                finish()

                            }else if(NEXT_ACTIVTY=="GATE_MODULE" && SignedInUserRole =="67"){
                                val intent = Intent(this@LoginActivity, GateLandingActivity::class.java)
                                intent.putExtra("TITLE", "Gate Module")
                                startActivity(intent)
                                finish()

                            }else{
                                Toast.makeText( applicationContext,"Main Page", Toast.LENGTH_LONG).show()
                                val i = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(i)
                                finishAffinity()
                            }


                        } else {
                            dialog.dismiss()
                            makeText(applicationContext, jsonObject.getString("message"), LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog.dismiss()
                        makeText(applicationContext, string.toast_login_activity_tryagain, LENGTH_LONG).show()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        dialog.dismiss()
                        makeText(applicationContext, string.toast_login_activity_tryagain, LENGTH_LONG).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("username", username)
                params.put("password", password)
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@LoginActivity)
        queue?.add(stringRequest)

    }

    private fun signUp() {
        val intent = Intent(this@LoginActivity, MobileNoAddActivity::class.java)
        intent.putExtra("TITLE", "Mobile No Entry")
        intent.putExtra("USER_TYPE", "GatePass")
        intent.putExtra("ACTIVITY", "registration")
        startActivity(intent)


    }
private fun passwordReset() {
        val intent = Intent(this@LoginActivity, MobileNoAddActivity::class.java)
        intent.putExtra("TITLE", "Mobile No Entry")
        intent.putExtra("USER_TYPE", "GatePass")
        intent.putExtra("ACTIVITY", "reset")
        startActivity(intent)


    }


}