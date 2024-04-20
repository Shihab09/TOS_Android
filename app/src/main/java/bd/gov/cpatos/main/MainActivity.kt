package bd.gov.cpatos.main



import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import bd.gov.cpatos.R
import bd.gov.cpatos.assignment.activities.AssignmentContainerList
import bd.gov.cpatos.cnf.edo.EdoLandingPageActivity
import bd.gov.cpatos.gate.activities.GateInActivity
import bd.gov.cpatos.gate.activities.GateLandingActivity
import bd.gov.cpatos.gate.activities.GateOutActivity
import bd.gov.cpatos.gatepass.GatePassHomeActivity
import bd.gov.cpatos.importexport.activities.ExportContainerLoadActivity
import bd.gov.cpatos.importexport.activities.ImportContainerDischargeActivity
import bd.gov.cpatos.other.activities.HelpLineActivity
import bd.gov.cpatos.signinsignup.LoginActivity
import bd.gov.cpatos.other.activities.SettingsActivity
import bd.gov.cpatos.pilot.activities.PilotLandingPage
import bd.gov.cpatos.publicuser.CPAGateEntryLandingPageActivity
import bd.gov.cpatos.publicuser.WebViewActivity
import bd.gov.cpatos.reeferwater.ReeferActivity
import bd.gov.cpatos.reeferwater.WaterSupplyActivity
import bd.gov.cpatos.utils.EndPoints
import bd.gov.cpatos.utils.ProgressDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONException
import org.json.JSONObject


private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
private var mPreferences: SharedPreferences? = null
private var preferencesEditor: SharedPreferences.Editor? = null
private  var isSignedIn:String? = null
private  var SignedInId:String? = null
private  var SignedInLoginId:String? = null
private  var SignedInSection:String? = null
private  var SignedInUserRole:String? = null


private var TITLE:String? =null

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var VEHICALE_ENTRY_FEE:String? = null
    private var HUMAN_ENTRY_FEE:String? = null
    private var lusername: TextView? = null
    private var loginUserDetails: TextView? = null
    private var tvTotalGateIn: TextView? = null
    private var tvTotalGateOut: TextView? = null
    private var btnTosWeb: Button? = null
    private var btnTosGatePass: Button? = null
    private var btnGateIn: Button? = null
    private var btnGateOut: Button? = null
    private var btnReefer: Button? = null
    private var btnWater: Button? = null
    private var btnImportDischarge: Button? = null
    private var btnExportLoad: Button? = null
    private var btnPilotageModule: Button? = null
    private var btnTruckEntryFee: Button? = null
    private var btnGateModule: Button? = null
    private var etFindGateTicket: TextInputEditText? = null
    private var findButton:Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
      // tvTotalGateIn = findViewById(R.id.tvTotalGateIn)
      // tvTotalGateOut = findViewById(R.id.tvTotalGateOut)

       // login()
        load_primary_data()
        btnTosWeb=findViewById(R.id.btnTosWeb)
        btnTosGatePass=findViewById(R.id.btnTosGatePass)
        btnGateIn=findViewById(R.id.btnGateIn)
        btnGateOut=findViewById(R.id.btnGateOut)
        btnReefer=findViewById(R.id.btnReefer)
        btnWater=findViewById(R.id.btnWater)
        btnImportDischarge=findViewById(R.id.btnImportDischarge)
        btnExportLoad=findViewById(R.id.btnExportLoad)
        btnPilotageModule=findViewById(R.id.btnPilotageModule)
        btnTruckEntryFee=findViewById(R.id.btnTruckEntryFee)
        btnGateModule=findViewById(R.id.btnGateModule)
        etFindGateTicket=findViewById(R.id.etFindGateTicket)
        SignedInUserRole
        findButton=findViewById(R.id.findButton)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor = mPreferences?.edit()
        isSignedIn = mPreferences?.getString("issignedin", "false")
        SignedInId =  mPreferences?.getString("issignedin", "false")
        SignedInLoginId =  mPreferences?.getString("issignedin", "false")
        SignedInSection =  mPreferences?.getString("issignedin", "false")
        SignedInUserRole = mPreferences?.getString("issignedin", "false")
        SignedInUserRole=mPreferences?.getString("SignedInUserRole",null)
