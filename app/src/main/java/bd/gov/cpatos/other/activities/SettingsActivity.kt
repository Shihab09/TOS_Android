package bd.gov.cpatos.other.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_gate_in.*

class SettingsActivity : AppCompatActivity() {
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var mPreferences: SharedPreferences? = null
    private var preferencesEditor: SharedPreferences.Editor? = null

    private  var SignedInId:String? = null
    private  var SignedInLoginId:String? = null
    private  var SignedInSection:String? = null
    private  var SignedInUserRole:String? = null
    private  var AlarmIp:String? = null


    private var etAlarmIp: TextInputEditText? = null
    private var btnUpdateAlarmIp: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        SignedInId = mPreferences?.getString("SignedInId", "null")
        SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
        SignedInSection = mPreferences?.getString("SignedInSection", "null")
        SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
        AlarmIp = mPreferences?.getString("AlarmIp", "null")


        etAlarmIp = findViewById(R.id.etAlarmIp)
        btnUpdateAlarmIp = findViewById(R.id.btnUpdateAlarmIp)

        etAlarmIp?.setText(AlarmIp)

        btnUpdateAlarmIp?.setOnClickListener {
            var etAlarmIpS = etAlarmIp?.text.toString()

            if(etAlarmIpS.equals("")){
//                val snack = Snackbar.make(View(this@SettingsActivity),"Please Set Alarm Ip",Snackbar.LENGTH_LONG)
//                snack.show()
            }else{
                preferencesEditor?.putString("AlarmIp", etAlarmIpS)
                preferencesEditor?.commit()
            }

        }



        }
    fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }

}