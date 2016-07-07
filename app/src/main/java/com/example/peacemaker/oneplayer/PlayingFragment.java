package com.example.peacemaker.oneplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 请叫我保尔 on 2015/10/1.
 */
public class PlayingFragment extends Fragment implements Animation.AnimationListener {
    CircleImageView circleImageView;
    LayoutInflater layoutInflater;
    Animation rotateAnim;
    MainActivity activity;
    Boolean isStart = false;
    RotateRunnable rotateRunnable;
    Thread rotateThread;
    @Nullable
    @BindView(R.id.OneWaveFromView) OneWaveFromView oneWaveFromView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutInflater = inflater;
        View view = inflater.inflate(R.layout
                .playing_fragment, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        activity = (MainActivity)getActivity();
        init();
        //rotateAnim = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim = AnimationUtils.loadAnimation(activity, R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
        rotateAnim.setFillAfter(true);
        rotateAnim.setAnimationListener(this);
        super.onActivityCreated(savedInstanceState);
    }
    public void init(){
        circleImageView = (CircleImageView)activity.findViewById(R.id.album);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.basetag.setVisibility(View.VISIBLE);
                activity.isAlbuming = false;
                activity.queueButton.setImageResource(R.drawable.ic_album_white_48dp);
                //activity.albumcontainer.setVisibility(View.GONE);
                activity.viewPager.setVisibility(View.VISIBLE);
            }
        });
    }
    public void playAnimation(){
//        Log.v("转动fragment","开始播放");
//        Log.v("开始动画",rotateAnim.getInterpolator()+"");
//        if (rotateAnim != null) {
//            albumImageView.startAnimation(rotateAnim);
//        }
        if(isStart) {
            try {
                rotateThread.start();
            }catch (IllegalThreadStateException e){
                Log.v("出错",e+"");
            }

        }else {
            rotateRunnable = new RotateRunnable();
            isStart = true;
            rotateThread = new Thread(rotateRunnable);
            rotateThread.start();
        }

    }
    public void setWaveData(byte[] data){
        Log.v("PlayingFragment","setWaveData()"+data);
        oneWaveFromView.setData(data);

    }
    public void stopAnimation(){
//        Log.v("转动fragment", "停止播放");
//        Log.v("转动fragment", currentPosition + "");
//        Log.v("转动fragment", duration + "");
//        albumImageView.clearAnimation();
//        Matrix matrix=new Matrix();
//        albumImageView.setScaleType(ImageView.ScaleType.MATRIX); //required
////        matrix.postRotate((float) (((float) currentPosition % 2000.0) / 2000.0 * 360), albumImageView.getWidth() / 2, albumImageView.getMaxHeight() / 2);
////
////        albumImageView.setImageMatrix(matrix);
//
//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.album);
//        // 设置旋转角度
//        matrix.setRotate((float) (((float) currentPosition % 10000.0) / 10000.0 * 360));
//        // 重新绘制Bitmap
//        Log.v("获取宽度",bitmap.getWidth()+"");
//        Log.v("获取高度",bitmap.getHeight()+"");
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        albumImageView.setImageBitmap(bitmap);
        if(rotateRunnable!=null){
            rotateRunnable.stopThread();
        }


    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    public class RotateRunnable implements Runnable{
        public void stopThread() {
            isStart = false;
        }
        @Override
        public void run() {
                while (isStart) {
                    circleImageView.setDegree(circleImageView.getDegree() + 3);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
}
