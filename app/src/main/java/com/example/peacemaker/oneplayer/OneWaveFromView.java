package com.example.peacemaker.oneplayer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.WindowManager;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by ouyan on 2016/6/6.
 */

public class OneWaveFromView extends View {
    private int width;
    private byte[] data;
    private float unitLength;
    private float offsetX = 20;
    private float offsetY = 1000;
    private float block_width;
    private float block_height;
    private ArrayList<Rect> rects = new ArrayList<>();
    private Point lastPoint = new Point();
    private Paint paint;
    private Rect rect = new Rect(0,0,0,0);
    //private ArrayList<ObjectAnimator> objectAnimators = new ArrayList<>();
    public OneWaveFromView(Context context) {
        super(context);
        Log.v("OneWaveFromView","构造函数()单参构造");

    }
    public OneWaveFromView(Context context, AttributeSet attrs){
        super(context, attrs, 0);
        Log.v("OneWaveFromView","构造函数()二参构造");
        paint = new Paint();
        paint.setAlpha(150);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        width = dm.widthPixels;
        offsetY = width;
        unitLength = width/31.f;
        block_width = unitLength*4/5;
        block_height = width*32/135;
        offsetX = unitLength/5;
        for(int i=0;i<31;i++){
            int x = (int)(i*unitLength+offsetX);
            //Log.v("OneWaveFormView","查看方块情况"+x);
            Rect rect = new Rect(x,(int)(offsetY-block_height),(int)(x+block_width),(int)(offsetY));
            rects.add(rect);
//            ObjectAnimator objectAnimator = new ObjectAnimator();
//            objectAnimator.setTarget(rect);
//            objectAnimator.setPropertyName("top");
//            objectAnimators.add(objectAnimator);
        }
    }

    public OneWaveFromView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        Log.v("OneWaveFromView","构造函数()三参构造");

    }
    public void setPaintColor(int color){
        paint.setColor(color);
        paint.setAlpha(150);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(canvas!=null) {
            for(int i=0;i<31;i++){
                canvas.drawRect(rects.get(i), paint);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(width, width);

    }
    public void setData(byte[] data){
        //Log.v("OneWaveFromView","setData()"+data);
//        if(this.data==null){
//            this.data = new byte[data.length];
//        }
//        System.arraycopy(this.data, 0, data, 0, data.length);
        this.data = data;
        //Log.v("OneWaveFromView","setData1()"+this.data);
        if(data!=null){
            for(int i=1;i<32;i++){
                //int value = data[i];
                int value = (int)Math.hypot(data[2*i],data[2*i+1]);
                int y = (int)(offsetY-block_height-3*value);
                //Log.v("OneWaveFormView", "setData()检查Y" +y);
                //ObjectAnimator objectAnimator = objectAnimators.get(i-1);
                //objectAnimator.setIntValues((int)rects.get(i-1).top,y);
                //Log.v("OneWaveFormView", "onDraw()检查插值" + (int)rects.get(i).top + "," +y);
                //objectAnimator.setDuration(50);
                //objectAnimator.start();
//                rects.get(i-1).bottom = offsetY;
                rects.get(i-1).top = y;

//                rects.get(i-1).left = x;
//                rects.get(i-1).right = x + block_width;


            }
        }
        invalidate();
    }
}
