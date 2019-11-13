package peace.one.oneplayer.music;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Virtualizer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ouyan_000 on 2015/8/10.
 */
public class OnePlayer implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static int cycle = 1;
    private final static int looping = 2;
    private final static int random = 3;
    public boolean isStarted = false;
    public int playMode = cycle;
    private int musicNumber;
    private int currentPosition = 0;
    private Handler handler;
    private int currentTime = 0;
    private int duration = 0;
    private Visualizer visualizer;
    private Equalizer equalizer;
    private BassBoost bassBoost;
    private PresetReverb presetReverb;
    private Virtualizer virtualizer;
    private EnvironmentalReverb environmentalReverb;
    private Timer timer;
    private TimerTask timerTask;
    private MediaPlayer mediaPlayer;
    private ArrayList<Music> playList;
    private OnMusicListener onMusicListener;
    private OneConfig oneConfig;
    private boolean isUsingAudioTrack;
    private AudioTrack audioTrack;
    private MusicDecoder musicDecoder;

    public OnePlayer(ArrayList<Music> playList,int currentPosition){
        setPlayList(playList,currentPosition);
    }
    public OnePlayer(OneConfig oneConfig,ArrayList<Music> playList,int currentPosition,OnMusicListener onMusicListener,boolean isUsingAudioTrack){
        this.oneConfig = oneConfig;
        this.onMusicListener = onMusicListener;
        setPlayList(playList,currentPosition);
        this.isUsingAudioTrack = isUsingAudioTrack;
    }


    public BassBoost getBassBoost() {
        return bassBoost;
    }

    public PresetReverb getPresetReverb() {
        return presetReverb;
    }
    public Virtualizer getVirtualizer() {
        return virtualizer;
    }
    public EnvironmentalReverb getEnvironmentalReverb() {
        return environmentalReverb;
    }
    public void init(Music music) {
        Log.v("OnePlayer","init()");
        if(onMusicListener!=null){
            onMusicListener.onMusicChanged(music);
            Log.v("OnePlayer","init()"+music.getDisplayName());
        }
        try {
            if(!isUsingAudioTrack){
                if(mediaPlayer==null){
                    initMediaPlayer();
                    initSoundEffects();
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(music.getUrl());
            }else {
                if(audioTrack==null){
                    initAudioTrack(music.getUrl());
                    initSoundEffects();
                }
            }
            isStarted = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initAudioTrack(String url){
        if(Build.VERSION.SDK_INT>=21){
            try {
                MusicDecoder.asynchronousDecode(new MediaCodec.Callback() {
                    @Override
                    public void onInputBufferAvailable(MediaCodec codec, int index) {

                    }

                    @Override
                    public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {

                    }

                    @Override
                    public void onError(MediaCodec codec, MediaCodec.CodecException e) {

                    }

                    @Override
                    public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {

                    }
                },url);
            }catch (IOException e) {

            }
        }
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                MusicDecoder.getSampleRate(),
                AudioFormat.CHANNEL_OUT_STEREO,
                MusicDecoder.getFormat(),
                AudioTrack.getMinBufferSize(MusicDecoder.getSampleRate(), AudioFormat.CHANNEL_OUT_STEREO, MusicDecoder.getFormat()),
                AudioTrack.MODE_STREAM
        );
    }


    private void initSoundEffects() {
        int sessionId;
        if(!isUsingAudioTrack){
            sessionId = mediaPlayer.getAudioSessionId();
        }else {
            sessionId = audioTrack.getAudioSessionId();
        }
        visualizer = new Visualizer(sessionId);
        equalizer = new Equalizer(0,sessionId);
        bassBoost = new BassBoost(0,sessionId);
        presetReverb = new PresetReverb(0,sessionId);
        virtualizer = new Virtualizer(0,sessionId);
        environmentalReverb = new EnvironmentalReverb(0,0);
        //mediaPlayer.attachAuxEffect(environmentalReverb.getId());
        visualizer.setCaptureSize(64);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                LogTool.log("OnePlayer","fft");
                if (onMusicListener != null) {
                    onMusicListener.onWaveForm(fft);
                }
            }
        }, Visualizer.getMaxCaptureRate()/2, false, true);
        LogTool.log("OnePlayer","visualizer已被唤醒，魔王再临");
        if(oneConfig.getBassBoostStrenth()==-1){
            oneConfig.setBassBoostStrenth(bassBoost.getRoundedStrength());
        }
        if(oneConfig.getPresetReverb()==-1){
            oneConfig.setPresetReverb(presetReverb.getPreset());
        }
        if(oneConfig.getVirtualizerStrenth()==-1){
            oneConfig.setVirtualizerStrenth(virtualizer.getRoundedStrength());
        }
        if(oneConfig.getBandLevels().size()==0){
            short bandNumber = equalizer.getNumberOfBands();
            ArrayList<Integer> temp = new ArrayList<>();
            for (short i = 0; i < bandNumber; i++) {
                temp.add((int)equalizer.getBandLevel(i));
            }
            oneConfig.setBandLevels(temp);
        }
        EnvironmentReverbConfig config = oneConfig.getEnvironmentReverbConfig();
        if(config.getDecayTime()==-1){
            config.setDecayTime(environmentalReverb.getDecayTime());
        }
        if(config.getDecayHFTime()==-1){
            config.setDecayHFTime(environmentalReverb.getDecayHFRatio());
        }
        if(config.getDensity()==-1){
            config.setDensity(environmentalReverb.getDensity());
        }
        if(config.getDiffusion()==-1){
            config.setDiffusion(environmentalReverb.getDiffusion());
        }
        if(config.getReflectionsDelay()==-1){
            config.setReflectionsDelay(environmentalReverb.getReflectionsDelay());
        }
        if(config.getReflectionsLevel()==-1){
            config.setReflectionsLevel(environmentalReverb.getReflectionsLevel());
        }
        if(config.getReverbDelay()==-1){
            config.setReverbDelay(environmentalReverb.getReverbDelay());
        }
        if(config.getReverbLevel()==-1){
            config.setReverbLevel(environmentalReverb.getReverbLevel());
        }
        if(config.getRoomLevel()==-1){
            config.setRoomLevel(environmentalReverb.getRoomLevel());
        }
        if(config.getRoomHFLevel()==-1){
            config.setRoomHFLevel(environmentalReverb.getRoomHFLevel());
        }
        oneConfig.setEnvironmentReverbConfig(config);
        onMusicListener.onSoundEffectLoaded(oneConfig);
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
        if(!isUsingAudioTrack){
            if(isStarted){
                Log.v("OnePlayer","已经开始");
                if(mediaPlayer.isPlaying()){
                    Log.v("OnePlayer","正在播放，暂停音乐");
                    pause();
                    activateSoundEffects(false);
                    if (onMusicListener != null) {
                        onMusicListener.onPause();
                    }
                }else {
                    Log.v("OnePlayer","暂停中，开始音乐");
                    mediaPlayer.start();
                    if (onMusicListener != null) {
                        onMusicListener.onContinue();
                    }
                    activateSoundEffects(true);
                }
            }else {
                Log.v("OnePlayer","尚未开始,重设");
                currentTime = 0;
                try {
                    mediaPlayer.prepareAsync();
                    activateSoundEffects(true);
                }catch (Exception e){
                }
                isStarted = true;
            }
        }else {
            if(isStarted){
                Log.v("OnePlayer","已经开始(AudioTrack模式)");
                if(audioTrack.getPlayState()==AudioTrack.PLAYSTATE_PLAYING){
                    Log.v("OnePlayer","正在播放，暂停音乐(AudioTrack模式)");
                    audioTrack.pause();
                    activateSoundEffects(false);
                    if (onMusicListener != null) {
                        onMusicListener.onPause();
                    }
                }else if(audioTrack.getPlayState()==AudioTrack.PLAYSTATE_PAUSED){
                    Log.v("OnePlayer","暂停中，开始音乐(AudioTrack模式)");
                    audioTrack.play();
                    byte[] chunk = musicDecoder.getChunk();
                    audioTrack.write(chunk,0,chunk.length);
                    if (onMusicListener != null) {
                        onMusicListener.onContinue();
                    }
                    activateSoundEffects(true);
                }
            }else {
                Log.v("OnePlayer","尚未开始,重设(AudioTrack模式)");
                currentTime = 0;
                try {
                    byte[] chunk = musicDecoder.getChunk();
                    LogTool.log("OnePlayer","play()检查Chunk"+chunk);
                    audioTrack.write(chunk,0,chunk.length);
                    activateSoundEffects(true);
                }catch (Exception e){
                }
                isStarted = true;
            }
        }

    }

    private void activateSoundEffects(boolean enabled) {
        visualizer.setEnabled(enabled);
        equalizer.setEnabled(enabled);
        bassBoost.setEnabled(enabled);
        presetReverb.setEnabled(enabled);
        virtualizer.setEnabled(enabled);
        environmentalReverb.setEnabled(enabled);
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
