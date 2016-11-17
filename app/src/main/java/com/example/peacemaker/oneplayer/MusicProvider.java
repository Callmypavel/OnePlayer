package com.example.peacemaker.oneplayer;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by ouyan on 2016/6/8.
 */

public class MusicProvider implements Parcelable{
    private static final long serialVersionUID = 1L;
    public static Parcelable.Creator<MusicProvider>  CREATOR =
            new Creator<MusicProvider>() {
                @Override
                public MusicProvider createFromParcel(Parcel source) {
                    return new MusicProvider(source);
                }

                @Override
                public MusicProvider[] newArray(int size) {
                    return new MusicProvider[size];
                }
            };
    private ArrayList<Music> musicSource;
    private ArrayList<Music> singers ;
    private ArrayList<Music> albums ;
    private ArrayList<IndexedMusic> indexedSingers;
    private ArrayList<IndexedMusic> indexedAlbums;
    private ArrayList<IndexedMusic> indexedSongs;
    private MusicProvider(Parcel in)
    {
        musicSource = in.readArrayList(getClass().getClassLoader());
        singers = in.readArrayList(getClass().getClassLoader());
        albums = in.readArrayList(getClass().getClassLoader());

    }
    public int getCount(){
        if(musicSource!=null){
            return musicSource.size();
        }else return 0;
    }
    public MusicProvider(ArrayList<Music> musicSource) {
        this.musicSource = musicSource;
        sortMusics();
        indexedSingers = IndexMusicTool.getIndexedMusics(singers, IndexMusicTool.IndexType.Singer);
        indexedAlbums = IndexMusicTool.getIndexedMusics(albums, IndexMusicTool.IndexType.Album);
        indexedSongs = IndexMusicTool.getIndexedMusics(musicSource, IndexMusicTool.IndexType.Song);

    }

    public ArrayList<IndexedMusic> getIndexedSongs() {
        return indexedSongs;
    }

    public ArrayList<IndexedMusic> getIndexedSingers() {
        return indexedSingers;
    }

    public ArrayList<IndexedMusic> getIndexedAlbums() {
        return indexedAlbums;
    }

    private void sortMusics(){
        if(musicSource!=null) {
            if(musicSource.size()!=0) {
                singers = new ArrayList<>();
                albums = new ArrayList<>();
                Comparator<Music> comparator = new Comparator<Music>() {
                    @Override
                    public int compare(Music lhs, Music rhs) {
                        if(lhs!=null&&rhs!=null) {
                            return Mandarin2Pinyin(lhs.getDisplayName()).toLowerCase().compareTo(Mandarin2Pinyin(rhs.getDisplayName()).toLowerCase());
                        }else {
                            return 0;
                        }
                    }
                };
                Collections.sort(musicSource, comparator);
                for (Music music : musicSource) {
                    Music music1 = new Music(music.getArtist());
                    ArrayList<Music> secondItems;
                    int singerIndex = getIndex(singers, music1);
                    if (singerIndex == -1) {
                       // Log.v("MusicProvider", "新增歌手" + music.getArtist() + "的" + music.getDisplayName());
                        secondItems = new ArrayList<>();
                        secondItems.add(music);
                        music1.setSecondItems(secondItems);
                        singers.add(music1);
                    } else {
                       // Log.v("MusicProvider", "更新歌手" + music.getArtist() + "的" + music.getDisplayName());
                        singers.get(singerIndex).addSecondItem(music);
                    }
                    Music music2 = new Music(music.getAlbum());
                    int albumIndex = getIndex(albums, music2);
                    if (albumIndex == -1) {
                        //Log.v("MusicProvider", "新增专辑" + music.getArtist() + "的" + music.getAlbum());
                        secondItems = new ArrayList<>();
                        secondItems.add(music);
                        music2.setSecondItems(secondItems);
                        albums.add(music2);
                    } else {
                        //Log.v("MusicProvider", "更新专辑" + music.getArtist() + "的" + music.getAlbum());
                        albums.get(albumIndex).addSecondItem(music);
                    }
                }
                Collections.sort(singers, comparator);
                for (Music singer : singers) {
                    Collections.sort(singer.getSecondItems(), comparator);
                    //Log.v("MusicProvider","抽查歌手"+singer.getSecondItems());
                    //Log.v("MusicProvider","抽查歌手歌曲"+singer.getSecondItems().get(0).getDisplayName());
                }
                Collections.sort(albums, comparator);
                for (Music album : albums) {
                    Collections.sort(album.getSecondItems(), comparator);
                }
            }
        }
    }

    public ArrayList<Music> getSingers(){
        return singers;
    }
    public ArrayList<Music> getAlbums(){
        return albums;
    }
    public ArrayList<Music> getSongs(){
        return musicSource;
    }
    public String Mandarin2Pinyin(String src){
        char[] t1 ;
        t1=src.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4="";
        int t0=t1.length;
        try {
            for (int i=0;i<t0;i++)
            {
                //判断是否为汉字字符
                if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], outputFormat);
                    t4+=t2[0];
                }
                else
                    t4+=java.lang.Character.toString(t1[i]);
            }
            return t4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t4;
    }
    private int getIndex(ArrayList<Music> musicArrayList,Music music){
        int index = -1;
        if(musicArrayList==null){
            return index;
        }
        for(int i=0;i<musicArrayList.size();i++){
            if (musicArrayList.get(i).getDisplayName().equals(music.getDisplayName())){
                index = i;
                return index;
            }
        }
        return index;
    }

    @Override
    public String toString() {
        String string = "";
        if(singers==null){
            string+="没有歌手列表，";
        }else {
            string+="有"+singers.size()+"位歌手";
        }
        if(albums==null){
            string+="没有专辑列表,";
        }else {
            string+="有"+albums.size()+"张专辑";
        }
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(musicSource);
        dest.writeList(singers);
        dest.writeList(albums);
    }
}
