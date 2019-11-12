package peacemaker.oneplayer.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;

import peacemaker.oneplayer.listener.OnMusicListener;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.tool.OnePlayer;
import peacemaker.oneplayer.entity.*;
import peacemaker.oneplayer.tool.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ouyan on 2016/7/6.
 */

public class OneApplication extends Application{
    private OneConfig oneConfig ;
    private boolean isNull = false;
    private ArrayList<Music> songArraylist;
    public static MusicProvider musicProvider;
    BroadcastReceiver playReceiver;
    BroadcastReceiver previousReceiver;
    BroadcastReceiver nextReceiver;
    private static OneActivity oneActivity;
    private boolean isNotiClikable = true;
    public MusicState musicState = new MusicState();
    public static Music currentMusic;
    public static Boolean isStorePermissionGranted = false;
    public static Boolean isLocationPermissionGranted = false;
    private int duration = 0;
    private OneLogger oneLogger;
    private static Activity currentActivity;
    private static OnePlayer onePlayer;
    public static Boolean isPermissionGranted = false;
    public static Bitmap loadingBitmap = Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
    public static Context context;
    public static OneApplication instance;


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
        context = this;
        instance = this;
        OneLogger.getLog();
        //initService();
        oneConfig = DatabaseOperator.loadConfig(this);
        LogTool.log("OneApplcation","onCreate()刚加载的配置高斯等级"+oneConfig.getBlurRadius());
        init(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SocketUtil.sendSimpleString("hello captain",true);
//            }
//        }).start();

    }

