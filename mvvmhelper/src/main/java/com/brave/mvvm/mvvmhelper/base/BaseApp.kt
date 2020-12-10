package com.brave.mvvm.mvvmhelper.base

import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import kotlin.system.exitProcess

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/27 16:33
 *
 * ***desc***       ：App基类
 */
abstract class BaseApp : Application() {
    // 伴生函数
    companion object {
        @JvmStatic
        @Volatile
        private var app: BaseApp? = null

        @JvmStatic
        val default: BaseApp
            get() {
                if (null == app) {
                    throw RuntimeException("The application is not initialized")
                }
                return app!!
            }
    }

    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////我是一条分割线哟//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 防止64K
        MultiDex.install(base)
    }

    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////我是一条分割线哟//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    override fun onCreate() {
        super.onCreate()
        initApp()
        initUtilCodeX()
    }

    /**
     * 初始化UtilCodeX
     */
    private fun initUtilCodeX() {
        Utils.init(this)
    }

    /**
     * 初始化app
     */
    private fun initApp() {
        app = this
    }

    /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////我是一条分割线哟//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 退出程序
     */
    open fun exitApp() {
        AppUtils.exitApp()
        Process.killProcess(Process.myPid())
        System.gc()
        exitProcess(0)
    }
}