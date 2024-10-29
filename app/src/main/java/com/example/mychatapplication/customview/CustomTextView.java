package com.example.mychatapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mychatapplication.R;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Paint backGroundPaint = new Paint();
    private Paint textPaint = new Paint();
    private Path path = new Path();
    private String side;
    public CustomTextView(@NonNull Context context) {
        super(context);
        Log.d("构造方法", "1");
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("构造方法", "2");
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        this.side = ta.getString(R.styleable.CustomTextView_side);
        if(this.side == null){
            this.side = "me";
        }
        Log.d("构造方法", side);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("构造方法", "绘画");
        Log.d("构造方法", side);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        textPaint.setColor(getCurrentTextColor());
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(getTextSize());
        textPaint.setLetterSpacing(getLetterSpacing());

        backGroundPaint.setColor(getResources().getColor(R.color.pine, null));
        backGroundPaint.setStyle(Paint.Style.FILL);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textWidth = textPaint.measureText(String.valueOf(getText()));
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        int sideLength = 12;
        if(side.equals("me")){
            if(getLineCount() == 1){
                RectF rectF = new RectF(width - textWidth - 2 * getPaddingLeft() - sideLength, 0 , width - sideLength, height);
                canvas.drawRoundRect(rectF, 10, 10, backGroundPaint);
                float baseline = getHeight() - getPaddingBottom() - fontMetrics.descent;
                canvas.drawText(String.valueOf(getText()), width - textWidth - getPaddingLeft() - sideLength, baseline, textPaint);
                path.moveTo((float) (width - (sideLength * (Math.sqrt(3) / 2)) - 2), height / 2.0f - 15);
                path.lineTo(width,  height / 2);
                path.lineTo((float) (width - (sideLength * (Math.sqrt(3) / 2)) - 2), height / 2.0f + 15);
                path.close();
                canvas.drawPath(path, backGroundPaint);
            }else{
                RectF rectF = new RectF(0, 0, width - sideLength, height);
                canvas.drawRoundRect(rectF, 10, 10, backGroundPaint);
                super.onDraw(canvas);
                path.moveTo((float) (width - (sideLength * (Math.sqrt(3) / 2)) - 2), textHeight - 15);
                path.lineTo(width,  textHeight);
                path.lineTo((float) (width - (sideLength * (Math.sqrt(3) / 2)) - 2), textHeight + 15);
                path.close();
                canvas.drawPath(path, backGroundPaint);
            }
        }else{
            if(getLineCount() == 1){
                RectF rectF = new RectF(sideLength, 0 , textWidth + 2 * getPaddingLeft(), height);
                canvas.drawRoundRect(rectF, 10, 10, backGroundPaint);
                float baseline = getHeight() - getPaddingBottom() - fontMetrics.descent;
                canvas.drawText(String.valueOf(getText()), getPaddingLeft(), baseline, textPaint);

                path.moveTo((float) ((sideLength * (Math.sqrt(3) / 2)) + 2), height / 2.0f - 15);
                path.lineTo(0,  height / 2.0f);
                path.lineTo((float) ((sideLength * (Math.sqrt(3) / 2)) + 2), height / 2.0f + 15);
                path.close();
                canvas.drawPath(path, backGroundPaint);
            }else{
                RectF rectF = new RectF(sideLength, 0, width - sideLength, height);
                canvas.drawRoundRect(rectF, 10, 10, backGroundPaint);
                super.onDraw(canvas);
                path.moveTo((float) ((sideLength * (Math.sqrt(3) / 2)) + 2), textHeight - 15);
                path.lineTo(1,  textHeight);
                path.lineTo((float) ((sideLength * (Math.sqrt(3) / 2)) + 2), textHeight + 15);
                path.close();
                canvas.drawPath(path, backGroundPaint);
            }
        }

        //画小三角
//        path.moveTo(width - 100, height / 2 - 3);
//        path.moveTo(width, height / 2);
//        path.moveTo(width - 100, height / 2 + 3);
//        path.close();

//        backGroundPaint.setColor(getResources().getColor(R.color.black, null));
//        canvas.drawLine(0, getHeight() - getPaddingBottom() - fontMetrics.descent, width, getHeight() - getPaddingBottom() - fontMetrics.descent, backGroundPaint);
//        float baseline =  height /	2 + (fontMetrics.descent - fontMetrics.ascent) / 2	- fontMetrics.descent;
//        canvas.drawText(String.valueOf(getText()), 0, baseline, textPaint);
//        super.onDraw(canvas);

    }
}
