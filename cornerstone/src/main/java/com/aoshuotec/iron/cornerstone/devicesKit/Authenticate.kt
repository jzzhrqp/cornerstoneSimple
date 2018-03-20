package com.aoshuotec.iron.cornerstone.devicesKit

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.io.*
import java.net.NetworkInterface
import java.util.*

/**
 * Created by iron on 17-12-27.
 */
class Authenticate {

    private val marshmallowMacAddress = "02:00:00:00:00:00"
    private val fileAddressMac = "/sys/class/net/wlan0/address"

    /**
     * 获取MAC地址
     */
   public fun getAdresseMAC(context: Context): String? {
        val wifiMan = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInf = wifiMan.connectionInfo

        if (wifiInf != null && marshmallowMacAddress == wifiInf.macAddress) {
            var result: String? = null
            try {
                result = getAdressMacByInterface()
                if (result != null) {
                    return result
                } else {
                    result = getAddressMacByFile(wifiMan)
                    return result
                }
            } catch (e: IOException) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC")
            } catch (e: Exception) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ")
            }

        } else {
            return if (wifiInf != null && wifiInf.macAddress != null) {
                wifiInf.macAddress
            } else {
                ""
            }
        }
        return marshmallowMacAddress
    }

    private fun getAdressMacByInterface(): String? {
        try {
            val all = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (nif.name.equals("wlan0", ignoreCase = true)) {
                    val macBytes = nif.hardwareAddress ?: return ""

                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02X:", b))
                    }

                    if (res1.isNotEmpty()) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            }

        } catch (e: Exception) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ")
        }

        return null
    }

    @Throws(Exception::class)
    private fun getAddressMacByFile(wifiMan: WifiManager): String {
        val ret: String
        val wifiState = wifiMan.wifiState

        wifiMan.isWifiEnabled = true
        val fl = File(fileAddressMac)
        val fin = FileInputStream(fl)
        ret = crunchifyGetStringFromStream(fin)
        fin.close()

        val enabled = WifiManager.WIFI_STATE_ENABLED == wifiState
        wifiMan.isWifiEnabled = enabled
        return ret
    }

    @Throws(IOException::class)
    private fun crunchifyGetStringFromStream(crunchifyStream: InputStream?): String {
        if (crunchifyStream != null) {
            val crunchifyWriter = StringWriter()

            val crunchifyBuffer = CharArray(2048)
            try {
                val crunchifyReader = BufferedReader(InputStreamReader(crunchifyStream, "UTF-8"))
                var counter=crunchifyReader.read(crunchifyBuffer)
                while (counter != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter)
                    counter=crunchifyReader.read(crunchifyBuffer)
                }
            } finally {
                crunchifyStream.close()
            }
            return crunchifyWriter.toString()
        } else {
            return "No Contents"
        }
    }

}