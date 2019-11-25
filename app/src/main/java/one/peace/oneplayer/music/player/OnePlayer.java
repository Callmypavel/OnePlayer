package one.peace.oneplayer.music.player;

import android.content.Context;
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

import one.peace.oneplayer.global.config.Config;
import one.peace.oneplayer.global.config.EnvironmentReverbConfig;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.util.LogTool;

/**
 * Created by ouyan_000 on 2015/8/10.
 */
public class OnePlayer implements Serializable {
    private static final long serialVersionUID = 1L;
    public final static int cycle = 1;
    public final static int looping = 2;
    public final static int random = 3;
    public boolean isStarted = false;
    public int playMode = cycle;
    private int musicNumber;
    private int currentPosition = 0;
    private Handler handler;
    private float currentTime = 0;
    private int duration = 0;
    private Visualizer visualizer;
    private Equalizer equalizer;
    private BassBoost bassBoost;
    private PresetReverb presetReverb;
    private Virtualizer virtualizer;
    private EnvironmentalReverb environmentalReverb;

    private MediaPlayer mediaPlayer;
    private ArrayList<MusicInfo> playList;
    private OnMusicListener onMusicListener;
    private Config config;
    private boolean isUsingAudioTrack = false;
    private AudioTrack audioTrack;
    private static MusicInfo lastMusic;
    private boolean isAudioEffectOpened = false;
    private Timer timer;
    private TimerTask timerTask;
    private int ticklePeriod = 500;
    private Boolean isErrorOcccured = false;

    public interface OnMusicListener {
        void onComplete();

        void onMusicChanged(MusicInfo music);

        void onMusicTickling(float time);

        void onPrepared(int dutation);

        void onError(int what, int extra);

        void onWaveForm(byte[] data);

        void onPause();

        void onContinue();

        void onSoundEffectLoaded(Config config);

