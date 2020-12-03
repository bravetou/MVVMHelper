package com.brave.mvvm.mvvmhelper.bus

import android.text.TextUtils
import androidx.collection.ArrayMap
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/30 11:12
 *
 * ***desc***       ：管理 CompositeSubscription
 */
object RxSubscriptions {
    private val maps: MutableMap<String, CompositeDisposable> =
        ArrayMap()

    @JvmStatic
    private fun <T> getKey(cls: Class<T>?): String {
        if (null == cls) {
            return ""
        }
        var key = ""
        val packageName = cls.getPackage().name
        if (!TextUtils.isEmpty(packageName)) {
            key += packageName.replace(".", "_")
        }
        val simpleName = cls.simpleName
        if (!TextUtils.isEmpty(simpleName)) {
            key += simpleName.replace(".", "_")
        }
        return key
    }

    @JvmStatic
    private fun getCompositeDisposable(key: String): CompositeDisposable {
        var disposable = maps[key]
        if (null == disposable) {
            disposable = CompositeDisposable()
        }
        return disposable
    }

    @JvmStatic
    fun <T> isDisposed(cls: Class<T>?): Boolean {
        val key = getKey(cls)
        val disposable = getCompositeDisposable(key)
        return disposable.isDisposed
    }

    @JvmStatic
    fun <T> add(cls: Class<T>?, s: Disposable?) {
        if (null != s) {
            val key = getKey(cls)
            val disposable = getCompositeDisposable(key)
            disposable.add(s)
        }
    }

    @JvmStatic
    fun <T> remove(cls: Class<T>?, s: Disposable?) {
        if (null != s) {
            val key = getKey(cls)
            val disposable = getCompositeDisposable(key)
            disposable.remove(s)
        }
    }

    @JvmStatic
    fun <T> clear(cls: Class<T>?) {
        val key = getKey(cls)
        val disposable = getCompositeDisposable(key)
        maps.remove(key)
        disposable.clear()
    }

    @JvmStatic
    fun <T> dispose(cls: Class<T>?) {
        val key = getKey(cls)
        val disposable = getCompositeDisposable(key)
        disposable.dispose()
    }
}