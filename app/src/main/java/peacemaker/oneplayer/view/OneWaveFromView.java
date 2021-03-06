package peacemaker.oneplayer.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioRecord;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.WindowManager;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.Properties;

import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.tool.LogTool;

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
    private ArrayList<Line> lines = new ArrayList<>();
    private Point lastPoint = new Point();
    private Paint paint;
    private Rect rect = new Rect(0,0,0,0);
    private int rectNumber = 32;
    private String text = "";
    private Boolean isLineMode = true;
    private Paint textPaint = new Paint();
    public class Line{
        public float startX;
        public float startY;
        public float endX;
        public float endY;
    }
    //private ArrayList<ObjectAnimator> objectAnimators = new ArrayList<>();
    public OneWaveFromView(Context context) {
        super(context);
        Log.v("OneWaveFromView","构造函数()单参构造");

    }
    public OneWaveFromView(Context context, AttributeSet attrs){
        super(context, attrs, 0);
        Log.v("OneWaveFromView","构造函数()二参构造");
        paint = new Paint();
        textPaint.setTextSize(50);
        //paint.setAlpha(150);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        width = dm.widthPixels;
        offsetY = width;
        unitLength = width/rectNumber*1.f-1;
        block_width = unitLength*4/5;
        block_height = width*(rectNumber*1.f-1)/135;
        offsetX = unitLength/5;
        for(int i=0;i<(rectNumber*1.f-1);i++){
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
        //paint.setAlpha(150);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(canvas!=null) {
            if(!isLineMode) {
                for (int i = 0; i < (rectNumber * 1.f - 1); i++) {
                    canvas.drawRect(rects.get(i), paint);
                }
            }else {
                for (int i = 0; i < lines.size(); i++) {
                    Line line = lines.get(i);
                    canvas.drawLine(line.startX,line.startY,line.endX,line.endY,paint);
                }
                canvas.drawText(text,offsetX,OneApplication.dip2px(200),textPaint);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(width, width);

    }
    private void resetRects(){
        unitLength = width*1.f/rectNumber*1.f-1;
        block_width = unitLength*4/5;
        block_height = width*(rectNumber*1.f-1)/135;
        offsetX = unitLength/5;
        //LogTool.log(this,"方块宽度"+block_width);
        for(int i=0;i<(rectNumber*1.f-1);i++){
            int x = (int)(i*unitLength+offsetX);
            Rect rect = new Rect(x,(int)(offsetY-block_height),(int)(x+block_width),(int)(offsetY));
            rects.add(rect);
        }
    }
    public void setAudioData(byte[] data,String text){
        this.text = text;
        this.data = data;
        int scale = data.length/540;
        lines = new ArrayList<>();
        resetRects();
        if(data!=null){
            for(int i=0;i<540;i++){
                Line line = new Line();
                line.startX = i*2+offsetX;
                line.startY = offsetY;
                line.endX = line.startX;
                line.endY = offsetY-OneApplication.dip2px(120)-data[i*scale];
                lines.add(line);
            }
        }
        invalidate();
    }
    public void setData(byte[] data){

        //Log.v("OneWaveFromView","setData()"+data);
//        if(this.data==null){
//            this.data = new byte[data.length];
//        }
//        System.arraycopy(this.data, 0, data, 0, data.length);
//        this.data = data;
//
//        //Log.v("OneWaveFromView","setData1()"+this.data);
//        if(data!=null){
//            for(int i=1;i<32;i++){
//                //int value = data[i];
//                int value = (int)Math.hypot(data[2*i],data[2*i+1]);
//                int y = (int)(offsetY-20-value);
//                //Log.v("OneWaveFormView", "setData()检查Y" +y);
//                //ObjectAnimator objectAnimator = objectAnimators.get(i-1);
//                //objectAnimator.setIntValues((int)rects.get(i-1).top,y);
//                //Log.v("OneWaveFormView", "onDraw()检查插值" + (int)rects.get(i).top + "," +y);
//                //objectAnimator.setDuration(50);
//                //objectAnimator.start();
////                rects.get(i-1).bottom = offsetY;
//                rects.get(i-1).top = y;
//
////                rects.get(i-1).left = x;
////                rects.get(i-1).right = x + block_width;
//
//
//            }
//        }
        invalidate();
    }
}
