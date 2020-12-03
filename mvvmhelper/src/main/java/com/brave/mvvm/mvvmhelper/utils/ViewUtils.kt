package com.brave.mvvm.mvvmhelper.utils

import android.view.View
import com.brave.mvvm.mvvmhelper.utils.RxUtils.schedulersIO
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/3 10:38
 *
 * ***desc***       ：View工具
 */
object ViewUtils {
    /**
     * View不占位隐藏
     */
    @JvmStatic
    fun View.gone() {
        if (this.visibility != View.GONE) {
            this.visibility = View.GONE
        }
    }

    /**
     * View显示
     */
    @JvmStatic
    fun View.visible() {
        if (this.visibility != View.VISIBLE) {
            this.visibility = View.VISIBLE
        }
    }

    /**
     * View占位隐藏
     */
    @JvmStatic
    fun View.invisible() {
        if (this.visibility != View.INVISIBLE) {
            this.visibility = View.INVISIBLE
        }
    }

    /**
     * 取消焦点
     */
    @JvmOverloads
    @JvmStatic
    fun View.cancelNextFocus(
        left: Boolean = true,
        up: Boolean = true,
        right: Boolean = true,
        down: Boolean = true,
    ) {
        if (null == this.id) return
        if (left) this.nextFocusLeftId = this.id
        if (up) this.nextFocusUpId = this.id
        if (right) this.nextFocusRightId = this.id
        if (down) this.nextFocusDownId = this.id
    }

    /**
     * 打开焦点
     */
    @JvmStatic
    fun View.openFocusable() {
        this.isFocusable = true
        this.isFocusableInTouchMode = true
    }

    /**
     * 关闭焦点
     */
    @JvmStatic
    fun View.closeFocusable() {
        this.isFocusable = false
        this.isFocusableInTouchMode = false
    }

    /**
     * View防止重复点击事件
     * @param duration 防止多次点击间隔时间（ms）
     * @param consumer 消费者
     */
    @JvmStatic
    @JvmOverloads
    fun View.clicks(
        consumer: Consumer<Any>?,
        duration: Long = 1000L,
    ): Disposable {
        return if (null == consumer) {
            this.clicks()
                .throttleFirst(duration, TimeUnit.MILLISECONDS)
                .schedulersIO()
                .subscribe()
        } else {
            this.clicks()
                .throttleFirst(duration, TimeUnit.MILLISECONDS)
                .schedulersIO()
                .subscribe(consumer)
        }
    }
}