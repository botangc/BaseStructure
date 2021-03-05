package gioneco.cd.helpdesk.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import gioneco.cd.helpdesk.MyApplication

/**
 * 跳转界面
 *
 * @param cls 待跳转界面
 * @param bundle 携带数据
 * @param request 请求码
 * @param isFinish 是否关闭自身
 */
fun <T> Context.jumpToActivity(
    cls: Class<T>,
    bundle: Bundle? = null,
    request: Int = -1,
    isFinish: Boolean = false
) {
    val intent = Intent(this, cls).apply {
        if (bundle != null) putExtras(bundle)
        if (this@jumpToActivity !is Activity) addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    //检查intent是否有效
    if (intent.resolveActivity(packageManager) == null) {
        return
    }
    when {
        request == -1 -> startActivity(intent)
        this is Activity -> startActivityForResult(intent, request)
        else -> {
            startActivity(intent)
            "----- context cannot startActivityForResult, so startActivity automatically -------".logE()
        }
    }
    if (isFinish && this is Activity) {
        this.finish()
    }
}

/**
 * 获取颜色值
 *
 * @param colorRes 颜色资源
 */
fun getMColor(@ColorRes colorRes: Int) = ContextCompat.getColor(MyApplication.instance, colorRes)

/**
 * 获取字符串
 *
 * @param stringRes 字符串资源id
 */
fun getMString(@StringRes stringRes: Int) = MyApplication.instance.getString(stringRes)

/**
 * 获取拼接字符串
 *
 * @param stringRes 字符串资源id
 * @param data 字符串替换内容
 */
fun getMString(@StringRes stringRes: Int, data: String) =
    MyApplication.instance.getString(stringRes, data)