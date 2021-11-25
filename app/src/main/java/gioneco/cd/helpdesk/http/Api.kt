package gioneco.cd.helpdesk.http

import gioneco.cd.helpdesk.bean.BaseResp
import retrofit2.http.GET


/**
 * 网络请求API
 *
 * @author tangbo
 */
interface Api {

    @GET("api/stations")
    suspend fun getStations(): BaseResp<Any>

}