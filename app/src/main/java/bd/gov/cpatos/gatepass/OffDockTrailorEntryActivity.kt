package bd.gov.cpatos.gatepass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R

class OffDockTrailorEntryActivity : AppCompatActivity() {
    private var btnNextOffDoc: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_off_dock_trailor_entry)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            supportActionBar?.title = title
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        btnNextOffDoc = findViewById(R.id.btnNextOffDoc)
        btnNextOffDoc?.setOnClickListener {
            nextActivity()
        }

    }
    private fun nextActivity(){
        val intent = Intent(this@OffDockTrailorEntryActivity, OffDockTrailorEntryDetailsActivity::class.java)
        intent.putExtra("TITLE", "OCD Details View For Payment")
        intent.putExtra("USER_TYPE", "GatePass")
        startActivity(intent)

    }
}