package com.example.peacemaker.oneplayer;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
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
    public int playMode = cycle;
    private int musicNumber;
    //private MainActivity activity;
    private int currentPosition = 0;
    //private Context context;
    private Handler handler;
    private int currentTime = 0;
    private int duration = 0;
    private Visualizer visualizer;
    private Equalizer equalizer;
    private BassBoost bassBoost;
    private PresetReverb presetReverb;
    private Timer timer;
    private TimerTask timerTask;
    public MediaPlayer mediaPlayer;
    private ArrayList<Music> playList;
    private OnMusicListener onMusicListener;

    public OnePlayer(ArrayList<Music> playList,int currentPosition){
        setPlayList(playList,currentPosition);
    }
    public OnePlayer(ArrayList<Music> playList,int currentPosition,OnMusicListener onMusicListener){
        this.onMusicListener = onMusicListener;
        setPlayList(playList,currentPosition);
    }


    public BassBoost getBassBoost() {
        return bassBoost;
    }

    public PresetReverb getPresetReverb() {
        return presetReverb;
    }

    public void init(Music music) {
        Log.v("OnePlayer","init()");
        if(onMusicListener!=null){
            onMusicListener.onMusicChanged(music);
            Log.v("OnePlayer","init()"+music.getDisplayName());
        }
        try {
            if(mediaPlayer==null){
<<<<<<< HEAD
                initMediaPlayer();
                initSoundEffects();
=======
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        initHandler();
                        initTimer();
                        mediaPlayer.start();

                        if (onMusicListener != null) {
                            duration = mediaPlayer.getDuration();
                            onMusicListener.onPrepared(duration);
                        }

                    }
                });
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
                equalizer = new Equalizer(0,mediaPlayer.getAudioSessionId());

>>>>>>> parent of 530f60b... 增加测试功能均衡器
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getUrl());
            isStarted = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSoundEffects() {
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
        LogTool.log("OnePlayer","visualizer已被唤醒，魔王再临");
        equalizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
        bassBoost = new BassBoost(0,mediaPlayer.getAudioSessionId());
        presetReverb = new PresetReverb(0,mediaPlayer.getAudioSessionId());
        activateSoundEffects(true);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                initHandler();
                initTimer();
                mediaPlayer.start();

                if (onMusicListener != null) {
                    duration = mediaPlayer.getDuration();
                    onMusicListener.onPrepared(duration);
                }

            }
        });
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
    }

    protected void play(){
        if(isStarted){
            Log.v("OnePlayer","已经开始");
            if(mediaPlayer.isPlaying()){
                Log.v("OnePlayer","正在播放，暂停音乐");
                pause();
<<<<<<< HEAD
                activateSoundEffects(false);
=======
                visualizer.setEnabled(false);
>>>>>>> parent of 530f60b... 增加测试功能均衡器
                if (onMusicListener != null) {
                    onMusicListener.onPause();
                }
            }else {
                Log.v("OnePlayer","暂停中，开始音乐");
                mediaPlayer.start();
                if (onMusicListener != null) {
                    onMusicListener.onContinue();
                }
<<<<<<< HEAD
                activateSoundEffects(true);
=======
                visualizer.setEnabled(true);
>>>>>>> parent of 530f60b... 增加测试功能均衡器
            }
        }else {
            Log.v("OnePlayer","尚未开始,重设");
            currentTime = 0;
            try {
                mediaPlayer.prepareAsync();
<<<<<<< HEAD
                activateSoundEffects(true);
=======
                visualizer.setEnabled(true);
>>>>>>> parent of 530f60b... 增加测试功能均衡器
            }catch (Exception e){
            }
            isStarted = true;
        }
    }

    private void activateSoundEffects(boolean enabled) {
        visualizer.setEnabled(enabled);
        equalizer.setEnabled(enabled);
        bassBoost.setEnabled(enabled);
        presetReverb.setEnabled(enabled);
    }

    protected void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //处理开始走时信息
                if (msg.what == 1) {
                    if(onMusicListener!=null){
                        onMusicListener.onMusicTickling(currentTime);
                    }
                    if(currentTime!=duration) {
                        currentTime += 1;
                    }
                }
                return false;
            }
        });
    }
    public void initTimer() {
        Log.v("OnePlayer","initTimer()");
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
    public void stopTimer(){
        Log.v("OnePlayer","stopTimer()");
        if(timer!=null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
    public void setPlayList(ArrayList<Music> playList){
        this.playList = playList;
        if(playList!=null) {
            musicNumber = playList.size();
        }
    }
    public void setPlayList(ArrayList<Music> playList,int currentPosition){
        this.currentPosition = currentPosition;
        this.playList = playList;
        if(playList!=null) {
            musicNumber = playList.size();
            init(playList.get(currentPosition));
        }

    }

    public Equalizer getEqualizer() {
        return equalizer;
    }
    public void setBandAndLevel(short band,short level){
        if(equalizer!=null){
            equalizer.setBandLevel(band,level);
        }
    }

    public void setMusicListener(OnMusicListener onMusicListener){
        this.onMusicListener = onMusicListener;
    }
    public void setPlayMode(int playMode){
        this.playMode=playMode;
    }
    public void changePlayMode(){
        if(playMode<3){
            playMode=playMode+1;
        }else {
            playMode=1;
        }

    }
    protected void pause(){
        mediaPlayer.pause();
    }
    protected void stop(){
        mediaPlayer.stop();
    }
    public void selectMusic(Music music,int position){
        Log.v("OnePlayer","selectMusic()");
        stopTimer();
        if (mediaPlayer.isPlaying()) {
            pause();
        }
        currentPosition = position;
        init(music);
        play();

    }
    public void getNextMusic(){
        if(playMode==looping){
            selectMusic(playList.get(currentPosition),currentPosition);
        }else {
            changeMusic(true);
        }
    }
    public void changeMusic(boolean isNext){
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
        selectMusic(playList.get(currentPosition),currentPosition);
    }

    protected void setLooping(){
        mediaPlayer.setLooping(true);
    }




    protected int getRandomPosition(int musicNumber){
        int position = (int) (Math.random()*musicNumber);
        return position;
    }
    protected void seekto(int msec){
        System.out.println("seek到"+msec);
        mediaPlayer.seekTo(msec);
        currentTime = msec/1000;
        if(onMusicListener!=null){
            onMusicListener.onMusicTickling(currentTime);
        }
    }



    public void release(){
        if(mediaPlayer!=null){
            Log.v("OnePlayer","release()释放");
            mediaPlayer.release();
        }
    }







}
