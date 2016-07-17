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

import com.example.peacemaker.oneplayer.databinding.OneAlbumDetailActivityBinding;
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
    int transferNum = 0;
    protected OneApplication oneApplication;
    Handler handler;
    Boolean isNotClikable = true;
    Boolean isFirstTime = true;
    private int totalTime;
    private int currentTime;
    private int currentColor = Color.WHITE;
    private Bitmap currentBitmap;
    private int playMode=1;
    private boolean isPlayView = false;
    private boolean isWhite = true;
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

    protected void initialize() {
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
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playModeButton.setOnClickListener(this);
        queueButton.setOnClickListener(this);
        bottomControllBar.setOnClickListener(this);
        playButton.setOnClickListener(this);
        mainContentBar.setBottom_controll_bar((RelativeLayout) mainContentBar.getChildAt(0));
        mainContentBar.setOnReachMaxListener(new OnReachMaxListener() {
            @Override
            public void onReachMax() {
                disableListview();
            }
        });

    }
    protected void banClick(){
        oneSeekBar.setClickable(false);
        nextButton.setClickable(false);
        previousButton.setClickable(false);
        playModeButton.setClickable(false);
    }

    public void setWaveData(byte[] data){
        oneWaveFromView.setData(data);

    }

    @Override
    public void onBackPressed() {
        if(isPlayView){
         quitPlayView();
        }else {
            super.onBackPressed();
        }
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

    public void selectMusic(boolean isDetail,Music music,int position) {
        oneApplication.selectMusic(music,isDetail,position);
    }
    public void updateMusicInfo(Music currentMusic){
        currentBitmap = currentMusic.getMiddleAlbumArt(this);
        Palette.generateAsync(currentBitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int musicColor = palette.getDarkVibrantColor(Color.WHITE);
                bottomControllBar.setBackgroundColor(musicColor);
                playViewControllBar.setBackgroundColor(musicColor);
                topToolBar.setBackgroundColor(musicColor);
                oneWaveFromView.setPaintColor(musicColor);
                if(!ColorUtil.getContrast(musicColor,currentColor)){
                    if (currentColor==Color.WHITE){
                        currentColor = Color.BLACK;
                    }else {
                        currentColor = Color.WHITE;
                    }
                }
                initColor();
            }
        });
        bottomAblumImageView.setImageBitmap(currentBitmap);
        bottomSingerNameText.setText(currentMusic.getArtist());
        bottomSongNameText.setText(currentMusic.getDisplayName());
        playViewAblumImageView.setImageBitmap(currentBitmap);
        topToolBarSingerNameText.setText(currentMusic.getArtist());
        topToolBarSongNameText.setText(currentMusic.getDisplayName());

    }
    private void initColor(){
        progressText.setTextColor(currentColor);
        durationText.setTextColor(currentColor);
        bottomSingerNameText.setTextColor(currentColor);
        bottomSongNameText.setTextColor(currentColor);
        topToolBarSingerNameText.setTextColor(currentColor);
        topToolBarSongNameText.setTextColor(currentColor);
        oneSeekBar.setColorInt(currentColor);
        oneSeekBar.setButtonBitmap(true);
        mainContentBar.setBackgroundColor(currentColor);
        if(currentColor==Color.WHITE){
            playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
            previousButton.setImageResource(R.drawable.ic_skip_previous_white_48dp);
            nextButton.setImageResource(R.drawable.ic_skip_next_white_48dp);
            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
            int i = transferNum % 3;
            switch (i) {
                case 0:
                    playModeButton.setImageResource(R.drawable.ic_repeat_white_48dp);
                    break;
                case 1:
                    playModeButton.setImageResource(R.drawable.ic_repeat_one_white_48dp);
                    break;
                case 2:
                    playModeButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
                    break;
            }
        }else if (currentColor==Color.BLACK){
            //bottomProgress.setBackgroundColor(Color.GRAY);
            playButton.setImageResource(R.drawable.pause);
            previousButton.setImageResource(R.drawable.previous);
            nextButton.setImageResource(R.drawable.next);
            queueButton.setImageResource(R.drawable.queue);
            int i = transferNum % 3;
            switch (i) {
                case 0:
                    playModeButton.setImageResource(R.drawable.repeat);
                    break;
                case 1:
                    playModeButton.setImageResource(R.drawable.repeat_one);
                    break;
                case 2:
                    playModeButton.setImageResource(R.drawable.shuffle);
                    break;
            }
        }
        if(!isNew) {
            initNotification(false, currentBitmap);
        }else {
            initNotification(true, currentBitmap);
        }
    }
    public void updateSeekbar() {
        try {
            totalTime = Integer.parseInt(oneApplication.getCurrentMusic().getDuration()) / 1000;
        }catch (Exception e){
            Log.v("MainActivity","获取时间出错");
            e.printStackTrace();
            totalTime = 0;
        }
        progressText.setText(ten2sixty(0));
        durationText.setText(ten2sixty(totalTime));
        oneSeekBar.setDegree(0);

    }

    public void queue() {
        quitPlayView();

    }
    public void toPlayView(){
        Log.v("MainActivity","toPlayView()");
        mainContentBar.toMaxHeight();
        isPlayView = true;
    }
    public void quitPlayView(){
        Log.v("MainActivity","quitPlayView()");
        if(!isSingerDetail&&!isAblumDetail) {
            enableListview();
        }
        mainContentBar.toMinHeight();
        isPlayView = false;
    }
    public void setTempmusicArrayList(ArrayList<Music> musicArrayList){
        tempmusicArrayList = musicArrayList;
        musicNumber = musicArrayList.size();

    }
    private void updateMusicArrayList(ArrayList<Music> musicArrayList){
        musicProvider = new MusicProvider(musicArrayList);
        songArraylist = musicProvider.getSongs();
        singerArraylist = musicProvider.getSingers();
        albumArraylist = musicProvider.getAlbums();
        currentPosition = 0;
        setTempmusicArrayList(musicArrayList);
    }



}
