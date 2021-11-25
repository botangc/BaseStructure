package gioneco.cd.helpdesk.bean

/**
 * 网络请求基本实体容器类
 *
 * @author tangbo
 */
data class BaseResp<T>(
    val code: Int = 0,
    val message: String,
    val data: T
)