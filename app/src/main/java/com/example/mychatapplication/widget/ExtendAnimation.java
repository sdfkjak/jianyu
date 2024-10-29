package com.example.mychatapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class ExtendAnimation extends TranslateAnimation {
    private float mFromXDelta;
    private float mFromYDelta;
    private float mToXDelta;
    private float mToYDelta;

    public ExtendAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        super(fromXDelta, toXDelta, fromYDelta, toYDelta);
        mFromXDelta = fromXDelta;
        mFromYDelta = fromYDelta;
        mToXDelta = toXDelta;
        mToYDelta = toYDelta;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Log.d("咋了", String.valueOf(interpolatedTime));
        float dx = mFromXDelta;
        float dy = mFromYDelta;
        if (mFromXDelta != mToXDelta) {
            dx = (mToXDelta - mFromXDelta) * interpolatedTime;
        }
        if (mFromYDelta != mToYDelta) {
            dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
        }
        t.getMatrix().setTranslate(dx, dy);
    }
}
