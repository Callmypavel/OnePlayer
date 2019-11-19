package one.peace.oneplayer.music.player;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import one.peace.oneplayer.BR;

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
    private int playMode = 1;
    private Bitmap currentBitmap;
    private float percentage;

    @Bindable
    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        notifyPropertyChanged(BR.percentage);
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
    public int getMusicColor() {
        return musicColor;
    }

    public void setMusicColor(int musicColor) {
        this.musicColor = musicColor;
        notifyPropertyChanged(BR.musicColor);
    }
}
