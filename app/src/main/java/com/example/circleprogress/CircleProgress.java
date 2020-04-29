package com.example.circleprogress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;


public class CircleProgress extends View {

    private int max = 0;
    private int min = 100;
    private int progress = 0;
    private int color = Color.GRAY;
    private float strokeWidth = 4;

    private Paint backagroundPaint;
    private Paint foregroundPaint;

    private RectF rectF;
    private Rect bounds;
    private Paint percentPaint;

    private boolean autoColord = false;
    private boolean showPercent = true;

    public CircleProgress(Context context) {
        super(context);
        init(context);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //attrs
        TypedArray ta = context
                .obtainStyledAttributes(attrs, R.styleable.CircleProgress, defStyleAttr, 0);
        min = ta.getInteger(R.styleable.CircleProgress_cp_min, 0);
        max = ta.getInteger(R.styleable.CircleProgress_cp_max, 0);

        if (max < min)
            max = min;
        progress = ta.getInteger(R.styleable.CircleProgress_cp_progress, min);
        if (progress < min) {
            progress = min;
        } else if (progress > max) {
            progress = max;
        }

        color = ta.getColor(R.styleable.CircleProgress_cp_color, Color.LTGRAY);
        strokeWidth = ta.getDimension(R.styleable.CircleProgress_cp_stroke_width, 4);
        autoColord = ta.getBoolean(R.styleable.CircleProgress_cp_auto_colored,false);
        showPercent  = ta.getBoolean(R.styleable.CircleProgress_cp_show_percent,true);


        ta.recycle();
        //initialize

        init(context);
        rectF = new RectF();
    }



    private void init(Context context) {

        backagroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backagroundPaint.setColor(adjustAlpha(color, 0.15f));
        backagroundPaint.setStyle(Paint.Style.STROKE);
        backagroundPaint.setStrokeWidth(strokeWidth);

        rectF = new RectF();
        bounds = new Rect();

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);

        percentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setColor(color);
        percentPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        percentPaint.setTextAlign(Paint.Align.CENTER);
        // چگالی پیکسل های صفحه نمایش این گوشی
        float density = context.getResources().getDisplayMetrics().density;

        percentPaint.setTextSize(25 * density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        rectF.set(strokeWidth / 2
                , strokeWidth / 2
                , min - strokeWidth / 2
                , min - strokeWidth / 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawOval(rectF, backagroundPaint);
        // *ترتیب مهم است*
        int sweepAngel = (progress - min) * 360 / (max - min);
        // ture or false = concent to center or not
        canvas.drawArc(rectF, -90, sweepAngel, false, foregroundPaint);

        int percent = (progress - min) * 100 / (max - min);

        // showpercent
        if(showPercent){
            String presentlable = percent + "%";
            float x = getPaddingLeft() + (getWidth() + getPaddingLeft() + getPaddingRight()) / 2;
            float y = getPaddingTop() + (getHeight() + getPaddingTop() + getPaddingBottom()) / 2;


            percentPaint.getTextBounds(presentlable,0, presentlable.length(),bounds);
            y += bounds.height() / 2;

            canvas.drawText(presentlable, x, y, percentPaint);

        }

        if(autoColord){
            int r =(100 - percent ) *255 / 100;
            int g = percent * 200 / 100;
            int b  = 30;
            int newColor = Color.rgb(r,g,b);
            backagroundPaint.setColor(adjustAlpha(newColor,0.2f));
            foregroundPaint.setColor(newColor);
            percentPaint.setColor(newColor);

            // شرط اینکه اگر خودکار نبود رنگ ثابت باشد
        } else{
            backagroundPaint.setColor(adjustAlpha(color,0.2f));
            foregroundPaint.setColor(color);
            percentPaint.setColor(color);
        }
    }



    public void setMin() {
        this.min = (min > 0) ? min : 0;
        invalidate();
    }

    public void setMax() {
        this.max = (max > min) ? max : min;
        invalidate();

    }

    public void setProgress(int value) {
        if (value < min) {
            this.progress = min;
        } else if (value > max) {
            this.progress = max;
        } else {
            this.progress = value;
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getProgress() {
        return progress;
    }

    public void getColor(@ColorInt int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        backagroundPaint.setColor(adjustAlpha(color, 0.2f));
        invalidate();
    }

    public boolean isAutoColord() {

        return autoColord;
    }

    public void setAutoColord(boolean autoColord) {

        if(this.autoColord == autoColord){
            return;

        }
        this.autoColord = autoColord;
        invalidate();
    }

    public boolean isShowPercent() {

        return showPercent;
    }

    public void setShowPercent(boolean showPercent) {
            if(this.showPercent == showPercent)
                return;
        this.showPercent = showPercent;
        invalidate();
    }

    public void getStrokeWidth() {
        if (strokeWidth < 0 || strokeWidth == this.strokeWidth)
            return;
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        backagroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();
        invalidate();
    }


    // factor 0.0f to 1.0f
    private int adjustAlpha(int color, float factor) {
        if (factor < 0.0f || factor > 1.0f)
            return color;
        float alpha = Math.round(Color.alpha(color) * factor);
        return Color.argb(
                (int) alpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }


    public void setProgressWithAnimation(int value){
        ObjectAnimator animator = ObjectAnimator.ofInt(this,"progress",value);
        // کاهش زمان برای درصد های کم
        long duration = Math.abs(value - progress) * 2000L / (max - min);
        // (درصد آخر)کاهش سرعت پرشدن
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(1800L);
        animator.start();
    }
}