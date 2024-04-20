package bd.gov.cpatos.gatepass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R

class LclTruckEntryActivity : AppCompatActivity() {
    private var btnNextLCL: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lcl_truck_entry)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        btnNextLCL = findViewById(R.id.btnNextLCL)
        btnNextLCL?.setOnClickListener {
            nextActivity()
        }

    }
    private fun nextActivity(){
        val intent = Intent(this@LclTruckEntryActivity, LclTruckEntryDetailsActivity::class.java)
        intent.putExtra("TITLE", "LCL Details View For Payment")
        intent.putExtra("USER_TYPE", "GatePass")
        startActivity(intent)

    }
}