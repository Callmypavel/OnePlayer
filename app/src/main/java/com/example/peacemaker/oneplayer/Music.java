package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class Music implements Parcelable{
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
    private String id;
    private String album = "<unknown>";
    private String displayName="<unknown>";
    private String url;
    //private Bitmap albumBitmap;
    private ArrayList<Music> musicArrayList;
    private boolean isPlayable;
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
    public Music(String artist,String duration,String album,String displayName,String url,boolean isPlayable){
        this.artist = artist;
        this.duration = duration;
        this.isPlayable = isPlayable;
        this.album = album;
        this.displayName = cutName(displayName);
        this.url = url;
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
        musicArrayList = in.readArrayList(getClass().getClassLoader());
    }

    public String getArtist(){
        if(artist!=null) {
        return artist;
    }else
        return "<unknown>";

    }
    public String getDuration(){
        return duration;
    }

   //public String getSize(){
        //return size;
    //}

    //public String getId(){
    //    return id;
    //}

    public String getAlbum(){
        if(album!=null) {
            return album;
        }else
            return "<unknown>";
    }
    public Bitmap getAlbumBitmap(Context context){
        Bitmap bitmap = OneMusicloader.getAlbumArt(url);
        if(bitmap==null){
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.music);
        }
        return OneMusicloader.getAlbumArt(url);
    }

    public String getDisplayName(){
        if(displayName!=null) {
            return displayName;
        }else
            return "<unknown>";
    }

    public String getUrl(){
        if(url!=null) {
            return url;
        }else
            return "<unknown>";
    }
    public ArrayList<Music> getSecondItems(){

        return musicArrayList;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    //public void setSize(String size){
    //    this.size = size;
    //}
    public boolean isPlayable(){
        return isPlayable;
    }


    public void setAlbum(String album){
        this.album = album;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public void setUrl(String url){
        this.url = url;
    }
    public void setSecondItems(ArrayList<Music> musicArrayList){
        //Log.v("Music","set二级项现场"+musicArrayList);
//        if(musicArrayList!=null){
//            for(int i=0;i<musicArrayList.size();i++){
//                Log.v("Music","领导视察二级项现场"+i+musicArrayList.get(i).getDisplayName());
//            }
//        }

        this.musicArrayList = musicArrayList;
    }
    public void addSecondItem(Music secondItem){
        if(musicArrayList!=null){
            musicArrayList.add(secondItem);
            //Log.v("Music","领导视察增加二级项现场"+secondItem.getDisplayName());
        }
    }

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
        dest.writeList(musicArrayList);
    }
}
