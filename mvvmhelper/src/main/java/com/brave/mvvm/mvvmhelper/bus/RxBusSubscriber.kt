package com.brave.mvvm.mvvmhelper.bus

import io.reactivex.rxjava3.observers.DisposableObserver

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/30 11:16
 *
 * ***desc***       ：为RxBus使用的Subscriber, 主要提供next事件的try,catch
 */
abstract class RxBusSubscriber<T> : DisposableObserver<T>() {
    override fun onNext(t: T) {
        try {
            onEvent(t)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onComplete() {}
    
    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    protected abstract fun onEvent(t: T)
}