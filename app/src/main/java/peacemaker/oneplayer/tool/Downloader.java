package peacemaker.oneplayer.tool;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ouyan_000 on 2015/8/10.
 */
public class Downloader {
    HttpURLConnection httpURLConnection;
    String apkname = "OnePlayer.apk";
    Boolean cancel = false;
    String savePath = Environment.getExternalStorageDirectory().getPath();
    int progress = 0;
    InputStream inputStream;
    FileOutputStream fileOutputStream;
    public void Downloader(String apkname){
        this.apkname = apkname;
    }
    public void getDownload(String url,Handler handler) {
        try {
            URL url1 = new URL(url);
            httpURLConnection = (HttpURLConnection)url1.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            File apk = new File(savePath,apkname);
            fileOutputStream = new FileOutputStream(apk);
            inputStream = httpURLConnection.getInputStream();
            Log.v("输入流",inputStream+"");
            byte buffer[] = new byte[1024];
                while (!cancel) {
                    int read = inputStream.read(buffer);
                    int length = httpURLConnection.getContentLength();
                    Message message = new Message();
                    if (read <= 0) {
                        //下载完成
                        Log.v("通知下载完成", "蛤蛤");
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                        message.what = 0x129;
                        cancel = true;
                    }else{
                        progress += read;
                        Bundle data = new Bundle();
                        data.putInt("progress", (int) (((float) progress / length) * 100));
                        message.what = 0x128;
                        message.setData(data);
                        fileOutputStream.write(buffer, 0, read);
                    }
                    handler.sendMessage(message);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void Download(final String url, final Handler handler){
        Log.v("download收到，","over");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //while(!cancel){
                    getDownload(url,handler);
                //}

            }
        });
        thread.start();
    }
}
