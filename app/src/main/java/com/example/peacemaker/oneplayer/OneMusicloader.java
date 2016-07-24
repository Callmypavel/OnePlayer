package com.example.peacemaker.oneplayer;

import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.drm.DrmManagerClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.DisplayMetrics;
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
    Handler handler;
    private static ContentResolver contentResolver;
    //Uri，指向external的database
    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String sortOrder = Media.DATA;
    private boolean isStop = false;

    public OneMusicloader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
    public OneMusicloader(Handler handler){
        this.handler = handler;

    }

    protected ArrayList<Music> loadLocalMusic() {
        Cursor cursor;
        ArrayList<Music> musicArrayList = new ArrayList<>();
        if(contentResolver!=null) {
            cursor = contentResolver.query(contentUri, null, null, null, sortOrder);
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
                    String duration = cursor.getString(cursor.getColumnIndex(Media.DURATION));
                    String size = cursor.getString(cursor.getColumnIndex(Media.SIZE));
                    String id = cursor.getString(cursor.getColumnIndex(Media._ID));
                    String album = cursor.getString(cursor.getColumnIndex(Media.ALBUM));
                    String displayName = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
                    String url = cursor.getString(cursor.getColumnIndex(Media.DATA));
                    Music music = new Music(artist, duration, album, displayName, url, id, size,true);
                    musicArrayList.add(music);
                }
                cursor.close();
            }
        }

        System.out.println("加载了" + musicArrayList.size() + "首歌");
        return musicArrayList;
    }
    public static Bitmap getAlbumArt(String url) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(url); //设置数据源
            byte[] embedPic = mediaMetadataRetriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
            //Log.v("OneMusicLoader","专辑封面原始大小：宽："+bitmap.getWidth()+",高："+bitmap.getHeight());
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (Exception e2) {
                //e2.printStackTrace();
            }
        }
        return bitmap;
    }



    //深度扫描
    protected void getdeepLoad(File file) {
        beginDeepLoad(file);
        //System.out.println("扫描了"+musicArrayList.size());
        //return musicArrayList;
    }
    public void stopLoading(){
        isStop = true;
    }



    public void beginDeepLoad(final File file) {
        // File tempfile = null;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isStop = false;
                deepload2(file);
                if(!isStop) {
                    Message message = new Message();
                    message.what = 0x127;
                    Log.v("OneMusicLoader", "handleMessage()0x127");
                    handler.sendMessage(message);
                }
            }
        });
        thread.start();


    }
    private void deepLoad(File file){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        if(!isStop) {
            File[] files = file.listFiles();
            int max = files.length-1;
            for (int i=0;i<max;i++) {
                File file1 = files[i];
                if (file1.isDirectory()) {
                    deepLoad(file1);
                } else {
                    Message message = new Message();
                    message.what = 0x125;
                    Bundle bundle = new Bundle();
                    bundle.putString("name", file1.getName());
                    bundle.putFloat("percent", i/(max*1.0f));
                    message.setData(bundle);
                    handler.sendMessage(message);
                    //listener.onSearch(file1.getName());
                    if (file1.getName().trim().toLowerCase().endsWith(".mp3")) {
                        try {
                            message = new Message();
                            message.what = 0x126;
                            bundle = new Bundle();
                            bundle.putString("name", file1.getName());
                            bundle.putParcelable("music", getMusic(file1,mediaMetadataRetriever));
                            message.setData(bundle);
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (file1.getName().trim().toLowerCase().endsWith(".lrc")) {
                        System.out.println("捕获大逆不道之罪人" + file1.getName());
                    }
                }
            }
        }

    }
    public void deepload2(File file){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        Message message;
        Bundle bundle;
        LinkedList directorylist = new LinkedList();
        File files[] = file.listFiles();
        for (File file1 : files) {
            message = new Message();
            message.what = 0x125;
            bundle = new Bundle();
            bundle.putString("name", file1.getName());
            bundle.putFloat("percent", 0);
            message.setData(bundle);
            handler.sendMessage(message);
            if (file1.isDirectory()) {
                directorylist.add(file1);
            } else{
                Music music = getMusic(file1,mediaMetadataRetriever);
                if(music!=null) {
                    message = new Message();
                    message.what = 0x126;
                    bundle = new Bundle();
                    bundle.putParcelable("music",music);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }
        File tmp;
        while (!directorylist.isEmpty()&&!isStop) {
            tmp = (File)directorylist.removeFirst();
            if (tmp.isDirectory()) {
                if (tmp.listFiles() == null)
                    continue;
                for (File file2 : tmp.listFiles()) {
                    if (file2.isDirectory()) {
                        directorylist.add(file2);
                    }else{
                        Music music = getMusic(file2,mediaMetadataRetriever);
                        if(music!=null) {
                            message = new Message();
                            message.what = 0x126;
                            bundle = new Bundle();
                            bundle.putParcelable("music",music);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                    message = new Message();
                    message.what = 0x125;
                    bundle = new Bundle();
                    bundle.putString("name", file2.getName());
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            } else {
                Music music = getMusic(tmp,mediaMetadataRetriever);
                if(music!=null) {
                    message = new Message();
                    message.what = 0x126;
                    bundle = new Bundle();
                    bundle.putParcelable("music",music);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }


        }
    }
    public void add2MusicList(File file,MediaMetadataRetriever mediaMetadataRetriever){
        String filename = file.getName().trim().toLowerCase();
        //Log.v("OneMusicLoader","凶手稽查"+filename);
        if (filename.endsWith(".mp3")||filename.endsWith(".ogg")||filename.endsWith(".flac")) {
            String path = file.getPath();
            if(mediaMetadataRetriever==null) {
                mediaMetadataRetriever = new MediaMetadataRetriever();
            }
            try {
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
                //System.out.println("标题为:"+title+" 专辑为:"+album+" mime为:"+mime+" 艺术家为:"+artist+" 长度为:"+duration+" 比特率为:"+bitrate+" 日期为:"+date);
                Music music = new Music(artist, duration, album, title, path,true);
                musicArrayList.add(music);
            }catch (Exception e){

            }

        }
    }
    public Music getMusic(File file,MediaMetadataRetriever mediaMetadataRetriever){
        Music music = null;
        String filename = file.getName().trim().toLowerCase();
        if (filename.endsWith(".mp3")||filename.endsWith(".ogg")||filename.endsWith(".flac")) {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            String path = file.getPath();
            if(mediaMetadataRetriever==null) {
                mediaMetadataRetriever = new MediaMetadataRetriever();
            }
            try {
                mediaMetadataRetriever.setDataSource(path);
                String title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String mime = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
                String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
                String bitrate = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
                String date = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
                if (title == null) {
                    title = file.getName();
                }
                //System.out.println("标题为:"+title+" 专辑为:"+album+" mime为:"+mime+" 艺术家为:"+artist+" 长度为:"+duration+" 比特率为:"+bitrate+" 日期为:"+date);
                music = new Music(artist, duration, album, title, path, true);
            }catch (Exception e){

            }
        }
        return music;
    }

}
