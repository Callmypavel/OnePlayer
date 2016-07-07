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
    int transferNum = 0;
    protected OneApplication oneApplication;
    Handler handler;
    Boolean isNotClikable = true;
    Boolean isFirstTime = true;
    private int totalTime;
    private int currentTime;
    @BindView(R.id.one_seekbar)
    OneSeekBar oneSeekBar;
    @BindView(R.id.play_view_previous)
    public ImageButton previousButton;
    @BindView(R.id.play_view_next)
    public ImageButton nextButton;
    @BindView(R.id.play_view_playmode)
    public ImageButton playModeButton;
    @BindView(R.id.play_view_queue)
    public ImageButton queueButton;
    public TextView updateText;
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
    @BindView(R.id.play_view_duration)
    public TextView durationText;
    @BindView(R.id.play_view_progress)
    public TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oneApplication = (OneApplication) getApplication();
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);
        oneSeekBar.setOnOneSeekBarListener(new OnOneSeekBarListener() {
            @Override
            public void onSeekBarUpdated(float progress) {
                int targetTime = (int) (progress * totalTime);
                progressText.setText(ten2sixty(targetTime));
                if (!oneApplication.isNew()) {
                    currentTime = targetTime;
                    oneApplication.seekTo(targetTime * 1000);
                    oneApplication.play();
                } else {
                    oneSeekBar.setProgress(0);
                    progressText.setText(ten2sixty(0));
                }
            }
            @Override
            public void onButtonClick() {
                oneApplication.play();
            }
        });
    }

    public void setWaveData(byte[] data){
        oneWaveFromView.setData(data);

    }


    public String ten2sixty(int ten) {
        String sixty;
        String minute = ten / 60 + "";
        String second = ten % 60 + "";
        if (ten / 60 >= 0 && ten / 60 < 10) {
            minute = "0" + minute;
        }
        if (ten % 60 >= 0 && ten % 60 < 10) {
            second = "0" + second;
        }
        sixty = minute + ":" + second;
        return sixty;
    }
    public void setMusicProgress(int time){

    }
    public void setMusicDuration(int duration){

    }

    public void selectMusic(Music music,int position) {

    }



}
