package one.peace.oneplayer.ui.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.palette.graphics.Palette;
import one.peace.oneplayer.MainActivity;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.OneApplication;
import one.peace.oneplayer.global.config.Config;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.player.MusicState;
import one.peace.oneplayer.music.player.OnePlayer;
import one.peace.oneplayer.ui.music.SoundEffectActivity;
import one.peace.oneplayer.ui.view.CircleSeekBar;
import one.peace.oneplayer.ui.view.StretchLayout;
import one.peace.oneplayer.util.ColorUtil;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.OneBitmapUtil;
import one.peace.oneplayer.util.PermissionUtil;
import one.peace.oneplayer.util.StringUtil;
import one.peace.oneplayer.util.ViewTool;



/**
 * Created by pavel on 2019/11/25.
 */
public abstract class BaseMusicControllActivity<T extends ViewModel> extends BaseActivity<T> {
    private StretchLayout stretchLayout;
    private MusicState mMusicState;
    private Config mConfig;
    private BroadcastReceiver playReceiver;
    private BroadcastReceiver previousReceiver;
    private BroadcastReceiver nextReceiver;
    private OnePlayer mOnePlayer;
    //状态栏的初始颜色
    private int statusInitColor = Color.argb(0, 0, 0, 0);

    @Override
    protected void onInitData(T viewModel, ViewDataBinding viewDataBinding) {
        initialize();
    }

