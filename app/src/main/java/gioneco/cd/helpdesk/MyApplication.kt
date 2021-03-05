package gioneco.cd.helpdesk

import android.app.Application

/**
 * 应用环境
 *
 * @since V1.0.0 tangbo 初建
 */
class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}