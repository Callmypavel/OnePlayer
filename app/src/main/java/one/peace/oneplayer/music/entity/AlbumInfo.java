package one.peace.oneplayer.music.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import one.peace.oneplayer.util.DataUtil;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.OneBitmapUtil;
import one.peace.oneplayer.BR;

/**
 * Created by peace on 2018/5/8.
 */

public class AlbumInfo extends BaseObservable implements Parcelable {
    private static final long serialVersionUID = 2L;
    public static Creator<AlbumInfo>  CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel source) {
            return new AlbumInfo(source);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };
    private String albumName;
    private String singerName;
    private int songsNumber = 0;
    private boolean isPlaying = false;
    private Bitmap albumImage = OneBitmapUtil.getDafaultBitmap();
    private String albumBitmapUrl;
    private ObservableArrayList<MusicInfo> musicInfos;

    public AlbumInfo(String albumName) {
        this.albumName = albumName;
    }

    private AlbumInfo(Parcel in) {
        albumName = in.readString();
        singerName = in.readString();
        songsNumber = in.readInt();
        isPlaying = in.readByte() != 0;
        //albumImage = Bitmap.CREATOR.createFromParcel(in);
        albumBitmapUrl = in.readString();
        musicInfos = DataUtil.convertToObservaleArrayList(in.createTypedArray(MusicInfo.CREATOR));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        LogTool.log(this,"战兔");
        dest.writeString(albumName);
        dest.writeString(singerName);
        dest.writeInt(songsNumber);
        dest.writeByte((byte)(isPlaying?1:0));
        //albumImage.writeToParcel(dest,0);
        dest.writeString(albumBitmapUrl);
        LogTool.log(this,"战兔");
        dest.writeTypedArray(DataUtil.convertToParcelableArray(musicInfos),0);
        LogTool.log(this,"战兔");
    }

    @Bindable
    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
        notifyPropertyChanged(BR.albumName);
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
    public boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public Bitmap getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(Bitmap albumImage) {
        this.albumImage = albumImage;
        notifyPropertyChanged(BR.albumImage);
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
    public String getAlbumBitmapUrl() {
        return albumBitmapUrl;
    }

    public void setAlbumBitmapUrl(String albumBitmapUrl) {
        this.albumBitmapUrl = albumBitmapUrl;
        notifyPropertyChanged(BR.albumBitmapUrl);
    }

    public ObservableArrayList<MusicInfo> getMusicInfos() {
        return musicInfos;
    }

    public void setMusicInfos(ObservableArrayList<MusicInfo> musicInfos) {
        this.musicInfos = musicInfos;
    }


}
