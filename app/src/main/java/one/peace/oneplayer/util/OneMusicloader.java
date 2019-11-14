package one.peace.oneplayer.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import one.peace.oneplayer.music.entity.MusicInfo;


/**
 * Created by ouyan_000 on 2015/8/18.
 */
public class OneMusicloader {
    public ArrayList<MusicInfo> musicArrayList = new ArrayList<MusicInfo>();
    Handler handler;
    private static ContentResolver contentResolver;
    //Uri，指向external的database
    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String sortOrder = Media.DATA;
    private Context context;
    private String[] supportedMusicFormats = new String[]{".mp3",".ogg",".flac"};
    private String rootDirectory = "/mnt/sdcard";//扫描开始的根目录
    public final int SCAN_PASS = 1;
    public final int SCAN_FOUND = 2;
    public final int SCAN_FINISHED = 3;
    private OneTimeWorkRequest mDeepLoadWorkRequest;
    private OneTimeWorkRequest mNormalLoadWorkRequest;
    public OneMusicloader(ContentResolver contentResolver,Context context) {
        this.contentResolver = contentResolver;
        this.context = context;
    }
    public OneMusicloader(Handler handler){
        this.handler = handler;
    }

    class DeepLoadLocalMusicWorker extends Worker{

        public DeepLoadLocalMusicWorker(@NonNull Context context, @NonNull WorkerParameters workerParams,File file) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
            beginDeepLoad();
            scanFinished();
            return Result.success();
        }
    }

    protected ArrayList<MusicInfo> loadLocalMusic() {
        Cursor cursor;
        ArrayList<MusicInfo> musicArrayList;
        musicArrayList = new ArrayList<>();
        if(contentResolver!=null) {
            cursor = contentResolver.query(contentUri, null, null, null, sortOrder);
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    String artist = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
                    //UNK
                    String duration = cursor.getString(cursor.getColumnIndex(Media.DURATION));
                    String size = cursor.getString(cursor.getColumnIndex(Media.SIZE));
                    String id = cursor.getString(cursor.getColumnIndex(Media._ID));
                    String album = cursor.getString(cursor.getColumnIndex(Media.ALBUM));
                    String displayName = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
                    String url = cursor.getString(cursor.getColumnIndex(Media.DATA));
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setDuration(duration);
                    musicInfo.setSize(size);
                    musicInfo.setId(id);
                    musicInfo.setAlbum(album);
                    musicInfo.setDisplayName(displayName);
                    musicInfo.setUrl(url);
                    musicInfo.setPlayable(true);
                    musicArrayList.add(musicInfo);
                }
                cursor.close();
            }
        }
        return musicArrayList;
    }


    public void stopLoading(){
        if (mOneTimeWorkRequest != null) {
            WorkManager.getInstance().cancelWorkById(mOneTimeWorkRequest.getId());
        }
    }

    private void scanFinished(){
        Message message = Message.obtain();
        message.what = SCAN_FINISHED;
        handler.sendMessage(message);
    }

    private void scanPass(String fileName){
        Message message = Message.obtain();
        message.what = SCAN_PASS;
        Bundle bundle = new Bundle();
        bundle.putString("fileName", fileName);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void scanFound(MusicInfo musicInfo){
        Message message = Message.obtain();
        message.what = SCAN_FOUND;
        Bundle bundle = new Bundle();
        bundle.putParcelable("musicInfo", musicInfo);
        message.setData(bundle);
        handler.sendMessage(message);
    }


    public void beginDeepLoad() {
        mOneTimeWorkRequest = new OneTimeWorkRequest.Builder(DeepLoadLocalMusicWorker.class).build();
        WorkManager.getInstance().enqueue(mOneTimeWorkRequest);
    }

    public void deepLoad(){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        LinkedList directorylist = new LinkedList();
        File file = new File(rootDirectory);
        for (File file1 : file.listFiles()) {
            //处理根目录下的文件
            scanPass(file1.getName());
            if (file1.isDirectory()) {
                directorylist.add(file1);
            } else {
                MusicInfo musicInfo = getMusic(file1,mediaMetadataRetriever);
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
                for (File file2 : tmp.listFiles()) {
                    //扫描目录下的文件，如果还是文件夹，就继续添加到文件夹的列表里，直到列表空了
                    if (file2.isDirectory()) {
                        directorylist.add(file2);
                    }else{
                        MusicInfo musicInfo = getMusic(file2,mediaMetadataRetriever);
                        if(musicInfo != null) {
                            scanFound(musicInfo);
                        }
                    }
                    scanPass(file2.getName());
                }
            } else {
                MusicInfo musicInfo = getMusic(tmp,mediaMetadataRetriever);
                if(musicInfo!=null) {
                    scanFound(musicInfo);
                }
            }


        }
    }
    public void add2MusicList(File file,MediaMetadataRetriever mediaMetadataRetriever){
        String filename = file.getName().trim().toLowerCase();
        musicArrayList.add(getMusic(file,mediaMetadataRetriever));
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
