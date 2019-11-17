package one.peace.oneplayer.music.entity;

import android.graphics.Bitmap;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import one.peace.oneplayer.util.OneBitmapUtil;
import one.peace.oneplayer.BR;

/**
 * Created by peace on 2018/5/8.
 */

public class AlbumInfo extends BaseObservable {
    private String albumName;
    private String singerName;
    private int songsNumber = 0;
    private Boolean isPlaying = false;
    private Bitmap albumImage = OneBitmapUtil.getDafaultBitmap();
    private String albumBitmapUrl;


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
}
