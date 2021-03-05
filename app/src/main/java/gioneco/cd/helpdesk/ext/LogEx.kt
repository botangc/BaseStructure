package gioneco.cd.helpdesk.ext

/**
 * 日志输出工具
 *
 * @author tangbo
 */

fun <T> T?.logE(tag: String?) {
    if (!LOG_DEBUG || this == null) {
        return
    }
    android.util.Log.e(tag ?: "metro", this.toString())
}

fun <T> T?.logE() {
    this.logE("metro")
}

fun <T> T?.logI(tag: String?) {
    if (!LOG_DEBUG || this == null) {
        return
    }
    android.util.Log.i(tag ?: "metro", this.toString())
}

fun <T> T?.logI() {
    this.logI("metro")
}

/**
 * 日志调试开关
 */
const val LOG_DEBUG = true