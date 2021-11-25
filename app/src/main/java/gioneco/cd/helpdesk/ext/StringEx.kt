package gioneco.cd.helpdesk.ext

import android.view.Gravity
import android.widget.Toast
import gioneco.cd.helpdesk.MyApplication
import java.security.MessageDigest
import java.util.regex.Pattern
import kotlin.experimental.and


/*
 * 字符串相关扩展函数
 *
 * @author tangbo
 */

private var mToast: Toast? = null

/**
 * 本地错误信息弹窗对应码
 */
const val TOAST_LOCAL_CODE = -1

/**
 * 显示吐司
 *
 * @param code 错误码
 */
fun String?.showToast(code: Int? = TOAST_LOCAL_CODE) {
    if (this.isNullOrEmpty()) {
        return
    }
    if (mToast != null) {
        mToast?.cancel()
    }
    mToast = Toast.makeText(MyApplication.instance, this, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
    }
    mToast?.show()
}

/**
 * 验证电话号码
 */
fun String?.verifyPhoneNum(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    val regExp = "^\\d+$"
    val p = Pattern.compile(regExp)
    val m = p.matcher(this)
    return m.matches()
}

/**
 * 字符串MD5处理
 */
fun String?.getMessageDigest(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    try {
        val mdTemp = MessageDigest.getInstance("MD5")
        mdTemp.update(this.toByteArray())
        val md = mdTemp.digest()
        val j = md.size
        val str = CharArray(j * 2)
        var k = 0
        for (i in 0 until j) {
            val byte0 = md[i]
            str[k++] = hexDigits[byte0.toInt().ushr(4) and 0xf]
            str[k++] = hexDigits[(byte0 and 0xf).toInt()]
        }
        return String(str)
    } catch (e: Exception) {
        return ""
    }
}