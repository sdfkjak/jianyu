package com.example.mychatapplication.util;

import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class ToolbarUtil {
    public static void setToolbarTitleCenter(Toolbar tb_head) {
        String title = "title";
        final CharSequence originalTitle = tb_head.getTitle();
        tb_head.setTitle(title);
        for (int i = 0; i < tb_head.getChildCount(); i++) {
            View view = tb_head.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.contentEquals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
                    textView.setTypeface(null, Typeface.BOLD);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
        }
        tb_head.setTitle(originalTitle);
    }

    public static void setToolbarTitle(Toolbar tb_head) {
        String title = "title";
        final CharSequence originalTitle = tb_head.getTitle();
        tb_head.setTitle(title);
        for (int i = 0; i < tb_head.getChildCount(); i++) {
            View view = tb_head.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.contentEquals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(14);
                    textView.setTypeface(null, Typeface.BOLD);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
        }
        tb_head.setTitle(originalTitle);
    }
}
