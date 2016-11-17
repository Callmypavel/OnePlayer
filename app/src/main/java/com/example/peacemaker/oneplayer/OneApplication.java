package com.example.peacemaker.oneplayer;

import android.app.Activity;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
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
    private OneConfig oneConfig ;
    private boolean isNull = false;
    private ArrayList<Music> songArraylist;
    public MusicProvider musicProvider;
    BroadcastReceiver playReceiver;
    BroadcastReceiver previousReceiver;
    BroadcastReceiver nextReceiver;
    private OneActivity oneActivity;
    private boolean isNotiClikable = true;
    public MusicState musicState = new MusicState();
    public Music currentMusic;
    private Music targetMusic;
    private int duration = 0;
    private OneLogger oneLogger;
    private Activity currentActivity;
    private OnePlayer onePlayer;
    //private MusicService musicService;
    private int statusColor;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//
//            musicService = ((MusicService.MusicBinder) service).getMusicService();
//            System.out.println("接受音乐服务" + musicService);
//
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };


    @Override
    public void onCreate() {
        super.onCreate();
        oneLogger = new OneLogger();
        oneLogger.getLog();
        //initService();
        oneConfig = DatabaseOperator.loadConfig(this);
        LogTool.log("OneApplcation","onCreate()刚加载的配置"+oneConfig.getBandLevels().size());
    }

//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        unbindService(serviceConnection);
//    }
//
//    public void initService() {
//        Log.v("主Activity", "初始化服务");
//        Intent i = new Intent(this, MusicService.class);
//        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
//        Log.v("主Activity", "初始化服务完毕");
//    }

    public OneConfig getOneConfig() {
        LogTool.log("OneApplication","getOneConfig()获取配置"+oneConfig.toString());
        return oneConfig;
    }

    public void setOneConfig(OneConfig oneConfig) {
        LogTool.log("OneApplication","setOneConfig()设置配置"+oneConfig.toString());
        this.oneConfig = oneConfig;
    }
    public void setThemeColor(int color){
        if(oneConfig!=null){
            oneConfig.setThemeColor(color);
        }
    }
    public int getThemeColor(){
        return oneConfig.getThemeColor();
    }
    public OnePlayer getOnePlayer(){
        return onePlayer;
    }


    public void saveOneConfig(Context context){
        DatabaseOperator.saveConfig(oneConfig,context);
    }

    public void setCurrentMusic(Music currentMusic){
        Log.v("OneApplication","setCurrentMusic()"+currentMusic.getDisplayName());
        if(this.currentMusic==null){
            this.currentMusic = new Music();
        }
        this.currentMusic.update(currentMusic);

    }
    public void setTargetMusic(Music targetMusic){
        this.targetMusic = targetMusic;
    }
    public void unRegister(){
        if(playReceiver!=null) {
            unregisterReceiver(playReceiver);
            unregisterReceiver(nextReceiver);
            unregisterReceiver(previousReceiver);
        }
    }
    public ArrayList<Music> getSingerArrayList(){
        if(musicProvider!=null){
            return musicProvider.getSingers();
        }
        return null;
    }
    public ArrayList<Music> getSongArrayList(){
        if(musicProvider!=null){
            return musicProvider.getSongs();
        }
        return null;
    }
    public ArrayList<Music> getAlbumArrayList(){
        if(musicProvider!=null){
            return musicProvider.getAlbums();
        }
        return null;
    }
    public ArrayList<IndexedMusic> getIndexedSingerArrayList(){
        if(musicProvider!=null){
            return musicProvider.getIndexedSingers();
        }
        return null;
    }
    public ArrayList<IndexedMusic> getIndexedAlbumArrayList(){
        if(musicProvider!=null){
            return musicProvider.getIndexedAlbums();
        }
        return null;
    }
    public ArrayList<IndexedMusic> getIndexedSongArrayList(){
        if(musicProvider!=null){
            return musicProvider.getIndexedSongs();
        }
        return null;
    }
    private OneActivity getOneActivty(){
        return oneActivity;
    }
    public Music getTargetMusic(){
        return targetMusic;
    }
    public void setOneActivity(OneActivity oneActivity){
        this.oneActivity = oneActivity;
        Log.v("OneApplication","setOneActivity()"+oneActivity);
    }
    public void setOneActivity(final MusicProvider musicProvider, final OneActivity oneActivity){
        Log.v("OneApplication","setOneActivity()");
        oneLogger.stopLogging();
        this.oneActivity = oneActivity;
        this.musicProvider = musicProvider;
        songArraylist = musicProvider.getSongs();
        if (musicProvider.getCount() == 0) {
            Toast.makeText(this, "抱歉，没有歌曲", Toast.LENGTH_SHORT).show();
            musicState.setIsClickable(false);
            return;
        }
        initReceiver();
        onePlayer = new OnePlayer(oneConfig,songArraylist, 0, new OnMusicListener() {
            @Override
            public void onComple() {
            }

            @Override
            public void onMusicChanged(Music music) {
                Log.v("OneApplication","onMusicChanged()"+music.getDisplayName());
                setCurrentMusic(music);
                if(oneActivity instanceof MainActivity){
                    ((MainActivity)oneActivity).refreshPlaylist();
                }
                updateMusicInfo();
            }

            @Override
            public void onMusicTickling(int time) {
                OneApplication.this.musicState.setProgress(ten2sixty(time));
                musicState.setPercentage(time*1.f/duration);

            }

            @Override
            public void onPrepared(int duration) {
                duration = duration/1000;
                Log.v("OneApplication","onPrepared()总时长"+duration);
                OneApplication.this.duration = duration;
                OneApplication.this.musicState.setDuration(ten2sixty(duration));
                musicState.setIsPlaying(false);
                initNotification(false,null);
            }

            @Override
            public void onError(int what, int extra) {

            }

            @Override
            public void onWaveForm(byte[] data) {
                musicState.setWaveformdata(data);
            }

            @Override
            public void onPause() {
                Log.v("OneApplication","onPause()");
                musicState.setIsPlaying(true);
                initNotification(true,null);
            }

            @Override
            public void onContinue() {
                Log.v("OneApplication","onContinue()");
                musicState.setIsPlaying(false);
                initNotification(false,null);
            }

            @Override
            public void onSoundEffectLoaded(OneConfig oneConfig){
                setOneConfig(oneConfig);

            }

        },false)
        ;
        LogTool.log("OneApplication","onSoundEffectLoaded()"+getOnePlayer().getEnvironmentalReverb().toString());


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

    public void updateMusicInfo(){
        Log.v("OneApplication","updateMusicInfo()");
        musicState.setCurrentBitmap(currentMusic.getMiddleAlbumArt(this));
        Log.v("OneApplication","updateMusicInfo()检查bitmap"+musicState.getCurrentBitmap());
        Palette.Builder builder = Palette.from(musicState.getCurrentBitmap());
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int color = palette.getDarkVibrantColor(Color.WHITE);
                musicState.setMusicColor(color);
                if(oneActivity.isPlayView) {
                    LogTool.log("OneApplication","设置状态栏颜色为"+color);
                    OneStatusUtil.setStatusColor(oneActivity, color);
                }
                int currentColor;
                if(musicState.getIsWhite()){
                    currentColor = Color.WHITE;
                }else {
                    currentColor = Color.BLACK;
                }
                if(!ColorUtil.getContrast(musicState.getMusicColor(),currentColor)){
                    musicState.setIsWhite(!musicState.getIsWhite());
                }
                if(!isNew()) {
                    initNotification(false, musicState.getCurrentBitmap());
                }else {
                    initNotification(true, musicState.getCurrentBitmap());
                }
            }
        });


    }
    public void unSeekable(){
        musicState.setPercentage(0);
        musicState.setProgress("00:00");
    }
    public void seekTo(float progress){
        int second = (int)(progress*duration);
        Log.v("OneApplication","seekTo()查看秒"+second);
        musicState.setProgress(ten2sixty(second));
        onePlayer.seekto(second*1000);
        //play();
    }
    public void play() {
        onePlayer.play();
    }
    public boolean isNew(){
        return !onePlayer.isStarted;
    }
    public void changePlayMode(){
        onePlayer.changePlayMode();
        int playMode = musicState.getPlayMode();
        if(playMode<3){
            playMode=playMode+1;
        }else {
            playMode=1;
        }
        musicState.setPlayMode(playMode);
    }
    public void queue(){
        oneActivity.quitPlayView();
    }
