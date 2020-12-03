package com.brave.mvvm.mvvmhelper.binding.adapter.image

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 18:04
 *
 * ***desc***       ：ImageView的绑定配置
 */
object ViewAdapter {
    @JvmStatic
    @BindingAdapter(value = ["url", "placeholder"], requireAll = false)
    fun setImageUrl(imageView: ImageView?, url: String?, placeholder: Int?) {
        if (null == imageView) return
        // 使用Glide框架加载图片
        if (null == placeholder) {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions()
                    .placeholder(placeholder))
                .into(imageView)
        }
    }
}