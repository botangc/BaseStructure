package gioneco.cd.helpdesk.ext

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonSyntaxException
import gioneco.cd.helpdesk.bean.BaseRep
import gioneco.cd.helpdesk.bean.CODE_ERROR_DATA
import gioneco.cd.helpdesk.bean.HTTP_SUCCESS
import gioneco.cd.helpdesk.bean.HTTP_TOKEN_OVERDUE
import gioneco.cd.helpdesk.http.AutoDisposable
import gioneco.cd.helpdesk.http.OnHttpListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.UnknownHostException

/**
 * 线程转换工具
 *
 * @author tangbo
 */

/**
 * Rx Observable线程转换
 */
fun <T> Observable<T>.transformThread(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

/**
 * 网络请求一般情况订阅回调，含超时判断
 *
 * @param listener 结果监听器
 * @param loginOverTime 超时监控
 */
fun <T> Observable<BaseRep<T>>.subscribeUsual(
    listener: OnHttpListener<T>,
    loginOverTime: MutableLiveData<Int>?
) : Disposable =
    this.subscribe(
        {
            when {
                it.code == HTTP_SUCCESS ->
                    if (it.data == null) listener.onFailure(CODE_ERROR_DATA, "data must not be empty")
                    else listener.onSuccess(it.data)
                // 登录token失效
                it.code == HTTP_TOKEN_OVERDUE && loginOverTime != null -> loginOverTime.value = it.code
                else -> listener.onFailure(it.code, it.message)
            }
            listener.onComplete()
        },
        {
            "$it".logE()
            listener.onError(when (it) {
                // 数据结构解析异常
                is JsonSyntaxException -> "json"
                // 没有网络
                is UnknownHostException -> "network"
                else -> it.message
            })
            listener.onComplete()
        }
    )

/**
 * 网络请求一般情况订阅回调
 *
 * @param listener 结果监听器
 */
fun <T> Observable<BaseRep<T>>.subscribeUsual(listener: OnHttpListener<T>) =
    subscribeUsual(listener, null)

/**
 * 将请求放入统一管理器中
 */
fun Disposable.addTo(autoDisposable: AutoDisposable) = autoDisposable.add(this)