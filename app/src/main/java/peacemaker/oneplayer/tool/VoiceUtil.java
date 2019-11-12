package peacemaker.oneplayer.tool;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telecom.Call;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.view.OneWaveFromView;

/**
 * Created by peace on 2018/5/10.
 */

public class VoiceUtil {
    private static EventManager wp;
    public static Boolean isRecognizaionEnabled = false;
    public static Boolean isRecognizaionStarted = false;
    private static Boolean isInited = false;
    private static OneRecorder oneRecorder;
    private static OneWaveFromView oneWaveFromView;
    private static String currentRecordingPath;
    private static String currentRecordingFileName;

    public static OneRecorder getOneRecorder() {
        return oneRecorder;
    }

    public static void setOneWaveFromView(OneWaveFromView oneWaveFromView) {
        LogTool.log("VoiceUtil", "oneWaveFromView",oneWaveFromView);
        VoiceUtil.oneWaveFromView = oneWaveFromView;
    }

    public static void init(){
        if (isInited){
            return;
        }
        LogTool.log("VoiceUtil", "伟大的骑士已整装");
//        oneRecorder = new OneRecorder();
//        oneRecorder.setOnSoundRecordListener(new OneRecorder.OnSoundRecordListener() {
//            @Override
//            public void onFrameRecorded(byte[] frameData) {
//                //LogTool.log("VoiceUtil", "oneWaveFromView","帧生成",oneWaveFromView);
//
//                //LogTool.log(this,frameData.length);
//            }
//
//            @Override
//            public void onSlowFramRecorded(byte[] frameData) {
//                if(oneWaveFromView!=null) {
//                    //LogTool.log("VoiceUtil", "oneWaveFromView","帧生成",oneWaveFromView);
//                    //采样32个点
//                    int scale = frameData.length/32;
//                    int sum = 0;
//                    for (int i=0;i<frameData.length;i+=scale){
//                        sum += Math.abs(frameData[i]);
//                    }
//                    oneWaveFromView.setAudioData(frameData,sum+"");
//                }
//            }
//
//            @Override
//            public void onWavFileGenerated() {
//                SocketUtil.sendFile(currentRecordingPath,currentRecordingFileName, new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(Message message) {
//                        if (message.what == SocketUtil.RESPONSE_SUCCESS) {
//                            //成功收到响应
//                        }else  if (message.what == SocketUtil.NETWORK_ERROR) {
//                            Toast.makeText(OneApplication.context,"网络错误",Toast.LENGTH_SHORT).show();
//                        }
//                        return false;
//                    }
//                }));
//            }
//        });
        wp = EventManagerFactory.create(OneApplication.context, "wp");
        EventListener wakeupListener = new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                LogTool.log(this, "初始化唤醒参数",String.format("event: name=%s, params=%s", name, params));
                //唤醒成功
                if(name.equals("wp.data")){
                    try {
                        JSONObject json = new JSONObject(params);
                        int errorCode = json.getInt("errorCode");
                        if(errorCode == 0){
                            //唤醒成功
                            LogTool.log(this, "黑暗骑士归来");
                        } else {
                            //唤醒失败
                            LogTool.log(this, "黑暗骑士凉了");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if("wp.exit".equals(name)){
                    //唤醒已停止
                    LogTool.log(this, "唤醒停止");
                }else if("wp.error".equals(name)){
                    //唤醒错误
                    LogTool.log(this, "唤醒错误");
                }
            }
        };
        wp.registerListener(wakeupListener);
        isInited = true;
    }

    public static void startRecognizing(){
        if(!isInited){
            init();
        }
        LogTool.log("OneApplication", "伟大的骑士请你醒来");
        String fileName = StringUtil.getCurrentTimeString();
        FileOperator.checkFile(fileName,StringUtil.record_path);
        currentRecordingPath = StringUtil.record_path+fileName+".wav";
        currentRecordingFileName = "/home/peace/voice/audios/"+fileName+".wav";
        oneRecorder.startRecording(StringUtil.record_path,fileName);
        isRecognizaionStarted = true;

    }

    public static void stopRecognizing(){
        LogTool.log("VoiceUtil", "伟大的骑士请你睡去");
        if(oneRecorder!=null) {
            oneRecorder.stopRecording();

        }
        isRecognizaionStarted = false;
    }

    public static void startRecognization(){
        if(!isInited){
            init();
        }
        Map<String, Object> params = new HashMap<>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin"); //唤醒词文件请去http://yuyin.baidu.com/wake下载
        params.put(com.baidu.speech.asr.SpeechConstant.APP_ID, "11223015");
        String json = new JSONObject(params).toString();
        LogTool.log("OneApplication", "伟大的骑士请你醒来",json);
        wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0);
        isRecognizaionEnabled = true;
    }

    public static void stopRecognization(){
        LogTool.log("OneApplication", "伟大的骑士请你睡去");
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0);
        isRecognizaionEnabled = false;
    }


}
