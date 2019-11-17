package one.peace.oneplayer.music.entity;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import one.peace.oneplayer.BR;

/**
 * Created by peace on 2018/5/8.
 */

public class SingerInfo extends BaseObservable {
    private String singerName;
    private int songsNumber = 0;
    private Boolean isPlaying;
    private String singerBitmapId;

    public SingerInfo(String singerName) {
        this.singerName = singerName;
    }

    @Bindable
    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
        notifyPropertyChanged(BR.singerName);
    }

    @Bindable
    public int getSongsNumber() {
        return songsNumber;
    }

    public void setSongsNumber(int songsNumber) {
        this.songsNumber = songsNumber;
        notifyPropertyChanged(BR.songsNumber);
    }

    @Bindable
    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public String getSingerBitmapId() {
        return singerBitmapId;
    }

    public void setSingerBitmapId(String singerBitmapId) {
        this.singerBitmapId = singerBitmapId;
        notifyPropertyChanged(BR.singerBitmapId);
    }

}
