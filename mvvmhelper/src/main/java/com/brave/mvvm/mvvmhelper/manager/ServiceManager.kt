package com.brave.mvvm.mvvmhelper.manager

import android.content.Context
import android.content.Intent
import androidx.collection.ArrayMap
import com.blankj.utilcode.util.Utils

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/2 15:48
 *
 * ***desc***       ：Service管理器
 */
class ServiceManager private constructor() {
    private val mServiceStack: MutableMap<String, Intent> = ArrayMap()

    private object SingletonHolder {
        val sInstance = ServiceManager()
    }

    companion object {
        @JvmStatic
        val instance = SingletonHolder.sInstance
    }

    private fun getContext(): Context {
        return Utils.getApp()
    }

    /**
     * 启动指定服务，可在Intent中携带参数
     */
    fun start(key: String, value: Intent) {
        var intent = mServiceStack[key]
        if (null == intent) {
            intent = value
            mServiceStack[key] = value
        }
        getContext().startService(intent)
    }

    /**
     * 启动指定服务，无参
     */
    fun start(key: String, cls: Class<*>) {
        start(key, Intent(getContext(), cls))
    }

    /**
     * 移除并停止指定的服务
     */
    fun stop(key: String) {
        var intent = mServiceStack[key]
        if (null != intent) {
            getContext().stopService(intent)
            mServiceStack.remove(key)
        }
    }

    /**
     * 清空并停止所有服务
     */
    fun clear() {
        mServiceStack.forEach {
            var intent = mServiceStack[it.key]
            if (null != intent) {
                getContext().stopService(intent)
            }
        }
        mServiceStack.clear()
    }
}