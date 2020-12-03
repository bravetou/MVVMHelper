package com.brave.mvvm.mvvmhelper.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 15:38
 *
 * ***desc***       ：常用的Service基
 */
abstract class CommonService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    // 订阅集
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    // 添加订阅
    protected fun addDisposable(mDisposable: Disposable?) {
        if (null != mDisposable) {
            mCompositeDisposable?.add(mDisposable)
        }
    }

    // 销毁所有订阅
    protected fun clearDisposable() {
        mCompositeDisposable?.clear()
    }

    // 销毁
    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
        println("=======================================================")
        println("${this.javaClass.simpleName}")
        println("=======================================================")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var command = super.onStartCommand(intent, flags, startId)
        startCommand(intent, flags, startId)
        return command
    }

    open fun startCommand(intent: Intent?, flags: Int, startId: Int) {
    }
}