package one.peace.oneplayer.music.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import one.peace.oneplayer.R;
import one.peace.oneplayer.base.BaseBean;
import one.peace.oneplayer.base.BaseViewModel;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.OneBitmapUtil;


/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class MusicInfo extends BaseBean {
//    private static final long serialVersionUID = 2L;
//    public static Creator<MusicInfo>  CREATOR =
//            new Creator<MusicInfo>() {
//                @Override
//                public MusicInfo createFromParcel(Parcel source) {
//                    return new MusicInfo(source);
//                }
//
//                @Override
//                public MusicInfo[] newArray(int size) {
//                    return new MusicInfo[size];
//                }
//            };
    private String artist;
    private String duration;
    private String size;
    private String id;
    private String album;
    private String displayName;
    private String url;
    //private ArrayList<MusicInfo> musicArrayList;
    private boolean isPlayable;


    public MusicInfo(){

    }
//    private MusicInfo(Parcel in)
//    {
//        artist = in.readString();
//        duration = in.readString();
//        album = in.readString();
//        displayName = in.readString();
//        url = in.readString();
//        isPlayable =in.readByte()!=0;
//        musicArrayList = in.readArrayList(getClass().getClassLoader());
//        id = in.readString();
//    }




    @Override
    public String toString() {
        return super.toString();
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
        mViewModel.updateData();
    }

    public void setDuration(String duration) {
        this.duration = duration;
        mViewModel.updateData();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
        mViewModel.updateData();
    }

    public void setId(String id) {
        this.id = id;
        mViewModel.updateData();
    }

    public void setAlbum(String album) {
        this.album = album;
        mViewModel.updateData();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUrl(String url) {
        this.url = url;
        mViewModel.updateData();
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void setPlayable(boolean playable) {
        isPlayable = playable;
        mViewModel.updateData();
    }

    public int getId(){
        return Integer.parseInt(id);
    }

    public String getAlbum() {
        return album;
    }

    public String getDisplayName() {
        return displayName;
    }


    public void update(MusicInfo musicInfo){
        setAlbum(musicInfo.getAlbum());
        setDuration(musicInfo.getDuration());
        setArtist(musicInfo.getArtist());
        setDisplayName(musicInfo.getDisplayName());
        setUrl(musicInfo.getUrl());
        //setSecondItems(music.getSecondItems());
    }

    public Bitmap getAlbumBitmap(Context context){
        LogTool.log("Music","getAlbumBitmap()拿url"+url);
        Bitmap bitmap = OneBitmapUtil.getAlbumArt(url);
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


    public String getDuration(){
        return duration;
    }


    public String getUrl(){
        if(url!=null) {
            return url;
        }else
            return "<unknown>";
    }
//    public ArrayList<MusicInfo> getSecondItems(){
//
//        return musicArrayList;
//    }

//    public void setSecondItems(ArrayList<MusicInfo> musicArrayList){
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
    public boolean equals(Object object) {
        MusicInfo musicInfo = (MusicInfo)object;
        if(musicInfo.getUrl().equals(url)){
            return true;
        }else {
            return false;
        }
    }

    public String getNameByFileName(String name){
        String getName = name;
        if(name==null){
            return null;
        }
        if (name.contains(".mp3")) {

            getName = name.split(".mp3")[0];
        }
        if (name.contains(".ogg")) {
            getName = name.split(".ogg")[0];
        }
        if(name.contains(" - ")) {
            String[] names = name.split(" - ");
            if (names.length >= 1) {
                getName = names[1];
            }
        }
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

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(artist);
//        dest.writeString(duration);
//        dest.writeString(album);
//        dest.writeString(displayName);
//        dest.writeString(url);
//        dest.writeByte((byte)(isPlayable ?1:0));
//        dest.writeList(musicArrayList);
//        dest.writeString(id);
//    }
}
