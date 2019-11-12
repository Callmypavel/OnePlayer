package peacemaker.oneplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.listener.OnOneSeekBarListener;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.tool.OneBitmapUtil;

/**
 * Created by ouyan on 2016/6/9.
 */

public class OneSeekBar extends View implements GestureDetector.OnGestureListener{
//    private Bitmap draw_bitmap;
//    private Bitmap play_white_bitmap;
//    private Bitmap pause_white_bitmap;
//    private Bitmap play_black_bitmap;
//    private Bitmap pause_black_bitmap;
    private Bitmap play_bitmap;
    private Bitmap pause_bitmap;
    private int centreX;
    private int centreY;
    private int radius;
    private int ringRadius = 40;
    private int innerRadius;
    private RectF oval;
    private Paint ringPaint;
    private Paint tintRingPaint;
    private Paint defaultPaint;
    private float degree = 0;
    private int colorInt = Color.WHITE;
    private OnOneSeekBarListener oneSeekBarListener;
    private GestureDetector gestureDetector;
    private boolean isPlaying = false;
    public OneSeekBar(Context context) {
        super(context);
        initialize(context);
    }

    public OneSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);


    }
    public OneSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }
    private void initialize(Context context){
        DisplayMetrics dm =getResources().getDisplayMetrics();
        radius = OneApplication.dip2px(42);
        ringRadius = (int)(radius*0.25);
        Log.v("OneSeekBar","initialize半径"+radius);
        centreX = radius;
        centreY = radius;
        oval = new RectF(centreX - radius + ringRadius/2, centreY - radius + ringRadius/2, centreX + radius - ringRadius/2, centreY + radius - ringRadius/2);
        innerRadius = radius - ringRadius;
        play_bitmap = OneBitmapUtil.zoomImg(context, R.drawable.ic_play_circle_outline_white_48dp,(int)(1.5f*radius),(int)(1.5f*radius));
        pause_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.ic_pause_circle_outline_white_48dp,(int)(1.5f*radius),(int)(1.5f*radius));
//        try {
//            play_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.ic_play_circle_outline_white_48dp,2*radius,2*radius);
//            pause_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.ic_pause_circle_outline_white_48dp,2*radius,2*radius);
//            play_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.ic_play_circle_black_48dp,2*radius,2*radius);
//            pause_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.ic_pause_circle_black_48dp,2*radius,2*radius);
//        }catch (OutOfMemoryError outOfMemoryError){
//            Log.v("OneSeekBar","原图缩放异常");
//            outOfMemoryError.printStackTrace();
//            try {
//                play_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_play_144,2*radius,2*radius);
//                pause_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_pause_144,2*radius,2*radius);
//                play_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_play_144,2*radius,2*radius);
//                pause_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_pause_144,2*radius,2*radius);
//            }catch (OutOfMemoryError outOfMemoryError1){
//                Log.v("OneSeekBar","144缩放异常");
//                outOfMemoryError1.printStackTrace();
//                try {
//                    play_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_play_96,2*radius,2*radius);
//                    pause_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_pause_96,2*radius,2*radius);
//                    play_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_play_96,2*radius,2*radius);
//                    pause_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_pause_96,2*radius,2*radius);
//                }catch (OutOfMemoryError outOfMemoryError2){
//                    Log.v("OneSeekBar","96缩放异常");
//                    outOfMemoryError2.printStackTrace();
//                    try {
//                        play_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_play_72,2*radius,2*radius);
//                        pause_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_pause_72,2*radius,2*radius);
//                        play_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_play_72,2*radius,2*radius);
//                        pause_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_pause_72,2*radius,2*radius);
//                    }catch (OutOfMemoryError outOfMemoryError3){
//                        Log.v("OneSeekBar","72缩放异常");
//                        outOfMemoryError3.printStackTrace();
//                        try {
//                            play_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_play_48,2*radius,2*radius);
//                            pause_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.white_pause_48,2*radius,2*radius);
//                            play_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_play_48,2*radius,2*radius);
//                            pause_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.black_pause_48,2*radius,2*radius);
//                        }catch (OutOfMemoryError outOfMemoryError4){
//                            Log.v("OneSeekBar","48缩放异常");
//                            outOfMemoryError4.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//        }