//        viewPager = findViewById(R.id.viewpager);
//        myadapter = SlideAdapter(this);
//        viewPager?.setAdapter(myadapter);
//        val imageList = ArrayList<SlideModel>() // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

//        imageList.add(
//            SlideModel(
//                "http://cpatos.gov.bd/pcs/assets/frontPage/fimg/slides/sl-4.jpg",
//                "Chittagong Port Authority",
//                ScaleTypes.CENTER_CROP
//            )
//        )
//        imageList.add(
//            SlideModel(
//                "http://cpatos.gov.bd/pcs/assets/frontPage/fimg/slides/slide7.png",
//                "বিকাশ, রকেট সহ যে কোনো ডেবিট/ক্রেডিট কার্ড দিয়ে বন্দরে গাড়ি প্রবেশের ফি জমা দিয়ে ঘরে বসেই গেট পাস সংগ্রহ করুন। স্ব-শরীরে উপস্থিত হয়ে ফি প্রদান ও গেট পাস সংগ্রহ করা পরিহার করুন। করোনা থেকে বাঁচুন।",
//                ScaleTypes.FIT
//            )
//        )
//        imageList.add(
//            SlideModel(
//                "http://cpatos.gov.bd/pcs/assets/frontPage/fimg/slides/slide8.png",
//                "TOS(Truck Entry is very easy)",
//                ScaleTypes.FIT
//            )
//        )
//
//        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
//        imageSlider.setImageList(imageList, ScaleTypes.FIT)


        // webView.webViewClient = MyWebViewClient(this)
      //  webView.loadUrl("http://cpatos.gov.bd/ctmsdashboard/")


