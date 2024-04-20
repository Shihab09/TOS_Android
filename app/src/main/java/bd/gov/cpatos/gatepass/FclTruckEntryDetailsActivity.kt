package bd.gov.cpatos.gatepass

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


class FclTruckEntryDetailsActivity : AppCompatActivity() {
    private var btnSonaliPay: ImageButton? = null
    private var btnEkPay: ImageButton? = null
    private var truck_visit: String? = null
    private var cont_no: String? = null
    private var truck_no: String? = null
    private var driver_id: String? = null
    private var driver_name: String? = null
    private var driver_mobile_no: String? = null
    private var helper_id: String? = null
    private var assist_name: String? = null
    private var cf_lic: String? = null
    private var cf_name: String? = null
    private var fee: String? = null
    private var visit_time_slot_start: String? = null
    private var visit_time_slot_end: String? = null
    private var strRequestId: String? = null
    private var etEntryFee: TextInputEditText?=null
    private var etVisitId: TextInputEditText?=null
    private var etContainerNo: TextInputEditText?=null
    private var etCnfWithLic: TextInputEditText?=null
    private var etTruckNo: TextInputEditText?=null
    private var etDriverCardNo: TextInputEditText?=null
    private var etDriverName: TextInputEditText?=null
    private var etDriverMobileNo: TextInputEditText?=null
    private var etHelperCardNo: TextInputEditText?=null
    private var etHelperName: TextInputEditText?=null
    private var tvTimeSlot: TextInputEditText?=null
    private var TODAY:String? = null
    private var MOBILE_NO:String? = null
    private var DEVICE_ID:String? = null
    private var assignmentType:String? = null
    private var mContext: Context? = null
    private var RefTranNo:String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fcl_truck_entry_details)
        mContext = this
        init()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE")
            supportActionBar?.title = title
            truck_visit = bundle.getString("truck_visit")
            cont_no = bundle.getString("cont_no")
            truck_no = bundle.getString("truck_no")
            driver_id = bundle.getString("driver_id")
            driver_name = bundle.getString("driver_name")
            driver_mobile_no =  bundle.getString("driver_mobile_no")
            helper_id = bundle.getString("helper_id")
            assist_name = bundle.getString("assist_name")
            cf_lic = bundle.getString("cf_lic")
            cf_name = bundle.getString("cf_name")
            assist_name = bundle.getString("assist_name")
            fee = bundle.getString("fee")
            visit_time_slot_start = bundle.getString("visit_time_slot_start")
            visit_time_slot_end = bundle.getString("visit_time_slot_end")
           // strRequestId = bundle.getString("strRequestId")
            assignmentType = bundle.getString("assignmentType")
            MOBILE_NO = bundle.getString("MOBILE_NO")
            DEVICE_ID = bundle.getString("DEVICE_ID")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setValue()
        btnSonaliPay?.setOnClickListener({
            GetUniqueId()
       })

        val currentDate =LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        TODAY = currentDate.format(formatter)
        btnEkPay?.setOnClickListener({
           // paymentBySonaliPay()
        })

    }
   private fun init(){

       etEntryFee= findViewById(R.id.etEntryFee)
       etVisitId= findViewById(R.id.etVisitId)
       etContainerNo= findViewById(R.id.etContainerNo)
       etCnfWithLic= findViewById(R.id.etCnfWithLic)
       etTruckNo= findViewById(R.id.etTruckNo)
       etDriverCardNo= findViewById(R.id.etDriverCardNo)
       etDriverName= findViewById(R.id.etDriverName)
       etDriverMobileNo= findViewById(R.id.etDriverMobileNo)
       etHelperCardNo= findViewById(R.id.etHelperCardNo)
       etHelperName= findViewById(R.id.etHelperName)
       tvTimeSlot= findViewById(R.id.tvTimeSlotDet)
       btnSonaliPay = findViewById(R.id.btnSonaliPay)
       btnEkPay = findViewById(R.id.btnEkPayfcl)
   }


    private fun GetUniqueId() {
    //    var dialog = ProgressDialog.progressDialog(this@FclTruckEntryDetailsActivity)
     //   dialog.show()
        //creating volley string request
        val stringRequest1 = object : StringRequest(
            Method.POST,
            EndPoints.URL_CNF_TRUCK_ENTRY,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                   Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                    //    dialog.dismiss()
                        strRequestId= jsonObject.getString("strRequestId").toString()
                        if(strRequestId !=""){
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
                val params = HashMap<String, String>()
                params.put("request_id", "sonali_pay")
                params.put("truck_visit",truck_visit.toString())
                params.put("assignmentType", assignmentType.toString())
                params.put("driver_mobile_no", driver_mobile_no.toString())
                params.put("login_id", MOBILE_NO.toString())


                return params
            }
        }

        //adding request to queue
        val queue1 = Volley.newRequestQueue(this@FclTruckEntryDetailsActivity)
        queue1?.add(stringRequest1)
    }



    private fun setValue(){

        etEntryFee?.setText(fee)
        etVisitId?.setText(truck_visit)
        etContainerNo?.setText(cont_no)
        etCnfWithLic?.setText(cf_lic+"("+cf_name+")")
        etTruckNo?.setText(truck_no)
        etDriverCardNo?.setText(driver_id)
        etDriverName?.setText(driver_name)
        etDriverMobileNo?.setText(driver_mobile_no)
        etHelperCardNo?.setText(helper_id)
        etHelperName?.setText(assist_name)
        tvTimeSlot?.setText(visit_time_slot_start+" to  "+visit_time_slot_end)
    }

    private fun paymentBySonaliPay(){
     //   val dialog = ProgressDialog.progressDialog(this@FclTruckEntryDetailsActivity)
      //  dialog.show()
        val mainObject = JSONObject() // Host object
        val AccessUser = JSONObject() // Included object
        try {
            AccessUser.put("userName", "CtGPoRt2015")
            AccessUser.put("password", "XporLocDbs\$TghDl23@34t97")

            mainObject.put("AccessUser", AccessUser)
            mainObject.put("strUserId", "CtGPoRt2015")
            mainObject.put("strPassKey", "XporLocDbs\$TghDl23@34t97")
            mainObject.put("strRequestId", strRequestId)
            mainObject.put("strAmount", fee)
            mainObject.put("strTranDate", TODAY)
            mainObject.put("strAccounts", "1113300250311-0820102000468")


        }catch (e: JSONException) {
            Log.e("JSON Error", "PARAM ERROR")
        }
        Log.e("PARAM", mainObject.toString())

        val stringRequest2: StringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_SONALI_GET_TOKEN_REQUEST,
            Response.Listener<String?> { response ->
                Log.e("1.Status", "INSIDE RESPONSE")
                //   val jsonObject = JSONObject(response)
                // val scretKey = jsonObject.getString("scretKey")

                var formatedStr = response
                Log.e("JSON VALL", formatedStr)
                try{
                    formatedStr=response?.replace("\\", "")?.replace("\"{", "{")?.replace("}\"", "}")
                    val jsonObject = JSONObject(formatedStr)
                    Log.e("1.Status", jsonObject.toString())
                    var scretKey:String = jsonObject.getString("scretKey")
                    if(scretKey != ""){
                      //  dialog.dismiss()
                        Log.e("RESPONSE-VAL", "DATA FOUND: " + scretKey)
                        paymentBySonaliPayWithKey(scretKey)

                    }

                } catch (e: JSONException) {
                    Log.e("JSON Error", formatedStr)
                    Toast.makeText(this,"Something went Worng",Toast.LENGTH_LONG).show()
                   // dialog.dismiss()
                }
            }, Response.ErrorListener {
              //  dialog.dismiss()

            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                headers["Authorization"] = "OAuth 2.0 token here"
                Log.e("1.Status", "INSIDE HEADER")
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                Log.e("1.Status", "INSIDE BODY"+ mainObject.toString().toByteArray())
                return mainObject.toString().toByteArray()
            }
        }
        val queue2 = Volley.newRequestQueue(this@FclTruckEntryDetailsActivity)
        queue2.add(stringRequest2)


    }

    private fun paymentBySonaliPayWithKey(secKey: String){
        val dialog = ProgressDialog.progressDialog(this@FclTruckEntryDetailsActivity)
        dialog.show()
        val mainObject = JSONObject() // Host object
        val Authentication = JSONObject() // Included object
        val ReferenceInfo = JSONObject() // Included object
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())
        try {
            RefTranNo = strRequestId+"_"+0
            val itemObject1 = JSONObject()
            itemObject1.put("SLNO", "1")
            itemObject1.put("CreditAccount", "1113300250311")
            itemObject1.put("CrAmount", "0.15")
            itemObject1.put("Purpose", "CHL")
            itemObject1.put("Onbehalf", "CTGPORT")

            val itemObject2 = JSONObject()
            itemObject2.put("SLNO", "2")
            itemObject2.put("CreditAccount", "0820102000468")
            itemObject2.put("CrAmount", "1")
            itemObject2.put("Purpose", "TRN")
            itemObject2.put("Onbehalf", "CTGPORT")

            val CreditInformations = JSONArray()
            CreditInformations.put(itemObject1)
            CreditInformations.put(itemObject2)



            Authentication.put("ApiAccessUserId", "CtGPoRt2015")
            Authentication.put("ApiAccessPassKey", secKey)

            ReferenceInfo.put("RequestId", strRequestId) //"'.$requst_id.'",
            ReferenceInfo.put("RefTranNo", RefTranNo)//"'.$ref.'",
            ReferenceInfo.put("RefTranDateTime", currentDate) //"'.$cur_time.'",
            ReferenceInfo.put(
                "ReturnUrl",
                "http://cpatos.gov.bd/tosapi/cnf/payment_info.php"
            )
            // "http://cpatos.gov.bd/pcs/index.php/ShedBillController/onlinePaymentSuccess",
            ReferenceInfo.put("ReturnMethod", "POST")
            ReferenceInfo.put("TranAmount", "1.15") //"'.$payAmt.'",
           // ReferenceInfo.put("TranAmount", fee) //"'.$payAmt.'",
            ReferenceInfo.put("ContactName", driver_name)//"'.$name.'",
            ReferenceInfo.put("ContactNo", driver_mobile_no) //"'.$contact.'",
            ReferenceInfo.put("PayerId", MOBILE_NO) // "'.$login_id.'",
            ReferenceInfo.put("Address", "Chittagong")

            mainObject.put("Authentication", Authentication)
            mainObject.put("ReferenceInfo", ReferenceInfo)
            mainObject.put("CreditInformations", CreditInformations)
        } catch (e: JSONException) {
            Log.e("JSON Error", "PARAM ERROR")
        }
        Log.e("MAIN OBJECT", mainObject.toString())

        val stringRequest3: StringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_SONALI_PAYMENT_REQUEST,
            Response.Listener<String?> { response ->
                // val jsonArray = JSONArray(response)
                Log.e("RESULT", response)
                var formatedStr = response

                try{
                    formatedStr=response?.replace("\\", "")?.replace("\"{", "{")?.replace("}\"", "}")
                    val jsonObject = JSONObject(formatedStr)
                    Log.e("1.Status", jsonObject.getString("status"))
                    Log.e("2.Session Token", jsonObject.getString("session_token"))
                    Log.e("3.Message", jsonObject.getString("message"))
                    if(jsonObject.getString("status") == "200"){
                        dialog.dismiss()
                        val token = jsonObject.getString("session_token").toString()
                        val URL =  "https://spg.com.bd:6313/SpgLanding/SpgLanding/"+token
                     //   startPayment(URL)

                    }else{

                        dialog.dismiss()
                    }

                } catch (e: JSONException) {
                    Log.e("JSON Error", formatedStr)
                    Toast.makeText(this,"Something went Worng",Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }

            }, Response.ErrorListener {
                dialog.dismiss()

            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                headers["Authorization"] = "OAuth 2.0 token here"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return mainObject.toString().toByteArray()
            }
        }
        val queue3 = Volley.newRequestQueue(this@FclTruckEntryDetailsActivity)
        queue3.add(stringRequest3)

    }

    private fun startPayment(){
        RefTranNo = strRequestId+"_"+0
        var PARAM:String?="?P_M=S_P&V_I="+truck_visit+"&R="+RefTranNo+"&R_I="+strRequestId+"&C="+driver_mobile_no

        val intent = Intent(this@FclTruckEntryDetailsActivity, PaymentActivity::class.java)
        intent.putExtra("TITLE", "Online Payment")
        intent.putExtra("USER_TYPE", "GatePass")
//        intent.putExtra("P_M","S_P")
//        intent.putExtra("V_I",truck_visit)
//        intent.putExtra("R",RefTranNo)
//        intent.putExtra("R_I",strRequestId)
//        intent.putExtra("C",driver_mobile_no)
        intent.putExtra("PARAM",PARAM)


        //intent.putExtra("URL", URL)
        startActivity(intent)
    }
}