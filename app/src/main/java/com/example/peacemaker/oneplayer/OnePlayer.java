package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
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
    public Boolean isStarted = false;
    private int playMode = cycle;
    private int musicNumber;
    //private MainActivity activity;
    private String[] urls;
    private int currentPosition = 0;
    //private Context context;
    private String path;
    private Handler handler;
    private int currentTime = 0;
    private Visualizer visualizer;
    private Timer timer;
    private TimerTask timerTask;
    public MediaPlayer mediaPlayer;
    private ArrayList<Music> playList;
    private OnOneErrorListener onOneErrorListener;
    private OnMusicListener onMusicListener;
    private OnPrepareListener onPrepareListener;
    private OnBuffedUpdateListener onBufferedUpdateListener;
    public OnePlayer(ArrayList<Music> playList,int currentPosition){
        setPlayList(playList,currentPosition);
    }


    public void init(Music music) {
        if(onMusicListener!=null){
            onMusicListener.onMusicChanged(music);
        }
        try {
            if(mediaPlayer==null){
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        initHandler();
                        initTimer();
                        mediaPlayer.start();
                        if (onMusicListener != null) {
                            onMusicListener.onPrepared(mediaPlayer.getDuration());
                        }

                    }
                });
                visualizer = new Visualizer(mediaPlayer.getAudioSessionId());
                visualizer.setCaptureSize(64);
                visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                    @Override
                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                    }

                    @Override
                    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                        if (onMusicListener != null) {
                            onMusicListener.onWaveForm(fft);
                        }
                    }
                }, Visualizer.getMaxCaptureRate()/2, false, true);
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isStarted = false;
    }
    protected void play(){
        if(isStarted){
            if(mediaPlayer.isPlaying()){
                pause();
            }else {
                mediaPlayer.start();
            }
        }else {
            currentTime = 0;
            try {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    if (onMusicListener != null) {
                        onMusicListener.onComple();
                    }
                    changeMusic(true);
                    }
                });
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        if(onMusicListener!=null){
                            onMusicListener.onError(what,extra);
                        }
                        return false;
                    }
                });
                mediaPlayer.prepareAsync();
            }catch (Exception e){
            }
            isStarted = true;
        }
    }
    protected void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //处理开始走时信息
                if (msg.what == 1) {
                   currentTime+=1;

                }
                return false;
            }
        });
    }
    public void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    public void setPlayList(ArrayList<Music> playList,int currentPosition){
        this.playList = playList;
        this.currentPosition = currentPosition;
        if(playList!=null) {
            musicNumber = playList.size();
            init(playList.get(currentPosition));
        }
    }
    public void setMusicListener(OnMusicListener onMusicListener){
        this.onMusicListener = onMusicListener;
    }
    public void setPlayMode(int playMode){
        this.playMode = playMode;
    }
    protected void pause(){
        mediaPlayer.pause();
    }
    protected void stop(){
        mediaPlayer.stop();
    }
    public void selectMusic(Music music){
        init(music);
        play();
        if(onMusicListener!=null){
            onMusicListener.onMusicChanged(music);
        }
    }
    public void changeMusic(boolean isNext){
        if (mediaPlayer.isPlaying()) {
            pause();
        }
        switch (playMode) {
            case random:
                currentPosition = getRandomPosition(musicNumber);
                break;
            default:
                if(isNext) {
                    currentPosition = (currentPosition + 1) % musicNumber;
                }else {
                    currentPosition = currentPosition - 1;
                    if (currentPosition < 0) {
                        currentPosition += musicNumber;
                    }
                }
                break;
        }
        init(playList.get(currentPosition));
        play();
    }

    protected void setLooping(){
        mediaPlayer.setLooping(true);
    }




    protected int getRandomPosition(int musicNumber){
        int position = (int) (Math.random()*musicNumber);
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


    public void setOnPrepareListener(OnPrepareListener onPrepareListener){
        this.onPrepareListener = onPrepareListener;

    }
    public void setOnBufferedUpdateListener(OnBuffedUpdateListener onBufferedUpdateListener){
        this.onBufferedUpdateListener = onBufferedUpdateListener;
    }
    public void setOnOneErrorListener(OnOneErrorListener onOneErrorListener){
        this.onOneErrorListener = onOneErrorListener;
    }
    public void release(){
        if(mediaPlayer!=null){
            Log.v("OnePlayer","release()释放");
            mediaPlayer.release();
        }
    }







}