//    public void toPlayView(){
//        oneActivity.toPlayView();
//    }


    public void next() {
        onePlayer.changeMusic(true);
    }

    public void previous() {
        onePlayer.changeMusic(false);
    }
    public void selectMusic(Music music,int position){
        if(music.isPlayable()) {
            if(!music.equals(currentMusic)) {
                oneActivity.toPlayView();
                if(oneActivity instanceof MainActivity){
                    onePlayer.setPlayList(musicProvider.getSongs());
                }else {
                    onePlayer.setPlayList(targetMusic.getSecondItems());
                 }
                onePlayer.selectMusic(music, position);

            }

        }
    }
    public void updateTargetMusic(Music music){
        if(targetMusic==null){
            targetMusic = new Music();
        }
        targetMusic.update(music);
    }
    private void initNotification(boolean isPlayState,Bitmap bitmap) {
        Log.v("OneApplication","initNotification()发出通知");
        if (isNull) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(OneApplication.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//
        PendingIntent pendingIntent = PendingIntent.getActivity(oneActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = buildRemoteViews(isPlayState,bitmap,false);
        RemoteViews bigRemoteViews = buildRemoteViews(isPlayState,bitmap,true);
        //remoteViews.setProgressBar(R.id.noti_seekBar, Integer.parseInt(musicArrayList.get(currentPosition).getDuration()) / 1000, 0, false);
        Notification.Builder builder;
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= 24) {
            builder = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("title")
                    .setContentText("describe")
                    .setCustomContentView(remoteViews)
                    .setCustomBigContentView(bigRemoteViews)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.start_small)
                    .setOngoing(true);
            notification = builder.build();
        }else {
            builder = new Notification.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("title")
                    .setContentText("describe")
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.start_small)
                    .setOngoing(true);
            notification = builder.build();
            notification.contentView = remoteViews;
            notification.bigContentView = bigRemoteViews;
        }
        notification.priority = Notification.PRIORITY_HIGH;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(0, notification);


    }
    private RemoteViews buildRemoteViews(boolean isPlayState,Bitmap bitmap,boolean isBig){
        RemoteViews remoteViews;
        if(isBig){
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_expand);
        }else {
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        }
        int currentColor = musicState.getIsWhite()?Color.WHITE:Color.BLACK;
        remoteViews.setTextViewText(R.id.noti_singer,currentMusic.getArtist());
        remoteViews.setTextViewText(R.id.noti_name, currentMusic.getDisplayName());
        remoteViews.setInt(R.id.noti_singer,"setTextColor",currentColor);
        remoteViews.setInt(R.id.noti_name,"setTextColor",currentColor);
        remoteViews.setInt(R.id.noti_background,"setBackgroundColor",musicState.getMusicColor());
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
        return remoteViews;
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
    public void destroy(){
        //musicService.onDestroy();
        onePlayer.release();
        System.exit(0);
    }
}