//        Log.v("OneSeekBar","initialize位图"+play_white_bitmap);
//        Matrix matrix = new Matrix();
//        matrix.setRectToRect(new RectF(0, 0, play_bitmap.getWidth(), play_bitmap.getHeight()), new RectF(0, 0, play_bitmap.getWidth()/3, play_bitmap.getHeight()/3), Matrix.ScaleToFit.CENTER);
//        pause_bitmap = Bitmap.createBitmap(pause_bitmap,0,0,pause_bitmap.getWidth(),pause_bitmap.getHeight(),matrix,true);
//        play_bitmap = Bitmap.createBitmap(play_bitmap,0,0,play_bitmap.getWidth(),play_bitmap.getHeight(),matrix,true);
//        draw_bitmap = play_white_bitmap;
//        radius = (draw_bitmap.getWidth())/2;
        setPaintColor();
        defaultPaint = new Paint();
        //oval = new RectF();
        Log.v("OneSeekBar","构造函数");
        gestureDetector = new GestureDetector(context,this);
    }
    public void setColorInt(int colorInt){
        this.colorInt = colorInt;
        setPaintColor();
        invalidate();
    }
    public void setPaintColor(){
        if(ringPaint==null) {
            ringPaint = new Paint();
        }
        ringPaint.setColor(colorInt);
        ringPaint.setStrokeWidth(ringRadius);
        ringPaint.setAntiAlias(true);
        ringPaint.setStyle(Paint.Style.STROKE);
        if(tintRingPaint==null) {
            tintRingPaint = new Paint();
        }
        tintRingPaint.setColor(colorInt);
        tintRingPaint.setStrokeWidth(ringRadius);
        tintRingPaint.setAntiAlias(true);
        tintRingPaint.setAntiAlias(true);
        tintRingPaint.setStyle(Paint.Style.STROKE);
        tintRingPaint.setAlpha(60);
        play_bitmap = OneBitmapUtil.drawColorToBitmap(play_bitmap,colorInt, PorterDuff.Mode.SRC_IN);
        pause_bitmap = OneBitmapUtil.drawColorToBitmap(pause_bitmap,colorInt, PorterDuff.Mode.SRC_IN);
//        if(colorInt==Color.WHITE) {
//            draw_bitmap = play_white_bitmap;
//        }else {
//            draw_bitmap = play_black_bitmap;
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRing(canvas);
        drawButton(canvas);
    }
    private void drawRing(Canvas canvas){
        canvas.drawArc(oval,-90,360,false,tintRingPaint);
        canvas.drawArc(oval,-90,degree,false,ringPaint);
    }
    private void drawButton(Canvas canvas){
        if(isPlaying){
            canvas.drawBitmap(pause_bitmap,radius-pause_bitmap.getWidth()/2,radius-pause_bitmap.getHeight()/2,defaultPaint);
        }else {
            canvas.drawBitmap(play_bitmap,radius-play_bitmap.getWidth()/2,radius-play_bitmap.getHeight()/2,defaultPaint);
        }
        //canvas.drawBitmap(draw_bitmap,0,0,defaultPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void setButtonBitmap(Boolean isPlaying){
//        if(isPlaying){
//            //Log.v("OneSeekBar","请绘制暂停bitmap");
//            if(colorInt==Color.WHITE) {
//                draw_bitmap = pause_white_bitmap;
//            }else {
//                draw_bitmap = pause_black_bitmap;
//            }
//            invalidate();
//            //isPlaying = false;
//        }else {
//            //Log.v("OneSeekBar","请绘制播放bitmap");
//            if(colorInt==Color.WHITE) {
//                draw_bitmap = play_white_bitmap;
//            }else {
//                draw_bitmap = play_black_bitmap;
//            }
//            invalidate();
//            //isPlaying = true;
//        }
        this.isPlaying = isPlaying;
        invalidate();
    }
    public void setDegree(float degree){
        this.degree = degree;
        invalidate();
    }
    public void setOnOneSeekBarListener(OnOneSeekBarListener oneSeekBarListener){
        this.oneSeekBarListener = oneSeekBarListener;
    }
    public void setProgress(float progress){
        setDegree(progress*360);
    }
    private float getProgress(MotionEvent motionEvent){
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float indexX = x - centreX;
        float indexY = y - centreY;
        float length = (float) Math.sqrt(Math.pow((indexX), 2) + Math.pow((indexY), 2));
        if (length > innerRadius) {
            if(indexY!=0) {
                degree = (float) (Math.atan((indexX) / (indexY)) / Math.PI * 180.0);
                if(indexX>0){
                    if(indexY>0){
                        degree=-degree;
                        degree+=180;
                        Log.v("OneSeekBar", "indexX>0,indexY>0"+degree);
                    }else {
                        degree=-degree;
                        Log.v("OneSeekBar", "indexX>0,indexY<0"+degree);
                    }
                }else {
                    if(indexY>0){
                        degree=-degree;
                        Log.v("OneSeekBar", "indexX<0,indexY>0"+degree);
                        degree+=180;
                    }else {
                        degree=-degree;
                        Log.v("OneSeekBar", "indexX<0,indexY<0"+degree);
                        degree+=360;
                    }
                }
                float progress = degree / 360;
                return progress;
            }
        } else {
            return -1.f;
        }
        return -1.f;
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        float progress = getProgress(motionEvent);
        if(progress==-1.f){
            oneSeekBarListener.onButtonClick();
        }else {
            oneSeekBarListener.onSeekBarUpdated(progress);
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}
