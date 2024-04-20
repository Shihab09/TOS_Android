package bd.gov.cpatos.gatepass

import android.content.Context.*
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.Formatter
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class FclTruckEntryActivity : AppCompatActivity() {
    private var ContNumberArray : ArrayList<String> = arrayListOf<String>()
    private var CityMetroNameArray : ArrayList<String> = arrayListOf<String>()
    private var VehicleCatArray : ArrayList<String> = arrayListOf<String>()
    private var DriverNoArray : ArrayList<String> = arrayListOf<String>()

    private var btnNextFCL: Button? = null
    private var etContainerNo: MaterialAutoCompleteTextView?=null
    private var etCityMetropolitan: MaterialAutoCompleteTextView?=null
    private var etVCategory:MaterialAutoCompleteTextView?=null
    private var etTruckNo:TextInputEditText?=null

    private var etDriverCardNo:MaterialAutoCompleteTextView?=null
    private var etDriverMobileNo: TextInputEditText?=null
    private var etHelperCardNo: TextInputEditText?=null
    private var timeSlotradioGroup: RadioGroup? = null

    private var MOBILE_NO:String? = null
    private var DEVICE_ID:String? = null
    private var SLOT:Int? = 1
    private var TRUCK_ID:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fcl_truck_entry)
        init()
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

        PreloadData()


        btnNextFCL?.setOnClickListener {
            TRUCK_ID = etCityMetropolitan?.text.toString()+" "+etVCategory?.text.toString()+" "+etTruckNo?.text.toString()
            if(etContainerNo?.text.toString() !="" && TRUCK_ID.toString() != "" && etDriverCardNo?.text.toString() !=""){
                DataSave()
            }else{
                Toast.makeText(this," Mandatory Field could not be Empty",Toast.LENGTH_LONG).show()
            }




        }

        etTruckNo?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {
               if(p0?.toString()?.length ==2){
                   etTruckNo?.append("-")
               }
            }
            override fun afterTextChanged(p0: Editable?) {

            }})

        timeSlotradioGroup!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioDriverGroup, i ->
            val id = timeSlotradioGroup!!.checkedRadioButtonId
            when (id) {
                R.id.radioSlot1 ->   SLOT = 1
                R.id.radioSlot2 ->  SLOT = 2
                else ->  SLOT = 3
            }



            // Toast.makeText(this," option "+radio.text+" is selected", Toast.LENGTH_LONG).show()

        })

    }
    private fun init(){
        btnNextFCL= findViewById(R.id.btnNextFCL)
        etContainerNo= findViewById(R.id.etContainerNo)
        etCityMetropolitan= findViewById(R.id.etCityMetropolitan)
        etVCategory= findViewById(R.id.etVCategory)
        etTruckNo= findViewById(R.id.etTruckNo)
        etDriverCardNo= findViewById(R.id.etDriverCardNo)
        etDriverMobileNo= findViewById(R.id.etDriverMobileNo)
        etHelperCardNo= findViewById(R.id.etHelperCardNo)
        timeSlotradioGroup= findViewById(R.id.timeSlotradioGroup)
    }


    private fun nextActivity(){
        val intent = Intent(this@FclTruckEntryActivity, FclTruckEntryDetailsActivity::class.java)
        intent.putExtra("TITLE", "FCL Details View For Payment")
        intent.putExtra("USER_TYPE", "GatePass")
        startActivity(intent)
    }
    private fun PreloadData() {
        var dialog = ProgressDialog.progressDialog(this@FclTruckEntryActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_CNF_TRUCK_ENTRY,
            Response.Listener<String> { response ->
                try {

                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                      val message = jsonObject.getString("message")
                    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val assignment_cont_list: JSONArray = jsonObject.getJSONArray("assignment_cont_list")
                        val truck_district: JSONArray = jsonObject.getJSONArray("truck_district")
                        val truck_reg: JSONArray = jsonObject.getJSONArray("truck_reg")
                        for (i in 0 until assignment_cont_list.length()) {
                            var dataInner: JSONObject = assignment_cont_list.getJSONObject(i)
                            val unit_gkey = dataInner.getString("unit_gkey")
                            val cont_no = dataInner.getString("cont_no")
                            if(cont_no !=""){
                                ContNumberArray.add(cont_no)
                            }
                        }
                        if(ContNumberArray.isNotEmpty()) {
                            val adapter1 = ArrayAdapter(this,android.R.layout.simple_list_item_1,ContNumberArray)
                            etContainerNo?.setAdapter(adapter1)
                        }
                        for (i in 0 until truck_district.length()) {
                            var dataInner: JSONObject = truck_district.getJSONObject(i)
                            val id = dataInner.getString("id")
                            val value = dataInner.getString("value")
                            if(value !=""){
                                CityMetroNameArray.add(value)
                            }
                        }
                        if(CityMetroNameArray.isNotEmpty()) {
                            val adapter2 = ArrayAdapter(this,android.R.layout.simple_list_item_1,CityMetroNameArray)
                            etCityMetropolitan?.setAdapter(adapter2)
                        }

                        for (i in 0 until truck_reg.length()) {
                            var dataInner: JSONObject = truck_reg.getJSONObject(i)
                            val id = dataInner.getString("id")
                            val value = dataInner.getString("value")

                            if(value !=""){
                                VehicleCatArray.add(value)

                            }
                        }
                        if(VehicleCatArray.isNotEmpty()) {
                            val adapter3 = ArrayAdapter(this,android.R.layout.simple_list_item_1,VehicleCatArray)
                            etVCategory?.setAdapter(adapter3)
                        }



                    } else {
                        dialog.dismiss()
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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
                params.put("request_id", "initial_data")
                params.put("mobile_no", MOBILE_NO.toString())
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@FclTruckEntryActivity)
        queue?.add(stringRequest)
    }

    private fun DataSave() {
        var dialog = ProgressDialog.progressDialog(this@FclTruckEntryActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_CNF_TRUCK_ENTRY,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val intent = Intent(this@FclTruckEntryActivity, FclTruckEntryDetailsActivity::class.java)
                        intent.putExtra("TITLE", "FCL Details View For Payment")
                        intent.putExtra("USER_TYPE", "GatePass")
                        intent.putExtra("truck_visit", jsonObject.getString("truck_visit"))
                        intent.putExtra("cont_no", jsonObject.getString("cont_no"))
                        intent.putExtra("truck_no", jsonObject.getString("truck_no"))
                        intent.putExtra("driver_id", jsonObject.getString("driver_id"))
                        intent.putExtra("driver_name", jsonObject.getString("driver_name"))
                        intent.putExtra("driver_mobile_no", jsonObject.getString("driver_mobile_no"))
                        intent.putExtra("helper_id", jsonObject.getString("helper_id"))
                        intent.putExtra("assist_name", jsonObject.getString("assist_name"))
                        intent.putExtra("cf_lic", jsonObject.getString("cf_lic"))
                        intent.putExtra("cf_name", jsonObject.getString("cf_name"))
                        intent.putExtra("assist_name", jsonObject.getString("assist_name"))
                        intent.putExtra("fee", jsonObject.getString("fee"))
                        intent.putExtra("visit_time_slot_start", jsonObject.getString("visit_time_slot_start"))
                        intent.putExtra("visit_time_slot_end", jsonObject.getString("visit_time_slot_end"))
                       // intent.putExtra("strRequestId", jsonObject.getString("strRequestId"))
                        intent.putExtra("assignmentType", jsonObject.getString("assignmentType"))
                        intent.putExtra("MOBILE_NO", MOBILE_NO)
                        intent.putExtra("DEVICE_ID",DEVICE_ID)


                        startActivity(intent)




                    } else {
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    // Toast.makeText(applicationContext, "Try Again", Toast.LENGTH_LONG).show()
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
                val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
                //textView.text = "Your Device IP Address: $ipAddress"
                val params = HashMap<String, String>()
                params.put("request_id", "feedback")
                params.put("cont_no", etContainerNo?.text.toString())
                params.put("truck_no", TRUCK_ID.toString())
                params.put("driver_id", etDriverCardNo?.text.toString())
                params.put("driver_mobile_no", etDriverMobileNo?.text.toString())
                params.put("helper_id", etHelperCardNo?.text.toString())
                params.put("login_id", MOBILE_NO.toString())
                params.put("ipaddr", ipAddress)
                params.put("slot", SLOT.toString())
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@FclTruckEntryActivity)
        queue?.add(stringRequest)
    }

}