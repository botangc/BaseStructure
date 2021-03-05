package gioneco.cd.helpdesk.widget

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import gioneco.cd.helpdesk.R

/**
 * 加载数据显示的dialog
 */
class LoadingDialog(context: Context) : Dialog(context, R.style.Theme_Light_NoTitle_Dialog) {
    /**
     * 自定义等待窗
     */
    private var mContentView: View = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
    /**
     * 等待窗文案
     */
    private val mMsg: TextView by lazy { mContentView.findViewById<TextView>(R.id.loading_msg) }

    init {
        setContentView(mContentView)
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
    }

    /**
     * 显示弹窗
     *
     * @param msg 显示文案，为-1时显示默认
     */
    fun showWithText(msg: Int) {
        if (msg != -1) {
            mMsg.setText(msg)
        }
        show()
    }
}