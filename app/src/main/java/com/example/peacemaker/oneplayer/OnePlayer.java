package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ouyan_000 on 2015/8/10.
 */
public class OnePlayer implements Serializable {
    private static final long serialVersionUID = 1L;
    final static int cycle = 1;
    final static int looping = 2;
    final static int random = 3;
    private Boolean isInit = false;
    private int time = 0;
    private int musicNumber;
    //private MainActivity activity;
    private String[] urls;
    private String playMode;
    //private Context context;
    private String path;
    private Handler handler;
    public MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;
    private OnCompleListener onCompleListener;
    private OnPrepareListener onPrepareListener;
    private OnBuffedUpdateListener onBufferedUpdateListener = null;
    private static String sdRootPath = Environment.getExternalStorageDirectory().getPath();
//    public OnePlayer(String path, Context context, Handler handler){
//        this.handler = handler;
//        this.path = path;
//        activity = (MainActivity)context;
//        System.out.println("检查"+activity);
//        init(path);
//    }
    public OnePlayer(){

       // this.handler = handler;
//        activity = (MainActivity)context;
    }


    public void init(String path) {
        System.out.println("初始化播放器");
        try {
            if(mediaPlayer!=null){
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            Log.v("OnePlayer","哥先看看路径"+path);
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            Log.v("OnePlayer", "哥先看看路径" + path);
            Log.v("OnePlayer", "IO异常");
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.v("OnePlayer", "开始播放" + mediaPlayer.getDuration());
                mediaPlayer.start();
                if (onPrepareListener != null) {
                    onPrepareListener.onPrepare(mediaPlayer.getDuration());
                }

            }
        });
        //mediaPlayer.setAudioStreamType(MediaPlayer.MEDIA_INFO_BUFFERING_START);
        isInit = false;
    }
    protected void play(){
        Log.v("OnePlayer", "播放");
        if(isInit){
            //已经初始化，继续播放
            mediaPlayer.start();
        }else {
            time = 0;
            try {
                mediaPlayer.prepareAsync();
            }catch (Exception e){
                Log.v("OnePlayer","IO异常不可避");
            }
            isInit = true;
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Message message = new Message();
//                message.what = 0x124;
//                handler.sendMessage(message);
                if (onCompleListener != null) {
                    onCompleListener.onComple();
                }
            }
        });
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                try {
                    onBufferedUpdateListener.onBuffedUpdate(percent);
                } catch (Exception e) {
                    Log.v("OnePlayer", e.getMessage());
                }
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v("我看看what", what + " " + extra);
                return false;
            }
        });

        //mediaPlayer.setAudioStreamType(MediaPlayer.MEDIA_INFO_BUFFERING_START);
    }
    protected void pause(){
        //timerTask.cancel();
        //timer = null;

        mediaPlayer.pause();
    }
    protected void stop(){

        System.out.println("stop停止了");
        mediaPlayer.stop();
    }


    protected void setmusicNumber(int musicNumber){
        this.musicNumber = musicNumber;
    }

    protected int getRandomPosition(int musicNumber){
        int position = (int) (Math.random()*musicNumber);
        System.out.println("获得的随机数" + position);
        return position;
    }
    protected void setUrls(String[] urls){
        this.urls = urls;
    }
    protected void seekto(int msec){
        System.out.println("seek到"+msec);
        mediaPlayer.seekTo(msec);
    }

    //public void updateTime(int time){
    //    this.time = time;
    //}
    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }


//    public void updateMusic(){
//        activity.updateActionbar();
//        activity.saveLastPlayed();
//        activity.currentMusic = activity.musicArrayList.get(activity.currentPosition).getUrl();
//        activity.updateSeekbar();
//    }
    //public MainActivity getActivity(){
    //    return (MainActivity)context;
    //}

    public void setOnCompleListener(OnCompleListener onCompleListener){
        this.onCompleListener = onCompleListener;

    }
    public void setOnPrepareListener(OnPrepareListener onPrepareListener){
        this.onPrepareListener = onPrepareListener;

    }
    public void setOnBufferedUpdateListener(OnBuffedUpdateListener onBufferedUpdateListener){
        this.onBufferedUpdateListener = onBufferedUpdateListener;


    }
    public void release(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
        }
    }







}
