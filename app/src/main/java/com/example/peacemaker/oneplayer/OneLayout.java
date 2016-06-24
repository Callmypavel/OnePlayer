package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by ouyan on 2016/6/20.
 */

public class OneLayout extends RelativeLayout {
    private int minHeight;
    private int maxHeight;
    private int screenHeight;
    private int screenWidth;
    private int currentHeight;
    private int childcurrentHeight;
    private int fixedSpeed = 10;
    private Handler handler;
    private RelativeLayout bottom_controll_bar;
    private OnReachMaxListener onReachMaxListener;

    public OneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public OneLayout(Context context) {
        super(context);
        init();
    }
    public OneLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void setOnReachMaxListener(OnReachMaxListener onReachMaxListener){
        this.onReachMaxListener = onReachMaxListener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(minHeight==0) {
            minHeight = getHeight();
            currentHeight = minHeight;
            childcurrentHeight = minHeight;
        }
    }

    public void setBottom_controll_bar(RelativeLayout bottom_controll_bar){
        this.bottom_controll_bar = bottom_controll_bar;
    }


    private void init(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
        screenWidth = wm.getDefaultDisplay().getWidth();
        maxHeight = screenHeight;
        //bottom_controll_bar = (RelativeLayout) getChildAt(0);
        Log.v("OneLayout","init()"+bottom_controll_bar);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==9527||msg.what==9526){
                    changeHeight();
                }
                if(msg.what==9526){
                    changeChildHeight();
                }
                if(msg.what==9528){
                    onReachMaxListener.onReachMax();
                }
                return false;
            }
        });

    }
    public void toMaxHeight(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(currentHeight<maxHeight) {
                    Message message = new Message();
                    currentHeight += fixedSpeed;
                    message.what = 9527;
                    if(currentHeight>=maxHeight-minHeight){
                        if(childcurrentHeight>=0) {
                            childcurrentHeight -= fixedSpeed;
                            if(childcurrentHeight<0){
                                childcurrentHeight=0;
                            }
                            message.what = 9526;
                        }
                    }
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(3);
                    } catch (Exception e) {

                    }
                }
                Message message = new Message();
                message.what = 9528;
                handler.sendMessage(message);
                currentHeight = maxHeight;

            }
        });
        thread.start();
    }
    public void toMinHeight(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(currentHeight>minHeight) {
                    Message message = new Message();
                    currentHeight -= fixedSpeed;
                    message.what = 9527;
                    if(currentHeight<=maxHeight-minHeight){
                        if(childcurrentHeight<=minHeight) {
                            childcurrentHeight += fixedSpeed;
                            if(childcurrentHeight>minHeight){
                                childcurrentHeight=minHeight;
                            }
                            message.what = 9526;
                        }
                    }


                    handler.sendMessage(message);
                    try {
                        Thread.sleep(3);
                    } catch (Exception e) {

                    }
                }
                currentHeight = minHeight;

            }
        });
        thread.start();
    }
    private void changeHeight(){
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = currentHeight;
        //Log.v("OneLayout","changHeight()查看高度"+currentHeight);
        setLayoutParams(layoutParams);
        postInvalidate();
    }
    private void changeChildHeight(){
        if(bottom_controll_bar!=null) {
            ViewGroup.LayoutParams layoutParams = bottom_controll_bar.getLayoutParams();
            layoutParams.height = childcurrentHeight;
            //Log.v("OneLayout", "changeChildHeight()查看高度" + childcurrentHeight);
            bottom_controll_bar.setLayoutParams(layoutParams);
            postInvalidate();
        }
    }

}
