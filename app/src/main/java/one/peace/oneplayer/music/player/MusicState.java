package one.peace.oneplayer.music.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.util.StringUtil;

/**
 * Created by ouyan on 2016/7/20.
 */

public class MusicState extends BaseObservable {
    private String progress = "00:00";
    private String duration = "00:00";
    private int musicColor = Color.WHITE;
    private boolean isWhite = true;//UI是否应该显示为白色
    private boolean isClickable = true;
    private boolean isPlaying = true;
    private boolean isInPlayView = false;
    private int playMode = 1;
    private Bitmap currentBitmap;
    private float percentage;
    private int durationInSeconds;
    private MusicInfo currentMusic;
    private byte[] waveformdata;
    private static MusicState sInstance;


    public static MusicState getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (MusicState.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    @Bindable
    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        notifyPropertyChanged(BR.percentage);
    }

    @Bindable
    public MusicInfo getCurrentMusic() {
        return currentMusic;
    }

    public void setCurrentMusic(MusicInfo currentMusic) {
        this.currentMusic = currentMusic;
        notifyPropertyChanged(BR.currentMusic);
    }

    @Bindable
    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
        notifyPropertyChanged(BR.currentBitmap);
    }

    @Bindable
    public boolean getIsClickable() {
        return isClickable;
    }

    public void setIsClickable(boolean clickable) {
        isClickable = clickable;
        notifyPropertyChanged(BR.isClickable);
    }

    @Bindable
    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean playing) {
        isPlaying = playing;
        notifyPropertyChanged(BR.isPlaying);
    }

    @Bindable
    public boolean getIsInPlayView() {
        return isInPlayView;
    }

    public void setInPlayView(boolean inPlayView) {
        isInPlayView = inPlayView;
        notifyPropertyChanged(BR.isInPlayView);
    }

    @Bindable
    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
        notifyPropertyChanged(BR.playMode);
    }

    @Bindable
    public boolean getIsWhite() {
        return isWhite;
    }

    public void setIsWhite(boolean white) {
        isWhite = white;
        notifyPropertyChanged(BR.isWhite);
    }

    @Bindable
    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.progress);
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        notifyPropertyChanged(BR.durationInSeconds);
        setDuration(StringUtil.ten2sixty(durationInSeconds));
    }

    @Bindable
    public int getMusicColor() {
        return musicColor;
    }

    public void setMusicColor(int musicColor) {
        this.musicColor = musicColor;
        notifyPropertyChanged(BR.musicColor);
    }

    @Bindable
    public byte[] getWaveformdata() {
        return waveformdata;
    }

    public void setWaveformdata(byte[] waveformdata) {
        this.waveformdata = waveformdata;
        notifyPropertyChanged(BR.waveformdata);
    }
}
