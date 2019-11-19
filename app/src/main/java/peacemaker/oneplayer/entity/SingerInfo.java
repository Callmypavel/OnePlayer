package peacemaker.oneplayer.entity;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import peacemaker.oneplayer.BR;

import java.util.Comparator;

import static peacemaker.oneplayer.tool.MandarinTool.Mandarin2Pinyin;

/**
 * Created by peace on 2018/5/8.
 */

public class SingerInfo extends BaseObservable {
    private String singerName;
    private int songsNumber = 0;
    private Boolean isPlaying;
    private String singerBitmapId;
    public static Comparator<SingerInfo> comparator = new Comparator<SingerInfo>() {
        @Override
        public int compare(SingerInfo lhs, SingerInfo rhs) {
            if(lhs!=null&&rhs!=null) {
                return Mandarin2Pinyin(lhs.getSingerName()).toLowerCase().compareTo(Mandarin2Pinyin(rhs.getSingerName()).toLowerCase());
            }else {
                return 0;
            }
        }
    };

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
