package com.example.peacemaker.oneplayer;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.IBinder;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ouyan on 2016/7/6.
 */

public class OneApplication extends Application{
    private Music currentMusic;
    private Music targetMusic;
    public int currentPosition;
    private static int singer = 0;
    private static int album = 1;
    private static int song = 2;
    private int themeColor = Color.WHITE;
    private Bitmap currentBitmap;
    private int musicColor = Color.WHITE;
    private boolean isWhite = true;
    private ArrayList<Music> albumArraylist;
    private ArrayList<Music> singerArraylist;
    private ArrayList<Music> songArraylist;
    MusicProvider musicProvider;
    BroadcastReceiver playReceiver;
    BroadcastReceiver previousReceiver;
    BroadcastReceiver nextReceiver;
    private ArrayList<Music> tempmusicArrayList;
    private OnePlayer onePlayer;
    MusicService musicService;
    private boolean isNull = true;
    int musicNumber;
    Visualizer visualizer;
    Boolean isPlaying = false;
    private Boolean isNew = true;
    private int currentColor;
    private OneActivity oneActivity;
    private boolean isNotiClikable = true;

    public void setCurrentMusic(Music currentMusic){
        this.currentMusic = currentMusic;
    }
    public void setTargetMusic(Music targetMusic){
        this.targetMusic = targetMusic;
    }
    public Music getCurrentMusic(){
        return currentMusic;
    }
    public Music getTargetMusic(){
        return targetMusic;
    }
    public void setOneActivity(MusicProvider musicProvider,final OneActivity oneActivity){
        this.oneActivity = oneActivity;
        this.musicProvider = musicProvider;
        singerArraylist = musicProvider.getSingers();
        albumArraylist = musicProvider.getAlbums();
        songArraylist = musicProvider.getSongs();
        if (musicProvider.getCount() == 0) {
            Toast.makeText(this, "抱歉，没有歌曲", Toast.LENGTH_SHORT).show();
            isNull = true;
            oneActivity.banClick();
            return;
        }
        onePlayer = new OnePlayer(songArraylist, 0, new OnMusicListener() {
            @Override
            public void onComple() {
            }

            @Override
            public void onMusicChanged(Music music) {
                currentMusic = music;
                initNotification(true,currentBitmap);
                updateMusicInfo();
            }

            @Override
            public void onMusicTickling(int time) {
                oneActivity.setMusicProgress(time);
            }

            @Override
            public void onPrepared(int duration) {
                oneActivity.setMusicDuration(duration);
            }

            @Override
            public void onError(int what, int extra) {

            }

            @Override
            public void onWaveForm(byte[] data) {
                oneActivity.setWaveData(data);
            }
        });

    }
    private void updateMusicInfo(){
        currentBitmap = currentMusic.getMiddleAlbumArt(this);
        Palette.generateAsync(currentBitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                musicColor = palette.getDarkVibrantColor(Color.WHITE);
                if(!ColorUtil.getContrast(musicColor,currentColor)){
                    isWhite = !isWhite;
                }
                initColor();
            }
        });
    }
    public void seekTo(int msec){
        onePlayer.seekto(msec);
    }
    public void play() {
        onePlayer.play();
    }
    public boolean isNew(){
        return onePlayer.isStarted;
    }
    public void changePlayMode(){
        onePlayer.changePlayMode();
    }
    public int getPlayMode(){
        return onePlayer.playMode;
    }
    public int getCurrentColor(){
        return this.currentColor;
    }
    public boolean isWhite(){

    }


    public void get
    public void queue(){
        oneActivity.quitPlayView();
    }
    public void toPlayView(){
        oneActivity.toPlayView();
    }


    public void next() {
        onePlayer.changeMusic(true);
    }

    public void previous() {
        onePlayer.changeMusic(false);
    }
    public void selectMusic(Music music,boolean isDetail,int position){
        if(isDetail) {
            onePlayer.setPlayList(getTargetMusic().getSecondItems(), position);
        }else {
            onePlayer.setPlayList(songArraylist, position);
        }
        onePlayer.selectMusic(music,position);
    }
    private void initNotification(boolean isPlayState,Bitmap bitmap) {
        if (tempmusicArrayList == null||tempmusicArrayList.size() == 0) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(OneApplication.this, OneApplication.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.noti_singer,currentMusic.getArtist());
        remoteViews.setTextViewText(R.id.noti_name, currentMusic.getDisplayName());
        remoteViews.setInt(R.id.noti_singer,"setTextColor",currentColor);
        remoteViews.setInt(R.id.noti_name,"setTextColor",currentColor);
        remoteViews.setInt(R.id.noti_background,"setBackgroundColor",musicColor);
        if(bitmap!=null) {
            remoteViews.setImageViewBitmap(R.id.noti_album_image, bitmap);
        }
        if(currentColor==Color.WHITE){
            remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.ic_skip_previous_white_48dp);
            remoteViews.setImageViewResource(R.id.noti_next, R.drawable.ic_skip_next_white_48dp);
            if (isPlayState) {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_play_circle_fill_white_48dp);
            }else {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_pause_circle_fill_white_48dp);
            }
        }else {
            remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.previous);
            remoteViews.setImageViewResource(R.id.noti_next, R.drawable.next);
            if (isPlayState) {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_play_circle_outline_black_48dp);
            }else {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_pause_circle_outline_black_48dp);
            }
        }
        //播放键点击事件
        Intent intent1 = new Intent("playButton");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_playandpause, pendingIntent1);
        //下一首点击事件
        Intent intent2 = new Intent("nextButton");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, pendingIntent2);
        //上一首点击事件
        Intent intent3 = new Intent("previousButton");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 0, intent3, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_previous, pendingIntent3);

        //remoteViews.setProgressBar(R.id.noti_seekBar, Integer.parseInt(musicArrayList.get(currentPosition).getDuration()) / 1000, 0, false);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("title")
                .setContentText("describe")
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.start_small)
                .setOngoing(true)
                .build();
        notification.priority = Notification.PRIORITY_HIGH;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(0, notification);


    }


    public void initReceiver() {
        //注册播放广播
        playReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                play();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("playButton");
        registerReceiver(playReceiver, filter);

        //上一首广播
        previousReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNotiClikable) {
                    previous();
                }
            }
        };
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("previousButton");
        registerReceiver(previousReceiver, filter1);

        //下一首广播
        nextReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNotiClikable) {
                    next();
                }
            }
        };
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("nextButton");
        registerReceiver(nextReceiver, filter2);
    }

    public void closeNotification() {
        System.out.println("调用关闭通知");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }
}
