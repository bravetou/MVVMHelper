package com.brave.mvvm.mvvmhelper.binding.adapter.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.brave.mvvm.mvvmhelper.binding.command.BindingCommand

/**
 * ***author***     ：BraveTou
 *
 * ***blog***       ：https://blog.csdn.net/bravetou
 *
 * ***time***       ：2020/12/3 10:21
 *
 * ***desc***       ：EditText的绑定配置
 */
object ViewAdapter {
    /**
     * EditText重新获取焦点的事件绑定
     */
    @JvmStatic
    @BindingAdapter(value = ["requestFocus"], requireAll = false)
    fun requestFocusCommand(editText: EditText?, requestFocus: Boolean) {
        if (null == editText) return
        if (requestFocus) {
            editText.setSelection(editText.text.toString().length)
            editText.requestFocus()
            val imm = editText.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /**
     * EditText输入文字改变的监听
     */
    @JvmStatic
    @BindingAdapter(value = ["textChanged"], requireAll = false)
    fun addTextChangedListener(editText: EditText?, textChanged: BindingCommand<String?>?) {
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