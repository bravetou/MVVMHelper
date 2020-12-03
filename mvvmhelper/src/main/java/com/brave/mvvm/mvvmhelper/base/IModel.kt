package com.brave.mvvm.mvvmhelper.base

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/30 11:25
 *
 * ***desc***       ：IModel
 */
interface IModel {
    /**
     * ViewModel销毁时清除Model，
     * 与ViewModel共消亡。
     * Model层同样不能持有长生命周期对象
     */
    fun onCleared()
}