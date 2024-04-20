package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PilotOnBoardToOffBoardActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var isSignedIn:String? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    companion object {
        const val START_ACTIVITY_ADDADDITIONALINFORMATION_REQUEST_CODE = 703
    }
    private var VVD_GKEY = ""
    private var ROTATION = ""
    private var ACTIVITY_FOR = ""
    private var SELECTED_BOX = 0
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
    private var linLayoutShiftFrom: LinearLayout? =null //
    private var linLayoutOnboard: LinearLayout? =null
    private var linLayoutLastLine: LinearLayout? =null
    private var linLayoutFirstLine: LinearLayout? =null
    private var linLayoutOffBoard: LinearLayout? =null
    private var linLayoutNextButton: LinearLayout? =null

//    private var btnOnBoardDate: TextInputLayout? =null
//    private var btnFirstLineDate: TextInputLayout? = null
//    private var btnLastLineDate: TextInputLayout? = null
//    private var btnOffBoardDate: TextInputLayout? = null
//    private var btnOnBoardTime: TextInputLayout? =null
//    private var btnFirstLineTime: TextInputLayout? = null
//    private var btnLastLineTime: TextInputLayout? = null
//    private var btnOffBoardTime: TextInputLayout? = null


    private var etShiftingFrom: TextInputEditText? =null
    private var swOnBoard: SwitchMaterial? = null
    private var etOnBoardDate: TextInputEditText? =null
    private var etOnBoardTime: TextInputEditText? =null
    private var cbxOnBoard: CheckBox? =null
    private var btnUpdateOnBoard: Button? =null

    private var swLastLine: SwitchMaterial? = null
    private var etLastLineDate: TextInputEditText? =null
    private var etLastLineTime: TextInputEditText? =null
    private var cbxLastLine: CheckBox? =null
    private var btnUpdateLastLine: Button? =null

    private var Berth : ArrayList<String> = arrayListOf<String>()
    private var actvBerth: AutoCompleteTextView? = null
    private var swFirstLine: SwitchMaterial? = null
    private var etFirstLineDate: TextInputEditText? =null
    private var etFirstLineTime: TextInputEditText? =null
    private var cbxFirstLine: CheckBox? =null
    private var btnUpdateFirstLine: Button? =null



    private var swOffBoard: SwitchMaterial? = null
    private var etOffBordDate: TextInputEditText? =null
    private var etOffBordTime: TextInputEditText? =null
    private var cbxOffBoard: CheckBox? =null
    private var btnUpdateOffboard: Button? =null
    private var btnNext: Button? =null




