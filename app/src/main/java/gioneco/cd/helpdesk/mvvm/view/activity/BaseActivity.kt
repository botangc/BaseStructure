package gioneco.cd.helpdesk.mvvm.view.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import gioneco.cd.helpdesk.ext.showToast
import gioneco.cd.helpdesk.mvvm.viewmodel.BaseViewModel
import gioneco.cd.helpdesk.widget.DialogLoading

/**
 * 使用ViewBinding的基础界面
 *
 * @since V1.0.0 tangbo 初建
 */
abstract class BaseActivity<T : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    /**
     * 提供特定的ViewBinding
     */
    abstract fun provideBinding(): T

    /**
     * 页面基础Binding类
     */
    private var mBinding: T? = null

    /**
     * 关联的ViewModel
     */
    protected var mViewModel: VM? = null

    /**
     * 等待窗
     */
    private val mDialogLoading: DialogLoading by lazy {
        DialogLoading(this)
    }

    /**
     * 默认字体大小
     */
    private val mDefaultFontScale = 1f

    /**
     * 非空页面基础Binding类
     */
    protected val mRoot get() = mBinding ?: throw Exception("method provideBinding() had something error")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = provideBinding()
        setContentView(mRoot.root)

        initVm()
        initWidget()
        initData()
        subscribeUi()
    }

    /**
     * 初始化视图
     */
    open fun initWidget() {}

    /**
     * 初始化数据
     */
    open fun initData() {}

    /**
     * 获取关联的ViewModel
     */
    open fun providerViewModel(): Class<VM>? = null

    /**
     * 初始化VM
     */
    private fun initVm() {
        providerViewModel()?.let {
            //便于configuration change等导致的复用，此方法在创建对象后存在activity相关的HashMap中
            mViewModel = ViewModelProvider(this).get(it).apply {
                bindLifecycle(this@BaseActivity.lifecycle)
            }
        }
    }

    /**
     * 设置点击监听
     *
     * @param views 控件
     * @param listener 监听器
     */
    fun setOnClickListener(vararg views: View, listener: View.OnClickListener) {
        for (v in views) {
            v.setOnClickListener(listener)
        }
    }

    /**
     * 订阅LiveData
     */
    @CallSuper
    open fun subscribeUi() {
        mViewModel?.apply {
            //吐司
            mToastPair.observe(this@BaseActivity, Observer {
                when (it.second) {
                    is Int -> getString(it.second as Int)
                    is String -> it.second as String
                    else -> it.second.toString()
                }.showToast(it.first)
            })
            //等待窗
            showLoadingDialogWithMsg.observe(this@BaseActivity, Observer {
                if (it.first) {
                    mDialogLoading.showWithText(it.second)
                } else {
                    mDialogLoading.dismiss()
                }
            })
        }
    }

    override fun onDestroy() {
        mViewModel?.onDestroy()
        mDialogLoading.dismiss()
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //运行中关闭权限，导致app重构，但会出现控件空指针，则直接关闭app
        finish()
    }
}