package gioneco.cd.helpdesk.http

import android.os.Build
import gioneco.cd.helpdesk.BuildConfig
import gioneco.cd.helpdesk.ext.logE
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义okHttp日志信息拦截器
 * 打印信息缺失参数和Header，暂时先不用打印
 *
 * @author tangbo
 */
class HttpTokenInterceptor : Interceptor {
    /**
     * 系统版本
     */
    private val sysVersion = Build.DISPLAY

    /**
     * 手机型号
     */
    private val vendor = Build.MODEL

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //1、可以重建所需请求，如添加token
        val newRequest = request
            .newBuilder()
            .addHeader("system", "Android")
            .addHeader("sys-version", sysVersion)
            .addHeader("version", BuildConfig.VERSION_CODE.toString())
            .addHeader("vendor", vendor)
            .build()

//        //2、请求前--打印请求信息
//        var t1 = 0L
//        if (LOG_DEBUG) {
//            t1 = System.currentTimeMillis()
//            String.format(
//                "Sending request %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()
//            ).logI()
//        }

        //3、网络请求
        val response = chain.proceed(newRequest)

//        //4、网络响应后--打印响应信息
//        //注意在release版中要注释
//        if (LOG_DEBUG) {
//            "Received response for ${response.request().url()} in ${System.currentTimeMillis() - t1}ms".logI()
//            //打印相应内容
//            response.body()?.source()?.apply {
//                request(Long.MAX_VALUE)
//            }?.buffer?.clone()?.readString(
//                response.body()?.contentType()?.charset(charset) ?: charset
//            ).logI()
//        }

        //5、成功则正常流程；非成功返回则抛异常，由rxJava处理
        if (response.isSuccessful) {
            return response
        } else {
            val code = response.code
            "errorCode = $code".logE()
            throw IOException("$code")
        }
    }
}