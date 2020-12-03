package com.brave.mvvm.mvvmhelper.base

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.trello.rxlifecycle4.LifecycleProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import java.lang.ref.WeakReference

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/1 11:16
 *
 * ***desc***       ：ViewModel基类（若不使用[BaseActivity.initViewModel]创建ViewModel,
 * 则继承至[BaseViewModel]时,必须重写[BaseViewModel]只带 application参数的构造方法）
 */
abstract class BaseViewModel<M : BaseModel?> @JvmOverloads constructor(
    application: Application,
    protected var model: M? = null,
) : AndroidViewModel(application), IBaseViewModel, Consumer<Disposable?> {
    // 弱引用持有
    private var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    // 管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private var mCompositeDisposable: CompositeDisposable?

    protected fun addSubscribe(disposable: Disposable?) {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    val lifecycleProvider: LifecycleProvider<*>
        get() = lifecycle!!.get()!!

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {}

    override fun onCreate() {}

    override fun onDestroy() {}

    override fun onStart() {}

    override fun onStop() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun registerRxBus() {}

    override fun removeRxBus() {}

    override fun onCleared() {
        super.onCleared()
        if (null != model) {
            model!!.onCleared()
        }
        // ViewModel销毁时会执行，同时取消所有异步任务
        if (null != mCompositeDisposable) {
            mCompositeDisposable!!.clear()
        }
    }

    @Throws(Exception::class)
    override fun accept(disposable: Disposable?) {
        addSubscribe(disposable)
    }

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    open fun toast(obj: Any) {
        var text = obj.toString() + ""
        if (TextUtils.isEmpty(text)) return
        ToastUtils.showLong(text)
    }
}