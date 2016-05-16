package com.example.peacemaker.oneplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class Music implements Parcelable {
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
    //private String size;
    //private String id;
    private String album = "<unknown>";
    private String displayName="<unknown>";
    private String url;

    public Music(String artist,String duration,String album,String displayName,String url){
        this.artist = artist;
        this.duration = duration;
        //this.size = size;
        //this.id = id;
        this.album = album;
        this.displayName = cutName(displayName);
        this.url = url;
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
    public void setArtist(String artist){
        this.artist = artist;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    //public void setSize(String size){
    //    this.size = size;
    //}


    public void setAlbum(String album){
        this.album = album;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public void setUrl(String url){
        this.url = url;
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
    }
}
