package one.peace.oneplayer.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.databinding.BindingAdapter;
import one.peace.oneplayer.R;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.OneBitmapUtil;

/**
 * Created by ouyan on 2016/6/9.
 */

public class CircleSeekBar extends View implements GestureDetector.OnGestureListener{
    private static String TAG = "CircleSeekBar";
    private Bitmap draw_bitmap;
    private Bitmap play_white_bitmap;
    private Bitmap pause_white_bitmap;
    private Bitmap play_black_bitmap;
    private Bitmap pause_black_bitmap;
    private int centreX;
    private int centreY;
    private int radius;
    private int innerRadius;
    private int ringRadius;
    private RectF oval;
    //进度圆环
    private Paint ringPaint;
    //进度背景圆环
    private Paint tintRingPaint;
    private Paint defaultPaint;
    private float degree = 0;
    private int colorInt = Color.WHITE;
    private OnSeekBarActionListener mOnSeekBarActionListener;
    private GestureDetector gestureDetector;
    //大环占屏幕宽度的比
    private float occupyRatio = 0.17f;
    //小环占大环的比
    private float innerRingRatio = 0.78f;
    //按键图占整体View的比
    private float buttonRatio = 0.6f;
    private boolean isPlaying = false;
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

    @BindingAdapter("android:isWhite")
    public static void setIsWhite(CircleSeekBar circleSeekBar, boolean isWhite) {
        int colorInt = isWhite?Color.WHITE:Color.BLACK;
        circleSeekBar.setColorInt(colorInt);
        circleSeekBar.setPaintColor(colorInt);
        circleSeekBar.updateButtonBitmap();
    }
    @BindingAdapter("android:seekBarColor")
    public static void setSeekBarColor(CircleSeekBar circleSeekBar, int color) {
        circleSeekBar.setColorInt(color);
    }
    @BindingAdapter("android:isPlaying")
    public static void setIsPlaying(CircleSeekBar circleSeekBar, boolean isPlaying) {
        circleSeekBar.setPlaying(isPlaying);
        circleSeekBar.updateButtonBitmap();
    }

    @BindingAdapter("android:percentage")
    public static void setPercentage(CircleSeekBar circleSeekBar, float percentage) {
        circleSeekBar.setProgress(percentage);
    }

    @BindingAdapter("android:occupyRatio")
    public static void setOccupyRatio(CircleSeekBar circleSeekBar, float ratio) {
        circleSeekBar.setOccupyRatio(ratio);
        LogTool.log(TAG, "查看宽高设置情况:占宽比" + ratio);
        circleSeekBar.resize();
        circleSeekBar.invalidate();
    }

    /*
    重新设置绘制范围并更新
     */
    private void resize() {
        Context context = getContext();
        DisplayMetrics dm =getResources().getDisplayMetrics();
        radius = (int) (dm.widthPixels * occupyRatio / 2);
        innerRadius = (int) (radius * innerRingRatio);
        ringRadius = radius - innerRadius;
        LogTool.log(TAG, "查看宽高设置情况:三巨头", radius, innerRadius, ringRadius);
        LogTool.log(TAG, "查看宽高设置情况:占宽比和内环比:" + occupyRatio + "," + innerRingRatio);

        centreX = radius;
        centreY = radius;
        oval = new RectF(centreX - radius + ringRadius/2, centreY - radius + ringRadius/2, centreX + radius - ringRadius/2, centreY + radius - ringRadius/2);

        play_white_bitmap = OneBitmapUtil.zoomImg(context, R.drawable.play_bare_white, (int) (2 * radius * buttonRatio), (int) (2 * radius * buttonRatio));
        pause_white_bitmap = OneBitmapUtil.zoomImg(context, R.drawable.pause_bare_white, (int) (2 * radius * buttonRatio), (int) (2 * radius * buttonRatio));
        play_black_bitmap = OneBitmapUtil.zoomImg(context, R.drawable.play_bare_black, (int) (2 * radius * buttonRatio), (int) (2 * radius * buttonRatio));
        pause_black_bitmap = OneBitmapUtil.zoomImg(context, R.drawable.pause_bare_black, (int) (2 * radius * buttonRatio), (int) (2 * radius * buttonRatio));

        draw_bitmap = play_white_bitmap;
    }

    private void initialize(Context context) {
        resize();

        setPaintColor(colorInt);
        defaultPaint = new Paint();

        gestureDetector = new GestureDetector(context,this);
    }

    public void setOccupyRatio(float occupyRatio) {
        this.occupyRatio = occupyRatio;

    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setColorInt(int colorInt){

        this.colorInt = colorInt;

    }

    public void setPaintColor(int colorInt){
        if(ringPaint==null) {
            ringPaint = new Paint();
            ringPaint.setStrokeWidth(ringRadius);
            ringPaint.setAntiAlias(true);
            ringPaint.setStyle(Paint.Style.STROKE);
        }
        ringPaint.setColor((colorInt == Color.WHITE)?Color.BLACK:Color.WHITE);
        if(tintRingPaint==null) {
            tintRingPaint = new Paint();
            tintRingPaint.setStrokeWidth(ringRadius);
            tintRingPaint.setAntiAlias(true);
            tintRingPaint.setStyle(Paint.Style.STROKE);
            tintRingPaint.setAlpha(60);
        }
        tintRingPaint.setColor(colorInt);
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
        canvas.drawBitmap(draw_bitmap, centreX - draw_bitmap.getWidth() / 2, centreY - draw_bitmap.getWidth() / 2, defaultPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void updateButtonBitmap(){
        //LogTool.logCrime(this,"改变按键图片"+isPlaying);
        if(isPlaying){
            if(colorInt== Color.WHITE) {
                draw_bitmap = pause_white_bitmap;
            }else {
                draw_bitmap = pause_black_bitmap;
            }
        }else {
            if(colorInt== Color.WHITE) {
                draw_bitmap = play_white_bitmap;
            }else {
                draw_bitmap = play_black_bitmap;
            }
        }
        postInvalidate();
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
