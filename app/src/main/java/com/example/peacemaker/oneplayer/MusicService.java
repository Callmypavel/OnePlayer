package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.firstparty.shared.FACLConfig;

import java.util.ArrayList;

/**
 * Created by ouyan_000 on 2015/8/27.
 */
public class MusicService extends Service{
    int transferNum = 0;
    private Music currentMusic;
    String lastPlayedMusic;
    int musicNumber;
    public int currentPosition;
    int playMode = cycle;
    final static int cycle = 1;
    final static int looping = 2;
    final static int random = 3;
    MusicService musicService;
    SharedPreferences sharedPreferences;
    ArrayList<Music> musicArrayList;
    DrawerLayout drawerLayout;
    PlaylistAdapter playlistAdapter;
    OneMusicloader oneMusicloader;
    TextView durationText;
    TextView progressText;
    Bundle bundle;
    OnePlayer onePlayer;
    ImageButton previousButton;
    ImageButton playButton;
    ImageButton nextButton;
    ImageButton playModeButton;
    SeekBar seekBar;
    ListView playlistView;
    DatabaseOperator databaseOperator;
    Boolean isPlaying = false;
    MainActivity mainActivity;
    private boolean isNew = false;
    private Music music;
    public MusicService(){

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        System.out.println("调用了onBind");
        bundle = intent.getExtras();
        lastPlayedMusic = bundle.getString("lastPlayedMusic");
        playMode = bundle.getInt("playMode");
        IBinder result = new MusicBinder();
        return result;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        if(onePlayer!=null) {
            onePlayer.release();
        }
    }
    public void pause(){
        onePlayer.pause();
    }
    public void play(){
        onePlayer.play();
    }

    public void setOnePlayer(OnePlayer onePlayer){
        this.onePlayer = onePlayer;
        if(onePlayer!=null){
            onePlayer.setMusicListener(new OnMusicListener() {
                @Override
                public void onComple() {

                }

                @Override
                public void onMusicChanged(Music music) {

                }
            });
        }
    }
    public void startService(){
    }
    public void seekTo(int msec){
        onePlayer.seekto(msec);
    }
    public class MusicBinder extends Binder{
        public MusicService getMusicService(){
            return MusicService.this;
        }
    }


}
