package gioneco.cd.helpdesk.mvvm.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gioneco.cd.helpdesk.http.AutoDisposable

/**
 * 基础VM
 *
 * @author tangbo
 */
open class BaseViewModel: ViewModel() {
    /**
     * 请求统一管理器
     */
    protected val mAutoDisposable = AutoDisposable()

    /**
     * 网络请求的dialog
     */
    val showLoadingDialogWithMsg: MutableLiveData<Pair<Boolean, Int>> by lazy {
        MutableLiveData<Pair<Boolean, Int>>()
    }
    /**
     * 吐司对<错误码, 错误信息>
     */
    val mToastPair: MutableLiveData<Pair<Int, Any>> by lazy { MutableLiveData<Pair<Int, Any>>() }

    /**
     * 绑定view的生命周期
     *
     * @param lifecycle 生命周期
     */
    fun bindLifecycle(lifecycle: Lifecycle) {
        mAutoDisposable.bindTo(lifecycle)
    }

    /**
     * 显示加载弹窗
     *
     * @param isLoading true表示显示，false表示隐藏
     * @param msg 弹窗文案
     */
    fun showLoading(isLoading: Boolean, msg: Int = -1) {
        showLoadingDialogWithMsg.value = Pair(isLoading, msg)
    }

    /**
     * 显示吐司
     */
    fun showToast(code: Int, msg: Any?) {
        if (msg != null) {
            mToastPair.value = Pair(code, msg)
        }
    }

    open fun onDestroy() { }
}
