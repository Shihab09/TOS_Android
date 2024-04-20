package bd.gov.cpatos.other.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import bd.gov.cpatos.R


class HelpLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_line)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


//        val mydata: MutableList<HelpLine> = DataClass.getHelpLineDataList()
//        var aboutUs = mydata[0]
//
//
//
//        detailTxtView.text = aboutUs.data
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Help Line"
        toolbar.setNavigationOnClickListener { _: View? -> this.onBackPressed() }
    }
    fun btnCallNetDept() {
    /*    val intent = Intent(Intent.ACTION_DIAL)
        val temp = "tel:01747810037"
        intent.data = Uri.parse(temp)
        startActivity(intent)
        */
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01747810037"))
            applicationContext!!.startActivity(intent)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun btnCallOpDept() {
        val intent = Intent(Intent.ACTION_DIAL)
        val temp = "tel:01749923327"
        intent.data = Uri.parse(temp)
        startActivity(intent)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

