package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.adapters.VesselListAdapter
import bd.gov.cpatos.pilot.models.Vessel
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_gate_in.*
import kotlinx.android.synthetic.main.activity_vessel_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class SearchActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private var USER =""
    private var searchVessel: SearchView? = null
    private var mContext: Context? = null
    private var adapterw: ArrayAdapter<Vessel>? =null
    lateinit var searchActivity: SearchActivity
    companion object {
        const val START_ACTIVITY_VESSELEDETAILS_REQUEST_CODE = 909
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vessel_list)
        mContext = this
        init()
        getWaterVessel()
    }
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        searchVessel = findViewById(R.id.searchVessel)

        searchActivity =this
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            USER = bundle.getString("user_id").toString()
            if(USER=="JALPARI"){
                llBtnHideShow.visibility = View.VISIBLE
            }else{
                llBtnHideShow.visibility = View.INVISIBLE
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }


    private fun getWaterVessel() {
        var dialog = ProgressDialog.progressDialog(this@SearchActivity)
        dialog.show()
        val vList = arrayListOf<Vessel>()
        //creating volley string request
        val stringRequest = object : StringRequest(Method.POST,
            EndPoints.URL_GET_WATER_VESSEL,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val dStations = JSONArray(jsonObject.getString("data"))
                        // val dStations: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until dStations.length()) {
                            var dataInner: JSONObject = dStations.getJSONObject(i)
                            //   Log.d(">>>>>>", dataInner.getString("name"))
                            vList.add(Vessel(
                                dataInner.getString("rotation_no"),
                                dataInner.getString("vessel_name"),
                                dataInner.getString("supply_type"),
                                dataInner.getString("berth"),
                                dataInner.getString("delivery_area"),
                                dataInner.getString("burge_name"),
                                dataInner.getString("demand_qty"),
                                dataInner.getString("demand_date"),
                                dataInner.getString("demand_by")
                            )
                            )
                        }
                        searchVessel?.setOnQueryTextListener(this)
                        adapterw = ArrayAdapter(this, android.R.layout.simple_list_item_1, vList)
                        vesselList.adapter = adapterw

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
                params.put("user_id", "JALPARI")
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@SearchActivity)
        queue?.add(stringRequest)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val text = newText!!
        System.out.println("TEXT"+text)
        adapterw?.filter!!.filter(newText.toString())
        return false
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_VESSELEDETAILS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val message = data!!.getStringExtra("message")
                //    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

}




