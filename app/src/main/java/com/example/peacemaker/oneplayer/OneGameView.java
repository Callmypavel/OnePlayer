package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by ouyan on 2016/9/16.
 */

public class OneGameView extends View implements View.OnTouchListener{
    private int width;
    private int unitLength;
    private int indexX = -1;
    private int indexY = -1;
    private int standardPoint = 64;
    private int waitTime = 10;
    private long lastTime;
    private Bitmap drumBitmap;
    private int point = 0;
    private float musicScale;
    private int pictureScale = 2;
    private int dismissTime = 200;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Paint paint = new Paint();
    public OneGameView(Context context) {
        super(context);
    }
    public OneGameView(Context context, AttributeSet attrs){
        super(context, attrs, 0);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        width = dm.widthPixels;
        unitLength = width*pictureScale/10;
        drumBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.album);
        drumBitmap = OneBitmapUtil.zoomImg(drumBitmap,unitLength,unitLength);
        lastTime = Calendar.getInstance().getTime().getTime();
        setOnTouchListener(this);
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        musicScale = current*1.f/max;
    }

    public OneGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, width);
    }
    public void drawDrum(){
        this.indexX = (int)(Math.random()*10);
        this.indexY = (int)(Math.random()*10);
        invalidate();
    }
    public void drawDrum(int indexX,int indexY){
        this.indexX = indexX;
        this.indexY = indexY;
        invalidate();
    }

    public void setData(byte[] data){
        if(data!=null){
            for(int i=1;i<32;i++){
                int value = (int)Math.hypot(data[2*i],data[2*i+1]);
                long currentTime = Calendar.getInstance().getTime().getTime();
                if(currentTime>lastTime+waitTime){
                    if (value*0.5/musicScale>standardPoint){
                        //drawDrum(i/3,(width-5*value)/unitLength);
                        drawDrum();
                        //drawDrum(width/(2*unitLength),width/(2*unitLength));
                        lastTime = currentTime;
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(dismissTime);
                                    dismissDrum();
                                }catch (Exception e){

                                }
                            }
                        });
                    }

                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(indexX!=-1&&indexY!=-1){
            canvas.drawBitmap(drumBitmap,indexX*unitLength,indexY*unitLength,paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int indexX = (int)(x/unitLength);
        int indexY = (int)(y/unitLength);
        Log.v("OneGameView","点击坐标("+indexX+","+indexY+")");
        Log.v("OneGameView","预期坐标("+this.indexX+","+this.indexY+")");
        if(indexX==this.indexX&&indexY==this.indexY){
            point+=100;
            Log.v("OneGameView","击中目标,当前分数"+point);
            dismissDrum();
        }
        return false;
    }
    private void dismissDrum(){
        this.indexX = -1;
        this.indexY = -1;
        invalidate();
    }
}
