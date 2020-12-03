package com.brave.mvvm.mvvmhelper.binding.command

import io.reactivex.rxjava3.functions.Function

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/27 17:31
 *
 * ***desc***       ：执行的命令事件转换
 * @param T 需转换的泛型
 * @param R 目标泛型
 */
class ResponseCommand<T, R> {
    private var execute: BindingFunction<R>? = null
    private var function: Function<T, R>? = null
    private var canExecute: BindingFunction<Boolean>? = null

    /**
     * like [BindingCommand],but ResponseCommand can return result when command has executed!
     *
     * @param execute function to execute when event occur.
     */
    constructor(execute: BindingFunction<R>?) {
        this.execute = execute
    }

    constructor(execute: Function<T, R>?) {
        function = execute
    }

    constructor(execute: BindingFunction<R>?, canExecute: BindingFunction<Boolean>?) {
        this.execute = execute
        this.canExecute = canExecute
    }

    constructor(execute: Function<T, R>?, canExecute: BindingFunction<Boolean>?) {
        function = execute
        this.canExecute = canExecute
    }

    fun execute(): R? {
        if (null == execute) return null
        if (!canExecute()) return null
        return execute!!.call()
    }

    private fun canExecute(): Boolean {
        if (null == canExecute) return true
        return canExecute!!.call()
    }

    @Throws(Throwable::class)
    fun execute(parameter: T): R? {
        if (null == function) return null
        if (!canExecute()) return null
        return function!!.apply(parameter)
    }
}