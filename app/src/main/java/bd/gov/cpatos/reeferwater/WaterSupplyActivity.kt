package bd.gov.cpatos.reeferwater


import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints.URL_WATER_INSERT
import bd.gov.cpatos.utils.EndPoints.URL_WATER_SELECT
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.util.*

class WaterSupplyActivity : AppCompatActivity() {
    private var etRotation: TextInputEditText? = null; private  var etVslName:TextInputEditText? = null; private  var etBerthName:TextInputEditText? = null; private  var etAta:TextInputEditText? = null; private  var etAtd:TextInputEditText? = null; private  var etQtyUnit:TextInputEditText? = null; private  var etQty:TextInputEditText? = null
    private var btnSearch: Button? = null; private  var btnSave:Button? = null
    private var vvdgKye: String? = null; private  var applied_to_natural_key:kotlin.String? = null; private  var Rotation:kotlin.String? = null; private  var VslName:kotlin.String? = null; private  var BerthName:kotlin.String? = null; private  var Ata:kotlin.String? = null; private  var Atd:kotlin.String? = null; private  var QtyUnit:kotlin.String? = null; private  var Qty:kotlin.String? = null
    private var radioGroupWaterBy: RadioGroup? = null
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private var EVENT_TYPE_GKEY: String? = null  // 169= Water by Line,168=Water By Boat
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private var TITLE:String? =null
    private var USER_TYPE:String? =null
    private var radioWaterByLine:RadioButton? =null
    private var radioWaterByBoat:RadioButton? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_supply)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            TITLE = bundle.getString("TITLE") // 1
            USER_TYPE = bundle.getString("USER_TYPE") // 1

            supportActionBar?.title = TITLE
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { this.onBackPressed() }

        etRotation = findViewById(R.id.etRotation)
        radioGroupWaterBy = findViewById(R.id.radioGroupWaterBy)
        radioWaterByLine  = findViewById(R.id.radioWaterByLine)
        radioWaterByBoat  = findViewById(R.id.radioWaterByBoat)

        etVslName = findViewById(R.id.etVslName)
        etBerthName = findViewById(R.id.etBerthName) 
        etBerthName = findViewById(R.id.etBerthName) 
        etAta = findViewById(R.id.etAta) 
        etAtd = findViewById(R.id.etAtd) 
        etQtyUnit = findViewById(R.id.etQtyUnit) 
        etQty = findViewById(R.id.etQty) 
        btnSearch = findViewById(R.id.btnSearch) 
        btnSave = findViewById(R.id.btnSave)

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")

         if(USER_TYPE=="Marin"){
            radioWaterByLine?.isChecked = false
            radioWaterByBoat?.isChecked = true
            radioWaterByLine?.isClickable = false
            EVENT_TYPE_GKEY = "168"
        }else{
            radioWaterByLine?.isChecked = true
            radioWaterByBoat?.isChecked = false
            radioWaterByBoat?.isClickable = false
            EVENT_TYPE_GKEY = "169"
        }
        radioGroupWaterBy?.isClickable = false


        btnSearch?.setOnClickListener(View.OnClickListener {
            Rotation = etRotation?.text.toString().trim()
            if (Rotation!!.isEmpty()) {
                Toast.makeText(this@WaterSupplyActivity, "please enter valid data", Toast.LENGTH_SHORT).show()
            } else {
                setData()
            }
        })
        radioGroupWaterBy!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioDriverGroup, i ->
            //  textView.text = "option "+i+" is selected"
            //var selectedId:Int = radioGroupWaterBy!!.getCheckedRadioButtonId();
            val radio: RadioButton = findViewById(i)

            if(radio.text == "Water By Line") EVENT_TYPE_GKEY = "169" else EVENT_TYPE_GKEY = "168"


            // Toast.makeText(this," option "+radio.text+" is selected", Toast.LENGTH_LONG).show()

        })

        btnSave?.setOnClickListener(View.OnClickListener {
            Rotation = etRotation?.text.toString().trim()
            if (Rotation!!.isEmpty()) {
                Toast.makeText(this@WaterSupplyActivity, "please enter valid data", Toast.LENGTH_SHORT).show()
            } else {


                //setData();
                getData()
                SaveData()
            }
        })
    }
    private fun setData() {
        val dialog = bd.gov.cpatos.utils.ProgressDialog.progressDialog(this@WaterSupplyActivity)
        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(Method.POST, URL_WATER_SELECT,
                Response.Listener { response ->
                    Log.e("anyText", response)
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        val message = jsonObject.getString("message")
                        if (success == "1") {
                            Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                            vvdgKye = jsonObject.getString("vvd_gkey")
                            applied_to_natural_key = jsonObject.getString("applied_to_natural_key")
                            VslName = jsonObject.getString("vsl_name")
                            BerthName = jsonObject.getString("berth")
                            Ata = jsonObject.getString("ata")
                            Atd = jsonObject.getString("atd")
                            QtyUnit = jsonObject.getString("quantity_unit")
                            //etRotation?.setText(Rotation);
                            etVslName?.setText(VslName)
                            etBerthName?.setText(BerthName)
                            etAta?.setText(Ata)
                            etAtd?.setText(Atd)
                            etQtyUnit?.setText(QtyUnit)
                            etQty?.setText("")
                        }
                        if (success == "0") {
                           // Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                        if (success == "3") {
                           // Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Faild$e", Toast.LENGTH_LONG).show()
                    }
                }, Response.ErrorListener { error ->
            dialog.dismiss()
            Toast.makeText(applicationContext, "Faild Error$error", Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                Rotation = etRotation?.text.toString().trim()
                params["rotation"] = Rotation!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun getData() {
        Rotation = etRotation?.text.toString().trim()
        VslName = etVslName?.text.toString().trim()
        BerthName = etBerthName?.text.toString().trim()
        Ata = etAta?.text.toString().trim()
        Atd = etAtd?.text.toString().trim()
        QtyUnit = etQtyUnit?.text.toString().trim()
        Qty = etQty?.text.toString().trim()
    }

    private fun clearData() {
        etRotation?.setText("")
        etVslName?.setText("")
        etBerthName?.setText("")
        etAta?.setText("")
        etAtd?.setText("")
        etQtyUnit?.setText("")
        etQty?.setText("")
    }

    private fun SaveData() {
        val dialog = bd.gov.cpatos.utils.ProgressDialog.progressDialog(this@WaterSupplyActivity)
        dialog.show()

        val stringRequest: StringRequest = object : StringRequest(Method.POST, URL_WATER_INSERT,
                Response.Listener { response ->
                    Log.e("anyText", response)
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        val message = jsonObject.getString("message")
                        if (success == "1") {
                            clearData()
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
                }, Response.ErrorListener { error ->
            dialog.dismiss()
            Toast.makeText(applicationContext, "Faild Error$error", Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["vvdgkey"] = vvdgKye!!
                params["applied_to_natural_key"] = applied_to_natural_key!!
                params["quantity"] = Qty!!
                params["unit"] = QtyUnit!!
                params["EVENT_TYPE_GKEY"] = EVENT_TYPE_GKEY!!
                params["user"] = SignedInLoginId!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    override fun onBackPressed() {


        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(R.string.app_name)
            .setMessage("Do you want to Logout?")
            .setPositiveButton("Yes",{ dialog, which ->

                preferencesEditor?.putString("issignedin", "false")
                preferencesEditor?.apply()

                finish()

            }).setNegativeButton("No", null).show()



    }

}