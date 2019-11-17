package one.peace.oneplayer.music.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.util.StringUtil;


/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class MusicInfo extends BaseObservable implements Parcelable {
    private static final long serialVersionUID = 2L;
    public static Creator<MusicInfo>  CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };
    private static String[] supportedMusicFormats = new String[]{".mp3", ".ogg", ".flac"};
    private String artist;
    private String duration;
    private String size;
    private String id;
    private String album;
    private String displayName;
    private String url;
    private boolean isPlayable;
    private boolean isPlaying;


    public MusicInfo(){

    }
    private MusicInfo(Parcel in)
    {
        artist = in.readString();
        duration = in.readString();
        album = in.readString();
        displayName = in.readString();
        url = in.readString();
        isPlayable = in.readByte() != 0;
        isPlaying = in.readByte() != 0;
        id = in.readString();
    }




    @Override
    public String toString() {
        return super.toString();
    }

    @Bindable
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        if (StringUtil.isEndWith(supportedMusicFormats, artist)) {
            this.artist = getArtistNameByFileName(artist);
        } else {
            this.artist = artist;
        }
        notifyPropertyChanged(BR.artist);
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    public void setAlbum(String album) {
        this.album = album;
        notifyPropertyChanged(BR.album);
    }

    public void setDisplayName(String displayName) {
        if (StringUtil.isEndWith(supportedMusicFormats, displayName)) {
            this.displayName = getNameByFileName(displayName);
        } else {
            this.displayName = displayName;
        }
        notifyPropertyChanged(BR.displayName);
    }


    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    @Bindable
    public String getUrl() {
        if (url != null) {
            return url;
        } else
            return "<unknown>";
    }

    @Bindable
    public boolean getPlayable() {
        return isPlayable;
    }

    public void setPlayable(boolean playable) {
        isPlayable = playable;
        notifyPropertyChanged(BR.playable);
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
    public int getId(){
        return Integer.parseInt(id);
    }

    @Bindable
    public String getAlbum() {
        return album;
    }

    @Bindable
    public String getDisplayName() {
        return displayName;
    }


    public void update(MusicInfo musicInfo){
        setAlbum(musicInfo.getAlbum());
        setDuration(musicInfo.getDuration());
        setArtist(musicInfo.getArtist());
        setDisplayName(musicInfo.getDisplayName());
        setUrl(musicInfo.getUrl());
    }

    @Override
    public boolean equals(Object object) {
        MusicInfo musicInfo = (MusicInfo)object;
        if(musicInfo.getUrl().equals(url)){
            return true;
        }else {
            return false;
        }
    }

    public String getNameByFileName(String name){
        //LogTool.log(this,"犯罪现场"+name);
        String getName = name;
        if (name == null) {
            return null;
        }
        for (String supported : supportedMusicFormats) {
            if (name.endsWith(supported)) {
                getName = name.replace(supported, "");
            }
        }
        if (getName.contains(" - ")) {
            String[] names = getName.split(" - ");
            if (names.length >= 1) {
                getName = names[1];
            }
        }

        //LogTool.log(this,"砂仁回忆"+getName);
        return getName;
    }
    public String getArtistNameByFileName(String name){
        if(name==null){
            return null;
        }
        if(name.contains(" - ")) {
            String[] names2 = name.split(" - ");
            name = names2[0];
        }
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artist);
        dest.writeString(duration);
        dest.writeString(album);
        dest.writeString(displayName);
        dest.writeString(url);
        dest.writeByte((byte)(isPlayable ?1:0));
        dest.writeByte((byte) (isPlaying ? 1 : 0));
        dest.writeString(id);
    }
}
