package com.brave.mvvm.helper

import android.app.Application
import com.brave.mvvm.mvvmhelper.base.CommonModel
import com.brave.mvvm.mvvmhelper.base.CommonViewModel

class MyTestViewModel : CommonViewModel<CommonModel> {
    constructor(application: Application) : super(application)
    constructor(application: Application, model: CommonModel?) : super(application, model)
}