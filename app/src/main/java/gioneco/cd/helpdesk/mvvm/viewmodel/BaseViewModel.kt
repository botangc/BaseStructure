package gioneco.cd.helpdesk.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gioneco.cd.helpdesk.R
import gioneco.cd.helpdesk.bean.*
import gioneco.cd.helpdesk.ext.TOAST_LOCAL_CODE
import gioneco.cd.helpdesk.ext.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException

/**
 * 基础VM
 *
 * @author tangbo
 */
open class BaseViewModel : ViewModel() {

    /**
     * 网络请求的dialog
     */
    val mDialogLoadingPair = MutableLiveData<Pair<Boolean, Int>>()

    /**
     * 吐司对<错误码, 错误信息>
     */
    val mToastPair = MutableLiveData<Pair<Int, Any>>()

    /**
     * 协程执行超时时间
     */
    private val mTimeout = 5000L

    /**
     * 错误信息
     */
    val mError = MutableLiveData<Int>()

    /**
     * 身份验证令牌失效，需单独处理，如清除缓存用户信息等
     */
    val mErrorToken = MutableLiveData<Boolean>()

    /**
     * 显示加载弹窗
     *
     * @param isLoading true表示显示，false表示隐藏
     * @param msg 弹窗文案
     */
    fun showLoading(isLoading: Boolean, msg: Int = -1) {
        mDialogLoadingPair.value = Pair(isLoading, msg)
    }

    /**
     * 显示吐司
     */
    fun showToast(msg: Any?, code: Int = TOAST_LOCAL_CODE) {
        if (msg != null) {
            mToastPair.value = Pair(code, msg)
        }
    }

    /**
     * 请求网络接口
     *
     * @param block 待执行的网络请求
     * @param onSuccess 请求成功结果
     * @param isShowLoading 是否显示等待窗
     * @param loadingResId 等待窗显示文案
     */
    fun <T> requestAPI(
        block: suspend CoroutineScope.() -> BaseResp<T>,
        onSuccess: (T) -> Unit,
        isShowLoading: Boolean = true,
        loadingResId: Int = -1
    ) {
        viewModelScope.launch {
            if (isShowLoading) {
                // 显示等待窗
                showLoading(true, loadingResId)
            }
            try {
                // 执行网络请求
                val rep = withTimeout(mTimeout) {
                    block()
                }
                // 处理返回结果
                when (rep.code) {
                    HTTP_SUCCESS -> onSuccess(rep.data)
                    HTTP_TOKEN -> mErrorToken.value = true
                    else -> {
                        mError.value = rep.code
                        showToast(rep.message, rep.code)
                    }
                }
            } catch (e: Exception) {
                // 添加统一日志输出
                "requestAPI exception = $e".logE()
                // 网络请求异常，仅单独处理没网，错误文案为本地固定文案
                mError.value = if (e is UnknownHostException) HTTP_NETWORK else HTTP_OTHER
                showToast(if (e is UnknownHostException) R.string.error_network else R.string.error_server)
            } finally {
                if (isShowLoading) {
                    // 隐藏等待窗
                    showLoading(false)
                }
            }
        }
    }
}
