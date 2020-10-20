package one.peace.oneplayer.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.AlbumInfo;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;

/**
 * Created by pavel on 2019/11/14.
 */
public abstract class MusicLoader extends Worker {

    public final static int LOAD_PASS = 1;
    public final static int LOAD_FOUND = 2;
    public final static int LOAD_PROGRESS = 3;
    public final static int LOAD_FINISHED = 4;
    private static IndexedEntities<MusicInfo> indexedSingerInfos;
    private static IndexedEntities<MusicInfo> indexAlbumInfos;
    private static ArrayList<LoadMusicListener> mLoadMusicListeners;
    private static Handler handler;

    public MusicLoader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        loadMusic();
        return Result.success();
    }

    public interface LoadMusicListener {
        void loadPass(String fileName);

        void loadFound(MusicInfo musicInfo);

        void loadFinished();

        void loadProgressChanged(int progress);
    }


    public static void addLoadMusicListener(LoadMusicListener loadMusicListener) {
        if (mLoadMusicListeners == null) {
            mLoadMusicListeners = new ArrayList<>();
        }
        mLoadMusicListeners.add(loadMusicListener);
    }

    public static void addLoadSingerAndAlbumListeners(IndexedEntities.IndexInfoChangedListener singerListener, IndexedEntities.IndexInfoChangedListener albumListener) {
        indexedSingerInfos = new IndexedEntities<MusicInfo>(MusicInfo.class, "artist", null, singerListener);
        indexAlbumInfos = new IndexedEntities<MusicInfo>(MusicInfo.class, "album", null, albumListener);
    }

    protected abstract void loadMusic();

    public static void initHandler() {
        //LogTool.logCrime("MusicLoader","调用几次！！！");
        //默认情况下获取当前线程的Looper
        if (handler == null) {
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (mLoadMusicListeners != null) {
                        switch (msg.what) {
                            case LOAD_PASS:
                                for (LoadMusicListener musicListener : mLoadMusicListeners) {
                                    musicListener.loadPass(msg.getData().getString("fileName"));
                                }
                                break;
                            case LOAD_FOUND:
                                MusicInfo musicInfo = msg.getData().getParcelable("musicInfo");
                                for (LoadMusicListener musicListener : mLoadMusicListeners) {
                                    musicListener.loadFound(musicInfo);
                                }
                                indexedSingerInfos.addNew(musicInfo);
                                indexAlbumInfos.addNew(musicInfo);
                                break;
                            case LOAD_PROGRESS:
                                for (LoadMusicListener musicListener : mLoadMusicListeners) {
                                    musicListener.loadProgressChanged(msg.getData().getInt("progress"));
                                }
                                break;
                            case LOAD_FINISHED:
                                for (LoadMusicListener musicListener : mLoadMusicListeners) {
                                    musicListener.loadFinished();
                                }
                                break;
                        }
                    }
                    return false;
                }
            });
        }
    }

//    public static void startLoading(){
//        mOneTimeWorkRequest = new OneTimeWorkRequest.Builder(this.getClass()).build();
//        WorkManager.getInstance().enqueue(mOneTimeWorkRequest);
//    }
//
//    public void stopLoading(){
//        if (mOneTimeWorkRequest != null) {
//            WorkManager.getInstance().cancelWorkById(mOneTimeWorkRequest.getId());
//        }
//    }

    protected void scanFinished(){
        sendSimpleMessage(LOAD_FINISHED);
    }

    protected void scanPass(String fileName){
        sendSimpleStringMessage(LOAD_PASS, "fileName", fileName);
    }

    protected void scanFound(MusicInfo musicInfo) {
        sendSimpleParcelableMessage(LOAD_FOUND, "musicInfo", musicInfo);
        //LogTool.log(this,"秋风:"+musicInfo.getUrl());
    }

    protected void scanProgressChanged(int progress) {
        sendSimpleIntMessage(LOAD_PROGRESS, "progress", progress);
    }

    private void sendSimpleParcelableMessage(int what, String key, Parcelable value) {
        Message message = Message.obtain();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, value);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void sendSimpleStringMessage(int what, String key, String value) {
        Message message = Message.obtain();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void sendSimpleIntMessage(int what, String key, int value) {
        Message message = Message.obtain();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void sendSimpleMessage(int what) {
        Message message = Message.obtain();
        message.what = what;
        handler.sendMessage(message);
    }
}
