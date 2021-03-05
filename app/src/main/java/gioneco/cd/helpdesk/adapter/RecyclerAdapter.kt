package gioneco.cd.helpdesk.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView适配器
 * 不同类型的ViewHolder交给对应的适配器处理
 *
 * @since V1.0.0 tangbo 初建
 */
open class RecyclerAdapter<T>(private val needItems: Boolean = false) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 是否已设置代理适配器
     */
    private var hasDelegateAdapter = false

    /**
     * 数据源
     */
    private var items = ArrayList<T>()

    /**
     * 设置数据源
     *
     * @param list 新数据源
     */
    open fun setDataList(list: ArrayList<T>) {
        if (!hasDelegateAdapter) {
            throw Exception("before set data list, must add delegate adapter")
        }
        items = list
        // 更新适配器数据源
        if (needItems) {
            adapters.forEach {
                it.mList = list
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 适配器代理集合
     */
    protected var adapters = ArrayList<IDelegateAdapter<T>>()

    /**
     * 添加不同类型的item对应的适配器
     *
     * @param adapter 适配器
     */
    fun addDelegateAdapter(vararg adapter: IDelegateAdapter<T>) {
        hasDelegateAdapter = true
        adapters.addAll(adapter)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapters[viewType].onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapters[holder.itemViewType].onBindViewHolder(holder, position, items[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (adapters.size > 1) {
            val item = items[position]
            adapters.forEachIndexed { index, adapter ->
                if (adapter.isTheViewType(position, item)) {
                    return index
                }
            }
        }
        return 0
    }
}