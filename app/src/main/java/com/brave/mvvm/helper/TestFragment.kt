package com.brave.mvvm.helper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brave.mvvm.helper.databinding.FragmentTestBinding
import com.brave.mvvm.mvvmhelper.base.BaseFragment

class TestFragment : BaseFragment<FragmentTestBinding, TestViewModel>() {
    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.fragment_test
    }

    override fun initVariableId(): Int {
        return BR.viewModel
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
    }
}