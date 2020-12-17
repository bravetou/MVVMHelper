package com.brave.mvvm.helper

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import com.brave.mvvm.mvvmhelper.base.CommonViewModel
import com.brave.mvvm.mvvmhelper.bus.RxBus
import com.brave.mvvm.mvvmhelper.http.download.DownloadMessage
import com.brave.mvvm.mvvmhelper.utils.RxUtils.schedulersIO
import com.google.gson.Gson


class MainViewModel @JvmOverloads constructor(
    application: Application,
    model: HttpDataModel? = null,
) : CommonViewModel<HttpDataModel>(application, model) {

    var mTestText = ObservableField("")

    override fun registerRxBus() {
        RxBus.default?.toObservable(DownloadMessage::class.java)
            ?.schedulersIO()
            ?.subscribe {
                Log.d(Companion.TAG, "registerRxBus: ${Gson().toJson(it)}")
            }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}