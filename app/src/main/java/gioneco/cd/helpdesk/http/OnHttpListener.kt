package gioneco.cd.helpdesk.http

import gioneco.cd.helpdesk.bean.CODE_ERROR_DATA
import gioneco.cd.helpdesk.bean.CODE_ERROR_NETWORK

/**
 * 网络请求监听
 *
 * @author tangbo
 */
interface OnHttpListener<T> {

    fun onSuccess(result: T)

    fun onFailure(code: Int, msg: String?)

    /**
     * 错误处理，包括网络错误、服务器错误等
     *
     * @param msg 错误消息，在[HttpTokenInterceptor]中错误消息定义为code
     */
    fun onError(msg: String?) {
        var code = -1
        var errorMsg = "Service exception, please try again later"
        when (msg) {
            "404" -> code = 404
            "500" -> code = 500
            "502" -> code = 502
            "json" -> {
                code = CODE_ERROR_DATA
                errorMsg = "The data exception, please try again later"
            }
            "network" -> {
                code = CODE_ERROR_NETWORK
                errorMsg = "Please check the network"
            }
            else -> errorMsg = "Network exception, please try again later"
        }
        onFailure(code, errorMsg)
    }

    fun onComplete() {}
}