//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Developed By Dreamtouch App.\nWeb: http://dreamtouchapps.com/", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        var navview2: View = navView.getHeaderView(0)
        lusername = navview2.findViewById(R.id.loginUserID)
        loginUserDetails = navview2.findViewById(R.id.loginUserDetails)

        //Toast.makeText(applicationContext,SignedInLoginId,Toast.LENGTH_LONG).show()
        if(isSignedIn.equals("false")){
            lusername?.text = "Guest User"
            inActiveAllService()
            isActiveLogIn(true)
        }else{
            inActiveAllService()
            isActiveLogout(true)
            lusername?.text = "User Name: $SignedInLoginId"

            //ASIF HIDE AND REMOVE NEVIGATION MENU START
            if(SignedInUserRole.equals("64")){ // 1 for admin User  Module
                //Show All Navigation Menu
                isActivePilot(true)
            }
        }
        var versionName: String? = ""
        try {
            versionName = applicationContext.packageManager.getPackageInfo(
                applicationContext.packageName,
                0
            ).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        loginUserDetails?.text = "Version: $versionName"

        //ASIF HIDE AND REMOVE NEVIGATION MENU END
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        btnTosWeb?.setOnClickListener {
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            intent.putExtra("TITLE", "Web View Activity")
            ContextCompat.startActivity(this, intent, Bundle())
        }
        btnTosGatePass?.setOnClickListener {
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            intent.putExtra("TITLE", "TOS Gate Pass")
//            intent.putExtra("NEXT_ACTIVTY", "TosGatePass")
//            ContextCompat.startActivity(this, intent, Bundle())
            if (isSignedIn.equals("false")) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("TITLE", "CPA TOS")
                intent.putExtra("NEXT_ACTIVTY", "Gate Pass")
                ContextCompat.startActivity(this, intent, Bundle())
            } else if(isSignedIn.equals("true") && SignedInUserRole.equals("64")) {
                val intent = Intent(this@MainActivity, GatePassHomeActivity::class.java)
                intent.putExtra("TITLE", "Gate Pass Module")
                ContextCompat.startActivity(this, intent, Bundle())
            }else{
                Toast.makeText( applicationContext,"Access Denied!! Please Login With Authorized User!! ",Toast.LENGTH_LONG).show()
            }



        }

        btnGateIn?.setOnClickListener {
            mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
            preferencesEditor = mPreferences?.edit()
            isSignedIn = mPreferences?.getString("issignedin", "false")
            SignedInId = mPreferences?.getString("SignedInId", "null")
            SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
            SignedInSection = mPreferences?.getString("SignedInSection", "null")
            SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")
            if (isSignedIn.equals("false")) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("TITLE", "Gate In")
                intent.putExtra("NEXT_ACTIVTY", "GATEIN")
                ContextCompat.startActivity(this, intent, Bundle())
            } else if(isSignedIn.equals("true") && SignedInUserRole.equals("67")) {
                val intent = Intent(this@MainActivity, GateInActivity::class.java)
                intent.putExtra("TITLE", "Gate In")
                ContextCompat.startActivity(this, intent, Bundle())
            }else{
                Toast.makeText( applicationContext,"Access Denied!! Please Login With Authorized User!! ",Toast.LENGTH_LONG).show()
            }
        }
        btnGateOut?.setOnClickListener {
            mPreferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
            preferencesEditor = mPreferences?.edit()
            isSignedIn = mPreferences?.getString("issignedin", "false")
            SignedInId = mPreferences?.getString("SignedInId", "null")
            SignedInLoginId = mPreferences?.getString("SignedInLoginId", "null")
            SignedInSection = mPreferences?.getString("SignedInSection", "null")
            SignedInUserRole = mPreferences?.getString("SignedInUserRole", "null")

            if (isSignedIn.equals("false")) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("TITLE", "Gate Out")
                intent.putExtra("NEXT_ACTIVTY", "GATEOUT")
                ContextCompat.startActivity(this, intent, Bundle())
            } else if(isSignedIn.equals("true") && SignedInUserRole.equals("67")) {
                val intent = Intent(this@MainActivity, GateInActivity::class.java)
                intent.putExtra("TITLE", "Gate Out")
                ContextCompat.startActivity(this, intent, Bundle())
            }else{
                Toast.makeText( applicationContext,"Access Denied!! Please Login With Authorized User!! ",Toast.LENGTH_LONG).show()
            }
        }
        btnReefer?.setOnClickListener {
            Toast.makeText(applicationContext, "Coming Soon", Toast.LENGTH_LONG).show()
        }
        btnWater?.setOnClickListener {
           // Toast.makeText(applicationContext, "Coming Soon", Toast.LENGTH_LONG).show()
//            val intent = Intent(this@MainActivity, WaterSupplyActivity::class.java)
//            intent.putExtra("TITLE", "Watter Supply Activity")
//            ContextCompat.startActivity(this, intent, Bundle())
            if (isSignedIn.equals("false")) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("TITLE", "CPA TOS")
                intent.putExtra("NEXT_ACTIVTY", "WaterByMarin")
                ContextCompat.startActivity(this, intent, Bundle())
            }else if(isSignedIn.equals("true") && SignedInUserRole.equals("64")) {
                val intent = Intent(this@MainActivity, WaterSupplyActivity::class.java)
                intent.putExtra("TITLE", "Watter Supply Activity")
                ContextCompat.startActivity(this, intent, Bundle())
            }else if(isSignedIn.equals("true") && SignedInUserRole.equals("79")) {
                val intent = Intent(this@MainActivity, GatePassHomeActivity::class.java)
                intent.putExtra("TITLE", "TOS Gate Pass")
                intent.putExtra("NEXT_ACTIVTY", "TosGatePass")
                ContextCompat.startActivity(this, intent, Bundle())
            }else{
                Toast.makeText( applicationContext,"Access Denied!! Please Login With Authorized User!! ",Toast.LENGTH_LONG).show()
            }
        }
        btnImportDischarge?.setOnClickListener {
            val intent = Intent(this@MainActivity, EdoLandingPageActivity::class.java)
            intent.putExtra("TITLE", "CPA TOS")
            intent.putExtra("NEXT_ACTIVTY", "WaterByMarin")
            ContextCompat.startActivity(this, intent, Bundle())
            Toast.makeText(applicationContext, "Coming Soon", Toast.LENGTH_LONG).show()
        }
        btnExportLoad?.setOnClickListener {
            Toast.makeText(applicationContext, "Coming Soon", Toast.LENGTH_LONG).show()
        }
        btnPilotageModule?.setOnClickListener {
            if (isSignedIn.equals("false")) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("TITLE", "CPA TOS")
                intent.putExtra("NEXT_ACTIVTY", "PilotLandingPage")
                ContextCompat.startActivity(this, intent, Bundle())
            } else if(isSignedIn.equals("true") && SignedInUserRole.equals("64")) {
                val intent = Intent(this@MainActivity, PilotLandingPage::class.java)
                intent.putExtra("TITLE", "Pilotage Module")
                ContextCompat.startActivity(this, intent, Bundle())
            }else{
                Toast.makeText( applicationContext,"Access Denied!! Please Login With Authorized User!! ",Toast.LENGTH_LONG).show()
            }
        }

        btnTruckEntryFee?.setOnClickListener{

            val intent = Intent(this@MainActivity, CPAGateEntryLandingPageActivity::class.java)
            intent.putExtra("TITLE", "Chottagong Port Entry Pass")
            intent.putExtra("NEXT_ACTIVTY", "CPAGateEntryLandingPageActivity")
            intent.putExtra("HUMAN",VEHICALE_ENTRY_FEE)
            intent.putExtra("VEHICLE",HUMAN_ENTRY_FEE)

            ContextCompat.startActivity(this, intent, Bundle())

        }
        btnGateModule?.setOnClickListener{

            if (isSignedIn.equals("false")) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.putExtra("TITLE", "CPA TOS")
                intent.putExtra("NEXT_ACTIVTY", "GATE_MODULE")
                ContextCompat.startActivity(this, intent, Bundle())
            } else if(isSignedIn.equals("true") && SignedInUserRole.equals("67")) {
                val intent = Intent(this@MainActivity, GateLandingActivity::class.java)
                intent.putExtra("TITLE", "Gate Module")
                ContextCompat.startActivity(this, intent, Bundle())
            }else{
                Toast.makeText( applicationContext,"Access Denied!! Please Login With Authorized User!! ",Toast.LENGTH_LONG).show()
            }

        }

        findButton?.setOnClickListener {
            var data =  etFindGateTicket?.text.toString()
            if(data !=""){

               // getScanDataResponse(data)
                val intent = Intent(this@MainActivity, MainWebView::class.java)
                intent.putExtra("TITLE", "GATE PASS")
                intent.putExtra("VISIT_ID", data)
                startActivity(intent)


            }
        }

    }

    private fun inActiveAllService(){
        isActiveDashboard(false)
        isActiveReeferAndWater(false)
        isActiveImportExport(false)
        isActiveAssignment(false)
        isActivePilot(false)
        isActiveMore(false)
        isActiveGate(false)
        isActiveLogout(false)
        isActiveLogIn(false)

    }

    private fun login() {
        var dialog = ProgressDialog.progressDialog(this@MainActivity)
        dialog.show()
        val stringRequest = object : StringRequest(
            Method.POST, EndPoints.URL_GATEDASHBORD_DATAGET,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    if (success.equals("1")) {
                        dialog.dismiss()
                        val total_gatein: String = jsonObject.getString("total_gatein")
                        val total_gateout: String = jsonObject.getString("total_gateout")

                        tvTotalGateIn?.text = "Total Gatein :$total_gatein"
                        tvTotalGateOut?.text = "Total Gateout :$total_gateout"

                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            jsonObject.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        R.string.toast_login_activity_tryagain,
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    dialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        R.string.toast_login_activity_tryagain,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                return params
            }
        }

        //adding request to queue
        val queue = Volley.newRequestQueue(this@MainActivity)
        queue?.add(stringRequest)

    }
    private fun load_primary_data() {
        var dialog = ProgressDialog.progressDialog(this@MainActivity)
        dialog.show()
        //creating volley string request
        val stringRequest = object : StringRequest(
            Method.POST,
            EndPoints.URL_LOAD_INITIAL_DATA,
            Response.Listener<String> { response ->
                try {
                    val jsonObject:JSONObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    //  val message = jsonObject.getString("message")
                    Log.e("Message", jsonObject.toString())
                    if (success.equals("1")) {
                        VEHICALE_ENTRY_FEE =  jsonObject.getString("rate_vehicle")
                        HUMAN_ENTRY_FEE =  jsonObject.getString("rate_human")
                        dialog.dismiss()

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
                val params = java.util.HashMap<String, String>()
                var versionName: String? = ""
                try {
                    versionName = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0).versionName
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                params.put("app_version", versionName.toString())
                params.put("app", "TOS")
                params.put("version", versionName.toString())



                return params
            }
        }
        //adding request to queue
        val queue = Volley.newRequestQueue(this@MainActivity)
        queue?.add(stringRequest)
    }



    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
//         //   super.onBackPressed()
//            var message:String? =null
//
//            if(isSignedIn.equals("true")){
//                message = "Do you want to Logout?"
//            }else{
//                message = "Do you want to exit?"
//            }
            AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.app_name)
                .setMessage( "Do you want to exit?")
                .setPositiveButton("Yes") { _,_ ->

                    preferencesEditor?.putString("issignedin", "false")
                    preferencesEditor?.apply()
                    preferencesEditor?.commit()
                    finish()
                    preferencesEditor?.clear()

                    Toast.makeText(this@MainActivity, "Activity closed", Toast.LENGTH_SHORT).show()
                }.setNegativeButton("No",null).show()


        }
    }





