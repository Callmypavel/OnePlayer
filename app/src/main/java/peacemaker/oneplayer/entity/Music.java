package peacemaker.oneplayer.entity;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;

import peacemaker.oneplayer.BR;
import peacemaker.oneplayer.tool.OneMusicloader;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.tool.LogTool;
import peacemaker.oneplayer.tool.OneBitmapUtil;

import java.util.ArrayList;

/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class Music extends BaseObservable implements Parcelable {
    private static final long serialVersionUID = 2L;
    public static Parcelable.Creator<Music>  CREATOR =
            new Creator<Music>() {
                @Override
                public Music createFromParcel(Parcel source) {
                    return new Music(source);
                }

                @Override
                public Music[] newArray(int size) {
                    return new Music[size];
                }
            };
    private String artist = "<unknown>";
    private String duration;
    private String size;
    private String id = "0";
    private String album = "<unknown>";
    private String displayName="<unknown>";
    private String url;
    //private Bitmap albumBitmap;
    //private ArrayList<Music> musicArrayList;
    private boolean isPlayable;
    private boolean isPlaying;
    private SingerInfo singerInfo;
    private AlbumInfo albumInfo;

    public Music(String displayName){
        this.displayName = displayName;
        this.isPlayable = false;
    }
    public Music(String displayName,Bitmap albumBitmap){
        this.displayName = displayName;
        this.isPlayable = false;
        //this.albumBitmap = albumBitmap;
    }

    public Music(String artist, String duration,String album,String displayName, String url, String id, String size, boolean isPlayable){
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.id = id;
        this.album = album;
        this.displayName = cutName(displayName);
        this.url = url;
        this.isPlayable = isPlayable;
        //this.albumBitmap = albumBitmap;
    }
    public Music(String artist,String duration,String album,String displayName,String url, String id,boolean isPlayable){
        this.artist = artist;
        this.duration = duration;
        this.isPlayable = isPlayable;
        this.album = album;
        this.displayName = cutName(displayName);
        this.url = url;
        this.id = id;
        //this.albumBitmap = albumBitmap;
    }
    public Music(String artist,String duration,String album,String displayName,String url,boolean isPlayable){
        this.artist = artist;
        this.duration = duration;
        this.isPlayable = isPlayable;
        this.album = album;
        this.displayName = cutName(displayName);
        this.url = url;
        this.id = id;
        //this.albumBitmap = albumBitmap;
    }
    public Music(){

    }
    private Music(Parcel in)
    {
        artist = in.readString();
        duration = in.readString();
        album = in.readString();
        displayName = in.readString();
        url = in.readString();
        isPlayable =in.readByte()!=0;
        //musicArrayList = in.readArrayList(getClass().getClassLoader());
        id = in.readString();
        isPlaying = in.readByte()!=0;

    }

    public SingerInfo getSingerInfo() {
        return singerInfo;
    }

    public void setSingerInfo(SingerInfo singerInfo) {
        this.singerInfo = singerInfo;
    }

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }

    @Override
    public String toString() {
        String message = "";
        message += "歌名:"+displayName+"\n";
        message += "歌手:"+singerInfo.getSingerName()+"\n";
        message += "专辑:"+albumInfo.getAlbumName()+"\n";
        return message;
    }
    @Bindable
    public String getArtist(){
        //Log.v("Music","获取艺术家"+artist);
        if(artist!=null) {
            return artist;
        }else
        return "<unknown>";
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setId(String id) {
        this.id = id;
    }


//    public void setMusicArrayList(ArrayList<Music> musicArrayList) {
//        this.musicArrayList = musicArrayList;
//    }

    public void setPlayable(boolean playable) {
        isPlayable = playable;
    }

    public String getSize() {

        return size;
    }

//    public ArrayList<Music> getMusicArrayList() {
//        return musicArrayList;
//    }




    public int getId(){
        return Integer.parseInt(id);
    }
    @Bindable
    public String getAlbum(){
        if(album!=null) {
            return album;
        }else
            return "<unknown>";
    }
    public void update(Music music){
        setAlbum(music.getAlbum());
        setDuration(music.getDuration());
        setArtist(music.getArtist());
        setDisplayName(music.getDisplayName());
        setUrl(music.getUrl());
        setSingerInfo(music.getSingerInfo());
        setAlbumInfo(music.getAlbumInfo());
        setPlaying(music.isPlaying());
        //setSecondItems(music.getSecondItems());
    }
    public Boolean isSame(Music music){
        if (getDisplayName().equals(music.getDisplayName())){
            if (getAlbum().equals(music.getAlbum())){
                return true;
            }
        }
        return false;
    }
    public Bitmap getAlbumBitmap(Context context){
        LogTool.log("Music","getAlbumBitmap()拿url"+url);
        Bitmap bitmap = OneMusicloader.getAlbumArt(url);
        if(bitmap==null){
            LogTool.log("Music","娶不到专辑图");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
        }
        return bitmap;
    }
    public Bitmap getMiddleAlbumArt(Context context) {
            Bitmap bitmap = getAlbumBitmap(context);
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            if (bitmap.getWidth() > width / 2) {
                bitmap = OneBitmapUtil.zoomImg(bitmap, width / 2, width / 2);
            } else {
            }

            return bitmap;
    }
    public Bitmap getSmallAlbumArt(Context context) {
        Bitmap bitmap = getAlbumBitmap(context);
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        bitmap = OneBitmapUtil.zoomImg(bitmap,width/6,width/6);
        return bitmap;
    }
    public Bitmap getMiddleAlbumArt(Bitmap bitmap,Context context) {
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        if(bitmap.getWidth()>width/2) {
            bitmap = OneBitmapUtil.zoomImg(bitmap, width / 2, width / 2);
        }else {
        }
        return bitmap;
    }
    public Bitmap getSmallAlbumArt(Bitmap bitmap,Context context) {
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        if(bitmap.getWidth()>width/6) {
        bitmap = OneBitmapUtil.zoomImg(bitmap,width/6,width/6);
        }else {
        }
        return bitmap;
    }

    @Bindable
    public String getDisplayName(){
        if(displayName!=null) {
            return displayName;
        }else
            return "<unknown>";
    }
    @Bindable
    public String getDuration(){
        return duration;
    }

    @Bindable
    public String getUrl(){
        if(url!=null) {
            return url;
        }else
            return "<unknown>";
    }
//    public ArrayList<Music> getSecondItems(){
//
//        return musicArrayList;
//    }
    public void setArtist(String artist){
        this.artist = artist;
        notifyPropertyChanged(BR.artist);
    }
    public void setDuration(String duration){
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }
    public boolean isPlayable(){
        return isPlayable;
    }


    public void setAlbum(String album){
        this.album = album;
        notifyPropertyChanged(BR.album);
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
        notifyPropertyChanged(BR.displayName);
    }

    public void setUrl(String url){
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    //    public void setSecondItems(ArrayList<Music> musicArrayList){
//        //Log.v("Music","set二级项现场"+musicArrayList);
////        if(musicArrayList!=null){
////            for(int i=0;i<musicArrayList.size();i++){
////                Log.v("Music","领导视察二级项现场"+i+musicArrayList.get(i).getDisplayName());
////            }
////        }
//
//        this.musicArrayList = musicArrayList;
//    }
//    public void addSecondItem(Music secondItem){
//        if(musicArrayList!=null){
//            musicArrayList.add(secondItem);
//            //Log.v("Music","领导视察增加二级项现场"+secondItem.getDisplayName());
//        }
//    }

    @Override
    public boolean equals(Object o) {
        Music music = (Music)o;
        if(music.getUrl().equals(url)){
            return true;
        }else {
            return false;
        }
    }

    public String cutName(String name){
        String getName = name;
        if(name==null){
            return null;
        }
        if (name.contains(".mp3")) {
            String[] names = name.split(".mp3");
            getName = names[0];
        }
        if (name.contains(".ogg")) {
            String[] names = name.split(".ogg");
            getName = names[0];
        }
        if(name.contains(" - ")) {
            String[] names2 = getName.split(" - ");
            getName = names2[1];
        }
        return getName;
    }
    public String getArtist(String name){
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
        //dest.writeList(musicArrayList);
        dest.writeString(id);
        dest.writeByte((byte)(isPlaying ?1:0));
    }

}
