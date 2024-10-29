package com.example.mychatapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.example.mychatapplication.R;

public class LeftImgButton extends androidx.appcompat.widget.AppCompatButton {
    private Context context;
    private Drawable drawable;
    private Paint textPaint = new Paint();
    private Paint blackPatin = new Paint();
    public LeftImgButton(Context context) {
        super(context);
        this.context = context;
    }

    public LeftImgButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LeftImgButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("LeftImgButton", "onDraw");
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        //getCompoundDrawables()分别对应左、上、右、下
        drawable = getCompoundDrawables()[0];
        int drawableIntrinsicWidth = drawable.getIntrinsicWidth();
        int drawableIntrinsicHeight = drawable.getIntrinsicHeight();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textWidth = textPaint.measureText((String) getText());
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        float totalWidth = drawableIntrinsicWidth + textWidth;
        float baseline = getHeight() - getPaddingBottom() - fontMetrics.descent;
        int start = (int)((width - totalWidth) / 2.0f);
        drawable.setBounds(start, (int) (textHeight + fontMetrics.ascent), start + (int)(((float)((int) (baseline + 10) - (int) (textHeight + fontMetrics.ascent)) / (float)drawableIntrinsicHeight) * (float)drawableIntrinsicWidth), (int) baseline + 10);
        textPaint.setColor(getCurrentTextColor());
        textPaint.setTextSize(getTextSize());
        drawable.draw(canvas);
        canvas.drawText((String) getText(), start + drawableIntrinsicWidth, baseline, textPaint);
        blackPatin.setColor(context.getResources().getColor(R.color.verdant, null));
        blackPatin.setStyle(Paint.Style.FILL);
//        canvas.drawLine(0, fontMetrics.descent, width, fontMetrics.descent, blackPatin);
//        canvas.drawLine(0, fontMetrics.bottom, width, fontMetrics.bottom, blackPatin);
//        canvas.drawLine(0, textHeight, width, textHeight, blackPatin);
//        canvas.drawLine(0, baseline, width, baseline, blackPatin);
//        canvas.drawLine(0, -fontMetrics.ascent, width, -fontMetrics.ascent, blackPatin);
//        canvas.drawLine(0, textHeight + fontMetrics.ascent, width, textHeight + fontMetrics.ascent, blackPatin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("LeftImgButton", "onMeasure");
        textPaint.setTextSize(getTextSize());
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textWidth = textPaint.measureText((String) getText());
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        heightMeasureSpec = 0;
        //set和super选一个
        setMeasuredDimension(widthMeasureSpec, (int) textHeight + getPaddingTop() + getPaddingBottom());
//        super.onMeasure((int) textHeight, (int) 1000);
    }
}
