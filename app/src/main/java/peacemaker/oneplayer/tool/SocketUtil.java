package peacemaker.oneplayer.tool;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.listener.SocketThread;
import peacemaker.oneplayer.listener.SocketThreadListener;

/**
 * Created by ouyan on 2016/11/27.
 */

public class SocketUtil {
    private static String url = UrlUtil.getUrl();
    private static int port = UrlUtil.getPort();
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static Timer timer;
    private static Socket socket;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static boolean isReciving = true;
    private static boolean isKillThred = false;
    public static final int RESPONSE_SUCCESS = 1;
    public static final int NETWORK_ERROR = 2;
    private static Boolean isInited = false;
    private static ArrayList<SocketThread> socketThreads = new ArrayList<>();



    public static void sendString(final String content,final Handler handler) {
        if(socket!=null&&socket.isConnected()){
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                startServerReplyListener(content, handler);
            }
        }).start();

//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                startServerReplyListener(content, handler);
//            }
//        });
    }

    public static void sendSimpleString (final String content,Boolean isEnd) throws Exception{
        initSocket();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        if(!content.equals("")){
            LogTool.log("SocketUtil","sendSimpleString()","发送简单文本:"+content);
            writer.write(content);
            writer.flush();

        }


    }

    private static void shutdownSocket(){
        if(socket!=null&&socket.isConnected()){
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private static void initSocket() throws Exception{
        if(isInited){
            return;
        }
        socket = new Socket(url, port);
        socket.setKeepAlive(true);
        isInited = true;

    }

    public static void reConnect(){
        if(isInited) {
            for(SocketThread socketThread : socketThreads){
                socketThread.stopThread();
            }
            socketThreads.clear();
            socket = null;
            isInited = false;
        }
    }

    public static void sendFile(final String path, final String fileName, final Handler handler){
        LogTool.log("SocketUtil","发送文件"+path);
        final SocketThread socketThread = new SocketThread();
        final SocketThreadListener socketThreadListener = new SocketThreadListener() {
                @Override
                public void onInit() {
                    try {
                        initSocket();
                        //sendSimpleString("receive_voice"+fileName,false);
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        byte[] array = new byte[4096];
                        if(!path.equals("")) {
                            File file = new File(path);
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
                            bufferedOutputStream.write(("receive_voice"+fileName).getBytes());
                            bufferedOutputStream.flush();
                            int i=0;
                            while (bufferedInputStream.read(array) != -1) {
                                //LogTool.log("SocketUtil写出",array);
                                i+=1;
                                bufferedOutputStream.write(array);
                                //Thread.sleep(2000);
                            }
                            LogTool.log("SocketUtil","写出"+i+"次");
                            //bufferedOutputStream.flush();
                            LogTool.log("SocketUtil","写出结束标志");
                            //sendSimpleString("receive_end", false);
                            Thread.sleep(500);
                            sendSimpleString("receive_end", false);
                        }
                        socketThread.isInitSuccess = true;
                    }catch (Exception e){
                        Message message = new Message();
                        message.what = NETWORK_ERROR;
                        handler.sendMessage(message);

                        socketThread.isInitSuccess = false;
                        reConnect();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCycling() {
                    try {
                        String line;
                        if(reader!=null) {
                            if ((line = reader.readLine()) != null) {
                                Log.v("SocketUtil", "查看响应" + line);
                                if (!line.equals("")) {
//                                    if (line.equals("CheckIsOnline")) {
//                                        writer.write("Online\n");
//                                        writer.flush();
//                                    } else {
//                                        if (line.equals("successful")) {
//                                            isReciving = false;
//                                        }
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("response", line);
                                        message.setData(bundle);
                                        message.what = RESPONSE_SUCCESS;
                                        handler.sendMessage(message);
//                                        writer.write("Receive\n");
//                                        writer.flush();
//                                    }

                                }

                            }
                        }else {
                            LogTool.log("SocketUtil","reader都是空的，你读你马呢");
                        }
                    }catch (Exception exception){
                        socketThread.stopThread();
                        LogTool.log("SocketUtil","你妈 出问题了"+exception.getMessage()+exception.getCause());
                        //exception.printStackTrace();
                        try {
                            reader.close();
                            writer.close();
                        }catch (Exception e){
                            LogTool.log("SocketUtil","你妈 老子关都关出问题");
                        }
                    }
                }

        };
        socketThread.setSocketThreadListener(socketThreadListener);
        socketThreads.add(socketThread);
        socketThread.start();
    }

    public static void startServerReplyListener(final String content, final Handler handler) {
        socket = null;
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(url, port);
                    socket.setKeepAlive(true);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    if(!content.equals("")){
                        writer.write(content+"\n\r");
                        writer.flush();
                    }
                    Log.v("SocketUtil","查看是否开始进行");
                    String line;
                    while (isReciving) {
                        //Log.v("SocketUtil","查看是否阻塞");
                        try {
                            while ((line = reader.readLine()) != null) {
                                Log.v("SocketUtil", "查看响应" + line);
                                if (!line.equals("")) {
                                    if (line.equals("CheckIsOnline")) {
                                        writer.write("Online\n");
                                        writer.flush();
                                    } else {
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("response", line);
                                        message.setData(bundle);
                                        message.what = 1;
                                        handler.sendMessage(message);
                                        writer.write("Receive\n");
                                        writer.flush();
                                    }

                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    interface ResponseArrivedListener {
        void ResponseArrived(String response);

        void ErrorAppears(String message);
    }


}
