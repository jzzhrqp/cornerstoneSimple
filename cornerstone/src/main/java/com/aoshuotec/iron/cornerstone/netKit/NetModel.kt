package com.aoshuotec.iron.cornerstone.netKit

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by iron on 18-1-3.
 */
class NetModel {
    //判断网络是否可用
    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

}