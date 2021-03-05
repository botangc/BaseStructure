package gioneco.cd.helpdesk.mvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import gioneco.cd.helpdesk.ext.showToast
import gioneco.cd.helpdesk.mvvm.viewmodel.BaseViewModel
import gioneco.cd.helpdesk.widget.LoadingDialog

/**
 * 基础Fragment
 *
 * @author tangbo
 */
abstract class BaseFragment<T : ViewBinding, VM : BaseViewModel> : Fragment() {
    /**
     * 加载数据
     */
    abstract fun loadData()

    /**
     * 提供特定的ViewBinding
     */
    abstract fun provideBinding(inflater: LayoutInflater, group: ViewGroup?): T

    /**
     * 页面基础Binding类
     */
    private var mBinding: T? = null

    /**
     * 非空页面基础Binding类
     */
    protected val mRoot get() = mBinding ?: throw Exception("method provideBinding() had something error")

    /**
     * 绑定的VM
     */
    protected var mViewModel: VM? = null

    /**
     * 等待窗
     */
    private var mLoadingDialog: LoadingDialog? = null

    /**
     * 是否初始化View完成
     */
    protected var isInitView = false

    /**
     * 是否需要加载数据，默认需要
     */
    private var needLoadData = true

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, bundle: Bundle?): View {
        mBinding = provideBinding(inflater, group)
        return mRoot.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initView()
        subscribeUi()
    }

    /**
     * 初始化VM
     */
    private fun initViewModel() {
        providerViewModel()?.let {
            mViewModel = ViewModelProvider(this).get(it).apply {
                bindLifecycle(this@BaseFragment.lifecycle)
            }
        }
    }

    /**
     * 初始化视图
     */
    open fun initView() {}

    /**M
     * 提供继承于[ViewModel]的子类
     */
    open fun providerViewModel(): Class<VM>? = null

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
     * 懒加载，利用[androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT]
     */
    override fun onResume() {
        super.onResume()
        initData()
    }

    /**
     * 初始化数据（懒加载）
     */
    private fun initData() {
        if (isInitView && needLoadData) {
            loadData()
            needLoadData = false
        }
    }

    /**
     * 订阅LiveData
     */
    @CallSuper
    open fun subscribeUi() {
        mViewModel?.apply {
            //吐司
            mToastPair.observe(viewLifecycleOwner, Observer {
                when (it.second) {
                    is Int -> getString(it.second as Int)
                    is String -> it.second as String
                    else -> it.second.toString()
                }.showToast(it.first)
            })
            //等待窗
            showLoadingDialogWithMsg.observe(viewLifecycleOwner, Observer {
                if (mLoadingDialog == null) {
                    mLoadingDialog = activity?.let { context -> LoadingDialog(context) }
                }
                if (it.first) {
                    mLoadingDialog?.showWithText(it.second)
                } else {
                    mLoadingDialog?.dismiss()
                }
            })
        }
    }

    override fun onDestroy() {
        mViewModel?.onDestroy()
        mLoadingDialog?.dismiss()
        super.onDestroy()
    }
}