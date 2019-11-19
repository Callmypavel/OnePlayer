package peacemaker.oneplayer.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;

import peacemaker.oneplayer.BR;
import peacemaker.oneplayer.activity.OneApplication;

import java.util.ArrayList;
import java.util.Comparator;

import static peacemaker.oneplayer.tool.MandarinTool.Mandarin2Pinyin;

/**
 * Created by peace on 2018/5/8.
 */

public class AlbumInfo extends BaseObservable {
    private String albumName;
    private String singerName;
    private int songsNumber = 0;
    private Boolean isPlaying = false;
    private Bitmap albumImage = OneApplication.loadingBitmap;
    private String albumBitmapId;
    public static Comparator<AlbumInfo> comparator = new Comparator<AlbumInfo>() {
        @Override
        public int compare(AlbumInfo lhs, AlbumInfo rhs) {
            if(lhs!=null&&rhs!=null) {
                return Mandarin2Pinyin(lhs.getAlbumName()).toLowerCase().compareTo(Mandarin2Pinyin(rhs.getAlbumName()).toLowerCase());
            }else {
                return 0;
            }
        }
    };

    public AlbumInfo(String albumName) {
        this.albumName = albumName;
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
    public boolean isPlaying() {
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
    public String getAlbumBitmapId() {
        return albumBitmapId;
    }

    public void setAlbumBitmapId(String albumBitmapId) {
        this.albumBitmapId = albumBitmapId;
        notifyPropertyChanged(BR.albumBitmapId);
    }
}
