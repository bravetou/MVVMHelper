package com.brave.mvvm.mvvmhelper.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.brave.mvvm.mvvmhelper.bus.Messenger
import com.brave.mvvm.mvvmhelper.utils.ViewUtils.preventRepeatedClicks
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import java.lang.reflect.ParameterizedType

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/1 15:25
 *
 * ***desc***       ：一个拥有DataBinding框架的基Activity,
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity,
 * 但是需要继承RxAppCompatActivity,
 * 方便LifecycleProvider管理生命周期
 */
abstract class CommonActivity<V : ViewDataBinding?, VM : CommonViewModel<*>?> :
    RxAppCompatActivity(), IBaseView {
    // binding
    protected var binding: V? = null
        private set
    protected val mBinding: V
        get() = binding!!

    // viewModel
    protected var viewModel: VM? = null
        private set
    protected val mViewModel: VM
        get() = viewModel!!

    // viewModelId
    private var viewModelId = 0

    // 管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private var mCompositeDisposable: CompositeDisposable? = null

    protected fun addSubscribe(disposable: Disposable?) {
        if (null == mCompositeDisposable) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    protected val context: Context
        get() = this

    protected val activity: FragmentActivity
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 页面接受的参数方法
        initParam()
        // 私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        // 页面数据初始化方法
        initData()
        // 页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        // 注册RxBus
        viewModel!!.registerRxBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消所有异步任务
        mCompositeDisposable?.clear()
        // 解除Messenger注册
        Messenger.getDefault().unregister(viewModel)
        if (null != viewModel) {
            viewModel!!.removeRxBus()
        }
        if (null != binding) {
            binding!!.unbind()
        }
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        // DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }
        // 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView<V>(this, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (null == viewModel) {
            val type = javaClass.genericSuperclass
            val modelClass: Class<*> = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<*>
            } else {
                // 如果没有指定泛型参数
                // 则默认使用BaseViewModel
                CommonViewModel::class.java
            }
            viewModel = ViewModelProvider(this).get(modelClass as Class<VM>)
        }
        // 关联ViewModel
        binding!!.setVariable(viewModelId, viewModel)
        // 支持LiveData绑定xml，数据改变，UI自动会更新
        binding!!.lifecycleOwner = this
        // 让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel!!)
        // 注入RxLifecycle生命周期
        viewModel!!.injectLifecycleProvider(this)
    }

    /**
     * 刷新布局
     */
    fun refreshLayout() {
        if (null != viewModel) {
            binding!!.setVariable(viewModelId, viewModel)
        }
    }

    /**
     * 跳转页面
     * @param clz 所跳转的目的Activity类
     * @param bundle 参数
     */
    @JvmOverloads
    fun <AC : Activity> startActivity(clz: Class<AC>, bundle: Bundle? = null) {
        var intent = Intent(this, clz)
        if (null != bundle) {
            intent.putExtra("bundle", bundle)
        }
        startActivity(intent)
    }

    /**
     * 获取页面传递参数
     * @param T
     * @param key
     * @param default
     * @return T
     */
    @JvmOverloads
    fun <T : Any> getPageParam(key: String, default: T? = null): T? {
        var bundle: Bundle? = intent?.getBundleExtra("bundle")
            ?: return null
        var param = bundle?.get(key)
        return if (null != default) {
            if (null == param) default
            else param as T
        } else {
            if (null == param) null
            else param as T
        }
    }

    override fun initParam() {}

    /**
     * 初始化根布局
     * @return 布局layout的id
     */
    @LayoutRes
    abstract fun initContentView(savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     * @return 继承BaseViewModel的ViewModel
     */
    abstract fun initViewModel(): VM?

    override fun initData() {}

    override fun initViewObservable() {}

    open fun toast(obj: Any) {
        var text = obj.toString() + ""
        if (TextUtils.isEmpty(text)) return
        ToastUtils.showLong(text)
    }

    /**
     * 添加防止重复点击事件到订阅
     */
    @JvmOverloads
    protected fun View?.addClickSubscribe(
        consumer: Consumer<Any>?,
        duration: Long = 1000L,
    ) {
        if (null == this) return
        addSubscribe(this.preventRepeatedClicks(consumer, duration))
    }
}