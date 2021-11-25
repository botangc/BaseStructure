package gioneco.cd.helpdesk.mvvm.viewmodel

import gioneco.cd.helpdesk.http.HttpRepository

/**
 * 主页面VM
 *
 * @since V1.0.0 tangbo 初建
 */
class MainViewModel : BaseViewModel() {

    fun test() {
        requestAPI(
            {
                HttpRepository.instance.apiClass.getStations()
            },
            {

            }
        )
    }
}