package com.brave.mvvm.mvvmhelper.binding.command

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/27 17:10
 *
 * ***desc***       ：一个泛型参数操作
 * @param T 泛型参数类型
 */
interface BindingConsumer<T> {
    fun call(t: T)
}