    private void init(Context context){
       //VoiceUtil.init(context);

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
        //LogTool.log("OneApplication","getOneConfig()获取配置"+oneConfig.toString());
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



    public void unRegister(){
        if(playReceiver!=null) {
            unregisterReceiver(playReceiver);
            unregisterReceiver(nextReceiver);
            unregisterReceiver(previousReceiver);
        }
    }
    public ObservableArrayList<SingerInfo> getSingerArrayList(){
        if(musicProvider!=null){
            return musicProvider.getSingers();
        }
        return null;
    }
    public ObservableArrayList<Music> getSongArrayList(){
        if(musicProvider!=null){
            return musicProvider.getSongs();
        }
        return null;
    }
    public ObservableArrayList<AlbumInfo> getAlbumArrayList(){
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

    public void setOneActivity(OneActivity oneActivity){
        this.oneActivity = oneActivity;
        VoiceUtil.setOneWaveFromView(this.oneActivity.oneWaveFromView);
        LogTool.log("OneApplication","setOneActivity()","一般的",oneActivity.getClass().getSimpleName(),oneActivity.oneWaveFromView);
    }
    public void setOneActivity(final MusicProvider musicProvider, final OneActivity oneActivity){
        LogTool.log("OneApplication","setOneActivity()","主要的那个",oneActivity.getClass().getSimpleName(),oneActivity.oneWaveFromView);
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
//                if(oneActivity instanceof MainActivity){
//                    ((MainActivity)oneActivity).refreshPlaylist();
//                }
                updateMusicInfo();
            }

            @Override
            public void onMusicTickling(float time) {
                OneApplication.this.musicState.setProgress(ten2sixty(time));
                musicState.setPercentage(time*1.f/duration);

            }

            @Override
            public void onPrepared(int duration) {
                duration = duration/1000;
                Log.v("OneApplication","onPrepared()总时长"+duration);
                OneApplication.this.duration = duration;
                OneApplication.this.musicState.setDuration(ten2sixty(duration));
                musicState.setIsPlaying(true);
                initNotification(false,null);
            }

            @Override
            public void onError(int what, int extra) {
                LogTool.log("OneApplication","onError()",what,extra);
            }

            @Override
            public void onWaveForm(byte[] data) {
                musicState.setWaveformdata(data);
            }

            @Override
            public void onPause() {
                Log.v("OneApplication","onPause()");
                musicState.setIsPlaying(false);
                initNotification(true,null);
            }

            @Override
            public void onContinue() {
                Log.v("OneApplication","onContinue()");
                musicState.setIsPlaying(true);
                initNotification(false,null);
            }

            @Override
            public void onSoundEffectLoaded(OneConfig oneConfig){
                setOneConfig(oneConfig);

            }

            @Override
            public void onPlayModeChanged(int playMode) {
                musicState.setPlayMode(playMode);
                String text="当前播放模式：";
                switch (playMode){
                    case OnePlayer.cycle:
                        text += "列表循环";break;
                    case OnePlayer.random:
                        text += "随机播放";break;
                    case OnePlayer.looping:
                        text += "单曲循环";break;
                }
                Snackbar.make(OneApplication.this.getOneActivty().mainContentBar,text,Snackbar.LENGTH_SHORT);
            }
        },false);
        LogTool.log("OneApplication","onSoundEffectLoaded()"+getOnePlayer().getEnvironmentalReverb().toString());
        VoiceUtil.setOneWaveFromView(this.oneActivity.oneWaveFromView);

    }


    public String ten2sixty(float ten) {
        //LogTool.log(this,"ten2sixty()","查看走时信息",ten);
        String sixty;
        String minute = (int)ten / 60 + "";
        String second = (int)ten % 60 + "";
        if (ten / 60 >= 0 && ten / 60 < 10) {
            minute = "0" + minute;
        }
        if (ten % 60 >= 0 && ten % 60 < 10) {
            second = "0" + second;
        }
//        if (musicState.getIsPlaying()) {
        sixty = minute + ":" + second;
//        }else {
//            float remain = ten-((int)ten);
//            LogTool.log(this,"ten2sixty()","查看剩余数",remain);
//            if(remain==0.5f){
//                sixty = minute + " " + second;
//            }else {
//                sixty = minute + ":" + second;
//            }
//        }
        //LogTool.log(this,"ten2sixty()","查看60进制后的结果",sixty);
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
                if(musicState.getIsPlayView()) {
                    LogTool.log("OneApplication","设置状态栏颜色为"+color);
                    OneStatusUtil.setStatusColor(oneActivity, Color.argb(0,0,0,0));
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
    }
    public void queue(){
        oneActivity.quitPlayView();
    }
//    public void toPlayView(){
//        oneActivity.toPlayView();
//    }


    public void next() {
        LogTool.log(this,"播放下一首");
        onePlayer.changeMusic(true);
    }

    public void previous() {
        LogTool.log(this,"播放上一首");
        onePlayer.changeMusic(false);
    }
    public void setCurrentMusic(Music currentMusic){
        LogTool.log("OneApplication","setCurrentMusic()","设置当前音乐",currentMusic);

        if(this.currentMusic==null){
            this.currentMusic = new Music();
        }
        this.currentMusic.update(currentMusic);
        //lastMusic = currentMusic;

        //this.currentMusic = currentMusic;
    }
    public static void selectMusic(Music music,int position,ArrayList<Music> playList){
        if(music.isPlayable()) {
            if(!music.equals(currentMusic)) {
//                try {
//                    Thread.sleep(100);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                LogTool.log("OneApplication","selectMusic()","选中音乐",music);

                oneActivity.toPlayView();
                if(oneActivity instanceof MainActivity){
                    onePlayer.setPlayList(musicProvider.getSongs());
                }else {
                    if(playList!=null) {
                        onePlayer.setPlayList(playList);
                    }
                 }
                onePlayer.selectMusic(music, position);

            }

        }
    }

    private void initNotification(boolean isPlayState,Bitmap bitmap) {
        Log.v("OneApplication","initNotification()发出通知"+bitmap);
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
        LogTool.log(this,"buildRemoteViews","通知栏专辑图"+bitmap);
        RemoteViews remoteViews;
        if(isBig){
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_expand);
        }else {
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        }
        //int currentColor = musicState.getIsWhite()?Color.WHITE:Color.BLACK;
        remoteViews.setTextViewText(R.id.noti_singer,currentMusic.getArtist());
        remoteViews.setTextViewText(R.id.noti_name, currentMusic.getDisplayName());
        remoteViews.setInt(R.id.noti_singer,"setTextColor",Color.BLACK);
        remoteViews.setInt(R.id.noti_name,"setTextColor",Color.BLACK);
        //remoteViews.setInt(R.id.noti_background,"setBackgroundColor",musicState.getMusicColor());
        if(bitmap!=null) {
            remoteViews.setImageViewBitmap(R.id.noti_album_image, bitmap);
        }
//        if(currentColor==Color.WHITE){
//            remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.ic_skip_previous_white_48dp);
//            remoteViews.setImageViewResource(R.id.noti_next, R.drawable.ic_skip_next_white_48dp);
//            if (isPlayState) {
//                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_play_circle_fill_white_48dp);
//            }else {
//                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_pause_circle_fill_white_48dp);
//            }
//        }else {
//        remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.previous);
//        remoteViews.setImageViewResource(R.id.noti_next, R.drawable.next);
//        if (isPlayState) {
//            remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_play_circle_outline_black_48dp);
//        }else {
//            remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_pause_circle_outline_black_48dp);
//        }
        remoteViews.setImageViewBitmap(R.id.noti_previous, OneBitmapUtil.drawColorToBitmap(this,R.drawable.previous,Color.BLACK));
        remoteViews.setImageViewBitmap(R.id.noti_next, OneBitmapUtil.drawColorToBitmap(this,R.drawable.next,Color.BLACK));
        remoteViews.setImageViewBitmap(R.id.noti_close, OneBitmapUtil.drawColorToBitmap(this,R.drawable.close_black,Color.BLACK));
        if (isPlayState) {
            remoteViews.setImageViewBitmap(R.id.noti_playandpause, OneBitmapUtil.drawColorToBitmap(this,R.drawable.ic_play_circle_outline_black_48dp,Color.BLACK));
        }else {
            remoteViews.setImageViewBitmap(R.id.noti_playandpause, OneBitmapUtil.drawColorToBitmap(this,R.drawable.ic_pause_circle_outline_black_48dp,Color.BLACK));
        }

//        }
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
        //关闭点击事件
        Intent intent4 = new Intent("closeButton");
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(this, 0, intent4, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_close, pendingIntent4);
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
        //关闭广播
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction("closeButton");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNotiClikable) {
                    closeNotification();
                    destroy();
                }
            }
        }, filter3);
    }

    public void closeNotification() {
        System.out.println("调用关闭通知");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }
    public static void destroy(){
        //musicService.onDestroy();
        onePlayer.release();
        System.exit(0);
    }
    public static int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
