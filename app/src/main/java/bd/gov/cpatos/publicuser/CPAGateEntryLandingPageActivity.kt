package bd.gov.cpatos.publicuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import bd.gov.cpatos.R


class CPAGateEntryLandingPageActivity : AppCompatActivity() {
    private var humanRedioButton: RadioButton? = null
    private var vehicaleRedioButton: RadioButton? = null
    private var deductionTicketButton: Button? =null
    private var addTicketButton: Button? =null
    private var proceedButton:Button?=null
    private var numberOfTicket:Int? = 0
    private var tvTicketNumber:TextView?=null
    private var tvFee:TextView?=null
    private var tvVat:TextView?=null
    private var tvTotal:TextView?=null
    private var ticketType:RadioGroup? =null
    private var VEHICALE_ENTRY_FEE:String? =null
    private var HUMAN_ENTRY_FEE:String? =null
    private var SELECTED_ENTRY_TYPE_FEE:String? ="NOTSET"
    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpagate_entry_landing_page)
        initUI()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bundle :Bundle? = intent.extras
        if (bundle!=null){
            title = bundle.getString("TITLE") // 1
            //VEHICALE_ENTRY_FEE = bundle.getString("HUMAN") // 1
            //HUMAN_ENTRY_FEE = bundle.getString("VEHICLE") // 1
            VEHICALE_ENTRY_FEE ="50.00"
            HUMAN_ENTRY_FEE ="10.00"

            supportActionBar?.title = title
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    private fun initUI(){
        humanRedioButton=findViewById(R.id.humanRedioButton)
        vehicaleRedioButton=findViewById(R.id.vehicaleRedioButton)
        deductionTicketButton=findViewById(R.id.deductionTicketButton)
        addTicketButton=findViewById(R.id.addTicketButton)
        proceedButton=findViewById(R.id.proceedButton)
        tvTicketNumber=findViewById(R.id.tvTicketNumber)
        tvFee=findViewById(R.id.tvFee)
        tvVat=findViewById(R.id.tvVat)
        tvTotal=findViewById(R.id.tvTotal)
        ticketType=findViewById(R.id.ticketTypeRg)

        deductionTicketButton?.setOnClickListener {
            if(SELECTED_ENTRY_TYPE_FEE == "NOTSET"){
                Toast.makeText(this, "Please Select Type of Pass",Toast.LENGTH_SHORT).show()
            }else{

            numberOfTicket= Integer.parseInt(tvTicketNumber?.text.toString())
                Log.e("numberOfTicket -:",""+numberOfTicket)
              if(numberOfTicket!! >1 && numberOfTicket!!<10 ) {
                  tvTicketNumber?.text = numberOfTicket(numberOfTicket!!, "-").toString()
                  entryFee(SELECTED_ENTRY_TYPE_FEE.toString(),tvTicketNumber?.text.toString())
              }else
                Toast.makeText(this, "Ticket Range Shoud Be 1-9",Toast.LENGTH_SHORT).show()

            }
        }
        addTicketButton?.setOnClickListener {
            if(SELECTED_ENTRY_TYPE_FEE == "NOTSET"){
                Toast.makeText(this, "Please Select Type of Pass",Toast.LENGTH_SHORT).show()
            }else{
                numberOfTicket= Integer.parseInt(tvTicketNumber?.text.toString())
            Log.e("numberOfTicket +:",""+numberOfTicket)
            if(numberOfTicket!! >= 1 && numberOfTicket!! < 9 ) {
                tvTicketNumber?.text = numberOfTicket(numberOfTicket!!, "+").toString()
                entryFee(SELECTED_ENTRY_TYPE_FEE.toString(),tvTicketNumber?.text.toString())
            }else
                Toast.makeText(this, "Ticket Range Shoud Be 1-9",Toast.LENGTH_SHORT).show()
            }
        }
        ticketType?.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            if (radioButton.isChecked) {
                if(radioButton.text.contains("Person"))
                    SELECTED_ENTRY_TYPE_FEE = HUMAN_ENTRY_FEE
                else
                    SELECTED_ENTRY_TYPE_FEE = VEHICALE_ENTRY_FEE

                entryFee(SELECTED_ENTRY_TYPE_FEE.toString(),tvTicketNumber?.text.toString())

            //                Toast.makeText(
//                    applicationContext,
//                    String.format("%s is checked", radioButton.text),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
        proceedButton?.setOnClickListener({
            val intent = Intent(this@CPAGateEntryLandingPageActivity, GatePassPaymentActivity::class.java)
            intent.putExtra("TITLE", "Add Basic Info")
            intent.putExtra("NEXT_ACTIVTY", "GatePassPaymentActivity")

            ContextCompat.startActivity(this, intent, Bundle())
        })
       // SELECTED_ENTRY_TYPE_FEE = HUMAN_ENTRY_FEE

        //entryFee(HUMAN_ENTRY_FEE.toString(),"1.00".toString())
    }

    private fun numberOfTicket(noOfTicket:Int,type:String): Int? {
        if(type=="+")
            numberOfTicket= numberOfTicket?.plus(1)
        else
            numberOfTicket = numberOfTicket?.minus(1)

        return numberOfTicket
    }

    private fun entryFee(type:String,number:String){
        Log.e("type +:",""+type.toFloatOrNull())
        Log.e("type 2+:",""+number)
        var myvalue = type.toFloat() * number.toFloat()

        //tvFee?.setText(Integer.parseInt(type)*Integer.parseInt(number))
        tvFee?.text = myvalue.toString()
        tvVat?.text = (myvalue*0.15).toString()
        tvTotal?.text = (myvalue+myvalue*0.15).toString()
    }


}