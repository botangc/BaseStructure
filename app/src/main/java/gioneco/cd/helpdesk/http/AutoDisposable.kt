package gioneco.cd.helpdesk.http

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import gioneco.cd.helpdesk.ext.logE
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 统一自动关闭请求，当绑定的view在onDestroy时
 *
 * @author tangbo
 */
class AutoDisposable: LifecycleObserver {
    /**
     * rxjava关闭器集合
     */
    private lateinit var mComposeDispose: CompositeDisposable

    /**
     * 绑定到view
     *
     * @param lifecycle view的生命周期
     */
    fun bindTo(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
        mComposeDispose = CompositeDisposable()
    }

    /**
     * 添加请求的关闭器
     *
     * @param disposable disposable
     */
    fun add(disposable: Disposable) {
        if (::mComposeDispose.isInitialized) {
            mComposeDispose.add(disposable)
        } else {
            "must bind AutoDisposable to a Lifecycle first".logE()
        }
    }

    /**
     * 当绑定的view在onDestroy时回调此方法
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mComposeDispose.dispose()
    }
}