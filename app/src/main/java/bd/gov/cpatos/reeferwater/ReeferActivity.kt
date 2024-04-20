package bd.gov.cpatos.reeferwater

import android.app.DatePickerDialog

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_reefer.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ReeferActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var actContainer: AutoCompleteTextView? = null
    private var etVslName: TextInputEditText? = null
    private var etRotation:TextInputEditText? = null
    private var etYard:TextInputEditText? = null
    private var etSize:TextInputEditText? = null
    private var etMlo:TextInputEditText? = null
    private var etCon1:TextInputEditText? = null
    private var etDiscon1:TextInputEditText? = null
    private var etCon2:TextInputEditText? = null
    private var etDiscon2: TextInputEditText? =null
    private var etCon3:TextInputEditText? = null
    private var etDiscon3:TextInputEditText? = null
    private var btnSave: Button? = null
    private var swConDisCon1: SwitchMaterial? = null
    private var gKey: String? =null
    private var Container:String? = null
    private var VslName:String? = null
    private var Rotation:String? = null
    private var Size:String? = null
    private var Mlo:String? = null
    private var Yard:String? = null
    private var Con1:String? = null
    private var Discon1:String? = null
    private var Con2:String? = null
    private var Discon2:String? = null
    private var Con3:String? = null
    private var Discon3:String? = null
    private var conButton1: TextInputLayout? =null
    private var disConButton1:TextInputLayout? = null
    private var conButton2:TextInputLayout? = null
    private var disConButton2:TextInputLayout? = null
    private var conButton3:TextInputLayout? = null
    private var disConButton3:TextInputLayout? = null
    private var day = 0
    private var month:Int = 0
    private var year:Int = 0
    private var hour:Int = 0
    private var minute:Int = 0
    private var myday = 0
    private var myMonth:Int = 0
    private var myYear:Int = 0
    private var myHour:Int = 0
    private var myMinute:Int = 0
    private var selecttedBox = 0

    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null

    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null

    private var TITLE:String? =null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reefer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            TITLE = bundle.getString("TITLE") // 1
            supportActionBar?.title = TITLE

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { this.onBackPressed() }
        actContainer = findViewById(R.id.actContainer) 
        etVslName = findViewById(R.id.etVslName)
        etRotation = findViewById(R.id.etRotation)
        etYard = findViewById(R.id.etYard)
        etSize = findViewById(R.id.etSize)
        etMlo = findViewById(R.id.etMlo)
        etCon1 = findViewById(R.id.etCon1)
        etDiscon1 = findViewById(R.id.etDiscon1)
        etCon2 = findViewById(R.id.etCon2)
        etDiscon2 = findViewById(R.id.etDiscon2)
        etCon3 = findViewById(R.id.etCon3)
        etDiscon3 = findViewById(R.id.etDiscon3)
        btnSave = findViewById(R.id.btnSave)
        conButton1 = findViewById(R.id.conButton1) 
        disConButton1 = findViewById(R.id.disConButton1) 
        conButton2 = findViewById(R.id.conButton2) 
        disConButton2 = findViewById(R.id.disConButton2) 
        conButton3 = findViewById(R.id.conButton3) 
        disConButton3 = findViewById(R.id.disConButton3) 
        swConDisCon1 = findViewById(R.id.swConDisCon1)



        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")


        conButton2!!.setEndIconOnClickListener {
            Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog =DatePickerDialog(this@ReeferActivity, this@ReeferActivity, year, month, day)
            datePickerDialog.show()
            selecttedBox = 20
        }
        disConButton2!!.setEndIconOnClickListener {
            Toast.makeText(applicationContext, "disConButton2", Toast.LENGTH_LONG).show()
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog =
                DatePickerDialog(this@ReeferActivity, this@ReeferActivity, year, month, day)
            datePickerDialog.show()
            selecttedBox = 21
        }
        conButton3!!.setEndIconOnClickListener {
            Toast.makeText(applicationContext, "conButton3", Toast.LENGTH_LONG).show()
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog =
                DatePickerDialog(this@ReeferActivity, this@ReeferActivity, year, month, day)
            datePickerDialog.show()
            selecttedBox = 30
        }
        disConButton3!!.setEndIconOnClickListener {
            Toast.makeText(applicationContext, "disConButton3", Toast.LENGTH_LONG).show()
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
            val datePickerDialog =
                DatePickerDialog(this@ReeferActivity, this@ReeferActivity, year, month, day)
            datePickerDialog.show()
            selecttedBox = 31
        }


