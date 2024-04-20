package bd.gov.cpatos.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import android.annotation.SuppressLint
import android.provider.Settings
class Utility {
//    fun encode(imageUri: Uri): String {
//        val input = activity.getContentResolver().openInputStream(imageUri)
//        val image = BitmapFactory.decodeStream(input , null, null)
//
//        // Encode image to base64 string
//        val baos = ByteArrayOutputStream()
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        var imageBytes = baos.toByteArray()
//        val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
//        return imageString
//    }
//
//    fun decode(imageString: String) {
//
//        // Decode base64 string to image
//        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
//        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//
//        imageview.setImageBitmap(decodedImage)
//    }
@SuppressLint("HardwareIds")
fun getSystemDetail(): String {
    return "Brand: ${Build.BRAND} \n" +
            "Model: ${Build.MODEL} \n" +
            "ID: ${Build.ID} \n" +
            "SDK: ${Build.VERSION.SDK_INT} \n" +
            "Manufacture: ${Build.MANUFACTURER} \n" +
            "Brand: ${Build.BRAND} \n" +
            "User: ${Build.USER} \n" +
            "Type: ${Build.TYPE} \n" +
            "Base: ${Build.VERSION_CODES.BASE} \n" +
            "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
            "Board: ${Build.BOARD} \n" +
            "Host: ${Build.HOST} \n" +
            "FingerPrint: ${Build.FINGERPRINT} \n" +
            "Version Code: ${Build.VERSION.RELEASE}"
}

  private fun checkForInternet(context: Context): Boolean {
     // register activity with the connectivity manager service
     val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
     // if the android version is equal to M
     // or greater we need to use the
     // NetworkCapabilities to check what type of
     // network has the internet connection
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         // Returns a Network object corresponding to
         // the currently active default data network.
         val network = connectivityManager.activeNetwork ?: return false
         // Representation of the capabilities of an active network.
         val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
         return when {
           // Indicates this network uses a Wi-Fi transport,
           // or WiFi has network connectivity
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
           // Indicates this network uses a Cellular transport. or
           // Cellular has network connectivity
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
           // else return false
           else -> false
         }
     }else{
       // if the android version is below M
       @Suppress("DEPRECATION") val networkInfo =
        connectivityManager.activeNetworkInfo ?: return false
       @Suppress("DEPRECATION")
       return networkInfo.isConnected
     }
  }
 }