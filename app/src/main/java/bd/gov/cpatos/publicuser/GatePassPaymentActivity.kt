package bd.gov.cpatos.publicuser

import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.gatepass.OpenPaymentActivity
import bd.gov.cpatos.gatepass.PaymentActivity
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class GatePassPaymentActivity : AppCompatActivity() {
    private var etUserMobileNofortruck:TextInputEditText? =null
    private var autcUserCnf: AutoCompleteTextView? = null
    private var CnFArrayList : ArrayList<String> = arrayListOf<String>()
    private var btnSonaliPay:ImageButton? = null
    private var btnEkPay:ImageButton? =null
    private var requestIdFromServer: String? = null
    private var visitIdFromServer: String? = null
    private var mobileNumberSring:String? = null
    private var DEVICE_ID:String? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_pass_payment)
        init()
        UI()
        getCnF()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        Actions()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun init(){
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
        DEVICE_ID = deviceId.toString()
        Log.d("MyApp", "Device ID: $deviceId")
    }
    fun UI(){
        etUserMobileNofortruck = findViewById(R.id.etUserMobileNofortruck)
        autcUserCnf = findViewById(R.id.autcUserCnf)
        btnSonaliPay = findViewById(R.id.btnSonaliPayment)
        btnEkPay = findViewById(R.id.btnEkPayment)

    }
    fun Actions(){

        btnSonaliPay?.setOnClickListener({
            mobileNumberSring = etUserMobileNofortruck?.text.toString()

            Log.e("VALUE IS: "+mobileNumberSring ,"LENGTH ID:"+mobileNumberSring?.count())

            if (mobileNumberSring?.length == 11) {
                GetUniqueId()
            }else{
                Toast.makeText(this,"Please enter a valid phone number",Toast.LENGTH_LONG).show()
            }

        })



    }

    private fun getCnF() {
        var dialog = ProgressDialog.progressDialog(this@GatePassPaymentActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_FnF_LIST,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            var dataInner: JSONObject = data.getJSONObject(i)
                            val cnf = dataInner.getString("cnf")
                            if (cnf != "") {
                                CnFArrayList.add(cnf)
                            }
                        }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, CnFArrayList)
                        autcUserCnf?.setAdapter(adapter)
                    } else {
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    // Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener {
                dialog.dismiss()
                Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                var versionName: String? = ""
                try {
                    versionName = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0).versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                params.put("app_version", versionName.toString())
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@GatePassPaymentActivity)
        queue?.add(stringRequest)
    }

    private fun GetUniqueId() {
        //    var dialog = ProgressDialog.progressDialog(this@FclTruckEntryDetailsActivity)
        //   dialog.show()
        //creating volley string request
        val stringRequest1 = object : StringRequest(
            Method.POST,
            EndPoints.URL_OPEN_VISIT_ID_CREATE,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        //    dialog.dismiss()
                        requestIdFromServer= jsonObject.getString("strRequestId").toString()
                        visitIdFromServer= jsonObject.getString("visitId").toString()
                        if(requestIdFromServer !="" && visitIdFromServer  !=""){
                            //  paymentBySonaliPay()
                           startPayment()
                        }else{
                            Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()

                        }
                    } else {
                        // dialog.dismiss()
                        Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // dialog.dismiss()
                    // Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    //   dialog.dismiss()
                    Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

                val params = HashMap<String, String>()
                params.put("request_id", "sonali_pay")
                params.put("mobile_no",etUserMobileNofortruck?.text.toString())
                params.put("cnf","101110502-AFRIN ENTERPRISE")
               // params.put("cnf",autcUserCnf?.text.toString())
                //params.put("login_id", MOBILE_NO.toString())
                params.put("ipaddr", DEVICE_ID.toString())
                return params
            }
        }

        //adding request to queue
        val queue1 = Volley.newRequestQueue(this@GatePassPaymentActivity)
        queue1?.add(stringRequest1)
    }

    private fun startPayment(){
        var RefTranNo:String? = requestIdFromServer+"_"+0
       // var PARAM:String?="?P_M=S_P&R="+RefTranNo+"&R_I="+requestIdFromServer+"&C="+mobileNumberSring.toString()
        var PARAM:String?="?P_M=S_P&V_I="+visitIdFromServer+"&R="+RefTranNo+"&R_I="+requestIdFromServer+"&C="+etUserMobileNofortruck?.text.toString()
        val intent = Intent(this@GatePassPaymentActivity, OpenPaymentActivity::class.java)
        intent.putExtra("TITLE", "Online Payment")
        intent.putExtra("USER_TYPE", "GatePass")
        intent.putExtra("PARAM",PARAM)
        startActivity(intent)
    }

}