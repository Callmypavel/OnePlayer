package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;

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
    private int fixedSpeed = 15;
    private Handler handler;
    private RelativeLayout bottom_controll_bar;
    private OnReachMaxListener onReachMaxListener;
    @BindView(R.id.bottom_song_name)
    public TextView bottomSongNameText;
    @BindView(R.id.bottom_singer_name)
    public TextView bottomSingerNameText;
    @BindView(R.id.bottom_album_image)
    public ImageView bottomAblumImageView;
    @BindView(R.id.bottom_controll_bar)
    public RelativeLayout bottomControllBar;
    @BindView(R.id.top_tool_bar_song_name)
    public TextView topToolBarSongNameText;
    @BindView(R.id.top_tool_bar_singer_name)
    public TextView topToolBarSingerNameText;
    @BindView(R.id.top_tool_bar)
    public RelativeLayout topToolBar;
    @BindView(R.id.play_view_album)
    public ImageView playViewAblumImageView;
    @BindView(R.id.play_view_controll_bar)
    public RelativeLayout playViewControllBar;
    @BindView(R.id.bottom_play_button)
    public ImageButton playButton;
    @BindView(R.id.main_content_bar)
    public OneLayout mainContentBar;
    @BindView(R.id.play_view_onewaveform)
    public OneWaveFromView oneWaveFromView;

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
//    public void setSinger(String singer){
//
//    }
//    public void setSong(String song){
//
//    }
//    public RelativeLayout getToolBar(){
//
//    }
//    public void setAblumImage(Bitmap bitmap){
//
//    }
//    public void setWaveData(byte[] data){
//
//    }
//    public void setMainColor(int color){
//
//    }
//    public void setBackgroundColor(){
//
//    }
    public void initialize(){
//        bottom_controll_bar = (RelativeLayout) getChildAt(0);
//        bottomAblumImageView = bottom_controll_bar.getChildAt(0);
//        bottomSongNameText = bottom_controll_bar.getChildAt(1);
//        bottomSingerNameText = bottom_controll_bar.getChildAt(2);
//        topToolBar = getChildAt(1);
//        topToolBarSongNameText = topToolBar.getChildAt(0);
//        topToolBarSingerNameText = topToolBar.getChildAt(1);
//        playViewAblumImageView = ((RelativeLayout)getChildAt(2)).getChildAt(0);
//        OneWaveFromView oneWaveFromView= ((RelativeLayout)getChildAt(2)).getChildAt(1);
//        playViewControllBar = getChildAt(3);
//        @BindView(R.id.bottom_play_button)
//        public ImageButton playButton;
//        @BindView(R.id.main_content_bar)
//        public OneLayout mainContentBar;
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
        ViewDataBinding binding = DataBindingUtil.bind(this);
        binding.set


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
        invalidate();
    }
    private void changeChildHeight(){
        if(bottom_controll_bar!=null) {
            ViewGroup.LayoutParams layoutParams = bottom_controll_bar.getLayoutParams();
            layoutParams.height = childcurrentHeight;
            //Log.v("OneLayout", "changeChildHeight()查看高度" + childcurrentHeight);
            bottom_controll_bar.setLayoutParams(layoutParams);
            invalidate();
        }
    }

}
