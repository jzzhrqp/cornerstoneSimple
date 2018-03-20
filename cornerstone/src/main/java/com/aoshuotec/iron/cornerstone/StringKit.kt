package com.aoshuotec.iron.cornerstone

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Created by iron on 17-12-27.
 */

class StringKit{
    /**
     * 将包含有unicode的字符串转为含中文的字符串
     * @param asciicode
     * @return
     */
    fun ascii2native(asciicode: String): String {
        val asciis = asciicode.split("\\\\u".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var nativeValue = asciis[0]
        try {
            for (i in 1 until asciis.size) {
                val code = asciis[i]
                nativeValue += Integer.parseInt(code.substring(0, 4), 16).toChar()
                if (code.length > 4) {
                    nativeValue += code.substring(4, code.length)
                }
            }
        } catch (e: NumberFormatException) {
            return asciicode
        }

        return nativeValue
    }

    /**
     * 去掉字符串中间的空格
     * @param str
     * @return
     */
    fun replaceBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }

    /**
     * 去掉字符串句尾的标点符号和字符串两头的空格
     * @param string
     * @return
     */
    fun trem(string: String): String {
        var string = string
        if (TextUtils.isEmpty(string)) {
            return ""
        }
        string = string.trim { it <= ' ' }
        val pattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~!@#￥%……& amp;*()——+|{}【】‘；：”“’。,、？]*$"
        val p = Pattern.compile(pattern)
        val matcher = p.matcher(string)
        string = matcher.replaceAll("")
        return string
    }
}
