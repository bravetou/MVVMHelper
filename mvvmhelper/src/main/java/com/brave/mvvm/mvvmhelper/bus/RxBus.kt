package com.brave.mvvm.mvvmhelper.bus

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/11/30 10:37
 *
 * ***desc***       ：只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
 */
class RxBus {
    private val mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized()
    private val mStickyEventMap: MutableMap<Class<*>, Any>

    /**
     * 发送事件
     */
    fun post(event: Any) {
        mBus.onNext(event)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservable(eventType: Class<T>?): Observable<T> {
        return mBus.ofType(eventType)
    }

    /**
     * 判断是否有订阅者
     */
    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    fun reset() {
        mDefaultInstance = null
    }
    /**
     * Stciky 相关
     */
    /**
     * 发送一个新Sticky事件
     */
    fun postSticky(event: Any) {
        synchronized(mStickyEventMap) { mStickyEventMap.put(event.javaClass, event) }
        post(event)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservableSticky(eventType: Class<T>): Observable<T> {
        synchronized(mStickyEventMap) {
            val observable = mBus.ofType(eventType)
            val event = mStickyEventMap[eventType]
            return if (event != null) {
                Observable.merge(
                    observable,
                    Observable.create(ObservableOnSubscribe { emitter ->
                        emitter.onNext(
                            eventType.cast(
                                event
                            )
                        )
                    })
                )
            } else {
                observable
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    fun <T> getStickyEvent(eventType: Class<T>): T {
        synchronized(mStickyEventMap) { return eventType.cast(mStickyEventMap[eventType]) }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T {
        synchronized(mStickyEventMap) { return eventType.cast(mStickyEventMap.remove(eventType)) }
    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) { mStickyEventMap.clear() }
    }

    companion object {
        @Volatile
        private var mDefaultInstance: RxBus? = null

        @JvmStatic
        val default: RxBus
            get() {
                if (mDefaultInstance == null) {
                    synchronized(RxBus::class.java) {
                        if (mDefaultInstance == null) {
                            mDefaultInstance = RxBus()
                        }
                    }
                }
                return mDefaultInstance!!
            }
    }

    init {
        mStickyEventMap = ConcurrentHashMap()
    }
}