package com.brave.mvvm.mvvmhelper.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 10:48
 *
 * ***desc***       ：RxJava&RxAndroid线程调度工具
 */
object RxUtils {
    /**
     * 用于文件读写，网络请求（线程池）等操作；
     * 用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；
     * 对于普通的计算任务，请使用Schedulers.computation()；
     * Schedulers.io( )默认是一个CachedThreadScheduler，很像一个有线程缓存的新线程调度器
     */
    @JvmStatic
    fun <T>
            Observable<T>.schedulersIO(): Observable<T> {
        return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 用于计算任务，如事件循环或和回调处理，极其占用CPU
     * 不要用于IO操作(IO操作请使用Schedulers.io())；
     * 默认线程数等于处理器的数量
     */
    @JvmStatic
    fun <T>
            Observable<T>.schedulersComputation(): Observable<T> {
        return this.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 开启常规新线程
     * 为每个任务创建一个新线程
     */
    @JvmStatic
    fun <T>
            Observable<T>.schedulersNewThread(): Observable<T> {
        return this.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 当其它排队的任务完成后
     * 在当前线程排队开始执行
     */
    @JvmStatic
    fun <T>
            Observable<T>.schedulersTrampoline(): Observable<T> {
        return this.subscribeOn(Schedulers.trampoline())
            .observeOn(AndroidSchedulers.mainThread())
    }
}