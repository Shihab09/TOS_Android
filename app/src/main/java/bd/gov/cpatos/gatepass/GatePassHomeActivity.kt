package bd.gov.cpatos.gatepass

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import com.google.android.material.textfield.TextInputEditText

class GatePassHomeActivity : AppCompatActivity() {
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private  var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null
    private  var isSignedIn:String? = null
    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private var btnShedDeliveryLCL: Button? =null
    private var btnYardDeliveryFCL: Button? =null
    private var btnOnChassisDeliveryOCD: Button? =null
    private var btOffDocDelivery: Button? =null
    private var btListOfGatePass: Button? =null
    private var IMEINumber:String?  =null
    private var btnSearch:Button? = null
    private var etTruckVisit: TextInputEditText? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gate_pass_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
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
        IMEINumber = deviceId.toString()
        Log.d("MyApp", "Device ID: $deviceId")

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")


        btnShedDeliveryLCL = findViewById(R.id.btnShedDeliveryLCL)
        btnYardDeliveryFCL = findViewById(R.id.btnYardDeliveryFCL)
        btnOnChassisDeliveryOCD = findViewById(R.id.btnOnChassisDeliveryOCD)
        btOffDocDelivery = findViewById(R.id.btOffDocDelivery)
        btListOfGatePass = findViewById(R.id.btListOfGatePass)
        btnSearch = findViewById(R.id.btnSearch)
        etTruckVisit = findViewById(R.id.etTruckVisit)
        btnSearch?.setOnClickListener{
            var truckvisit = etTruckVisit?.text.toString()
            if(truckvisit !="") {
                val intent = Intent(this@GatePassHomeActivity, GatePassViewActivity::class.java)
                intent.putExtra("TITLE", "Gate Pass")
                intent.putExtra("USER_TYPE", "GatePass")
                intent.putExtra("trucVisitId", truckvisit)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Truck Visit is empty",Toast.LENGTH_LONG).show()
            }

        }

        btnShedDeliveryLCL?.setOnClickListener{

            Toast.makeText(this,"Coming Soon!",Toast.LENGTH_LONG).show()

            val intent = Intent(this@GatePassHomeActivity, LclTruckEntryActivity::class.java)
                intent.putExtra("TITLE", "LCL Truck Entry")
                intent.putExtra("USER_TYPE", "GatePass")
                intent.putExtra("MOBILE_NO", SignedInLoginId)
                intent.putExtra("DEVICE_ID", IMEINumber)
                startActivity(intent)
                Toast.makeText(this,"Coming Soon!",Toast.LENGTH_LONG).show()

        }
        btnYardDeliveryFCL?.setOnClickListener{


                   val intent = Intent(this@GatePassHomeActivity, FclTruckEntryActivity::class.java)
                    intent.putExtra("TITLE", "FCL Truck Entry")
                    intent.putExtra("USER_TYPE", "GatePass")
                    intent.putExtra("MOBILE_NO", SignedInLoginId)
                    intent.putExtra("DEVICE_ID", IMEINumber)
                    startActivity(intent)

        }
        btnOnChassisDeliveryOCD?.setOnClickListener{
//            val intent = Intent(this@GatePassHomeActivity, OnChas::class.java)
//            intent.putExtra("TITLE", "LCL Truck Entry")
//            intent.putExtra("USER_TYPE", "GatePass")
//            startActivity(intent)
            Toast.makeText(this,"Coming Soon!",Toast.LENGTH_LONG).show()

        }
        btOffDocDelivery?.setOnClickListener{
//            val intent = Intent(this@GatePassHomeActivity, OffDockTrailorEntryActivity::class.java)
//            intent.putExtra("TITLE", "Off Dock Truck Entry")
//            intent.putExtra("USER_TYPE", "GatePass")
//            intent.putExtra("MOBILE_NO", SignedInLoginId)
//            intent.putExtra("DEVICE_ID", IMEINumber)
//            startActivity(intent)
            Toast.makeText(this,"Coming Soon!",Toast.LENGTH_LONG).show()

        }

        btListOfGatePass?.setOnClickListener{
            val intent = Intent(this@GatePassHomeActivity, GatePassListActivity::class.java)
            intent.putExtra("TITLE", "Gate Pass List")
            intent.putExtra("USER_TYPE", "GatePass")
            intent.putExtra("user_id", SignedInLoginId)
            startActivity(intent)
        }

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