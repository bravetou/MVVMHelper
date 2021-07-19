package com.brave.mvvm.helper

import android.os.Bundle
import com.brave.mvvm.helper.databinding.ActivityMainBinding
import com.brave.mvvm.mvvmhelper.base.CommonActivity
import com.brave.mvvm.mvvmhelper.http.download.DownloadService
import com.brave.mvvm.mvvmhelper.utils.RxUtils.schedulersIO
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : CommonActivity<ActivityMainBinding, MainViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): MainViewModel? {
        TODO("Not yet implemented")
    }

    private var testFragment: TestFragment? = null

    override fun initData() {
        var transaction = supportFragmentManager.beginTransaction()
        if (null == testFragment) {
            testFragment = TestFragment()
            transaction.add(R.id.fragment, testFragment!!)
        } else {
            transaction.show(testFragment!!)
        }
        transaction.commit()
    }

    var flag = 0

    override fun initViewObservable() {
        binding?.mTestText?.setOnClickListener {
            viewModel?.mTestText?.set(
                when (flag % 4) {
                    0 -> "lb"
                    1 -> "dwk"
                    2 -> "sy"
                    else -> "ty"
                }
            )
            flag++
        }

        Observable.interval(0, 1, TimeUnit.SECONDS)
            .schedulersIO()
            .subscribe({
                viewModel?.mTestText?.set(UUID.randomUUID().toString())
            }, {
                viewModel?.mTestText?.set(it.message)
            })

        DownloadService.startSelf(
            "http://upgrade.medrd.com/[selftv]_release_v1.1.10_10_11061428.apk"
        )
    }
}