//    private var etEditionalTug: TextInputEditText? =null
//    private var etAdditionalPilot: TextInputEditText? =null
//    private var etRemarks: TextInputEditText? =null
//    private var etBerth: TextInputEditText? =null
//    private var imgPilot: ImageView? = null
//    private var btnTakePhoto: Button? =null
//    private var btnSave: Button? =null
//    private var btnReport: Button? =null
//    private var btnMailSend: Button? =null

    private val calander: Calendar = Calendar.getInstance()
    private val date_format = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private var checkSw1:Int = 0
    private var checkSw2:Int = 0
    private var checkSw3:Int = 0
    private var checkSw4:Int = 0

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var bitmap: Bitmap? = null
    var mContext: Context?=null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilot_on_board_to_off_board)
        mContext=this
        init()
        findViewById()
        getBerth()
        inActivateLayout()
        Log.e("ONC", "inActivateLayout")
        inActivateButton()
        getVesselEvent()

        dateTimeFieldIconButtonAction()
        switchAndCheckBoxAction()
        addNexButtonAction()



    }
    private fun init(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = this.title
            VVD_GKEY = bundle.getString("VVD_GKEY")!!
            ROTATION = bundle.getString("ROTATION")!!
            ACTIVITY_FOR = bundle.getString("ACTIVITY_FOR")!!
            Log.e("ROTATION-2", ROTATION)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        isSignedIn = mPreferences?.getString("issignedin", "false")
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")

    }
    private  fun findViewById(){
        linLayoutShiftFrom = findViewById(R.id.liLay01New)
        linLayoutOnboard = findViewById(R.id.liLay01)
        linLayoutLastLine = findViewById(R.id.liLay02)
        linLayoutFirstLine = findViewById(R.id.liLay03)
        linLayoutOffBoard = findViewById(R.id.liLay04)
        linLayoutNextButton = findViewById(R.id.liLay05)

//        btnOnBoardDate = findViewById(R.id.btnOnBoardDate)
//        btnOnBoardTime = findViewById(R.id.btnOnBoardTime)
//
//        btnFirstLineDate = findViewById(R.id.btnFirstLineDate)
//        btnFirstLineTime = findViewById(R.id.btnFirstLineTime)
//
//        btnLastLineDate = findViewById(R.id.btnLastLineDate)
//        btnLastLineTime = findViewById(R.id.btnLastLineTime)
//
//        btnOffBoardDate = findViewById(R.id.btnOffBoardDate)
//        btnOffBoardTime = findViewById(R.id.btnOffBoardTime)




        etShiftingFrom= findViewById(R.id.etShiftingFrom)
        swOnBoard = findViewById(R.id.swOnBoard)
        etOnBoardDate = findViewById(R.id.etOnBoardDate)
        etOnBoardTime = findViewById(R.id.etOnBoardTime)
        cbxOnBoard = findViewById(R.id.cbxOnBoard)
        btnUpdateOnBoard = findViewById(R.id.btnUpdateOnBoard)

        swLastLine = findViewById(R.id.swLasttLine)
        etLastLineDate = findViewById(R.id.etLastLineDate)
        etLastLineTime = findViewById(R.id.etLastLineTime)
        cbxLastLine = findViewById(R.id.cbxLastLine)
        btnUpdateLastLine = findViewById(R.id.btnUpdateLastLine)

        actvBerth = findViewById(R.id.actvBerth)
        swFirstLine = findViewById(R.id.swFirstLine)
        etFirstLineDate = findViewById(R.id.etFirstLineDate)
        etFirstLineTime = findViewById(R.id.etFirstLineTime)
        cbxFirstLine = findViewById(R.id.cbxFirstLine)
        btnUpdateFirstLine = findViewById(R.id.btnUpdateFirstLine)

        swOffBoard = findViewById(R.id.swOffBoard)
        etOffBordDate = findViewById(R.id.etOffBordDate)
        etOffBordTime = findViewById(R.id.etOffBordTime)
        cbxOffBoard = findViewById(R.id.cbxOffBoard)
        btnUpdateOffboard = findViewById(R.id.btnUpdateOffboard)
        btnNext = findViewById(R.id.btnNext)


        if(ACTIVITY_FOR =="outgoing"){
            swLastLine?.text = "CAST OFF"
        }else{
            swLastLine?.text = "LAST LINE"
        }



//        etEditionalTug = findViewById(R.id.etEditionalTug)
//        etAdditionalPilot = findViewById(R.id.etAdditionalPilot)
//        etRemarks = findViewById(R.id.etRemarks)
//        etBerth = findViewById(R.id.etBerth)
//        imgPilot = findViewById(R.id.imgPilot)
//        btnTakePhoto = findViewById(R.id.btnTakePhoto)
//        btnSave = findViewById(R.id.btnSave)
//        btnReport = findViewById(R.id.btnReport)
//        btnMailSend = findViewById(R.id.btnMailSend)

//        etOnBoardDate?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(1,etOnBoardDate,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etOnBoardTime?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(2,etOnBoardTime,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etFirstLineDate?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(1,etFirstLineDate,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etFirstLineTime?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(2,etFirstLineTime,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etLastLineDate?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(1,etLastLineDate,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etLastLineTime?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(2,etLastLineTime,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etOffBordDate?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(1,etLastLineDate,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})
//        etOffBordTime?.addTextChangedListener(object: TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) {}
//            override fun onTextChanged(p0: CharSequence?,p1: Int, p2: Int, p3: Int) { formatAndValidateDateAndTime(2,etOffBordTime,p0,p1, p2, p3) }
//            override fun afterTextChanged(p0: Editable?) {}})

    }

