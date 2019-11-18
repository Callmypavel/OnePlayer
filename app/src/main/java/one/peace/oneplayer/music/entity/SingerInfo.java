package one.peace.oneplayer.music.entity;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.util.DataUtil;

/**
 * Created by peace on 2018/5/8.
 */

public class SingerInfo extends BaseObservable implements Parcelable{
    private static final long serialVersionUID = 2L;
    public static Parcelable.Creator<SingerInfo> CREATOR = new Parcelable.Creator<SingerInfo>() {
        @Override
        public SingerInfo createFromParcel(Parcel source) {
            return new SingerInfo(source);
        }

        @Override
        public SingerInfo[] newArray(int size) {
            return new SingerInfo[size];
        }
    };
    private String singerName;
    private int songsNumber = 0;
    private boolean isPlaying = false;
    private String singerBitmapId;
    private ObservableArrayList<MusicInfo> musicInfos;

    public SingerInfo(String singerName) {
        this.singerName = singerName;
    }
    private SingerInfo(Parcel in) {
        singerName = in.readString();
        songsNumber = in.readInt();
        isPlaying = in.readByte() != 0;
        musicInfos = DataUtil.convertToObservaleArrayList(in.createTypedArray(MusicInfo.CREATOR));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(singerName);
        dest.writeInt(songsNumber);
        dest.writeByte((byte)(isPlaying?1:0));
        dest.writeTypedArray(DataUtil.convertToParcelableArray(musicInfos),0);
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

    public ObservableArrayList<MusicInfo> getMusicInfos() {
        return musicInfos;
    }

    public void setMusicInfos(ObservableArrayList<MusicInfo> musicInfos) {
        this.musicInfos = musicInfos;
    }

}
