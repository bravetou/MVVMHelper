package com.brave.mvvm.mvvmhelper.binding.adapter.text

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.brave.mvvm.mvvmhelper.binding.command.BindingCommand

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/3 10:21
 *
 * ***desc***       ：TextView的绑定配置
 */
object ViewAdapter {
    /**
     * TextView文字改变的监听
     */
    @JvmStatic
    @BindingAdapter(value = ["textChanged"], requireAll = false)
    fun addTextChangedListener(editText: TextView?, textChanged: BindingCommand<String?>?) {
        if (null == editText) return
        if (null == textChanged) return
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
            override fun onTextChanged(text: CharSequence, i: Int, i1: Int, i2: Int) {
                textChanged.execute(text.toString())
            }
        })
    }
}