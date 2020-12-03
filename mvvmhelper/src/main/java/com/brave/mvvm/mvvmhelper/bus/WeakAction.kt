package com.brave.mvvm.mvvmhelper.bus

import com.brave.mvvm.mvvmhelper.binding.command.BindingAction
import com.brave.mvvm.mvvmhelper.binding.command.BindingConsumer
import java.lang.ref.WeakReference

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/30 10:40
 *
 * ***desc***       ：WeakBindingAction
 */
class WeakAction<T> {
    var bindingAction: BindingAction? = null
        private set

    private var consumer: BindingConsumer<T>? = null

    val isLive: Boolean
        get() {
            if (reference == null) {
                return false
            }
            return reference!!.get() != null
        }

    val target: Any?
        get() = if (reference != null) {
            reference!!.get()
        } else null

    private var reference: WeakReference<*>?

    constructor(target: Any?, action: BindingAction?) {
        reference = WeakReference(target)
        bindingAction = action
    }

    constructor(target: Any?, consumer: BindingConsumer<T>?) {
        reference = WeakReference(target)
        this.consumer = consumer
    }

    fun execute() {
        if (bindingAction != null && isLive) {
            bindingAction!!.call()
        }
    }

    fun execute(parameter: T) {
        if (consumer != null
            && isLive
        ) {
            consumer!!.call(parameter)
        }
    }

    fun markForDeletion() {
        reference!!.clear()
        reference = null
        bindingAction = null
        consumer = null
    }

    val bindingConsumer: BindingConsumer<*>?
        get() = consumer
}