    protected void initialize() {
        mMusicState = MusicState.getInstance(this);
        mOnePlayer = OnePlayer.getInstance(this);
        mOnePlayer.setOnMusicListener(new OnePlayer.OnMusicListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onMusicChanged(MusicInfo musicInfo) {
                mMusicState.setCurrentMusic(musicInfo);
                updateMusicInfo(musicInfo);
            }

            @Override
            public void onMusicTickling(float time) {
                mMusicState.setProgress(StringUtil.ten2sixty(time));
                mMusicState.setPercentage(time*1.f/mMusicState.getDurationInSeconds());
            }

            @Override
            public void onPrepared(int duration) {
                duration = duration/1000;
                Log.v("OneApplication","onPrepared()总时长"+duration);
                mMusicState.setDurationInSeconds(duration);
                mMusicState.setIsPlaying(true);
                initNotification(false,null);
            }

            @Override
            public void onError(int what, int extra) {

            }

            @Override
            public void onWaveForm(byte[] data) {
                mMusicState.setWaveformdata(data);
            }

            @Override
            public void onPause() {
                Log.v("OneApplication","onPause()");
                mMusicState.setIsPlaying(false);
                initNotification(true,null);
            }

            @Override
            public void onContinue() {
                Log.v("OneApplication","onContinue()");
                mMusicState.setIsPlaying(true);
                initNotification(false,null);
            }

            @Override
            public void onSoundEffectLoaded(Config config) {
                mConfig = config;
            }

            @Override
            public void onPlayModeChanged(int playMode) {
                mMusicState.setPlayMode(playMode);
                String text="当前播放模式：";
                switch (playMode){
                    case OnePlayer.cycle:
                        text += "列表循环";break;
                    case OnePlayer.random:
                        text += "随机播放";break;
                    case OnePlayer.looping:
                        text += "单曲循环";break;
                }
                Snackbar.make(stretchLayout,text,Snackbar.LENGTH_SHORT);
            }
        });
        CircleSeekBar circleSeekBar = getWindow().getDecorView().findViewById(R.id.one_seekbar);
        circleSeekBar.setOnSeekBarActionListener(new CircleSeekBar.OnSeekBarActionListener() {
            @Override
            public void onProgressUpdated(float progress) {
                Log.v("OneActivity", "onSeekBarUpdated()progress:" + progress);
                if (mOnePlayer.isStarted) {
                    seekTo(progress);
                } else {
                    unSeek();
                }
            }

            @Override
            public void onButtonClick() {
                mOnePlayer.play();

            }
        });
        StretchLayout stretchLayout = getWindow().getDecorView().findViewById(R.id.main_content_bar);
        stretchLayout.setOnReachMaxListener(new StretchLayout.OnReachListener() {
            @Override
            public void onReachMax() {
                disableViewInteraction();
                ViewTool.setStatusColor(BaseMusicControllActivity.this, statusInitColor);
            }

            @Override
            public void onReachMin() {
                mMusicState.setInPlayView(false);
            }
        });
        PermissionUtil.requestAllPermission(this);
    }

    public void setOneConfig(Config config) {
        LogTool.log("OneApplication","setOneConfig()设置配置"+config.toString());
        mConfig = config;
    }

    public void updateMusicInfo(MusicInfo musicInfo) {
        mMusicState.setCurrentBitmap(OneBitmapUtil.getMiddleAlbumArt(this,musicInfo.getUrl()));
        Palette.Builder builder = Palette.from(mMusicState.getCurrentBitmap());
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int color = palette.getDarkVibrantColor(Color.WHITE);
                mMusicState.setMusicColor(color);
                if (mMusicState.getIsInPlayView()) {
                    LogTool.log(this, "设置状态栏颜色为" + color);
                    ViewTool.setStatusColor(BaseMusicControllActivity.this,statusInitColor);
                }
                int currentColor;
                if (mMusicState.getIsWhite()) {
                    currentColor = Color.WHITE;
                } else {
                    currentColor = Color.BLACK;
                }
                if (!ColorUtil.getContrast(mMusicState.getMusicColor(), currentColor)) {
                    mMusicState.setIsWhite(!mMusicState.getIsWhite());
                }
                if (mOnePlayer.isStarted) {
                    initNotification(false, mMusicState.getCurrentBitmap());
                } else {
                    initNotification(true, mMusicState.getCurrentBitmap());
                }
            }
        });


    }


    private void initNotification(boolean isPlayState, Bitmap bitmap) {
        Log.v("OneApplication","initNotification()发出通知"+bitmap);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = buildRemoteViews(isPlayState,bitmap,false);
        RemoteViews bigRemoteViews = buildRemoteViews(isPlayState,bitmap,true);
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
        remoteViews.setTextViewText(R.id.noti_singer,mMusicState.getCurrentMusic().getArtist());
        remoteViews.setTextViewText(R.id.noti_name, mMusicState.getCurrentMusic().getDisplayName());
        remoteViews.setInt(R.id.noti_singer,"setTextColor",Color.BLACK);
        remoteViews.setInt(R.id.noti_name,"setTextColor",Color.BLACK);
        if(bitmap!=null) {
            remoteViews.setImageViewBitmap(R.id.noti_album_image, bitmap);
        }
        remoteViews.setImageViewBitmap(R.id.noti_previous, OneBitmapUtil.getBitmapByResId(this,R.drawable.skip_previous_black));
        remoteViews.setImageViewBitmap(R.id.noti_next, OneBitmapUtil.getBitmapByResId(this,R.drawable.skip_next_black));
        remoteViews.setImageViewBitmap(R.id.noti_close, OneBitmapUtil.getBitmapByResId(this,R.drawable.close_black));
        if (isPlayState) {
            remoteViews.setImageViewBitmap(R.id.noti_playandpause, OneBitmapUtil.getBitmapByResId(this,R.drawable.play_circle_outline_black));
        }else {
            remoteViews.setImageViewBitmap(R.id.noti_playandpause, OneBitmapUtil.getBitmapByResId(this,R.drawable.pause_circle_outline_black));
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
                mOnePlayer.play();
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
                    playPrevious();
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
                    playNext();
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
                }
            }
        }, filter3);
    }

    public void closeNotification() {
        System.out.println("调用关闭通知");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOnePlayer.release();
        if(playReceiver!=null) {
            unregisterReceiver(playReceiver);
        }
        if(nextReceiver!=null) {
            unregisterReceiver(nextReceiver);
        }
        if(previousReceiver!=null) {
            unregisterReceiver(previousReceiver);
        }
    }

    public void seekTo(float progress) {
        int second = (int) (progress * mMusicState.getDurationInSeconds());
        Log.v("OneApplication", "seekTo()查看秒" + second);
        mMusicState.setProgress(StringUtil.ten2sixty(second));
        mOnePlayer.seekto(second * 1000);
        //play();
    }

    public void unSeek() {
        mMusicState.setPercentage(0);
        mMusicState.setProgress("00:00");
    }

    public void queue() {
        quitPlayView();

    }

    public void toSoundEffect(View v) {
        Intent intent = new Intent();
        intent.setClass(this, SoundEffectActivity.class);
        startActivity(intent);
    }

    public void toPlayView() {
        Log.v("OneActivity", "toPlayView()");
        stretchLayout.toMaxHeight();
        mMusicState.setInPlayView(true);
    }

    public void quitPlayView() {
        Log.v("OneActivity", "quitPlayView()");
        stretchLayout.toMinHeight();
        MusicState.getInstance(this).setInPlayView(false);
        ViewTool.setStatusColor(this,mConfig.getThemeColor());
    }

    //在底部控制栏占据屏幕时，为了防止误触，要让它背后遮住的控件不响应触控
    protected abstract void disableViewInteraction();

    protected abstract void enableViewInteraction();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onPermissionResult(this, requestCode, grantResults);
    }
}