package com.brave.mvvm.helper;

import android.content.Context;
import android.view.View;

import com.brave.mvvm.mvvmhelper.utils.ViewUtils;

public class Test {
    public static final void click(Context context) {
        View view = new View(context);
        ViewUtils.preventRepeatedClicks(view, o -> {

        }, 1000);
    }
}