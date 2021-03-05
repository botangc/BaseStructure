package gioneco.cd.helpdesk.mvvm.view.activity

import android.util.Log
import gioneco.cd.helpdesk.databinding.ActivityMainBinding
import gioneco.cd.helpdesk.mvvm.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun provideBinding() = ActivityMainBinding.inflate(layoutInflater)
    override fun providerViewModel() = MainViewModel::class.java

    override fun initWidget() {
        mRoot.tvHello.text = "哈哈"
        mRoot.tvHello.setOnClickListener {
            Log.e("text", "onClick")
        }
    }
}