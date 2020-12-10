package com.brave.mvvm.helper

import android.app.Application
import com.brave.mvvm.mvvmhelper.base.BaseModel
import com.brave.mvvm.mvvmhelper.base.BaseViewModel

class MyTestViewModel : BaseViewModel<BaseModel> {
    constructor(application: Application) : super(application)
    constructor(application: Application, model: BaseModel?) : super(application, model)
}