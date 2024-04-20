package bd.gov.cpatos.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import bd.gov.cpatos.pilot.activities.activity
import bd.gov.cpatos.signinsignup.LoginActivity

class UserSessionManager() {

    private lateinit var _context: Context
    private val PREFS_FILENAME = "bd.gov.cpatos.user_data"
    private var rPreferences: SharedPreferences? = null
    private var preferencesEditor: Editor? = null
    private var issignedin: String? = null
    private var SignedInId: String? = null
    private var SignedInLoginId: String? = null
    private var SignedInSection: String? = null
    private var SignedInUserRole: String? = null
    private var user = ""
    private var password = ""
    private var MODE_PRIVATE = 0

//    companion object {
//        fun getUserPreferenceData() {
//            rPreferences = activity.getSharedPreferences(PREFS_FILENAME,MODE_PRIVATE)
//            preferencesEditor =rPreferences?.edit()
//            issignedin =rPreferences?.getString("issignedin", "false")
//            SignedInId = rPreferences?.getString("SignedInId", "null")
//            SignedInLoginId = rPreferences?.getString("SignedInLoginId", "null")
//            SignedInSection = rPreferences?.getString("SignedInSection", "null")
//            SignedInUserRole = rPreferences?.getString("SignedInUserRole", "null")
//        }
//    }

    constructor(_context: Context) : this() {
        this._context = _context
        rPreferences= _context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        preferencesEditor=rPreferences?.edit()
    }





    fun createUserLoginSession(user:String, password:String){
       preferencesEditor?.putString("user","devpilot")
       preferencesEditor?.putString(password,"pilotctms123")
       preferencesEditor?.putBoolean(issignedin.toString(),true)
       preferencesEditor?.commit()
   }

//    fun checkLogin(): Boolean {
//        if(issignedin){
//            val i= Intent(_context, LoginActivity::class.java)
//            _context.startActivity(i)
//            return false
//        }
//
//        return true
//    }


        fun getUserDetails(): HashMap<String, String>
        {
            var user= HashMap<String, String>()
            user.put("Name:", rPreferences?.getString("User",null).toString())
            user.put("Password:",rPreferences?.getString("Password",null).toString())
            user.put("SignedInId:", rPreferences?.getString("SignedInId",null).toString())
            user.put("SignedInLoginId:", rPreferences?.getString("SignedInLoginId",null).toString())
            user.put("SignedInSection:", rPreferences?.getString("SignedInSection",null).toString())
            user.put("SignedInUserRole:", rPreferences?.getString("SignedInUserRole",null).toString())
            user.put("issignedin:", rPreferences?.getString("issignedin",null).toString())

        return user
    }



    fun logoutUser(){
        preferencesEditor?.clear()
        preferencesEditor?.commit()
        val i=Intent(_context,LoginActivity::class.java)
        _context.startActivity(i)
    }

    public fun issignedin(): Boolean {
        return rPreferences!!.getBoolean("issignedin",false)
    }

    companion object {
        fun getUserPreferenceData() {
            TODO("Not yet implemented")
        }
    }


}
