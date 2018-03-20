package com.aoshuotec.iron.cornerstone.devicesKit

import android.content.Context
import android.content.res.Configuration

/**
 * Created by iron on 18-1-3.
 */
class DevicesKit{
    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @return 平板返回 True，手机返回 False
     */
    fun isPad(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}