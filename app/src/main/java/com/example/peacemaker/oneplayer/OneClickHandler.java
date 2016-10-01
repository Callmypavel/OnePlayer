package com.example.peacemaker.oneplayer;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ouyan on 2016/7/7.
 */

public class OneClickHandler {
    @BindingAdapter("android:bitmapSrc")
    public static void setBitmapSrc(ImageView imageView, Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
    @BindingAdapter("android:isWhite")
    public static void setIsWhite(OneSeekBar oneSeekBar, boolean isWhite){
            if (isWhite) {
                Log.v("OneActivity","updateMusicInfo()绘制白色");
                oneSeekBar.setColorInt(Color.WHITE);
            } else {
                Log.v("OneActivity","updateMusicInfo()绘制黑色");
                oneSeekBar.setColorInt(Color.BLACK);
            }
            oneSeekBar.setButtonBitmap(true);
    }
    @BindingAdapter("android:isPlaying")
    public static void setIsPlaying(OneSeekBar oneSeekBar, boolean isPlaying){
           if(isPlaying) {
               oneSeekBar.setButtonBitmap(false);
           }else {
               oneSeekBar.setButtonBitmap(true);
           }
    }
    @BindingAdapter("android:paintColor")
    public static void setPaintColor(OneWaveFromView oneWaveFromView, int color){
        oneWaveFromView.setPaintColor(color);
    }
    @BindingAdapter("android:percentage")
    public static void setPercentage(OneSeekBar oneSeekBar, float percentage){
        oneSeekBar.setProgress(percentage);
    }
    @BindingAdapter("android:waveformdata")
    public static void setWaveformdata(OneWaveFromView oneWaveFromView, byte[] data){
        oneWaveFromView.setData(data);
    }
    @BindingAdapter("android:waveformdata")
    public static void setWaveformdata(OneGameView oneGameView, byte[] data){
        oneGameView.setData(data);
    }
    @BindingAdapter("android:bitmapImage")
    public static void setImageResource(ImageView imageView,Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
    @BindingAdapter("android:index")
    public static void setIndex(IndexView indexView, ArrayList<IndexedMusic> indexedMusics){
        indexView.setIndexedMusics(indexedMusics);
    }
    @BindingAdapter("android:highlightColor")
    public static void setHighlightColor(IndexView indexView, int highlightColor){
        indexView.setHighlightColor(highlightColor);
    }
    @BindingAdapter("backgroundColor")
    public static void setBackgroundColor(View view, int color){
        view.setBackgroundColor(color);
        view.invalidate();
    }

    public void onButtonClick(View view){
        OneApplication oneApplication = (OneApplication)(view.getContext().getApplicationContext());
        switch (view.getId()){
            case R.id.play_view_previous:
                oneApplication.previous();
                break;
            case R.id.play_view_next:
                oneApplication.next();
                break;
            case R.id.play_view_queue:
                oneApplication.queue();
                break;
            case R.id.play_view_playmode:
                oneApplication.changePlayMode();
                break;
            case R.id.bottom_play_button:
                oneApplication.play();
                break;
            case R.id.bottom_controll_bar:
                Log.v("OneClickHandler","查看上下文"+view.getContext());
                ((OneActivity)view.getContext()).toPlayView();
                break;
            }
        }
}

