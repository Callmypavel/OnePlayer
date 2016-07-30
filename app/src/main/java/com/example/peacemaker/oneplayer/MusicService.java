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

import java.util.ArrayList;

/**
 * Created by ouyan_000 on 2015/8/27.
 */
public class MusicService extends Service implements Runnable{
    ArrayList<Music> musicArrayList;
    Bundle bundle;
    OnePlayer onePlayer;
    public MusicService(){

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        System.out.println("调用了onBind");
        bundle = intent.getExtras();
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
        System.out.println("服务创建");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        super.onStartCommand(intent, flags, startId);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        onePlayer.release();
    }
    public void pause(){
        onePlayer.pause();
    }
    public void play(){
        onePlayer.play();
    }

    @Override
    public void run() {
        onePlayer.play();
    }
    public void setOnePlayer(OnePlayer onePlayer){
        this.onePlayer = onePlayer;

    }
    public OnePlayer getOnePlayer(){
        return this.onePlayer;
    }

    public class MusicBinder extends Binder{
        public MusicService getMusicService(){
            System.out.println("服务接受开始回传");
            return MusicService.this;
        }
    }


}
