package bd.gov.cpatos.pilot.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_vessel.*
import kotlinx.android.synthetic.main.activity_vessel_details.*
import org.json.JSONObject
import java.util.*

class AddVesselActivity : AppCompatActivity() {
    private var etVesselName: TextInputEditText? = null
    private var etMasterName: TextInputEditText? = null
    private var etFlag: TextInputEditText? = null
    private var etGrt: TextInputEditText? = null
    private var etNrt: TextInputEditText? = null
    private var etDechCargo: TextInputEditText? = null
    private var etLoa: TextInputEditText? = null
    private var etLocalAgent: TextInputEditText? = null
    private var etLastPort: TextInputEditText? = null
    private var etNextPort: TextInputEditText? = null
    private var etRotation: TextInputEditText? = null
    private var stetVesselName:String? = null
    private var stetMasterName:String? = null
    private var stetFlag:String? = null
    private var stetGrt:String? = null
    private var stetNrt:String? = null
    private var stetDechCargo:String? = null
    private var stetLoa:String? = null
    private var stetLocalAgent:String? = null
    private var stetLastPort:String? = null
    private var stetNextPort:String? = null
    private var stetRotation:String? = null

    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null

    private var SignedInId:String? = null
    private var SignedInLoginId:String? = null
    private var SignedInSection:String? = null
    private var SignedInUserRole:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vessel)

        init()
        getUserPreferenceData()
        getUI()
        btnSaveVsl?.setOnClickListener{
            getData()
            if(stetVesselName.equals("") || stetMasterName.equals("")||stetFlag.equals("")
                || stetGrt.equals("")|| stetNrt.equals("")|| stetDechCargo.equals("")
                || stetLoa.equals("")|| stetLocalAgent.equals("")|| stetLastPort.equals("")
                || stetNextPort.equals("")|| stetRotation.equals("")){
                Toast.makeText(applicationContext, "Please fill up All field", Toast.LENGTH_LONG).show()
            }else{
                AddNewVessel()
            }
        }
    }
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getUserPreferenceData(){
        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
    }
    private fun getUI(){
        etVesselName = findViewById(R.id.etVesselName)
        etMasterName = findViewById(R.id.etMasterName)
        etFlag = findViewById(R.id.etFlag)
        etGrt = findViewById(R.id.etGrt)
        etNrt = findViewById(R.id.etNrt)
        etDechCargo = findViewById(R.id.etDechCargo)
        etLoa = findViewById(R.id.etLoa)
        etLocalAgent = findViewById(R.id.etLocalAgent)
        etLastPort = findViewById(R.id.etLastPort)
        etNextPort = findViewById(R.id.etNextPort)
        etRotation = findViewById(R.id.etRotation)
    }
    private fun getData() {
        stetVesselName = etVesselName?.text.toString().trim()
        stetMasterName = etMasterName?.text.toString().trim()
        stetFlag = etFlag?.text.toString().trim()
        stetGrt = etGrt?.text.toString().trim()
        stetNrt = etNrt?.text.toString().trim()
        stetDechCargo = etDechCargo?.text.toString().trim()
        stetLoa = etLoa?.text.toString().trim()
        stetLocalAgent = etLocalAgent?.text.toString().trim()
        stetLastPort = etLastPort?.text.toString().trim()
        stetNextPort = etNextPort?.text.toString().trim()
        stetRotation = etRotation?.text.toString().trim()
    }
    private fun clearData() {
        etVesselName?.setText("")
        etMasterName?.setText("")
        etRotation?.setText("")
        etFlag?.setText("")
        etGrt?.setText("")
        etNrt?.setText("")
        etDechCargo?.setText("")
        etLoa?.setText("")
        etLocalAgent?.setText("")
        etLastPort?.setText("")
        etNextPort?.setText("")
        etRotation?.setText("")
    }
    private fun AddNewVessel() {
        val dialog = ProgressDialog.progressDialog(this@AddVesselActivity)

        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_ADD_NEW_VESSEL,
            Response.Listener { response ->
                Log.e("anyText", response)
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success == "1") {
                        clearData()
                        finish()
                        Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    if (success == "0") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    if (success == "3") {
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
                params["strVslName"] = stetVesselName!!
                params["strFlag"] = stetFlag!!
                params["strRotationNumber"] = stetRotation!!
                params["strGrt"] = stetGrt!!
                params["strNrt"] = stetNrt!!
                params["strDeckCargo"] = stetDechCargo!!
                params["strLoa"] = stetLoa!!
                params["strLocalAgent"] = stetLocalAgent!!
                params["strLastPort"] = stetLastPort!!
                params["strNextPort"] = stetNextPort!!
                params["strRotationNumber"] = stetRotation!!
                params["entry_by"] = SignedInLoginId!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

}