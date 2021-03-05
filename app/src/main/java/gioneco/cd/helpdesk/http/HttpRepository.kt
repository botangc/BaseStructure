package gioneco.cd.helpdesk.http

import com.google.gson.GsonBuilder
import gioneco.cd.helpdesk.BuildConfig
import gioneco.cd.helpdesk.ext.LOG_DEBUG
import gioneco.cd.helpdesk.ext.logI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络请求仓库
 */
class HttpRepository private constructor() {
    /**
     * 超时时长
     */
    private val timeout = 8L
    /**
     * 网络请求OkHttpClient
     */
    private val mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
        //log 拦截器，添加统一Header
        .addInterceptor(HttpTokenInterceptor())
        .apply {
            if (LOG_DEBUG) addInterceptor(
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
                    override fun log(message: String) {
                        message.logI()
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .build()

    private val mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.HOST)
        .client(mOkHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .build()

    private val api: Api by lazy { mRetrofit.create(Api::class.java) }

    val apiClass: Api get() = api

    /**
     * 单例
     */
    companion object {
        val instance = EHttpClientHolder.instance
    }

    private object EHttpClientHolder {
        val instance = HttpRepository()
    }
}