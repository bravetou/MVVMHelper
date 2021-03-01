package com.brave.mvvm.mvvmhelper.binding.adapter.view

import android.view.View
import androidx.databinding.BindingAdapter
import com.brave.mvvm.mvvmhelper.binding.command.BindingCommand
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import java.util.concurrent.TimeUnit

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 17:38
 *
 * ***desc***       ：View的绑定配置
 */
object ViewAdapter {
    // 防重复点击间隔(秒)
    private const val CLICK_INTERVALS = 1L

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick 事件绑定
     * onClickCommand 绑定的命令
     * isThrottleFirst 是否开启防止过快点击
     */
    @JvmStatic
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    fun onClickCommand(
        view: View?,
        clickCommand: BindingCommand<*>?,
        isThrottleFirst: Boolean = true,
    ) {
        if (null == view) return
        if (null == clickCommand) return
        var clicks = view.clicks()
        if (isThrottleFirst) {
            clicks.throttleFirst(CLICK_INTERVALS, TimeUnit.SECONDS)
        }
        clicks.subscribe {
            clickCommand.execute()
        }
    }

    /**
     * view的onLongClick事件绑定
     */
    @JvmStatic
    @BindingAdapter(value = ["onLongClickCommand"], requireAll = true)
    fun onLongClickCommand(view: View?, clickCommand: BindingCommand<*>?) {
        if (null == view) return
        if (null == clickCommand) return
        view.longClicks()
            .subscribe {
                clickCommand.execute()
            }
    }

    /**
     * view的显示隐藏
     */
    @JvmStatic
    @BindingAdapter(value = ["isVisible"], requireAll = true)
    fun isVisible(view: View?, isVisible: Boolean) {
        if (null == view) return
        view.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    /**
     * view的选中
     */
    @JvmStatic
    @BindingAdapter(value = ["isSelected"], requireAll = true)
    fun isSelected(view: View?, isSelected: Boolean) {
        if (null == view) return
        view.isSelected = isSelected
    }

    /**
     * view的隐藏显示
     */
    @JvmStatic
    @BindingAdapter(value = ["isGone"], requireAll = true)
    fun isGone(view: View?, isGone: Boolean) {
        if (null == view) return
        view.visibility = if (isGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    /**
     * view是否需要获取焦点
     */
    @JvmStatic
    @BindingAdapter(value = ["requestFocus"], requireAll = true)
    fun requestFocusCommand(view: View?, requestFocus: Boolean) {
        if (null == view) return
        if (!view.isFocusableInTouchMode) return
        if (requestFocus) {
            view.requestFocus()
        } else {
            view.clearFocus()
        }
    }

    /**
     * view的焦点开关
     */
    @JvmStatic
    @BindingAdapter(value = ["isOpenFocusable"], requireAll = true)
    fun isOpenFocusableCommand(view: View?, isOpenFocusable: Boolean) {
        if (null == view) return
        if (isOpenFocusable) {
            view.isFocusable = true
            view.isFocusableInTouchMode = true
        } else {
            view.isFocusable = false
            view.isFocusableInTouchMode = false
        }
    }

    /**
     * view的焦点发生变化的事件绑定
     */
    @JvmStatic
    @BindingAdapter(value = ["onFocusChangeCommand"], requireAll = true)
    fun onFocusChangeCommand(view: View?, onFocusChangeCommand: BindingCommand<Boolean?>?) {
        if (null == view) return
        if (null == onFocusChangeCommand) return
        view.setOnFocusChangeListener { _, hasFocus ->
            onFocusChangeCommand.execute(hasFocus)
        }
    }
}