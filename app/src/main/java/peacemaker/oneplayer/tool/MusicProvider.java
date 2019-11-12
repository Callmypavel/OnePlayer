package peacemaker.oneplayer.tool;

import android.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.entity.AlbumInfo;
import peacemaker.oneplayer.entity.IndexedMusic;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.entity.SingerInfo;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;

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
    private static ObservableArrayList<Music> musicSource;
    private ObservableArrayList<SingerInfo> singers ;
    private ObservableArrayList<AlbumInfo> albums ;
    private ArrayList<IndexedMusic> indexedSingers;
    private ArrayList<IndexedMusic> indexedAlbums;
    private ArrayList<IndexedMusic> indexedSongs;
    private MusicProvider(Parcel in)
    {

        musicSource = EntityConverter.ArraylistToObservableOne(in.readArrayList(getClass().getClassLoader()));
        singers = EntityConverter.ArraylistToObservableOne(in.readArrayList(getClass().getClassLoader()));
        albums = EntityConverter.ArraylistToObservableOne(in.readArrayList(getClass().getClassLoader()));

    }
    public int getCount(){
        if(musicSource!=null){
            return musicSource.size();
        }else return 0;
    }
    public MusicProvider(ObservableArrayList<Music> musicSource) {
        this.musicSource = musicSource;
        sortMusics();
//        indexedSingers = IndexMusicTool.getIndexedMusics(singers, IndexMusicTool.IndexType.Singer);
//        indexedAlbums = IndexMusicTool.getIndexedMusics(albums, IndexMusicTool.IndexType.Album);
//        indexedSongs = IndexMusicTool.getIndexedMusics(musicSource, IndexMusicTool.IndexType.Song);

    }
    public static void loadAlbum(final String albumId,final AlbumInfo albumInfo){
        //异步地去加载模拟网络环境
        new Thread(new Runnable() {
            @Override
            public void run() {
                albumInfo.setAlbumName(albumId);
            }
        }).start();
    }
    public static void loadSongsByAlbumId(final String albumId,final ObservableArrayList<Music> musics,final AlbumInfo albumInfo){
        //异步地去加载模拟网络环境
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Music music : musicSource) {
                    //LogTool.log(this,music.getAlbum());
                    if (music.getAlbum().equals(albumId)){
                        //LogTool.log(this,"找到专辑中的歌曲！");
                        musics.add(music);
                    }
                }
                albumInfo.setAlbumImage(musics.get(0).getAlbumBitmap(OneApplication.context));
            }
        }).start();

    }
    public static void loadSinger(final String singerId,final SingerInfo singerInfo){
        //异步地去加载模拟网络环境
        new Thread(new Runnable() {
            @Override
            public void run() {
                singerInfo.setSingerName(singerId);
            }
        }).start();
    }

    public static void loadAlbumImage(final AlbumInfo albumInfo){
        //异步地去加载模拟网络环境
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Music music : musicSource) {
                    if (music.getAlbum().equals(albumInfo.getAlbumBitmapId())){
                        Bitmap bitmap = music.getMiddleAlbumArt(OneApplication.context);
                        albumInfo.setAlbumImage(bitmap);
                        OneImageCache.getImageCache().addToCache(albumInfo.getAlbumBitmapId(), bitmap);
                        break;
                    }
                }
            }
        }).start();
    }

    public static void loadSongsBySingerId(final String singerId,final ObservableArrayList<Music> musics){
        //异步地去加载模拟网络环境
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Music music : musicSource) {
                    if (music.getArtist().equals(singerId)){
                        musics.add(music);
                    }
                }
            }
        }).start();

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
                singers = new ObservableArrayList<>();
                albums = new ObservableArrayList<>();
                Comparator<Music> comparator = new Comparator<Music>() {
                    @Override
                    public int compare(Music lhs, Music rhs) {
                        if(lhs!=null&&rhs!=null) {
                            return lhs.getDisplayName().toLowerCase().compareTo(rhs.getDisplayName().toLowerCase());
                        }else {
                            return 0;
                        }
                    }
                };
                Collections.sort(musicSource, comparator);
                for (Music music : musicSource) {
                    SingerInfo singerInfo = new SingerInfo(music.getArtist());
                    int singerIndex = getIndex(singers, singerInfo);
                    if (singerIndex == -1) {
                       // Log.v("MusicProvider", "新增歌手" + music.getArtist() + "的" + music.getDisplayName());
//                        secondItems = new ArrayList<>();
//                        secondItems.add(music);
//                        music1.setSecondItems(secondItems);
                        singerInfo.setSongsNumber(1);
                        singers.add(singerInfo);
                        music.setSingerInfo(singerInfo);
                    } else {
                       // Log.v("MusicProvider", "更新歌手" + music.getArtist() + "的" + music.getDisplayName());
                        singers.get(singerIndex).setSongsNumber(singers.get(singerIndex).getSongsNumber()+1);
                        music.setSingerInfo(singers.get(singerIndex));
                    }
                    AlbumInfo albumInfo = new AlbumInfo(music.getAlbum());
                    int albumIndex = getIndex(albums, albumInfo);
                    if (albumIndex == -1) {
                        //Log.v("MusicProvider", "新增专辑" + music.getArtist() + "的" + music.getAlbum());
//                        secondItems = new ArrayList<>();
//                        secondItems.add(music);
//                        music2.setSecondItems(secondItems);
//                        albums.add(music2);
                        albumInfo.setSingerName(music.getArtist());
                        albumInfo.setAlbumBitmapId(music.getAlbum());
                        albumInfo.setSongsNumber(1);
                        albums.add(albumInfo);
                        music.setAlbumInfo(albumInfo);
                    } else {
                        //Log.v("MusicProvider", "更新专辑" + music.getArtist() + "的" + music.getAlbum());
                        albums.get(albumIndex).setSongsNumber(albums.get(albumIndex).getSongsNumber()+1);
                        music.setAlbumInfo(albums.get(albumIndex));
                    }
                }
                Collections.sort(singers, SingerInfo.comparator);
//                for (Music singer : singers) {
//                    Collections.sort(singer.getSecondItems(), comparator);
//                    //Log.v("MusicProvider","抽查歌手"+singer.getSecondItems());
//                    //Log.v("MusicProvider","抽查歌手歌曲"+singer.getSecondItems().get(0).getDisplayName());
//                }
                Collections.sort(albums, AlbumInfo.comparator);
//                for (Music album : albums) {
//                    Collections.sort(album.getSecondItems(), comparator);
//                }
            }
        }
    }

    public ObservableArrayList<SingerInfo> getSingers(){
        return singers;
    }
    public ObservableArrayList<AlbumInfo> getAlbums(){
        return albums;
    }
    public ObservableArrayList<Music> getSongs(){
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
    private int getIndex(ArrayList<SingerInfo> singerInfos,SingerInfo singerInfo){
        int index = -1;
        if(singerInfos==null){
            return index;
        }
        for(int i=0;i<singerInfos.size();i++){
            if (singerInfos.get(i).getSingerName().equals(singerInfo.getSingerName())){
                index = i;
                return index;
            }
        }
        return index;
    }
    private int getIndex(ArrayList<AlbumInfo> albumInfos,AlbumInfo albumInfo){
        int index = -1;
        if(albumInfos==null){
            return index;
        }
        for(int i=0;i<albumInfos.size();i++){
            if (albumInfos.get(i).getAlbumName().equals(albumInfo.getAlbumName())){
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
