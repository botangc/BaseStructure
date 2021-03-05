package gioneco.cd.helpdesk.utils

import android.app.Activity
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * 图片加载工具Glide
 *
 * @author tangbo
 */
object GlideUtils {

    /**
     * 加载图片
     *
     * @param context 上下文
     * @param url 图片url
     * @param iv 待加载控件
     * @param isCircle true表示加载圆形图片
     * @param isCircle true表示中心截取
     * @param placeHolderId 占位图id
     * @param option 其他选项
     */
    private fun loadImage(
        context: Any, url: String, iv: ImageView,
        isCircle: Boolean = false, isCenterCrop: Boolean = true,
        placeHolderId: Int = -1, option: RequestOptions? = null
    ) {
        when (context) {
            is Fragment -> Glide.with(context)
                .load(url)
                .apply {
                    if (isCircle) circleCrop()
                    if (isCenterCrop) centerCrop()
                    if (placeHolderId > 0) placeholder(placeHolderId)
                    if (option != null) apply(option)
                }
                .into(iv)
            is Activity -> Glide.with(context)
                .load(url)
                .apply {
                    if (isCircle) circleCrop()
                    if (isCenterCrop) centerCrop()
                    if (placeHolderId > 0) placeholder(placeHolderId)
                    if (option != null) apply(option)
                }
                .into(iv)
            else -> throw Exception("the context is not activity or fragment !")
        }
    }

    /**
     * 加载图片
     *
     * @param context 上下文
     * @param url 图片url
     * @param iv 待加载控件
     */
    fun loadImageSimply(context: Any, url: String, iv: ImageView, placeHolderId: Int = -1) {
        loadImage(context, url, iv, placeHolderId = placeHolderId)
    }

    /**
     * 加载圆角图片
     *
     * @param context 上下文
     * @param url 图片url
     * @param iv 待加载控件
     * @param round 圆角
     */
    fun loadImageRound(context: Any, url: String, iv: ImageView, round: Int, holder: Int = -1) {
        loadImage(
            context,
            url,
            iv,
            isCircle = false,
            isCenterCrop = false,
            placeHolderId = holder,
            option = RequestOptions().transform(MultiTransformation(CenterCrop(), RoundedCorners(round)))
        )
    }

    /**
     * 加载圆形图片
     *
     * @param context 上下文
     * @param url 图片url
     * @param iv 待加载控件
     */
    fun loadImageCircle(context: Any, url: String, iv: ImageView, placeHolderId: Int = -1) {
        loadImage(
            context,
            url,
            iv,
            isCircle = true,
            isCenterCrop = false,
            placeHolderId = placeHolderId
        )
    }
}