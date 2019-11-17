package one.peace.oneplayer.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import one.peace.oneplayer.music.entity.MusicInfo;

/**
 * Created by pavel on 2019/11/14.
 */
public abstract class MusicLoader extends Worker {

    public final static int LOAD_PASS = 1;
    public final static int LOAD_FOUND = 2;
    public final static int LOAD_PROGRESS = 3;
    public final static int LOAD_FINISHED = 4;
    private static ArrayList<LoadMusicListener> mLoadMusicListeners;
    private static Handler handler;

    public MusicLoader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);


    }

    @NonNull
    @Override
    public Result doWork() {

        loadMusic();
        scanFinished();
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

    protected abstract void loadMusic();

    public static void initHandler() {
        //默认情况下获取当前线程的Looper
        if (handler == null) {
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (mLoadMusicListeners != null) {
                        for (LoadMusicListener musicListener : mLoadMusicListeners) {
                            switch (msg.what) {
                                case LOAD_PASS:
                                    musicListener.loadPass(msg.getData().getString("fileName"));
                                    break;
                                case LOAD_FOUND:
                                    musicListener.loadFound((MusicInfo) msg.getData().getParcelable("musicInfo"));
                                    break;
                                case LOAD_PROGRESS:
                                    musicListener.loadProgressChanged(msg.getData().getInt("progress"));
                                    break;
                                case LOAD_FINISHED:
                                    musicListener.loadFinished();
                                    break;
                            }
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
        Message message = Message.obtain();
        message.what = LOAD_FINISHED;
        handler.sendMessage(message);
    }

    protected void scanPass(String fileName){
        Message message = Message.obtain();
        message.what = LOAD_PASS;
        Bundle bundle = new Bundle();
        bundle.putString("fileName", fileName);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    protected void scanFound(MusicInfo musicInfo){
        Message message = Message.obtain();
        message.what = LOAD_FOUND;
        Bundle bundle = new Bundle();
        bundle.putParcelable("musicInfo", musicInfo);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    protected void scanProgressChanged(int progress){
        Message message = Message.obtain();
        message.what = LOAD_PROGRESS;
        Bundle bundle = new Bundle();
        bundle.putInt("progress", progress);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
