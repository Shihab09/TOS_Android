package bd.gov.cpatos.gatepass

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.pilot.adapters.TruckVisitListAdapter
import bd.gov.cpatos.pilot.models.TruckVisit
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_gate_pass_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GatePassListActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private var USER_TYPE = ""
    private var USER_ID = ""
    private var searchTruckVisit: SearchView? = null
    private var mContext: Context? = null
    private var adapter: TruckVisitListAdapter? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_pass_list)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        searchTruckVisit = findViewById(R.id.searchTruckVisit)
        mContext = this
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            USER_TYPE = bundle.getString("USER_TYPE")!!
            USER_ID = bundle.getString("user_id")!!

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        getGatePassList()
//        btnAddNewVessel?.setOnClickListener{
//            val intent = Intent(this@GatePassListActivity, AddVesselActivity::class.java)
//            intent.putExtra("TITLE", "Add New Vessel")
//            ContextCompat.startActivity(this, intent, Bundle())
//        }
    }


    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun getGatePassList() {
        var dialog = ProgressDialog.progressDialog(this@GatePassListActivity)
        dialog.show()
        val vList = arrayListOf<TruckVisit>()
        //creating volley string request

        Log.d(">>>>>>",EndPoints.URL_GET_TRUCK_VISIT_LIST)

        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_TRUCK_VISIT_LIST,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    if (success.equals("1")) {
                        dialog.dismiss()
                        //  val dStations = JSONArray(jsonObject.getString("data"))
                        val dStations: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until dStations.length()) {
                            var dataInner: JSONObject = dStations.getJSONObject(i)
                            Log.d(">>>>>>", dataInner.getString("visit_id"))
                            vList.add(
                                TruckVisit(
                                    dataInner.getString("visit_id"),
                                    dataInner.getString("import_rotation"),
                                    dataInner.getString("cont_no"),
                                    dataInner.getString("truck_id"),
                                    dataInner.getString("driver_name"),
                                    dataInner.getString("driver_gate_pass"),
                                    dataInner.getString("assistant_name"),
                                    dataInner.getString("assistant_gate_pass"),
                                    dataInner.getString("visit_time_slot_start"),
                                    dataInner.getString("visit_time_slot_end"))
                            )
                        }
                        adapter = TruckVisitListAdapter(this, vList)
                        truckVisitList.adapter = adapter
                        searchTruckVisit?.setOnQueryTextListener(this)
//                        vesselList.setOnItemClickListener { _, _, position, _ ->
//                            val intent = Intent(this@GatePassListActivity, VesselDetailsActivity::class.java)
//                            // Pass the values to next activity (StationActivity)
//                            intent.putExtra("TITLE", "Vessle Details")
//                            intent.putExtra("VVD_GKEY", vList[position].vvdGkey)
//                            intent.putExtra("ROTATION", vList[position].ibVvg)
//                            intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
//                            Log.e("ROTATION", vList[position].ibVvg)
//                            startActivity(intent)
//                        }
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
                params.put("user_id", USER_ID)
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@GatePassListActivity)
        queue?.add(stringRequest)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val text = newText!!
        System.out.println("TEXT"+text)
        adapter?.filter!!.filter(newText.toString())
        return false
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +

}