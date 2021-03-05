package gioneco.cd.helpdesk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gioneco.cd.helpdesk.MyApplication

/**
 * 适配器代理接口
 *
 * @since V1.0.0 tangbo 初建
 */
abstract class IDelegateAdapter<T> {

    /**
     * item视图解析器
     */
    protected val mInflater: LayoutInflater = LayoutInflater.from(MyApplication.instance)

    /**
     * 数据源
     */
    var mList: ArrayList<T>? = null

    /**
     * 是否为匹配的类型
     */
    abstract fun isTheViewType(position: Int, t: T): Boolean

    /**
     * 创建ViewHolder
     *
     * @param parent 父视图
     * @param viewType 视图类型
     */
    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 绑定ViewHolder
     *
     * @param holder 复用的Holder
     * @param position 位置
     * @param t 对应的数据Bean
     */
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, t: T)

    /**
     * 获取上个item
     *
     * @param position 位置
     */
    fun getLastItem(position: Int): T? {
        val lastPosition = position - 1
        if (0 <= lastPosition && lastPosition < mList?.size ?: -1) {
            return mList?.get(lastPosition)
        }
        return null
    }


    /**
     * 获取下个item
     *
     * @param position 位置
     */
    fun getNextItem(position: Int): T? {
        val nextPosition = position + 1
        if (0 <= nextPosition && nextPosition < mList?.size ?: -1) {
            return mList?.get(nextPosition)
        }
        return null
    }
}