        void onPlayModeChanged(int playMode);

    }
    private static OnePlayer sInstance;
    public static OnePlayer getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OnePlayer.class) {
                if (sInstance == null) {
                    sInstance = new OnePlayer(context);
                }
            }
        }
        return sInstance;
    }

    public OnePlayer(Context context){
        config = Config.getInstance(context);
        currentPosition = 0;
    }

    public OnePlayer(ArrayList<MusicInfo> playList, int currentPosition) {
        setPlayList(playList, currentPosition);
    }

    public OnePlayer(Config config, ArrayList<MusicInfo> playList, int currentPosition, OnMusicListener onMusicListener, boolean isUsingAudioTrack) {
        this.config = config;
        this.onMusicListener = onMusicListener;
        setPlayList(playList, currentPosition);
        this.isUsingAudioTrack = isUsingAudioTrack;
    }

    public void setOnMusicListener(OnMusicListener onMusicListener) {
        this.onMusicListener = onMusicListener;
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

    public void init(MusicInfo music) {
        Log.v("OnePlayer", "init()");
        if (onMusicListener != null) {
            if (lastMusic != null) {
                lastMusic.setPlaying(false);
                lastMusic.getSingerInfo().setPlaying(false);
                lastMusic.getAlbumInfo().setPlaying(false);
            }
            music.setPlaying(true);
            music.getSingerInfo().setPlaying(true);
            music.getAlbumInfo().setPlaying(true);
            lastMusic = music;
            onMusicListener.onMusicChanged(music);
            Log.v("OnePlayer", "init()" + music.getDisplayName());
        }
        try {
            if (!isUsingAudioTrack) {
                if (mediaPlayer == null) {
                    initMediaPlayer();
                    initSoundEffects();
                    activateSoundEffects(true);
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(music.getUrl());
            } else {
                if (audioTrack == null) {
                    //initAudioTrack(music.getUrl());
                    initSoundEffects();
                }
            }
            isStarted = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initSoundEffects() {
        int sessionId;
        if (!isUsingAudioTrack) {
            sessionId = mediaPlayer.getAudioSessionId();
        } else {
            sessionId = audioTrack.getAudioSessionId();
        }

        visualizer = new Visualizer(sessionId);
        equalizer = new Equalizer(5, sessionId);
        bassBoost = new BassBoost(5, sessionId);
        presetReverb = new PresetReverb(5, sessionId);
        virtualizer = new Virtualizer(5, sessionId);

        //mediaPlayer.attachAuxEffect(environmentalReverb.getId());
        visualizer.setCaptureSize(64);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                //LogTool.log("OnePlayer","fft");
                if (onMusicListener != null) {
                    onMusicListener.onWaveForm(fft);
                }
            }
        }, Visualizer.getMaxCaptureRate() / 2, false, true);
        LogTool.log("OnePlayer", "visualizer已被唤醒，魔王再临");
        if (config.getBassBoostStrenth() == -1) {
            config.setBassBoostStrenth(bassBoost.getRoundedStrength());
        }
        if (config.getPresetReverb() == -1) {
            config.setPresetReverb(presetReverb.getPreset());
        }
        if (config.getVirtualizerStrength() == -1) {
            config.setVirtualizerStrength(virtualizer.getRoundedStrength());
        }
        if (config.getBandLevels().size() == 0) {
            short bandNumber = equalizer.getNumberOfBands();
            ArrayList<Integer> temp = new ArrayList<>();
            for (short i = 0; i < bandNumber; i++) {
                LogTool.log("OnePlayer", i + "查看bandlevel" + equalizer.getBandLevel(i));
                temp.add(equalizer.getBandLevel(i) / 100);
            }
            config.setBandLevels(temp);
        }
        config.setBandLevelRange(equalizer.getBandLevelRange());

        EnvironmentReverbConfig environmentReverbConfig = config.getEnvironmentReverbConfig();
        environmentalReverb = new EnvironmentalReverb(5, 0);
        if (environmentReverbConfig.getDecayTime() == -1) {
            environmentReverbConfig.setDecayTime(environmentalReverb.getDecayTime());
        }
        if (environmentReverbConfig.getDecayHFTime() == -1) {
            environmentReverbConfig.setDecayHFTime(environmentalReverb.getDecayHFRatio());
        }
        if (environmentReverbConfig.getDensity() == -1) {
            environmentReverbConfig.setDensity(environmentalReverb.getDensity());
        }
        if (environmentReverbConfig.getDiffusion() == -1) {
            environmentReverbConfig.setDiffusion(environmentalReverb.getDiffusion());
        }
        if (environmentReverbConfig.getReflectionsDelay() == -1) {
            environmentReverbConfig.setReflectionsDelay(environmentalReverb.getReflectionsDelay());
        }
        if (environmentReverbConfig.getReflectionsLevel() == -1) {
            environmentReverbConfig.setReflectionsLevel(environmentalReverb.getReflectionsLevel());
        }
        if (environmentReverbConfig.getReverbDelay() == -1) {
            environmentReverbConfig.setReverbDelay(environmentalReverb.getReverbDelay());
        }
        if (environmentReverbConfig.getReverbLevel() == -1) {
            environmentReverbConfig.setReverbLevel((short) (environmentalReverb.getReverbLevel() / 1000));
        }
        if (environmentReverbConfig.getRoomLevel() == -1) {
            environmentReverbConfig.setRoomLevel((short) (environmentalReverb.getRoomLevel() / 1000));
        }
        if (environmentReverbConfig.getRoomHFLevel() == -1) {
            environmentReverbConfig.setRoomHFLevel((short) (environmentalReverb.getRoomHFLevel() / 1000));
        }
        config.setEnvironmentReverbConfig(environmentReverbConfig);
        onMusicListener.onSoundEffectLoaded(config);
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
                activateVisualizer(true);

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isErrorOcccured) {
                    if (onMusicListener != null) {
                        onMusicListener.onComplete();
                    }
                    LogTool.log(this, "当前音乐播放完成");
                    changeMusic(true);
                } else {
                    LogTool.log(this, "当前音乐播放出错");
                    isErrorOcccured = false;
                }
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
                if (onMusicListener != null) {
                    onMusicListener.onError(what, extra);
                }
                isErrorOcccured = true;
                return false;
            }
        });
    }

    public void play() {
        if (!isUsingAudioTrack) {
            if (isStarted) {
                Log.v("OnePlayer", "已经开始");
                if (mediaPlayer.isPlaying()) {
                    Log.v("OnePlayer", "正在播放，暂停音乐");
                    pause();
                    activateVisualizer(false);
                    if (onMusicListener != null) {
                        onMusicListener.onPause();
                    }
                } else {
                    Log.v("OnePlayer", "暂停中，开始音乐");
                    mediaPlayer.start();
                    if (onMusicListener != null) {
                        onMusicListener.onContinue();
                    }
                    activateVisualizer(true);
                }
            } else {
                Log.v("OnePlayer", "尚未开始,重设");
                currentTime = 0;
                try {
                    mediaPlayer.prepareAsync();

                } catch (Exception e) {
                }
                isStarted = true;
            }
        } else {
            if (isStarted) {
                Log.v("OnePlayer", "已经开始(AudioTrack模式)");
                if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                    Log.v("OnePlayer", "正在播放，暂停音乐(AudioTrack模式)");
                    audioTrack.pause();
                    activateSoundEffects(false);
                    if (onMusicListener != null) {
                        onMusicListener.onPause();
                    }
                } else if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PAUSED) {
                    Log.v("OnePlayer", "暂停中，开始音乐(AudioTrack模式)");
                    audioTrack.play();
                    //byte[] chunk = musicDecoder.getChunk();
                    //audioTrack.write(chunk, 0, chunk.length);
                    if (onMusicListener != null) {
                        onMusicListener.onContinue();
                    }
                    activateSoundEffects(true);
                }
            } else {
                Log.v("OnePlayer", "尚未开始,重设(AudioTrack模式)");
                currentTime = 0;
                try {
                    //byte[] chunk = musicDecoder.getChunk();
                    //LogTool.log("OnePlayer", "play()检查Chunk" + chunk);
                    //audioTrack.write(chunk, 0, chunk.length);
                    activateSoundEffects(true);
                } catch (Exception e) {
                }
                isStarted = true;
            }
        }

    }

    public void activateSoundEffects(boolean enabled) {
        visualizer.setEnabled(enabled);
        equalizer.setEnabled(enabled);
        bassBoost.setEnabled(enabled);
        presetReverb.setEnabled(enabled);
        virtualizer.setEnabled(enabled);
        environmentalReverb.setEnabled(enabled);
    }

    public void activateSoundEffectsExceptVisualizer(boolean enabled) {
        equalizer.setEnabled(enabled);
        bassBoost.setEnabled(enabled);
        presetReverb.setEnabled(enabled);
        virtualizer.setEnabled(enabled);
        environmentalReverb.setEnabled(enabled);
    }

    public void activateVisualizer(boolean enabled) {
        visualizer.setEnabled(enabled);
    }

    protected void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //处理开始走时信息
                if (msg.what == 1) {
                    if (onMusicListener != null) {
                        onMusicListener.onMusicTickling(currentTime);
                    }
                    if (currentTime != duration) {
                        currentTime += ticklePeriod * 1.f / 1000;
                    }
                }
                return false;
            }
        });
    }

    public void initTimer() {
        Log.v("OnePlayer", "initTimer()");
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
        timer.schedule(timerTask, 0, ticklePeriod);
    }

    public void stopTimer() {
        Log.v("OnePlayer", "stopTimer()");
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void setPlayList(ArrayList<MusicInfo> playList) {
        this.playList = playList;
        if (playList != null) {
            musicNumber = playList.size();
        }
    }

    public void setPlayList(ArrayList<MusicInfo> playList, int currentPosition) {
        this.currentPosition = currentPosition;
        this.playList = playList;
        if (playList != null) {
            musicNumber = playList.size();
            init(playList.get(currentPosition));
        }

    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public void setBandAndLevel(short band, short level) {
        if (equalizer != null) {
            equalizer.setBandLevel(band, level);
        }
    }

    public void setMusicListener(OnMusicListener onMusicListener) {
        this.onMusicListener = onMusicListener;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public void changePlayMode() {
        if (playMode < 3) {
            playMode = playMode + 1;
        } else {
            playMode = 1;
        }
        onMusicListener.onPlayModeChanged(playMode);

    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void selectMusic(MusicInfo musicInfo, int position) {
        Log.v("OnePlayer", "selectMusic()");
        stopTimer();
        if (mediaPlayer.isPlaying()) {
            pause();
        }
        currentPosition = position;
        init(musicInfo);
        play();

    }

    public void getNextMusic() {
        if (playMode == looping) {
            selectMusic(playList.get(currentPosition), currentPosition);
        } else {
            changeMusic(true);
        }
    }

    public void changeMusic(boolean isNext) {
        LogTool.log(this, "changeMusic()");
        switch (playMode) {
            case random:
                currentPosition = getRandomPosition(musicNumber);
                break;
            default:
                if (isNext) {
                    if (playMode != looping) {
                        currentPosition = (currentPosition + 1) % musicNumber;
                    }
                } else {
                    currentPosition = currentPosition - 1;
                    if (currentPosition < 0) {
                        currentPosition += musicNumber;
                    }
                }
                break;
        }
        selectMusic(playList.get(currentPosition), currentPosition);
    }

    protected void setLooping() {
        mediaPlayer.setLooping(true);
    }


    protected int getRandomPosition(int musicNumber) {
        int position = (int) (Math.random() * musicNumber);
        return position;
    }

    public void seekto(int msec) {
        System.out.println("seek到" + msec);
        mediaPlayer.seekTo(msec);
        currentTime = msec / 1000;
        if (onMusicListener != null) {
            onMusicListener.onMusicTickling(currentTime);
        }
    }


    public void release() {
        if (mediaPlayer != null) {
            Log.v("OnePlayer", "release()释放");
            mediaPlayer.release();
        }
    }


}
