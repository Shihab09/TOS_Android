package bd.gov.cpatos.utils

import android.util.Log
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class IPFinder {
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                       val sAddr: String = addr.hostAddress.uppercase(Locale.ROOT)
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) {
                                if(sAddr.contains("192.168.") || sAddr.contains("10.1.")) {
                                    Log.e("IP", sAddr)
                                    return "http://192.168.16.42/"
                                }else {
                                    Log.e("IP",sAddr)
                                    return "http://122.152.54.179/"

                                }
                            }
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 port suffix
                                return if (delim < 0) sAddr else sAddr.substring(0, delim)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}