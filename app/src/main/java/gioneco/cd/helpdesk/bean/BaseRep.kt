package gioneco.cd.helpdesk.bean

/**
 * 网络请求基本实体容器类
 *
 * @author tangbo
 */
data class BaseRep<T>(
    val code: Int = 0,
    val message: String?,
    val data: T?
)

/**
 * 接口请求成功code
 */
const val HTTP_SUCCESS = 200

/**
 * 网络异常码
 */
const val CODE_ERROR_NETWORK = 888

/**
 * 数据结果
 */
const val CODE_ERROR_DATA = 666

/**
 * 登录token失效
 */
const val HTTP_TOKEN_OVERDUE = 2000