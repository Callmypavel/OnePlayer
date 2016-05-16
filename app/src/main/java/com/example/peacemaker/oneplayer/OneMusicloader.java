package com.example.peacemaker.oneplayer;

import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.drm.DrmManagerClient;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class OneMusicloader {
    public ArrayList<Music> musicArrayList = new ArrayList<Music>();
    FileSearchingListener listener;
    Handler handler;
    private static ContentResolver contentResolver;
    //Uri，指向external的database
    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String[] projection = {
            Media._ID,
            Media.DISPLAY_NAME,
            Media.DATA,
            Media.ALBUM,
            Media.ARTIST,
            Media.DURATION,
    };
    private String where = "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";
    private String sortOrder = Media.DATA;
    private String artist;

    public OneMusicloader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
    public OneMusicloader(Handler handler){
        this.handler = handler;

    }
    public OneMusicloader(FileSearchingListener listener){
        this.listener = listener;
    }

    protected ArrayList<Music> loadLocalMusic() {

        ArrayList<Music> musicArrayList = new ArrayList<Music>();
        Cursor cursor = contentResolver.query(contentUri, null, null, null, sortOrder);
        while (cursor.moveToNext()) {
            String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
            String duration = cursor.getString(cursor.getColumnIndex(Media.DURATION));
            //String size = cursor.getString(cursor.getColumnIndex(Media.SIZE));
            //String id = cursor.getString(cursor.getColumnIndex(Media._ID));
            String album = cursor.getString(cursor.getColumnIndex(Media.ALBUM));
            String displayName = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
            String url = cursor.getString(cursor.getColumnIndex(Media.DATA));
            Music music = new Music(artist, duration, album, displayName, url);
            musicArrayList.add(music);
        }
        System.out.println("加载了" + musicArrayList.size() + "首歌");
        return musicArrayList;
    }

    //深度扫描
    protected ArrayList<Music> getdeepLoad(File file) {
        deepLoad(file);
        System.out.println("扫描了"+musicArrayList.size());
        return musicArrayList;
    }
    public void setFileSearchingListener(FileSearchingListener listener){
        this.listener = listener;

    }


    public void deepLoad(File file) {

        File[] files = file.listFiles();
        // File tempfile = null;
        for (File file1 : files) {
            //如果file1路径为目录
            if (file1.isDirectory()) {
                deepLoad(file1);
            } else {
                //listener.onSearch(file1.getName());
                Message message = new Message();
                message.what = 0x126;
                Bundle bundle = new Bundle();
                bundle.putString("name",file1.getName());
                message.setData(bundle);
                handler.sendMessage(message);
                if (file1.getName().trim().toLowerCase().endsWith(".mp3")) {
                    System.out.println("获得音乐" + file1.getName());
                    try {
                        add2MusicList(file1);
                    } catch (Exception e) {
                        System.out.println("尼玛 抛异常了");
                        e.printStackTrace();
                    }
                }
                if (file1.getName().trim().toLowerCase().endsWith(".lrc")){
                    System.out.println("捕获大逆不道之罪人"+file1.getName());
                }
            }
        }

    }
    public void deepload2(File file){
        int num = 0;
        LinkedList directorylist = new LinkedList();
        File files[] = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory())
                directorylist.add(file1);
            else{

                System.out.println(file1.getAbsolutePath());
                num++;
            }
        }
        File tmp;
        while (!directorylist.isEmpty()) {
            tmp = (File)directorylist.removeFirst();//首个目录
            if (tmp.isDirectory()) {
                if (tmp.listFiles() == null)
                    continue;
                for (File file2 : tmp.listFiles()) {
                    if (file2.isDirectory())
                        directorylist.add(file2);//目录则加入目录列表，关键
                    else{
                        add2MusicList(file2);
                        System.out.println(file2);
                        num++;
                    }
                }
            } else {
                add2MusicList(tmp);
                System.out.println(tmp);
                num++;
            }
        }
    }
    public void add2MusicList(File file){
        String path = file.getPath();
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String mime = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        String bitrate = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        String date = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        if(title==null){
            title = file.getName();
        }
        System.out.println("标题为:"+title+" 专辑为:"+album+" mime为:"+mime+" 艺术家为:"+artist+" 长度为:"+duration+" 比特率为:"+bitrate+" 日期为:"+date);
        Music music = new Music(artist, duration, album, title, path);
        musicArrayList.add(music);
    }

}