package com.brave.mvvm.mvvmhelper.binding.command

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/27 17:09
 *
 * ***desc***       ：无参函数
 * @param T 返回结果类型
 */
interface BindingFunction<T> {
    fun call(): T
}