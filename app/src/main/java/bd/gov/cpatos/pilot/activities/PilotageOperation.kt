package bd.gov.cpatos.pilot.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.*
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class PilotageOperation : AppCompatActivity() {


    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var isSignedIn:String? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    //----//
    private var VVD_GKEY = ""
    private var ROTATION = ""
    private var ACTIVITY_FOR = ""
    private var linLayoutShiftFrom: LinearLayout? =null //
    private var relLayPOB: RelativeLayout? =null
    private var relLayLastLine: RelativeLayout? =null
    private var relLayFirstLine: RelativeLayout? =null
    private var relLayDOP: RelativeLayout? =null
    private var linLayoutNextButton: LinearLayout? =null

    private var etShiftingFrom: AutoCompleteTextView? =null
    private var etOnBoardDate: TextInputEditText? =null
    private var etOnBoardTime: TextInputEditText? =null
    private var dividerImg: ImageView?=null
    private var etLastLineTitle: TextView? =null
    private var etLastLineDate: TextInputEditText? =null
    private var etLastLineTime: TextInputEditText? =null

    private var Berth : ArrayList<String> = arrayListOf<String>()
    private var actvBerth: AutoCompleteTextView? = null
    private var toBertTitle: TextInputLayout?=null
    private var etFirstLineDate: TextInputEditText? =null
    private var etFirstLineTime: TextInputEditText? =null

    private var etOffBordDate: TextInputEditText? =null
    private var etOffBordTime: TextInputEditText? =null
    private var btnNext: Button? =null

    private var mContext: Context?=null
    private val calander: Calendar = Calendar.getInstance()
    private val date_format = SimpleDateFormat("yyyy-MM-dd")

    companion object {
        const val START_ACTIVITY_ADDADDITIONALINFORMATION_REQUEST_CODE = 703
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilotage_operation)
        mContext=this
        init()
        getUI()
        inActivateLayout()
        getBerth()
        if(ACTIVITY_FOR != "shifting")
            getVesselEvent()


        btnNext?.setOnClickListener {

            val pobDate= etOnBoardDate?.text.toString().trim()
            val pobTime=etOnBoardTime?.text.toString().trim()
            val lastLineDate= etLastLineDate?.text.toString().trim()
            val lastLineTime=etLastLineTime?.text.toString().trim()
            val firstLineDate= etFirstLineDate?.text.toString().trim()
            val firstLineTime=etFirstLineTime?.text.toString().trim()
            val dopDate= etOffBordDate?.text.toString().trim()
            val dopTime=etOffBordTime?.text.toString().trim()

            if(ACTIVITY_FOR == "incoming"){
                if(pobDate =="" || pobTime=="" || firstLineDate=="" || firstLineTime==""|| dopDate=="" || dopTime==""){
                    Toast.makeText(mContext, "Please Fill All Field First To Go Next", Toast.LENGTH_LONG).show()
                }else if(actvBerth?.text.toString() ==""){
                    Toast.makeText(mContext,"Shifting To not given",Toast.LENGTH_LONG).show()
                }else if(pobTime.length ==4 && firstLineTime.length ==4 && dopTime.length ==4){
                    val pob = pobDate+" "+ setTime(pobTime)
                    val lastLine = lastLineDate+" "+setTime("0000")
                    val firstLine = firstLineDate+" "+setTime(firstLineTime)
                    val dop = dopDate+" "+setTime(dopTime)

                  //  Log.e("INITAL SUBMIT VALUE","pob: "+pob+" lastLine:"+lastLine+" firstLine:"+firstLine+ " dop:"+dop)

                    if(checkDate(pobDate,firstLineDate)){
                        if(checkTime(pobTime,firstLineTime)){
                            if(checkDate(firstLineDate,dopDate)){
                                if(checkTime(firstLineTime,dopTime)){
                                    vesselEventAction(pob, lastLine, firstLine, dop)
                                }else{
                                    etOffBordTime?.setError("Wrong! Time Can't higher Then FIRST LINE.")
                                }
                            }else{
                                etOffBordDate?.setError("Wrong! Date Can't higher Then FIRST LINE.")
                            }

                        }else{
                            etFirstLineTime?.setError("Wrong! Time Can't higher Then POB.")
                        }

                    }else{
                        etFirstLineDate?.setError("Wrong! Date Can't higher Then POB.")
                    }


                }else{
                   // Toast.makeText(mContext,"Time Format is invalid",Toast.LENGTH_LONG).show()
                }



            }
            else if(ACTIVITY_FOR == "shifting"){
                if(pobDate =="" || pobTime=="" || firstLineDate=="" || firstLineTime=="" || lastLineDate=="" || lastLineTime==""  || dopDate=="" || dopTime==""){
                    Toast.makeText(
                        mContext,
                        "Please Fill All Field First To Go Next",
                        Toast.LENGTH_LONG
                    ).show()
                }else if(actvBerth?.text.toString() ==""){
                    Toast.makeText(mContext,"Shifting To not given",Toast.LENGTH_LONG).show()
                }else if(pobTime.length ==4 && firstLineTime.length ==4 && lastLineTime.length ==4 && dopTime.length ==4){
                    val pob = pobDate+" "+ setTime(pobTime)
                    val lastLine = lastLineDate+" "+setTime(lastLineTime)
                    val firstLine = firstLineDate+" "+setTime(firstLineTime)
                    val dop = dopDate+" "+setTime(dopTime)
                    if(checkDate(pobDate,lastLineDate)){
                        if(checkTime(pobTime,lastLineTime)){
                            if(checkDate(lastLineDate,firstLineDate)){
                                if(checkTime(lastLineTime,firstLineTime)){
                                    if(checkDate(firstLineDate,dopDate)){
                                        if(checkTime(firstLineTime,dopTime)){
                                            vesselEventAction(pob, lastLine, firstLine, dop)
                                        }else{
                                            etOffBordTime?.setError("Wrong! Time Can't higher Then FIRST LINE")
                                        }
                                    }else{
                                        etOffBordDate?.setError("Wrong! Date Can't higher Then FIRST LINE")
                                    }
                                }else{
                                    etFirstLineTime?.setError("Wrong! Time Can't higher Then LAST LINE")
                                }
                            }else{
                                etFirstLineDate?.setError("Wrong! Date Can't higher Then LAST LINE.")
                            }
                        }else{
                            etLastLineTime?.setError("Wrong! Time Can't higher Then POB.")
                        }
                    }else{
                        etLastLineDate?.setError("Wrong! Date Can't higher Then POB.")
                    }
                }else{
                   // Toast.makeText(mContext,"Time Format is invalid",Toast.LENGTH_LONG).show()
                }


            }
            else{
                if(pobDate =="" || pobTime==""|| lastLineDate=="" || lastLineTime==""  || dopDate=="" || dopTime==""){
                    Toast.makeText(mContext,"Please Fill All Field First To Go Next",Toast.LENGTH_LONG).show()
                }else if(pobTime.length ==4 && lastLineTime.length ==4  && dopTime.length ==4){
                    val pob = pobDate+" "+ setTime(pobTime)
                    val lastLine = lastLineDate+" "+setTime(lastLineTime)
                    val firstLine = firstLineDate+" "+setTime("0000")
                    val dop = dopDate+" "+setTime(dopTime)

                    if(checkDate(pobDate,lastLineDate)){
                        if(checkTime(pobTime,lastLineTime)){
                            if(checkDate(lastLineDate,dopDate)){
                                if(checkTime(lastLineTime,dopTime)){
                                    vesselEventAction(pob, lastLine, firstLine, dop)
                                }else{
                                    etOffBordTime?.setError("Wrong! Time Can't higher Then LAST LINE.")
                                }

                            }else{
                                etOffBordDate?.setError("Wrong! Date Can't higher Then LAST LINE.")
                            }

                        }else{
                            etLastLineTime?.setError("Wrong! Time Can't higher Then POB.")
                        }

                    }else{
                        etLastLineDate?.setError("Wrong! Date Can't higher Then POB.")
                    }

                }else{
                   // Toast.makeText(mContext,"Time Format is invalid",Toast.LENGTH_LONG).show()
                }
            }


        }





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
            //Log.e("ROTATION-2", ROTATION)
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
    private  fun  getUI(){
        linLayoutShiftFrom = findViewById(R.id.liLay01New)
        relLayPOB = findViewById(R.id.relLayPOB)
        relLayLastLine = findViewById(R.id.relLayLastLine)
        relLayFirstLine = findViewById(R.id.relLayFirstLine)
        relLayDOP = findViewById(R.id.relLayDOP)
        linLayoutNextButton = findViewById(R.id.liLay05)
        etShiftingFrom= findViewById(R.id.etShiftingFrom)
        etOnBoardDate = findViewById(R.id.etOnBoardDate)
        etOnBoardTime = findViewById(R.id.etOnBoardTime)
        dividerImg= findViewById(R.id.dividerImg)
        etLastLineTitle = findViewById(R.id.etLastLineTitle)
        etLastLineDate = findViewById(R.id.etLastLineDate)
        etLastLineTime = findViewById(R.id.etLastLineTime)
        actvBerth = findViewById(R.id.actvBerth)
        etFirstLineDate = findViewById(R.id.etFirstLineDate)
        etFirstLineTime = findViewById(R.id.etFirstLineTime)
        toBertTitle = findViewById(R.id.txtInputLayoutToBerth)
        etOffBordDate = findViewById(R.id.etOffBordDate)
        etOffBordTime = findViewById(R.id.etOffBordTime)
        btnNext = findViewById(R.id.btnNext)
        if(ACTIVITY_FOR =="outgoing"){
            etLastLineTitle?.text = "CAST OFF"
            toBertTitle?.hint = "Type And Select Berth"
            dividerImg?.visibility = View.INVISIBLE
        }else if(ACTIVITY_FOR =="shifting"){
            toBertTitle?.hint = "Shifting To Berth"
            etLastLineTitle?.text = "LAST LINE CAST OFF"
            dividerImg?.visibility = View.VISIBLE
        }else{
            etLastLineTitle?.text = "LAST LINE"
            toBertTitle?.hint = "Type And Select Berth"
            dividerImg?.visibility = View.INVISIBLE
        }
        val current_date: String = date_format.format(calander.time)
        etOnBoardDate?.setText(current_date)
        etLastLineDate?.setText(current_date)
        etFirstLineDate?.setText(current_date)
        etOffBordDate?.setText(current_date)

        etOnBoardTime?.filters = arrayOf<InputFilter>(MinMaxFilter(0, 2359))
        etLastLineTime?.filters = arrayOf<InputFilter>(MinMaxFilter(0, 2359))
        etFirstLineTime?.filters = arrayOf<InputFilter>(MinMaxFilter(0, 2359))
        etOffBordTime?.filters = arrayOf<InputFilter>(MinMaxFilter(0, 2359))

    }
    private fun inActivateLayout() {
        when (ACTIVITY_FOR) {
            "incoming" -> {
                linLayoutShiftFrom?.visibility = View.GONE
                relLayPOB?.visibility = View.VISIBLE
                relLayLastLine?.visibility = View.GONE
                relLayFirstLine?.visibility = View.VISIBLE
                relLayDOP?.visibility = View.VISIBLE
                linLayoutNextButton?.visibility = View.VISIBLE
            }
            "shifting" -> {
                linLayoutShiftFrom?.visibility = View.VISIBLE
                relLayPOB?.visibility = View.VISIBLE
                relLayLastLine?.visibility = View.VISIBLE
                relLayFirstLine?.visibility = View.VISIBLE
                relLayDOP?.visibility = View.VISIBLE
                linLayoutNextButton?.visibility = View.VISIBLE
            }
            "outgoing" -> {
                linLayoutShiftFrom?.visibility = View.VISIBLE
                relLayPOB?.visibility = View.VISIBLE
                relLayLastLine?.visibility = View.VISIBLE
                relLayFirstLine?.visibility = View.GONE
                relLayDOP?.visibility = View.VISIBLE
                linLayoutNextButton?.visibility = View.VISIBLE
            }
            else -> {
                linLayoutShiftFrom?.visibility = View.GONE
                relLayPOB?.visibility = View.GONE
                relLayLastLine?.visibility = View.GONE
                relLayFirstLine?.visibility = View.GONE
                relLayDOP?.visibility = View.GONE
                linLayoutNextButton?.visibility = View.GONE
            }
        }
    }

    private fun getBerth() {
        var dialog = ProgressDialog.progressDialog(this@PilotageOperation)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_GET_BERTH_LIST,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    // val message = jsonObject.getString("message")
                  //  Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val data: JSONArray = jsonObject.getJSONArray("data")
                        val berthFrom = jsonObject.getString("berthFrom")
                       /// etShiftingFrom?.setText(berthFrom)

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
                        etShiftingFrom?.setAdapter(adapter)

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
                val params = HashMap<String, String>()
                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@PilotageOperation)
        queue?.add(stringRequest)
    }
    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        // Initialized
        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        // Check if input c is in between min a and max b and
        // returns corresponding boolean
        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
    private fun getVesselEvent() {
        var dialog = ProgressDialog.progressDialog(this@PilotageOperation)
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
                  //  Log.e("Message", jsonObject.toString())
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
                               // Log.e("ACT-", "INCOMING")
                                if (pilot_on_board != "") {
                                    if (pilot_on_board != "" && pilot_on_board != null) {
                                        etOnBoardDate?.setText(getDate(pilot_on_board))
                                        etOnBoardTime?.setText(getTime(pilot_on_board))
                                    }
                                }
                                if (mooring_frm_time != "") {
                                    if (mooring_frm_time != "" && mooring_frm_time != null) {
                                        etFirstLineDate?.setText(getDate(mooring_frm_time))
                                        etFirstLineTime?.setText(getTime(mooring_frm_time))
                                    }
                                    actvBerth?.setText(berth)
                                }
                                if (pilot_off_board != "") {
                                    if (pilot_off_board != "" && pilot_off_board != null) {
                                        etOffBordDate?.setText(getDate(pilot_off_board))
                                        etOffBordTime?.setText(getTime(pilot_off_board))
                                    }
                                }
                            } else if (ACTIVITY_FOR == "shifting") {
                             //   Log.e("ACT-", "SHIFTING")
                                if (pilot_on_board != "") {
                                    // etOnBoard?.setText(pilot_on_board)
                                    if (pilot_on_board != "" && pilot_on_board != null) {
                                        etOnBoardDate?.setText(getDate(pilot_on_board))
                                        etOnBoardTime?.setText(getTime(pilot_on_board))
                                    }
                                }
                                if (mooring_to_time != "") {
                                    // etLastLine?.setText(mooring_to_time)
                                    if (mooring_to_time != "" && mooring_to_time != null) {
                                        etLastLineDate?.setText(getDate(mooring_to_time))
                                        etLastLineTime?.setText(getTime(mooring_to_time))
                                    }
                                }
                                if (mooring_frm_time != "") {
                                    //etFirstLine?.setText(mooring_frm_time)
                                    if (mooring_frm_time != "" && mooring_frm_time != null) {
                                        etFirstLineDate?.setText(getDate(mooring_frm_time))
                                        etFirstLineTime?.setText(getTime(mooring_frm_time))
                                    }
                                    actvBerth?.setText(berth)
                                }
                                if (pilot_off_board != "") {
                                    // etOffBord?.setText(pilot_off_board)
                                    if (pilot_off_board != "" && pilot_off_board != null) {
                                        etOffBordDate?.setText(getDate(pilot_off_board))
                                        etOffBordTime?.setText(getTime(pilot_off_board))
                                    }
                                }
                            } else {
                              //  Log.e("ACT-", "OUTGOING")
                                if (pilot_on_board != "") {
                                    //etOnBoard?.setText(pilot_on_board)
                                    if (pilot_on_board != "" && pilot_on_board != null) {
                                        etOnBoardDate?.setText(getDate(pilot_on_board))
                                        etOnBoardTime?.setText(getTime(pilot_on_board))
                                    }
                                }
                                if (mooring_to_time != "") {
                                    // etLastLine?.setText(mooring_to_time)
                                    if (mooring_to_time != "" && mooring_to_time != null) {
                                        etLastLineDate?.setText(getDate(mooring_to_time))
                                        etLastLineTime?.setText(getTime(mooring_to_time))
                                    }
                                }
                                if (pilot_off_board != "") {
                                    //  etOffBord?.setText(pilot_off_board)
                                    if (pilot_off_board != "" && pilot_off_board != null) {
                                        etOffBordDate?.setText(getDate(pilot_off_board))
                                        etOffBordTime?.setText(getTime(pilot_off_board))
                                    }
                                }
                            }

                        }

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
                val params = HashMap<String, String>()
                params.put("request_for", ACTIVITY_FOR)
                params.put("vvd_gkey", VVD_GKEY)
                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@PilotageOperation)
        queue?.add(stringRequest)
    }
    private fun getDate(datetime: String):String {
        val parts: List<String> = datetime.split(" ")
        return parts[0]
    }
    private fun getTime(datetime: String):String {
        val parts: List<String> = datetime.split(" ")
        return parts[1].replace(":","")
    }
    private fun setTime(myTime: String): String {

        return myTime.substring(0, 2) + ":" + myTime.substring(2, myTime.length)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PilotOnBoardToOffBoardActivity.START_ACTIVITY_ADDADDITIONALINFORMATION_REQUEST_CODE) {
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


    private fun checkDate(firstDate:String,secondDate:String):Boolean{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val firstDate: Date = sdf.parse(firstDate)
        val secondDate: Date = sdf.parse(secondDate)

        val cmp = firstDate.compareTo(secondDate)
        when {
            cmp > 0 -> {
                return false
                System.out.printf("%s is after %s", firstDate, secondDate)
            }
            cmp < 0 -> {
                return true
                System.out.printf("%s is before %s", firstDate, secondDate)
            }
            else -> {
                print("Both dates are equal")
                return true;
            }
        }
        return true
    }
    private fun checkTime(firstTime:String,secondTime:String):Boolean{
        if(firstTime.toInt()>secondTime.toInt()){
            return false
        }else if(firstTime.toInt()<secondTime.toInt()){
            return true
        }else{
            return true
        }

        return true;
    }
    private fun vesselEventAction(pob: String?, lastLine: String?, firstLine: String?, dop: String?) {

   //    Log.e("TAG--","vesselEventAction pob:"+ pob+" lastLine:"+ lastLine+ " firstLine:"+ firstLine+" dop:"+ pob)
        var dialog = ProgressDialog.progressDialog(this@PilotageOperation)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_SET_VESSEL_EVENT,
            Response.Listener<String> { response ->
                try {
                    val jsonObject: JSONObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                  //  Log.e("success", jsonObject.toString())
                    if (success.equals("1")) {
                        dialog.dismiss()
                        var BERTH_FROM = ""
                        var intent = Intent(this@PilotageOperation, AddAdditionalInformationActivity::class.java)

                        if (ACTIVITY_FOR == "incoming") {
                            intent.putExtra("TITLE", "Incoming Vessel:(" + ROTATION + ")")
                            BERTH_FROM = "SEA"
                        }else if (ACTIVITY_FOR == "shifting") {
                            intent.putExtra("TITLE", "Shifting Vessel:(" + ROTATION + ")")
                            BERTH_FROM =  etShiftingFrom?.text.toString()
                        }else {
                            intent.putExtra("TITLE", "Outgoing Vessel:(" + ROTATION + ")")
                            BERTH_FROM =  etShiftingFrom?.text.toString()
                        }
                        // Pass the values to next activity (StationActivity)


                        intent.putExtra("VVD_GKEY", VVD_GKEY)
                        intent.putExtra("ROTATION", ROTATION)
                        intent.putExtra("ACTIVITY_FOR", ACTIVITY_FOR)
                        intent.putExtra("BERTH_FROM",BERTH_FROM)
                        intent.putExtra("DEPURTURE_OF_PILOT", dop+":00")
                        startActivityForResult(intent,
                            START_ACTIVITY_ADDADDITIONALINFORMATION_REQUEST_CODE
                        )

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
//                Log.e("rotation", ROTATION)
//                Log.e("vvd_gkey", VVD_GKEY)
//                Log.e("request_type", ACTIVITY_FOR)
//                Log.e("pilot_name", SignedInLoginId.toString())
//                Log.e("pob", pob.toString())
//                Log.e("lastLine", lastLine.toString())
//                Log.e("firstLine", firstLine.toString())
//                Log.e("dop", dop.toString())
//                Log.e("final_submit", "0")
//                Log.e("berth_from",if (etShiftingFrom?.text.toString() == "") "" else etShiftingFrom?.text.toString())
//                Log.e("berth",if (actvBerth?.text.toString() == "") "" else actvBerth?.text.toString())


                params.put("rotation", ROTATION)
                params.put("vvd_gkey", VVD_GKEY)
                params.put("request_type", ACTIVITY_FOR)
                params.put("pilot_name", SignedInLoginId.toString())
                params.put("pob", pob.toString())
                params.put("lastLine", lastLine.toString())
                params.put("firstLine", firstLine.toString())
                params.put("dop", dop.toString())
                params.put("final_submit", "0")
                params.put("berth_from",if (etShiftingFrom?.text.toString() == "") "" else etShiftingFrom?.text.toString())
                params.put("berth",if (actvBerth?.text.toString() == "") "" else actvBerth?.text.toString())

                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@PilotageOperation)
        queue?.add(stringRequest)
    }

}