//        swCC.setChecked(true);


//        swCC.setChecked(true);
        actContainer?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Container = actContainer?.text.toString()
                if (Container!!.length == 11) {
                    setData()
                }
            }
        })


        btnSave?.setOnClickListener {
            Container = actContainer?.text.toString().trim()
            if (Container!!.isEmpty()) {
                Toast.makeText(this@ReeferActivity, "please enter valid data", Toast.LENGTH_SHORT)
                        .show()
            } else {


                getData()
                this.saveData()
            }
        }


        inActivateComponent()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myday = dayOfMonth
        myMonth = month
        val c = Calendar.getInstance()
        hour = c[Calendar.HOUR]
        minute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(this@ReeferActivity,this@ReeferActivity,hour,minute, DateFormat.is24HourFormat(this) )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        val myadatetime = myYear.toString() + "-" + String.format("%02d",myMonth + 1) + "-" + String.format("%02d", myday) + " " + String.format("%02d", myHour) + ":" + String.format("%02d", myMinute) + ":00"
        when (selecttedBox) {
            20 -> etCon2?.setText(myadatetime)
            21 -> etDiscon2?.setText(myadatetime)
            30 -> etCon3?.setText(myadatetime)
            else -> etDiscon3?.setText(myadatetime)
        }
        selecttedBox = 0
    }


    private fun setData() {
        val dialog = ProgressDialog.progressDialog(this@ReeferActivity)

        dialog.show()

        Toast.makeText(this@ReeferActivity, "inside", Toast.LENGTH_SHORT).show()

        //getting the record values
        Container = actContainer?.text.toString()



        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_REF_SELECT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    if (success.equals("1")) {
                        dialog.dismiss()
                        gKey = jsonObject.getString("gkey")
                        Container = jsonObject.getString("cont_no")
                        VslName = jsonObject.getString("name")
                        Rotation = jsonObject.getString("rotation_no")
                        Yard = jsonObject.getString("yard")
                        Size = jsonObject.getString("size")
                        Mlo = jsonObject.getString("mlo")
                        Con1 = jsonObject.getString("con1")
                        Discon1 = jsonObject.getString("discon1")
                        this.Con2 = jsonObject.getString("con2")
                        Discon2 = jsonObject.getString("discon2")
                        Con3 = jsonObject.getString("con3")
                        Discon3 = jsonObject.getString("discon3")

                        etVslName?.setText(VslName)
                        etRotation?.setText(Rotation)
                        etYard?.setText(Yard)
                        etSize?.setText(Size)
                        etMlo?.setText(Mlo)
                        if (Con1.equals("") && Discon1.equals("") && Con2.equals("") && Discon2.equals("") && Con3.equals("") && Discon3.equals("")) {
                            inActivateComponent()
                            swConDisCon1?.isClickable = true
                        } else if (!Con1.equals("") && Discon1.equals("") && Con2.equals("") && Discon2.equals("") && Con3.equals("") && Discon3.equals("")){
                            inActivateComponent()
                            swConDisCon1?.isChecked = true
                            swConDisCon1?.isClickable = true
                        } else if (!Con1.equals("") && !Discon1.equals("") && Con2.equals("") && Discon2.equals("") && Con3.equals("") && Discon3.equals("")) {
                            inActivateComponent()
                            etCon2?.isEnabled = true
                            conButton2!!.isEnabled = true
                        } else if (!Con1.equals("") && !Discon1.equals("") && !Con2.equals("") && Discon2.equals("") && Con3.equals("") && Discon3.equals("")) {
                            inActivateComponent()
                            etDiscon2?.isEnabled = true
                            disConButton2?.isEnabled = true
                        } else if (!Con1.equals("") && !Discon1.equals("" ) && !Con2.equals("") && !Discon2.equals("") && Con3.equals("") && Discon3.equals("")) {
                            inActivateComponent()
                            etCon3?.isEnabled = true
                            conButton3?.isEnabled = true
                        } else if (!Con1.equals("") && !Discon1.equals("") && !Con2.equals("") && !Discon2.equals("") && !Con3.equals("") && Discon3.equals("")) {
                            inActivateComponent()
                            etDiscon3?.isEnabled = true
                            disConButton3!!.isEnabled = true
                        } else {
                            inActivateComponent()
                        }
                        etCon1?.setText(Con1)
                        etDiscon1?.setText(Discon1)
                        etCon2?.setText(Con2)
                        etDiscon2?.setText(Discon2)
                        etCon3?.setText(Con3)
                        etDiscon3?.setText(Discon3)
                    }
                    if (success == "0") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    if (success == "3") {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    Toast.makeText(applicationContext,R.string.toast_login_activity_tryagain,Toast.LENGTH_LONG).show()
                }
            },
                Response.ErrorListener {
                    dialog.dismiss()
                    Toast.makeText(applicationContext,R.string.toast_login_activity_tryagain,Toast.LENGTH_LONG).show()
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["container"] = Container!!
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@ReeferActivity)
        queue?.add(stringRequest)

    }
    private fun inActivateComponent() {
        swConDisCon1?.isChecked = false
        swConDisCon1?.isClickable = false
        etCon2?.isEnabled = false
        conButton2?.isEnabled = false
        etDiscon2?.isEnabled = false
        disConButton2?.isEnabled = false
        etCon3?.isEnabled = false
        conButton3?.isEnabled = false
        etDiscon3?.isEnabled = false
        disConButton3!!.isEnabled = false
    }

    private fun getData() {
        VslName = etVslName?.text.toString().trim()
        Rotation = etRotation?.text.toString().trim()
        Yard = etYard?.text.toString().trim()
        Size = etSize?.text.toString().trim()
        Mlo = etMlo?.text.toString().trim()
        Con1 = etCon1?.text.toString().trim()
        Discon1 = etDiscon1?.text.toString().trim()
        Con2 = etCon2?.text.toString().trim()
        Discon2 = etDiscon2?.text.toString().trim()
        Con3 = etCon3?.text.toString().trim()
        Discon3 = etDiscon3?.text.toString().trim()
    }

    private fun clearData() {
        actContainer?.setText("")
        etVslName?.setText("")
        etRotation?.setText("")
        etYard?.setText("")
        etSize?.setText("")
        etMlo?.setText("")
        etCon1?.setText("")
        etDiscon1?.setText("")
        etCon2?.setText("")
        etDiscon2?.setText("")
        etCon3?.setText("")
        etDiscon3?.setText("")
        inActivateComponent()
    }
    private fun saveData() {
        val dialog = ProgressDialog.progressDialog(this@ReeferActivity)

        dialog.show()
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_REF_SAVE,
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
            },
            Response.ErrorListener { error ->
                dialog.dismiss()
                Toast.makeText(applicationContext, "Faild Error$error", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()

                val statusSwitch1 = if (swConDisCon1?.isChecked == true) {
                    swConDisCon1?.textOn.toString()
                } else {
                    swConDisCon1?.textOff.toString()
                }
                params["gkey"] = gKey!!
                params["cont_id"] = Container!!
                params["rotation_no"] = Rotation!!
                params["name"] = VslName!!
                params["con1switch"] = statusSwitch1
                params["size"] = Size!!
                params["yard"] = Yard!!
                params["mlo"] = Mlo!!
                params["con1"] = Con1!!
                params["discon1"] = Discon1!!
                params["con2"] = Con2!!
                params["discon2"] = Discon2!!
                params["con3"] = Con3!!
                params["discon3"] = Discon3!!
                params["entry_by"] = SignedInLoginId!!
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

}
