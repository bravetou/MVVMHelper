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

    fun start(key: String, value: Intent) {
        var intent = mServiceStack?.get(key)
        if (null == intent) {
            intent = value
            mServiceStack?.put(key, value)
        }
        getContext().startService(intent)
    }

    fun start(key: String, cls: Class<*>) {
        start(key, Intent(getContext(), cls))
    }

    fun stop(key: String) {
        var intent = mServiceStack?.get(key)
        if (null != intent) {
            getContext().stopService(intent)
            mServiceStack.remove(key)
        }
    }

    fun clear() {
        mServiceStack?.forEach {
            var intent = mServiceStack?.get(it.key)
            if (null != intent) {
                getContext().stopService(intent)
            }
        }
        mServiceStack?.clear()
    }
}