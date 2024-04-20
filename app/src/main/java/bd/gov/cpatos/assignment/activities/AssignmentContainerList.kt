package bd.gov.cpatos.assignment.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.R.layout.activity_assignment_container_list
import bd.gov.cpatos.assignment.adapters.AssignmentContainerListAdapter
import bd.gov.cpatos.assignment.models.ContainerForAssignment
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_assignment_container_list.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class AssignmentContainerList : AppCompatActivity() {
    private var ACTIVITY_FOR = ""
    private var adapter: AssignmentContainerListAdapter? = null
    private var etSearch: TextInputEditText? = null
    private var tvtotalContainer: TextView? = null
    private var btnRefresh: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_assignment_container_list)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
            ACTIVITY_FOR = bundle.getString("ACTIVITY_FOR").toString()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        getContainerList()
        etSearch = findViewById(R.id.etSearch)
        tvtotalContainer = findViewById(R.id.tvtotalContainer)
        btnRefresh = findViewById(R.id.btnRefresh)
        tvtotalContainer?.text = "Total Container|0"

        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s)
            }
            override fun afterTextChanged(s: Editable) {}
        })

        btnRefresh?.setOnClickListener({
            getContainerList()
        })

    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun getContainerList() {
        var dialog = ProgressDialog.progressDialog(this@AssignmentContainerList)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(Method.POST,
            EndPoints.URL_GET_ASSIGNMENT_CONTAINER_LIST,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    if (success.equals("1")) {
                        dialog.dismiss()
                        //  val dStations = JSONArray(jsonObject.getString("data"))
                        val dContainers: JSONArray = jsonObject.getJSONArray("data")
                        var sl = 1
                        val contList = arrayListOf<ContainerForAssignment>()
                        tvtotalContainer?.text = "Total Container|"+dContainers.length()
                        for (i in 0 until dContainers.length()) {
                            var dataInner: JSONObject = dContainers.getJSONObject(i)
                            contList.add(
                                ContainerForAssignment(
                                    sl,
                                    dataInner.getString("gkey"),
                                    dataInner.getString("cont_no"),
                                    dataInner.getString("cnf"),
                                    dataInner.getString("cnf_addr"),
                                    dataInner.getString("flex_date01"),
                                    dataInner.getString("bl_nbr"),
                                    dataInner.getString("bizu_gkey"),
                                    dataInner.getString("mfdch_value"),
                                    dataInner.getString("mfdch_desc"),
                                    dataInner.getString("Yard_No"),
                                    dataInner.getString("Bloack_No"),
                                    dataInner.getString("created"),
                                    dataInner.getString("created1"),
                                    dataInner.getString("size"),
                                    dataInner.getString("height"),
                                    dataInner.getString("cvcvd_gkey"),
                                    dataInner.getString("rot_no"),
                                    dataInner.getString("v_name")
                                )
                            )
                            sl++
                        }
                        adapter = AssignmentContainerListAdapter(this, contList, tvtotalContainer!!)
                        containerList.adapter = adapter
                        containerList.setOnItemClickListener { _, _, position, _ ->
                            //  var intent = Intent(this@AssignmentContainerList, VesselDetailsActivity::class.java)
                            // Pass the values to next activity (StationActivity)
//                            intent?.putExtra("TITLE", "Vessle Details")
//                            intent?.putExtra("VVD_GKEY",contList[position].vvdGkey)
//                            intent?.putExtra("ROTATION",contList[position].ibVvg)
//                            intent?.putExtra("ACTIVITY_FOR",ACTIVITY_FOR)
//                            Log.e("ROTATION",contList[position].ibVvg)
                            // startActivity(intent)
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
                params.put("request_for", ACTIVITY_FOR)
                params.put("cont", "")
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@AssignmentContainerList)
        queue?.add(stringRequest)
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +


}