//    private fun formatAndValidateDateAndTime(dateOrtime:Int?,textField:TextInputEditText?,p0: CharSequence?,p1: Int, p2: Int, p3: Int){
//
//        if(dateOrtime==1){
//
//            if(p0?.toString()?.length ==4 && p0?.toString() !="-"){
//                textField?.append("-")
//            }else if(p0?.toString()?.length ==7 && p0?.toString() !="-") {
//                textField?.append("-")
//            }else if(p0?.toString()?.length!! > 10){
//                Toast.makeText(mContext,"Invalid Date Format",Toast.LENGTH_LONG).show()
//          //      textField?.append("")
//            }
//        }else if(dateOrtime==2){
//            if(p0?.toString()?.length ==2 && p0?.toString() !=":"){
//                textField?.append(":")
//            }else if(p0?.toString()?.length!! > 5){
//                Toast.makeText(mContext,"Invalid Time Format",Toast.LENGTH_LONG).show()
//             //   textField?.remove(p0[8])
//            }
//
//
//        }else{
//
//        }
//
//    }


    private fun inActivateLayout() {
        if(ACTIVITY_FOR == "incoming") {
            linLayoutShiftFrom?.visibility = View.GONE
            linLayoutOnboard?.visibility = View.VISIBLE
            linLayoutFirstLine?.visibility = View.VISIBLE
            linLayoutLastLine?.visibility = View.GONE
            linLayoutOffBoard?.visibility = View.VISIBLE
            linLayoutNextButton?.visibility = View.VISIBLE
        }else if(ACTIVITY_FOR == "shifting"){
            linLayoutShiftFrom?.visibility = View.VISIBLE
            linLayoutOnboard?.visibility = View.VISIBLE
            linLayoutLastLine?.visibility = View.VISIBLE
            linLayoutFirstLine?.visibility = View.VISIBLE
            linLayoutOffBoard?.visibility = View.VISIBLE
            linLayoutNextButton?.visibility = View.VISIBLE
        }else if(ACTIVITY_FOR == "outgoing"){
            linLayoutShiftFrom?.visibility = View.GONE
            linLayoutOnboard?.visibility = View.VISIBLE
            linLayoutLastLine?.visibility = View.VISIBLE
            linLayoutFirstLine?.visibility = View.GONE
            linLayoutOffBoard?.visibility = View.VISIBLE
            linLayoutNextButton?.visibility = View.VISIBLE
        }else{
            linLayoutShiftFrom?.visibility = View.GONE
            linLayoutOnboard?.visibility = View.GONE
            linLayoutLastLine?.visibility = View.GONE
            linLayoutFirstLine?.visibility = View.GONE
            linLayoutOffBoard?.visibility = View.GONE
            linLayoutNextButton?.visibility = View.GONE
        }
    }
    private fun inActivateButton() {
//        btnOnBoardDate?.isEnabled = false
//        btnLastLineDate?.isEnabled = false
//        btnFirstLineDate?.isEnabled = false
//        btnOffBoardDate?.isEnabled = false
        btnUpdateOnBoard?.visibility = View.GONE
        btnUpdateLastLine?.visibility = View.GONE
        btnUpdateFirstLine?.visibility = View.GONE
        btnUpdateOffboard?.visibility = View.GONE
    }

    private fun dateTimeFieldIconButtonAction(){
//        btnOnBoardDate!!.setEndIconOnClickListener {
//            Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
//            val calendar = Calendar.getInstance()
//            year = calendar[Calendar.YEAR]
//            month = calendar[Calendar.MONTH]
//            day = calendar[Calendar.DAY_OF_MONTH]
//            val datePickerDialog = DatePickerDialog(
//                this@PilotOnBoardToOffBoardActivity,
//                this@PilotOnBoardToOffBoardActivity,
//                year,
//                month,
//                day
//            )
//            datePickerDialog.show()
//            SELECTED_BOX = 1
//        }
//        btnLastLineDate!!.setEndIconOnClickListener {
//            Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
//            val calendar = Calendar.getInstance()
//            year = calendar[Calendar.YEAR]
//            month = calendar[Calendar.MONTH]
//            day = calendar[Calendar.DAY_OF_MONTH]
//            val datePickerDialog = DatePickerDialog(
//                this@PilotOnBoardToOffBoardActivity,
//                this@PilotOnBoardToOffBoardActivity,
//                year,
//                month,
//                day
//            )
//            datePickerDialog.show()
//            SELECTED_BOX = 2
//        }
//        btnFirstLineDate!!.setEndIconOnClickListener {
//            Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
//            val calendar = Calendar.getInstance()
//            year = calendar[Calendar.YEAR]
//            month = calendar[Calendar.MONTH]
//            day = calendar[Calendar.DAY_OF_MONTH]
//            val datePickerDialog = DatePickerDialog(
//                this@PilotOnBoardToOffBoardActivity,
//                this@PilotOnBoardToOffBoardActivity,
//                year,
//                month,
//                day
//            )
//            datePickerDialog.show()
//            SELECTED_BOX = 3
//        }
//        btnOffBoardDate!!.setEndIconOnClickListener {
//            Toast.makeText(applicationContext, "conButton2", Toast.LENGTH_LONG).show()
//            val calendar = Calendar.getInstance()
//            year = calendar[Calendar.YEAR]
//            month = calendar[Calendar.MONTH]
//            day = calendar[Calendar.DAY_OF_MONTH]
//            val datePickerDialog = DatePickerDialog(
//                this@PilotOnBoardToOffBoardActivity,
//                this@PilotOnBoardToOffBoardActivity,
//                year,
//                month,
//                day
//            )
//            datePickerDialog.show()
//            SELECTED_BOX = 4
//        }
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myday = dayOfMonth
        myMonth = month
        val c = Calendar.getInstance()
        hour = c[Calendar.HOUR]
        minute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this@PilotOnBoardToOffBoardActivity,
            this@PilotOnBoardToOffBoardActivity,
            hour,
            minute,
            DateFormat.is24HourFormat(
                this
            )
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        myHour = hourOfDay
//        myMinute = minute
//        val myadatetime = myYear.toString() + "-" + String.format("%02d", myMonth + 1) + "-" + String.format(
//            "%02d",
//            myday) + " " + String.format("%02d", myHour) + ":" + String.format("%02d", myMinute) + ":00"
//        when (SELECTED_BOX) {
//            1 -> etOnBoard?.setText(myadatetime)
//            2 -> etLastLine?.setText(myadatetime)
//            3 -> etFirstLine?.setText(myadatetime)
//            else -> etOffBord?.setText(myadatetime)
//        }
//        SELECTED_BOX=0
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun  switchAndCheckBoxAction(){
        checkSw1 = 1
        checkSw2 = 1
        checkSw3 = 1
        checkSw4 = 1

        swOnBoard?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val date_time: String = date_format.format(calander.time)
                if(checkSw1 == 1)
                    vesselEventAction(1, date_time)
            } else {
                if(etOnBoardDate?.text.toString() != "")
                    swOnBoard?.isChecked = true
            }
        }
        swLastLine?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val date_time: String = date_format.format(calander.time)
                if(checkSw2 == 1)
                    vesselEventAction(2, date_time)
            }else{
                if (etLastLineDate?.text.toString() != "") swLastLine?.isChecked = true
            }

        }
        swFirstLine?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val date_time: String = date_format.format(calander.time)
                if(checkSw3 == 1)
                    vesselEventAction(3, date_time)

            } else {
                if (etFirstLineDate?.text.toString() != "") swFirstLine?.isChecked = true
            }

        }
        swOffBoard?.setOnCheckedChangeListener { _, isChecked ->
            val date_time: String = date_format.format(calander.time)
            if (isChecked) {
                if(checkSw4==1)
                    vesselEventAction(4, date_time)
            }else {
                if(etOffBordDate?.text.toString() != "")
                    swOffBoard?.isChecked = true
            }
        }
        cbxOnBoard?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && etOnBoardDate?.text.toString() != ""){
                btnUpdateOnBoard?.visibility = View.VISIBLE
             //   btnOnBoardDate?.isEnabled = true
                  etOnBoardDate?.isEnabled = true
                  etOnBoardTime?.isEnabled = true
            }else{
                btnUpdateOnBoard?.visibility = View.GONE
                cbxOnBoard?.isChecked =false
               // btnOnBoardDate?.isEnabled = false
                etOnBoardDate?.isEnabled = false
                etOnBoardTime?.isEnabled = false
            }
        }
        cbxLastLine?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && etLastLineDate?.text.toString() != "") {
                btnUpdateLastLine?.visibility = View.VISIBLE
              //  btnLastLineDate?.isEnabled = true
                etLastLineDate?.isEnabled = true
                etLastLineTime?.isEnabled = true
            }else{
                btnUpdateLastLine?.visibility = View.GONE
                cbxLastLine?.isChecked =false
                //btnLastLineDate?.isEnabled = false
                etLastLineDate?.isEnabled = false
                etLastLineTime?.isEnabled = false
            }
        }
        cbxFirstLine?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && etFirstLineDate?.text.toString() != "") {
                btnUpdateFirstLine?.visibility = View.VISIBLE
             //   btnFirstLineDate?.isEnabled = true
                etFirstLineDate?.isEnabled = true
                etFirstLineTime?.isEnabled = true
            }else{
                btnUpdateFirstLine?.visibility = View.GONE
                cbxFirstLine?.isChecked =false
              //  btnFirstLineDate?.isEnabled = false
                etFirstLineDate?.isEnabled = false
                etFirstLineTime?.isEnabled = false
            }
        }
        cbxOffBoard?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && etOffBordDate?.text.toString() != "") {
                btnUpdateOffboard?.visibility = View.VISIBLE
            //    btnOffBoardDate?.isEnabled = true
                etOffBordDate?.isEnabled = true
                etOffBordTime?.isEnabled = true
            }else{

                btnUpdateOffboard?.visibility = View.GONE
                cbxOffBoard?.isChecked =false
              //  btnOffBoardDate?.isEnabled = false
                etOffBordDate?.isEnabled = false
                etOffBordTime?.isEnabled = false

            }

        }

        btnUpdateOnBoard?.setOnClickListener{
            val date_time = etOnBoardDate?.text.toString().trim()+" "+ etOnBoardTime?.text.toString().trim()
            if(date_time !="") {
                vesselEventAction(1, date_time)
                btnUpdateOnBoard?.visibility = View.GONE
                cbxOnBoard?.isChecked = false
               // btnOnBoardDate?.isEnabled = false
                etOffBordDate?.isEnabled = false
                etOffBordTime?.isEnabled = false
            }
        }
        btnUpdateLastLine?.setOnClickListener{
            val date_time = etLastLineDate?.text.toString().trim()+" "+ etLastLineTime?.text.toString().trim()
            if(date_time !="") {
                vesselEventAction(2, date_time)
                btnUpdateLastLine?.visibility = View.GONE
                cbxLastLine?.isChecked = false
              //  btnLastLineDate?.isEnabled = false
                etLastLineDate?.isEnabled = false
                etLastLineTime?.isEnabled = false
            }
        }
        btnUpdateFirstLine?.setOnClickListener{
            val date_time = etFirstLineDate?.text.toString().trim()+" "+ etFirstLineTime?.text.toString().trim()
            if(date_time !="") {
                vesselEventAction(3, date_time)
                btnUpdateFirstLine?.visibility = View.GONE
                cbxFirstLine?.isChecked = false
               // btnFirstLineDate?.isEnabled = false
                etFirstLineDate?.isEnabled = false
                etFirstLineTime?.isEnabled = false

            }
        }
        btnUpdateOffboard?.setOnClickListener{
            val date_time = etOffBordDate?.text.toString().trim()+" "+ etOffBordTime?.text.toString().trim()
            if(date_time !="") {
                vesselEventAction(4, date_time)
                btnUpdateOffboard?.visibility = View.GONE
                cbxOffBoard?.isChecked = false
               // btnOffBoardDate?.isEnabled = false
                etOffBordDate?.isEnabled = false
                etOffBordTime?.isEnabled = false
            }
        }
    }
    private fun addNexButtonAction(){
        btnNext!!.setOnClickListener {
            var intent = Intent(
                this@PilotOnBoardToOffBoardActivity,
                AddAdditionalInformationActivity::class.java
            )
            if(ACTIVITY_FOR == "incoming")
                intent.putExtra("TITLE", "Incoming Vessel:(" + ROTATION + ")")
            else if(ACTIVITY_FOR == "shifting")
                intent.putExtra("TITLE", "Shifting Vessel:(" + ROTATION + ")")
            else
                intent.putExtra("TITLE", "Outgoing Vessel:(" + ROTATION + ")")
            // Pass the values to next activity (StationActivity)
            intent.putExtra("VVD_GKEY", VVD_GKEY)
            intent.putExtra("ROTATION", ROTATION)
            intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
            startActivityForResult(intent, START_ACTIVITY_ADDADDITIONALINFORMATION_REQUEST_CODE)
        }
    }
    private fun getBerth() {
        var dialog = ProgressDialog.progressDialog(this@PilotOnBoardToOffBoardActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_BERTH_LIST,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")
                        val berthFrom = jsonObject.getString("berthFrom")
                        etShiftingFrom?.setText(berthFrom)

                        for (i in 0 until data.length()) {
                            var dataInner: JSONObject = data.getJSONObject(i)
                            val gkey = dataInner.getString("gkey")
                            val berth_name = dataInner.getString("berth_name")
                            if (berth_name != "") {
                                Berth.add(berth_name)
                            }
                        }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Berth)
                        actvBerth?.setAdapter(adapter)
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
                val params = HashMap<String, String>()
                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@PilotOnBoardToOffBoardActivity)
        queue?.add(stringRequest)
    }
    private fun getVesselEvent() {
    var dialog = ProgressDialog.progressDialog(this@PilotOnBoardToOffBoardActivity)
    dialog.show()
    //creating volley string request
    val stringRequest = object : StringRequest(
        Method.POST,
        EndPoints.URL_GET_VESSEL_EVENT,
        Response.Listener<String> { response ->
            try {
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                //  val message = jsonObject.getString("message")
                Log.e("Message", jsonObject.toString())
                if (success.equals("1")) {
                    dialog.dismiss()
                    val data: JSONArray = jsonObject.getJSONArray("data")
                    if (data.length() > 0) {
                        var dataInner: JSONObject = data.getJSONObject(0)
                        val pilot_on_board = dataInner.getString("pilot_on_board")
                        val pilot_off_board = dataInner.getString("pilot_off_board")
                        val mooring_frm_time = dataInner.getString("mooring_frm_time")
                        val mooring_to_time = dataInner.getString("mooring_to_time")
//                            val aditional_pilot = dataInner.getString("aditional_pilot")
//                            val aditional_tug = dataInner.getString("aditional_tug")
//                            val remarks = dataInner.getString("remarks")
                        val berth = dataInner.getString("berth")
//                            val photo = dataInner.getString("photo")
                        if (ACTIVITY_FOR == "incoming") {
                            Log.e("ACT-", "INCOMING")
                            if (pilot_on_board != "") {
                                if (pilot_on_board !="" && pilot_on_board != null){
                                    etOnBoardDate?.setText(getDate(pilot_on_board))
                                    etOnBoardTime?.setText(getTime(pilot_on_board))
                                }
                                checkSw1 = 2
                                swOnBoard?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutFirstLine?.visibility = View.VISIBLE
                            }
//                                if (mooring_to_time != "") {
//                                    etLastLine?.setText(mooring_to_time)
//                                    checkSw2 = 2
//                                    swLastLine?.isChecked = true
//                                    linLayoutOnboard?.setVisibility(View.VISIBLE);
//                                    linLayoutLastLine?.setVisibility(View.VISIBLE);
//                                   // linLayoutFirstLine?.setVisibility(View.VISIBLE);
//                                }
                            if (mooring_frm_time != "") {
                                if (mooring_frm_time !="" && mooring_frm_time != null){
                                    etFirstLineDate?.setText(getDate(mooring_frm_time))
                                    etFirstLineTime?.setText(getTime(mooring_frm_time))
                                }
                                actvBerth?.setText(berth)
                                // searchBerth?.setQuery(berth, false);
                                checkSw3 = 2
                                swFirstLine?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutFirstLine?.visibility = View.VISIBLE
                                linLayoutOffBoard?.visibility = View.VISIBLE
                            }
                            if (pilot_off_board != "") {
                                if (pilot_off_board !="" && pilot_off_board != null){
                                    etOffBordDate?.setText(getDate(pilot_off_board))
                                    etOffBordTime?.setText(getTime(pilot_off_board))
                                }
                                checkSw4 = 2
                                swOffBoard?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutFirstLine?.visibility = View.VISIBLE
                                linLayoutOffBoard?.visibility = View.VISIBLE
                                linLayoutNextButton?.visibility = View.VISIBLE
                            }
                        } else if (ACTIVITY_FOR == "shifting") {
                            Log.e("ACT-", "SHIFTING")
                            if (pilot_on_board != "") {
                               // etOnBoard?.setText(pilot_on_board)
                                if (pilot_on_board !="" && pilot_on_board != null){
                                    etOnBoardDate?.setText(getDate(pilot_on_board))
                                    etOnBoardTime?.setText(getTime(pilot_on_board))
                                }
                                checkSw1 = 2
                                swOnBoard?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                            }
                            if (mooring_to_time != "") {
                               // etLastLine?.setText(mooring_to_time)
                                if (mooring_to_time !="" && mooring_to_time != null){
                                    etLastLineDate?.setText(getDate(mooring_to_time))
                                    etLastLineTime?.setText(getTime(mooring_to_time))
                                }
                                checkSw2 = 2
                                swLastLine?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                                linLayoutFirstLine?.visibility = View.VISIBLE
                            }
                            if (mooring_frm_time != "") {
                                //etFirstLine?.setText(mooring_frm_time)
                                if (mooring_frm_time !="" && mooring_frm_time != null){
                                    etFirstLineDate?.setText(getDate(mooring_frm_time))
                                    etFirstLineTime?.setText(getTime(mooring_frm_time))
                                }
                                actvBerth?.setText(berth)
                                // searchBerth?.setQuery(berth, false);
                                checkSw3 = 2
                                swFirstLine?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                                linLayoutFirstLine?.visibility = View.VISIBLE
                                linLayoutOffBoard?.visibility = View.VISIBLE
                            }
                            if (pilot_off_board != "") {
                               // etOffBord?.setText(pilot_off_board)
                                if (pilot_off_board !="" && pilot_off_board != null){
                                    etOffBordDate?.setText(getDate(pilot_off_board))
                                    etOffBordTime?.setText(getTime(pilot_off_board))
                                }
                                checkSw4 = 2
                                swOffBoard?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                                linLayoutFirstLine?.visibility = View.VISIBLE
                                linLayoutOffBoard?.visibility = View.VISIBLE
                                linLayoutNextButton?.visibility = View.VISIBLE
                            }
                        } else {
                            Log.e("ACT-", "OUTGOING")
                            if (pilot_on_board != "") {
                                //etOnBoard?.setText(pilot_on_board)
                                if (pilot_on_board !="" && pilot_on_board != null){
                                    etOnBoardDate?.setText(getDate(pilot_on_board))
                                    etOnBoardTime?.setText(getTime(pilot_on_board))
                                }
                                checkSw1 = 2
                                swOnBoard?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                            }
                            if (mooring_to_time != "") {
                               // etLastLine?.setText(mooring_to_time)
                                if (mooring_to_time !="" && mooring_to_time != null){
                                    etLastLineDate?.setText(getDate(mooring_to_time))
                                    etLastLineTime?.setText(getTime(mooring_to_time))
                                }
                                checkSw2 = 2
                                swLastLine?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                                linLayoutOffBoard?.visibility = View.VISIBLE
                            }
//
//                                if (mooring_frm_time != "") {
//                                    etFirstLine?.setText(mooring_frm_time)
//                                    checkSw3 = 2
//                                    swFirstLine?.isChecked = true
//                                    linLayoutOnboard?.setVisibility(View.VISIBLE);
//                                    linLayoutLastLine?.setVisibility(View.VISIBLE);
//                                    linLayoutFirstLine?.setVisibility(View.VISIBLE);
//                                    linLayoutOffBoard?.setVisibility(View.VISIBLE);
//                                }
                            if (pilot_off_board != "") {
                              //  etOffBord?.setText(pilot_off_board)
                                if (pilot_off_board !="" && pilot_off_board != null){
                                    etOffBordDate?.setText(getDate(pilot_off_board))
                                    etOffBordTime?.setText(getTime(pilot_off_board))
                                }
                                checkSw4 = 2
                                swOffBoard?.isChecked = true
                                linLayoutOnboard?.visibility = View.VISIBLE
                                linLayoutLastLine?.visibility = View.VISIBLE
                                linLayoutOffBoard?.visibility = View.VISIBLE
                                linLayoutNextButton?.visibility = View.VISIBLE
                            }
                        }
//                            if(aditional_pilot != "0" || aditional_tug != "0" || remarks != "" || berth != ""){
//
//                                etAdditionalPilot?.setText(aditional_pilot)
//                                etEditionalTug?.setText(aditional_tug)
//                                etRemarks?.setText(remarks)
//                                etBerth?.setText(berth)
//                                if(photo != "") {
//                                    val imageBytes = Base64.decode(photo, Base64.DEFAULT)
//                                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes,
//                                        0,
//                                        imageBytes.size)
//
//                                    imgPilot?.setImageBitmap(decodedImage)
//                                }
//
//                            }
                    }
//                        val incoming_data: JSONArray = jsonObject.getJSONArray("incoming_data")
//                        if (incoming_data.length() > 0) {
//                            inActivateLayout()
////                            linLayoutOnboard?.setVisibility(View.VISIBLE);
////                            linLayoutLastLine?.setVisibility(View.VISIBLE);
////                            linLayoutFirstLine?.setVisibility(View.VISIBLE);
////                            val incoming_dataInner: JSONObject = data.getJSONObject(0)
//
//                        }
//                        val shifting_data: JSONArray = jsonObject.getJSONArray("shifting_data")
//                        if (shifting_data.length() > 0) {
////                            linLayoutOnboard?.setVisibility(View.VISIBLE);
////                            linLayoutLastLine?.setVisibility(View.VISIBLE);
////                            linLayoutFirstLine?.setVisibility(View.VISIBLE);
////                            linLayoutOffBoard?.setVisibility(View.VISIBLE);
////
////                            val shifting_dataInner: JSONObject = data.getJSONObject(0)
//                        }
                } else {
                    dialog.dismiss()
//                        Toast.makeText(
//                            applicationContext,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()
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
            val params = HashMap<String, String>()
            params.put("request_for", ACTIVITY_FOR)
            params.put("vvd_gkey", VVD_GKEY)
            return params
        }
    }

    //adding request to queue
    val queue = Volley.newRequestQueue(this@PilotOnBoardToOffBoardActivity)
    queue?.add(stringRequest)
    }
    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
    private fun vesselEventAction(flag: Int, date_time: String) {
        var dialog = ProgressDialog.progressDialog(this@PilotOnBoardToOffBoardActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_SET_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                  //  Log.e("Message", jsonObject.toString())
                    if(success.equals("1")) {
                        dialog.dismiss()
                        val date_time = jsonObject.getString("date_time")
                        if(date_time != "") {

                            if(ACTIVITY_FOR == "incoming") {
                                if(flag == 1) {
                                    if(date_time !="" && date_time != null){
                                        etOnBoardDate?.setText(getDate(date_time))
                                        etOnBoardTime?.setText(getTime(date_time))
                                    }
                                    swOnBoard?.isChecked = true
                                    linLayoutFirstLine?.visibility = View.VISIBLE
                                } else if(flag == 3) {
                                    if(date_time !="" && date_time != null){
                                        etFirstLineDate?.setText(getDate(date_time))
                                        etFirstLineTime?.setText(getTime(date_time))
                                    }
                                    val berth = jsonObject.getString("berth")
                                    actvBerth?.setText(berth)
                                    //searchBerth?.setQuery(berth, false);
                                    swFirstLine?.isChecked = true
                                    linLayoutOffBoard?.visibility = View.VISIBLE
                                } else if(flag == 4) {
                                    if(date_time !="" && date_time != null){
                                        etOffBordDate?.setText(getDate(date_time))
                                        etOffBordTime?.setText(getTime(date_time))
                                    }

                                    swOffBoard?.isChecked = true
                                    linLayoutNextButton?.visibility = View.VISIBLE
                                }
                            } else if(ACTIVITY_FOR == "shifting") {
                                if(flag == 1) {
                                    if(date_time !="" && date_time != null){
                                        etOnBoardDate?.setText(getDate(date_time))
                                        etOnBoardTime?.setText(getTime(date_time))
                                    }
                                    swOnBoard?.isChecked = true
                                    linLayoutLastLine?.visibility = View.VISIBLE
                                } else if(flag == 2) {
                                    if(date_time !="" && date_time != null){
                                        etLastLineDate?.setText(getDate(date_time))
                                        etLastLineTime?.setText(getTime(date_time))
                                    }
                                    swLastLine?.isChecked = true
                                    linLayoutFirstLine?.visibility = View.VISIBLE
                                } else if (flag == 3) {
                                    if (date_time !="" && date_time != null){
                                        etFirstLineDate?.setText(getDate(date_time))
                                        etFirstLineTime?.setText(getTime(date_time))
                                    }
                                    val berth = jsonObject.getString("berth")
                                    actvBerth?.setText(berth)
                                    //searchBerth?.setQuery(berth, false);
                                    swFirstLine?.isChecked = true
                                    linLayoutOffBoard?.visibility = View.VISIBLE
                                } else if (flag == 4) {
                                    if (date_time !="" && date_time != null){
                                        etOffBordDate?.setText(getDate(date_time))
                                        etOffBordTime?.setText(getTime(date_time))
                                    }
                                    swOffBoard?.isChecked = true
                                    linLayoutNextButton?.visibility = View.VISIBLE
                                }
                            } else if (ACTIVITY_FOR == "outgoing") {
                                if (flag == 1) {
                                    if (date_time !="" && date_time != null){
                                        etOnBoardDate?.setText(getDate(date_time))
                                        etOnBoardTime?.setText(getTime(date_time))
                                    }
                                    swOnBoard?.isChecked = true
                                    linLayoutLastLine?.visibility = View.VISIBLE
                                } else if (flag == 2) {
                                    if (date_time !="" && date_time != null){
                                        etLastLineDate?.setText(getDate(date_time))
                                        etLastLineTime?.setText(getTime(date_time))
                                    }
                                    swLastLine?.isChecked = true
                                    linLayoutOffBoard?.visibility = View.VISIBLE
                                } else if (flag == 4) {
                                    if (date_time !="" && date_time != null){
                                        etOffBordDate?.setText(getDate(date_time))
                                        etOffBordTime?.setText(getTime(date_time))
                                    }
                                    swOffBoard?.isChecked = true
                                    linLayoutNextButton?.visibility = View.VISIBLE
                                }
                            }
                        }
                    } else {
                        dialog.dismiss()
//                        Toast.makeText(
//                            applicationContext,
//                            jsonObject.getString("message"),
//                            Toast.LENGTH_LONG
//                        ).show()
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
                val params = HashMap<String, String>()
                params.put("rotation", ROTATION)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("pilot_name", SignedInLoginId.toString())
                params.put("date_time", date_time)
                params.put("berth_from", if(etShiftingFrom?.text.toString() =="") "" else etShiftingFrom?.text.toString())

                Log.e("FLAG", flag.toString())
                Log.e("ROTATION", ROTATION)
                Log.e("vvd_gkey", VVD_GKEY)
                Log.e("date_time", date_time)
                Log.e("etShiftingFrom",  if(etShiftingFrom?.text.toString() =="") "" else etShiftingFrom?.text.toString())

                if(flag == 1) params.put("request_for", "ONBOARD")
                else if (flag == 2) params.put("request_for", "LASTLINE")
                else if (flag == 3){
                    params.put("request_for", "FIRSTLINE")
                    params.put("berth", actvBerth?.text.toString())
                  }
                else if (flag == 4) params.put("request_for", "OFFBOARD")
                /*else if(flag == 5){
                    val image: String = bitmap?.let { getStringImage(it) }!!
                    params.put("request_for", "ADDITIONAL")
                    params.put("addiPilot", etAdditionalPilot?.text.toString())
                    params.put("addiTug", etEditionalTug?.text.toString())
                    params.put("remarks", etRemarks?.text.toString())
                    params.put("berth", etBerth?.text.toString())
                    params.put("imgString", image)
                }*/

                if(ACTIVITY_FOR == "incoming") params.put("request_type", "incoming")
                else if(ACTIVITY_FOR == "shifting") params.put("request_type", "shifting")
                else if(ACTIVITY_FOR == "outgoing") params.put("request_type", "outgoing")
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@PilotOnBoardToOffBoardActivity)
        queue?.add(stringRequest)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == START_ACTIVITY_ADDADDITIONALINFORMATION_REQUEST_CODE) {
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
    private fun getDate(datetime: String):String {
        val parts: List<String> = datetime.split(" ")
        return parts[0]
    }
    private fun getTime(datetime: String):String {
        val parts: List<String> = datetime.split(" ")
        return parts[1]
    }

}