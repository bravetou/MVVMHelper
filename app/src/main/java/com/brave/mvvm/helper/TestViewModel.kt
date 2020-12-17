package com.brave.mvvm.helper

import android.app.Application
import androidx.databinding.ObservableField
import com.brave.mvvm.mvvmhelper.base.CommonViewModel

class TestViewModel : CommonViewModel<HttpDataModel> {
    constructor(application: Application) : super(application)
    constructor(application: Application, model: HttpDataModel?) : super(application, model)

    var mTestText = ObservableField("")
}