//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                // Handle the camera action
            }
            R.id.nav_gatein -> {
                val intent = Intent(this@MainActivity, GateInActivity::class.java)
                intent.putExtra("TITLE", "Gate In Activity")
                ContextCompat.startActivity(this, intent, Bundle())

            }
            R.id.nav_gateout -> {
                val intent = Intent(this@MainActivity, GateOutActivity::class.java)
                intent.putExtra("TITLE", "Gate Out Activity")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_reefer -> {
                val intent = Intent(this@MainActivity, ReeferActivity::class.java)
                intent.putExtra("TITLE", "Reefer Activity")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_water -> {
                val intent = Intent(this@MainActivity, WaterSupplyActivity::class.java)
                intent.putExtra("TITLE", "Watter Supply Activity")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_exportload -> {
                val intent = Intent(this@MainActivity, ExportContainerLoadActivity::class.java)
                intent.putExtra("TITLE", "Export Load")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_importdischarge -> {
                val intent = Intent(this@MainActivity, ImportContainerDischargeActivity::class.java)
                intent.putExtra("TITLE", "Import Discharge")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_incoming -> {
                if (isSignedIn.equals("false")) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.putExtra("TITLE", "Login")
                    ContextCompat.startActivity(this, intent, Bundle())

                } else {
                    val intent = Intent(this@MainActivity, PilotLandingPage::class.java)
                    intent.putExtra("TITLE", "Pilotage Module")
                    ContextCompat.startActivity(this, intent, Bundle())
                }
            }
            R.id.nav_shifting -> {
                if (isSignedIn.equals("false")) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.putExtra("TITLE", "Login")
                    ContextCompat.startActivity(this, intent, Bundle())

                } else {
                    val intent = Intent(this@MainActivity, PilotLandingPage::class.java)
                    intent.putExtra("TITLE", "Pilotage Module")
                    ContextCompat.startActivity(this, intent, Bundle())
                }
            }
            R.id.nav_outgoing -> {
                if (isSignedIn.equals("false")) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.putExtra("TITLE", "Login")
                    ContextCompat.startActivity(this, intent, Bundle())

                } else {
                    val intent = Intent(this@MainActivity, PilotLandingPage::class.java)
                    intent.putExtra("TITLE", "Pilotage Module")
                    ContextCompat.startActivity(this, intent, Bundle())
                }
            }
            R.id.nav_assignment_by_container -> {
                val intent = Intent(this@MainActivity, AssignmentContainerList::class.java)
                intent.putExtra("TITLE", "Assignment By Container")
                intent.putExtra("ACTIVITY_FOR", "container")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_assignment_special -> {
                val intent = Intent(this@MainActivity, AssignmentContainerList::class.java)
                intent.putExtra("TITLE", "Special Assignment")
                intent.putExtra("ACTIVITY_FOR", "special")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_assignment_all -> {
                val intent = Intent(this@MainActivity, AssignmentContainerList::class.java)
                intent.putExtra("TITLE", "All Assignment")
                intent.putExtra("ACTIVITY_FOR", "all")
                ContextCompat.startActivity(this, intent, Bundle())
            }


            R.id.nav_setting -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                intent.putExtra("TITLE", "Setting")
                ContextCompat.startActivity(this, intent, Bundle())
            }

            R.id.nav_aboutus -> {
//                val intent = Intent(this@MainActivity, AboutUsActivity::class.java)
//                intent.putExtra("TITLE","About Apps")
//                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_helpline -> {
                val intent = Intent(this@MainActivity, HelpLineActivity::class.java)
                intent.putExtra("TITLE", "Help Line")
                ContextCompat.startActivity(this, intent, Bundle())
            }
            R.id.nav_logout -> {
                preferencesEditor?.putString("issignedin", "false")
                preferencesEditor?.apply()
                isActiveLogout(false)
                isActiveLogIn(true)
              //  val intent = Intent(this@MainActivity, LoginActivity::class.java)
               // startActivity(intent)
               // finish()
            }
            R.id.nav_logIn -> {
                preferencesEditor?.putString("issignedin", "false")
                preferencesEditor?.apply()

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                //   finish()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun isActiveDashboard(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_dashboard).isVisible = isActive

    }
    private fun isActiveGate(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_gatein).isVisible = isActive
        nav_Menu.findItem(R.id.nav_gateout).isVisible = isActive
    }
    private fun isActiveReeferAndWater(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_reefer).isVisible = isActive
        nav_Menu.findItem(R.id.nav_water).isVisible = isActive
    }
    private fun isActiveImportExport(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_importdischarge).isVisible = isActive
        nav_Menu.findItem(R.id.nav_exportload).isVisible = isActive
    }
    private fun isActiveAssignment(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_assignment_by_container).isVisible = isActive
        nav_Menu.findItem(R.id.nav_assignment_special).isVisible = isActive
        nav_Menu.findItem(R.id.nav_assignment_all).isVisible = isActive
    }
    private fun isActivePilot(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_incoming).isVisible = isActive
        nav_Menu.findItem(R.id.nav_shifting).isVisible = isActive
        nav_Menu.findItem(R.id.nav_outgoing).isVisible = isActive
    }
    private fun isActiveMore(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_aboutus).isVisible = isActive
        nav_Menu.findItem(R.id.nav_setting).isVisible = isActive
       // nav_Menu.findItem(R.id.nav_helpline).setVisible(false)
    }
    private fun isActiveLogout(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_logout).isVisible = isActive

    }
    private fun isActiveLogIn(isActive: Boolean) {
        val navigationView: NavigationView = findViewById<NavigationView>(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_logIn).isVisible = isActive

    }



}



