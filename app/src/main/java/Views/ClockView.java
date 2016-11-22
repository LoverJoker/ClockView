package Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.joker.clockview.R;

import java.util.Calendar;

/**
 * Created by Joker on 2016/11/21.
 */

public class ClockView extends View {

    private int mColorBackground ;
    private int mColorLongPointer;
    private int mColorShortPointer ;
    private int mColorSencond ;
    private int mColorOther;
    private Paint mPaint;
    private int mRadius; // 圆的半径
    private Calendar mCalendar; //获取当前系统时间
    private String[] zhengdian = {"12","1","2","3","4","5","6","7","8","9","10","11"};

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取系统时间

        //获取自定义的属性
        TypedArray arrays = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ClockView, defStyleAttr, 0);
        int indexCount = arrays.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = arrays.getIndex(i);
            switch (attr){
                case R.styleable.ClockView_ColorBackground:
                    mColorBackground = arrays.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ClockView_ColorLongPointer:
                    mColorLongPointer = arrays.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.ClockView_ColorShortPointer:
                    mColorShortPointer = arrays.getColor(attr,Color.parseColor("#cccccc"));
                    break;
                case R.styleable.ClockView_ColorSencond:
                    mColorSencond = arrays.getColor(attr,Color.RED);
                    break;
                case R.styleable.ClockView_ColorOther:
                    mColorOther = arrays.getColor(attr,Color.BLACK);
                    break;
            }
        }
        //用完回收
        arrays.recycle();


    }
    /*
    * 画笔的初始化
    * */
    private void initPaint() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置防抖动
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = 1000;
        int height = 1000;
        if (widthMode == MeasureSpec.EXACTLY){
            width = Math.min(width,widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = Math.min(height,heightSize);
        }
        setMeasuredDimension(width,height);
    }

    /*
    * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    * */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        canvas.save();
        //重置原点
        canvas.translate(mRadius,mRadius);
        drawClockFace(canvas);
        drawClockPointer(canvas);
        canvas.restore();

        postInvalidateDelayed(1000);



    }



    /*画表盘*/
    private void drawClockFace(Canvas canvas){
        mPaint.setColor(mColorBackground);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.save();
        canvas.drawCircle(0,0,mRadius,mPaint);

        for (int i = 0; i < 60; i++) {
            if (i%5==0){
                mPaint.setColor(mColorLongPointer);
                mPaint.setStrokeWidth(13);
                canvas.drawLine(0,-(mRadius-20),0,-(mRadius-80),mPaint);
            }else {
                mPaint.setColor(mColorShortPointer);
                mPaint.setStrokeWidth(5);
                canvas.drawLine(0,-(mRadius-20),0,-(mRadius-60),mPaint);
            }
            canvas.rotate(6);
        }
        canvas.restore();
    }

    /*画三个指针，*/
    private void drawClockPointer(Canvas canvas){
        mCalendar = Calendar.getInstance();
        int second = mCalendar.get(Calendar.SECOND);
        int minute = mCalendar.get(Calendar.MINUTE);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int AngleSecond = second *6;
        int AngleMinute = minute*6+second/10;
        int AngleHour = hour*30+minute/2;
        mPaint.setColor(mColorSencond);
        mPaint.setStrokeWidth(13);

        //秒钟
        canvas.save();
        canvas.rotate(AngleSecond);
        canvas.drawLine(0,60,0,-(mRadius-20),mPaint);
        canvas.restore();

        //分钟
        mPaint.setColor(mColorOther);
        mPaint.setStrokeWidth(20);
        canvas.save();
        canvas.rotate(AngleMinute);
        canvas.drawLine(0,40,0,-(mRadius-180),mPaint);
        canvas.restore();


        //时钟
        mPaint.setColor(mColorOther);
        mPaint.setStrokeWidth(23);
        canvas.save();
        canvas.rotate(AngleHour);
        canvas.drawLine(0,40,0,-(mRadius-230),mPaint);
        canvas.restore();

        //画秒钟尾部的小圆
        mPaint.setColor(mColorSencond);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0,0,27,mPaint);

    }
}
