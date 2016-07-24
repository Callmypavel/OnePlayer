package com.example.peacemaker.oneplayer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/7/7.
 */

public class OneActivity extends AppCompatActivity {
    protected OneApplication oneApplication;
    Handler handler;
    protected boolean isPlayView = false;
    @BindView(R.id.one_seekbar)
    OneSeekBar oneSeekBar;
    @BindView(R.id.main_content_bar)
    public OneLayout mainContentBar;
    @BindView(R.id.play_view_onewaveform)
    public OneWaveFromView oneWaveFromView;
    @BindView(R.id.id_appbarlayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.id_coordinatorlayout)
    public CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oneApplication = (OneApplication) getApplication();

    }
//    public void pause(){
//        oneSeekBar.setButtonBitmap(false);
//    }
//    public void start(){
//        oneSeekBar.setButtonBitmap(true);
//    }

    protected void initialize() {
        oneSeekBar.setOnOneSeekBarListener(new OnOneSeekBarListener() {
            @Override
            public void onSeekBarUpdated(float progress) {
                Log.v("OneActivity","onSeekBarUpdated()progress:"+progress);
                if (!oneApplication.isNew()) {
                    oneApplication.seekTo(progress);
                } else {
                   oneApplication.unSeekable();
                }
            }
            @Override
            public void onButtonClick() {
                oneApplication.play();
                //oneSeekBar.setButtonBitmap(false);
            }
        });
        mainContentBar.setBottom_controll_bar((RelativeLayout) mainContentBar.getChildAt(0));
        mainContentBar.setOnReachMaxListener(new OnReachMaxListener() {
            @Override
            public void onReachMax() {
                disableListview();
                isPlayView = true;
            }
        });

    }

    protected void disableListview(){
        Log.v("OneActivity","关闭");
    }
    protected void enableListview(){
        Log.v("OneActivity","开启");
    }
//    protected void banClick(){
//        oneSeekBar.setClickable(false);
//        nextButton.setClickable(false);
//        previousButton.setClickable(false);
//        playModeButton.setClickable(false);
//    }

    public void setWaveData(byte[] data){
        oneWaveFromView.setData(data);

    }

    public void selectMusic(Music music,int position) {
        oneApplication.selectMusic(music,position);
    }
//    public void updateMusicInfo(MusicState musicState, Bitmap bitmap){
//        if(oneWaveFromView!=null) {
//            oneWaveFromView.setPaintColor(musicState.getMusicColor());
//        }
//        if(oneSeekBar!=null) {
//            if (musicState.getIsWhite()) {
//                Log.v("OneActivity","updateMusicInfo()绘制白色");
//                oneSeekBar.setColorInt(Color.WHITE);
//            } else {
//                Log.v("OneActivity","updateMusicInfo()绘制黑色");
//                oneSeekBar.setColorInt(Color.BLACK);
//            }
//            oneSeekBar.setButtonBitmap(true);
//        }
//        if(bottomAblumImageView!=null) {
//            bottomAblumImageView.setImageBitmap(bitmap);
//            playViewAblumImageView.setImageBitmap(bitmap);
//        }
//        Log.v("OneActivity","updateMusicInfo()检查imageView"+bottomAblumImageView+bitmap);
//    }
//    public void updateSeekbar() {
//        try {
//            totalTime = Integer.parseInt(oneApplication.currentMusic.getDuration()) / 1000;
//        }catch (Exception e){
//            Log.v("MainActivity","获取时间出错");
//            e.printStackTrace();
//            totalTime = 0;
//        }
//        oneSeekBar.setDegree(0);
//    }

    public void queue() {
        quitPlayView();

    }
    public void toPlayView(){
        Log.v("OneActivity","toPlayView()");
        mainContentBar.toMaxHeight();
    }

    public void quitPlayView(){
        Log.v("OneActivity","quitPlayView()");
        mainContentBar.toMinHeight();
        isPlayView = false;
        enableListview();
    }




}
