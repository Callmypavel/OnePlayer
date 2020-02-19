package one.peace.oneplayer.util;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.AlbumInfo;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;

public class LocalMusicLoader extends MusicLoader {
    private String[] supportedMusicFormats = new String[]{".mp3",".ogg",".flac"};
    private String rootDirectory = "/mnt/sdcard";//扫描开始的根目录

    public LocalMusicLoader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @Override
    protected void loadMusic() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        LinkedList directorylist = new LinkedList();
        File[] rootFiles = new File(rootDirectory).listFiles();
        for (File rootFile : rootFiles) {
            //处理根目录下的文件
            scanPass(rootFile.getName());
            if (rootFile.isDirectory()) {
                directorylist.add(rootFile);
            } else {
                MusicInfo musicInfo = getMusic(rootFile,mediaMetadataRetriever);
                if(musicInfo != null) {
                    scanFound(musicInfo);

                }
            }
        }
        File tmp;
        while (!directorylist.isEmpty()) {
            tmp = (File)directorylist.removeFirst();
            if (tmp.isDirectory()) {
                if (tmp.listFiles() == null)
                    continue;
                for (File directoryFile : tmp.listFiles()) {
                    //扫描目录下的文件，如果还是文件夹，就继续添加到文件夹的列表里，直到列表空了
                    if (directoryFile.isDirectory()) {
                        directorylist.add(directoryFile);
                    }else{
                        MusicInfo musicInfo = getMusic(directoryFile,mediaMetadataRetriever);
                        if(musicInfo != null) {
                            scanFound(musicInfo);
                        }
                    }
                    scanPass(directoryFile.getName());
                }
            } else {
                MusicInfo musicInfo = getMusic(tmp,mediaMetadataRetriever);
                if(musicInfo!=null) {
                    scanFound(musicInfo);
                }
            }


        }


    }

    public MusicInfo getMusic(File file,MediaMetadataRetriever mediaMetadataRetriever){
        MusicInfo musicInfo = null;
        String filename = file.getName().trim().toLowerCase();
        if (StringUtil.isEndWith(supportedMusicFormats,filename)) {
            String path = file.getPath();
            if(mediaMetadataRetriever==null) {
                mediaMetadataRetriever = new MediaMetadataRetriever();
            }
            try {
                mediaMetadataRetriever.setDataSource(path);
                String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
                if (title == null) {
                    title = file.getName();
                }
                musicInfo = new MusicInfo();
                musicInfo.setDisplayName(title);
                musicInfo.setAlbum(album);
                musicInfo.setArtist(artist);
                musicInfo.setDuration(duration);
                musicInfo.setUrl(path);
                musicInfo.setPlayable(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return musicInfo;
    }
}
