package one.peace.oneplayer.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import one.peace.oneplayer.R;
import one.peace.oneplayer.util.OneBitmapUtil;

/**
 * Created by ouyan on 2016/6/9.
 */

public class CircleSeekBar extends View implements GestureDetector.OnGestureListener{
    private Bitmap draw_bitmap;
    private Bitmap play_white_bitmap;
    private Bitmap pause_white_bitmap;
    private Bitmap play_black_bitmap;
    private Bitmap pause_black_bitmap;
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
    private OnSeekBarActionListener mOnSeekBarActionListener;
    private GestureDetector gestureDetector;
    public CircleSeekBar(Context context) {
        super(context);
        initialize(context);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);


    }
    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public interface OnSeekBarActionListener {
        void onProgressUpdated(float progress);
        void onButtonClick();
    }

    private void initialize(Context context){
        DisplayMetrics dm =getResources().getDisplayMetrics();
        radius = (int)(dm.widthPixels/6.f);
        ringRadius = (int)(dm.widthPixels/27.f);

        centreX = radius;
        centreY = radius;
        oval = new RectF(centreX - radius + ringRadius/2, centreY - radius + ringRadius/2, centreX + radius - ringRadius/2, centreY + radius - ringRadius/2);
        innerRadius = radius - ringRadius;
        play_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.play_bare_white,2*radius,2*radius);
        pause_white_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.pause_bare_white,2*radius,2*radius);
        play_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.play_bare_black,2*radius,2*radius);
        pause_black_bitmap = OneBitmapUtil.zoomImg(context,R.drawable.pause_bare_black,2*radius,2*radius);

        draw_bitmap = play_white_bitmap;
        setPaintColor();
        defaultPaint = new Paint();

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
        tintRingPaint.setStyle(Paint.Style.STROKE);
        tintRingPaint.setAlpha(60);
        if(colorInt== Color.WHITE) {
            draw_bitmap = play_white_bitmap;
        }else {
            draw_bitmap = play_black_bitmap;
        }
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
        canvas.drawBitmap(draw_bitmap,0,0,defaultPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void setButtonBitmap(Boolean isPlaying){
        if(isPlaying){

            if(colorInt== Color.WHITE) {
                draw_bitmap = pause_white_bitmap;
            }else {
                draw_bitmap = pause_black_bitmap;
            }
            invalidate();

        }else {
            if(colorInt== Color.WHITE) {
                draw_bitmap = play_white_bitmap;
            }else {
                draw_bitmap = play_black_bitmap;
            }
            invalidate();
        }
    }

    public void setDegree(float degree){
        this.degree = degree;
        invalidate();
    }

    public void setOnSeekBarActionListener(OnSeekBarActionListener onSeekBarActionListener) {
        mOnSeekBarActionListener = onSeekBarActionListener;
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
                degree = -degree;
                if(indexX>0){
                    if(indexY>0){
                        degree+=180;
                    }
                }else {
                    if(indexY>0){
                        degree+=180;
                    }else {
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
        if(mOnSeekBarActionListener != null) {
            if (progress == -1.f) {
                mOnSeekBarActionListener.onButtonClick();
            } else {
                mOnSeekBarActionListener.onProgressUpdated(progress);
            }
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