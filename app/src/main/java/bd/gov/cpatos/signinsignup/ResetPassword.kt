package bd.gov.cpatos.signinsignup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private var mPreferences: SharedPreferences? = null
    private var is_signed_in  = ""
    private var preferencesEditor: SharedPreferences.Editor? = null


    private var lusername: TextInputEditText? = null
    private var lpassword: TextInputEditText? = null
    private var newPassTinputEditText: TextInputEditText? = null
    private var reTypenewPassTinputEditText: TextInputEditText? = null
     private var lusernameTL: TextInputLayout? = null
    private var lpasswordTL: TextInputLayout? = null
    private var newPassTinputEditTextTL: TextInputLayout? = null
    private var reTypenewPassTinputEditTextTL: TextInputLayout? = null

    private var updatePassbutton: Button? = null
    private var mContext: Context?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        getAllWidgets()
        mContext = this
        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor =mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")


        lusername?.setText(SignedInLoginId)
        //buttonAction()
        updatePassbutton?.setOnClickListener {
            val lusername = lusername?.text.toString()
            val lpassword = lpassword?.text.toString()
            val newPassTinputEditText = newPassTinputEditText?.text.toString()
            val reTypenewPassTinputEditText = reTypenewPassTinputEditText?.text.toString()

            if (lusername == "" || lpassword == "" || newPassTinputEditText == "" || reTypenewPassTinputEditText == ""){
                Toast.makeText(mContext, "Any Field should not be Empty", Toast.LENGTH_LONG).show()
            }else {


                //getting the record values

                if (newPassTinputEditText != reTypenewPassTinputEditText) {

                    reTypenewPassTinputEditTextTL?.error = "Password Not Matched"
                } else {

                    password_reset(lusername,lpassword,newPassTinputEditText)
                }
           }

        }

    }
    private fun getAllWidgets(){
        lusername= findViewById(R.id.userName)
        lpassword= findViewById(R.id.oldPassword)
        newPassTinputEditText= findViewById(R.id.newPassTinputEditText)
        reTypenewPassTinputEditText= findViewById(R.id.reTypenewPassTinputEditText)
        lusernameTL= findViewById(R.id.lusernameTL)
        lpasswordTL= findViewById(R.id.lpasswordTL)
        newPassTinputEditTextTL= findViewById(R.id.newPassTinputEditTextTL)
        reTypenewPassTinputEditTextTL= findViewById(R.id.reTypenewPassTinputEditTextTL)
        updatePassbutton= findViewById(R.id.updatePassbutton)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = this.title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun password_reset(userName:String?,oldPass:String?,newPass:String?) {
        var dialog = ProgressDialog.progressDialog(this@ResetPassword)
        dialog.show()



            //creating volley string request
            val stringRequest = object : StringRequest(
                Request.Method.POST, EndPoints.URL_RESETPASS,
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

                            preferencesEditor?.putString("issignedin", "true")
                            preferencesEditor?.putString("SignedInId", SignedInId)
                            preferencesEditor?.putString("SignedInLoginId", SignedInLoginId)
                            preferencesEditor?.putString("SignedInSection", SignedInSection)
                            preferencesEditor?.putString("SignedInUserRole", SignedInUserRole)
                            //preferencesEditor?.putString("AlarmIp", "XXX.XXX.X.XXX")
                            preferencesEditor?.commit()

                            finish()

                        } else {
                            dialog.dismiss()
                            Toast.makeText(
                                applicationContext,
                                jsonObject.getString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            R.string.toast_login_activity_tryagain,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        dialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            R.string.toast_login_activity_tryagain,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params.put("username", userName!!)
                    params.put("password", oldPass!!)
                    params.put("new_password", newPass!!)
                    return params
                }
            }

            //adding request to queue
            val queue = Volley.newRequestQueue(this@ResetPassword)
            queue?.add(stringRequest)

    }

}