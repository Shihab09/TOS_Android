package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_vessel_details.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class VesselDetailsActivity : AppCompatActivity(),View.OnClickListener {


    private var VVD_GKEY_old = ""
    private var ROTATION_old = ""
    private var ACTIVITY_FOR_old = ""
    private var VVD_GKEY = ""
    private var ROTATION = ""
    private var ACTIVITY_FOR = ""
    companion object {
        const val START_ACTIVITY_PILOTONBOARDOFFBPOARD_REQUEST_CODE = 702
    }
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var isSignedIn:String? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
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
    private var btnProcide: Button? = null

    private var radioGroupForChanel: RadioGroup? = null
    private var radioChannelByKaraphuli: RadioButton? =null
    private var radioChannelByMatarbari: RadioButton? =null
    private var CHANNEL=1
//    val sharedPreference: SharedPreference = SharedPreference(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vessel_details)
        init()
        getUserPreferenceData()
        getUI()
        getVesselDetails()

        radioGroupForChanel!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioDriverGroup, i ->
            //  textView.text = "option "+i+" is selected"
            //var selectedId:Int = radioGroupWaterBy!!.getCheckedRadioButtonId();
            val radio: RadioButton = findViewById(i)

            if(radio.text == "Karnaphuli Channel")
                CHANNEL
            else
                CHANNEL = 2


            // Toast.makeText(this," option "+radio.text+" is selected", Toast.LENGTH_LONG).show()

        })
        btnProcide?.setOnClickListener(this)
    }
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            VVD_GKEY_old = bundle.getString("VVD_GKEY").toString()
            ROTATION_old = bundle.getString("ROTATION").toString()
            ACTIVITY_FOR_old = bundle.getString("ACTIVITY_FOR").toString()

            VVD_GKEY =VVD_GKEY_old
            ROTATION = ROTATION_old
            ACTIVITY_FOR = ACTIVITY_FOR_old
      //      Log.e("ROTATION-2",ROTATION)
        }
        VVD_GKEY =VVD_GKEY_old
        ROTATION = ROTATION_old
        ACTIVITY_FOR = ACTIVITY_FOR_old
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
    private fun getUserPreferenceData(){
        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        isSignedIn = mPreferences?.getString("issignedin", "false")
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
        btnProcide = findViewById(R.id.btnProceed)
        radioGroupForChanel = findViewById(R.id.radioGroupChannel)
        radioChannelByKaraphuli  = findViewById(R.id.radioKarnaphuli)
        radioChannelByMatarbari  = findViewById(R.id.radioMatarbari)

    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnProceed ->{
                AddVesselDetails()
            }
        }
    }
    private fun getVesselDetails() {
        var dialog = ProgressDialog.progressDialog(this@VesselDetailsActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_VESSEL_DETAILS,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    //Log.e("Message",jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val dStations = JSONArray(jsonObject.getString("data"))
                        // val dStations: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until dStations.length()) {
                            var dataInner: JSONObject = dStations.getJSONObject(i)
                            etVesselName?.setText(dataInner.getString("name"))
                            etMasterName?.setText(dataInner.getString("Name_of_Master"))
                            etFlag?.setText(dataInner.getString("flag"))
                            etGrt?.setText(dataInner.getString("gross_registered_ton"))
                            etNrt?.setText(dataInner.getString("net_registered_ton"))
                            etLoa?.setText(dataInner.getString("loa_cm"))
                            etLocalAgent?.setText(dataInner.getString("localagent"))
                            etLastPort?.setText(dataInner.getString("last_port"))
                            etNextPort?.setText(dataInner.getString("next_port"))
                            etRotation?.setText(ROTATION)
                        }
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
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("rotation", ROTATION)
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@VesselDetailsActivity)
        queue?.add(stringRequest)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_PILOTONBOARDOFFBPOARD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val message = data!!.getStringExtra("message")
                // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                sendDataBackToPreviousActivity()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun sendDataBackToPreviousActivity() {
        val intent = Intent().apply {
            putExtra("message", "This is a message from Activity3")
            // Put your data here if you want.
            //finish()
        }
        setResult(Activity.RESULT_OK, intent)
    }
    private fun AddVesselDetails(){

        var dialog = ProgressDialog.progressDialog(this@VesselDetailsActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_ADD_VESSEL_DETAILS,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                  //  Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        var intent:Intent? = null
                        if(ACTIVITY_FOR == "incoming") {
                            intent = Intent(this@VesselDetailsActivity, PilotageOperation::class.java)
                            intent.putExtra("TITLE", "Incoming Rot:(" + ROTATION + ")")
                        }else if(ACTIVITY_FOR == "shifting") {
                            intent = Intent(this@VesselDetailsActivity, PilotageOperation::class.java)
                            intent.putExtra("TITLE", "Shifting Rot:(" + ROTATION + ")")

                        }else  if(ACTIVITY_FOR == "outgoing") {
                            intent = Intent(this@VesselDetailsActivity, PilotageOperation::class.java)
                            intent.putExtra("TITLE", "Outgoing Rot:(" + ROTATION + ")")
                        }
                        else if(ACTIVITY_FOR=="cancel"){
                            intent = Intent(this@VesselDetailsActivity, CancelationActivity::class.java)
                            intent.putExtra("TITLE", "Cancel Rot:(" + ROTATION + ")")
                        }
                        // Pass the values to next activity (StationActivity)
                        intent?.putExtra("VVD_GKEY",VVD_GKEY)
                        intent?.putExtra("ROTATION",ROTATION)
                        intent?.putExtra("ACTIVITY_FOR",ACTIVITY_FOR)
                        startActivityForResult(intent,START_ACTIVITY_PILOTONBOARDOFFBPOARD_REQUEST_CODE)

                    }else {
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()

                params.put("vsl_name",  etVesselName?.text.toString())
                params.put("master_name", etMasterName?.text.toString())
                params.put("vvd_gkey",  VVD_GKEY)
                params.put("flag",  etFlag?.text.toString())
                params.put("grt", etGrt?.text.toString())
                params.put("nrt", etNrt?.text.toString())
                params.put("deck_cargo", etDechCargo?.text.toString())
                params.put("loa", etLoa?.text.toString())
                params.put("local_agent", etLocalAgent?.text.toString())
                params.put("last_port", etLastPort?.text.toString())
                params.put("next_port", etNextPort?.text.toString())
                params.put("rotation", etRotation?.text.toString())
                params.put("entry_by", SignedInLoginId.toString())
                params.put("channel",CHANNEL.toString())

                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@VesselDetailsActivity)
        queue?.add(stringRequest)
    }
}