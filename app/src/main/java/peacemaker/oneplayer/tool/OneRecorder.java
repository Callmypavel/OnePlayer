package peacemaker.oneplayer.tool;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Looper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by peace on 2018/5/12.
 */

public class OneRecorder {
    private AudioRecord audioRecord ;
    private int recordBufSize = 0;
    //采样率
    private int sampleFrequency = 16000;
    //通道配置
    private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    //编码配置
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private Boolean isRecording = false;
    private Boolean isInited = false;
    private Boolean isWriteToFile = true;
    private OnSoundRecordListener onSoundRecordListener;
    private int slowRatio = 5;
    public interface OnSoundRecordListener{
        void onFrameRecorded(byte[] frameData);
        void onSlowFramRecorded(byte[] frameData);
        void onWavFileGenerated();
    }

    public void setOnSoundRecordListener(OnSoundRecordListener onSoundRecordListener) {
        this.onSoundRecordListener = onSoundRecordListener;
    }

    public void init() {
        recordBufSize = AudioRecord.getMinBufferSize(sampleFrequency, channelConfiguration, audioEncoding);  //audioRecord能接受的最小的buffer大小
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleFrequency, channelConfiguration, audioEncoding, recordBufSize);
    }

    public void startRecording(final String pathName,final String fileName){
        if(!isInited){
            init();
        }

        final byte data[] = new byte[recordBufSize];
        try {
            final FileOutputStream os = new FileOutputStream(pathName+fileName);
            audioRecord.startRecording();
            isRecording = true;

            if (null != os) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int acculation = 0;
                            while (isRecording) {
                                int read = audioRecord.read(data, 0, recordBufSize);
                                // 如果读取音频数据没有出现错误，就将数据写入到文件
                                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                                    try {
                                        if(isWriteToFile) {
                                            os.write(data);
                                        }
                                        if(onSoundRecordListener!=null){
                                            onSoundRecordListener.onFrameRecorded(data);
                                        }
                                        if(onSoundRecordListener!=null){
                                            acculation+=1;
                                            if (acculation>slowRatio){
                                                onSoundRecordListener.onSlowFramRecorded(data);
                                                acculation = 0;
                                            }
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if(isWriteToFile) {
                                os.flush();
                            }
                            os.close();
                            if(isWriteToFile) {
                                convertWaveFile(pathName,fileName);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void convertWaveFile(String pathName,String fileName) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen;
        byte[] data = new byte[recordBufSize];
        try {
            in = new FileInputStream(pathName+fileName);
            fileName+=".wav";
            FileOperator.checkFile(fileName,pathName);
            out = new FileOutputStream(pathName+fileName);
            totalAudioLen = in.getChannel().size();
            //由于不包括RIFF和WAV
            out.write(getWavHeader(totalAudioLen));
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
            Looper.prepare();

            onSoundRecordListener.onWavFileGenerated();

            Looper.loop();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getWavHeader(long totalAudioLen){
        int mChannels = 1;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = sampleFrequency;
        long byteRate = sampleFrequency * 2 * mChannels;

        byte[] header = new byte[44];
        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) mChannels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * mChannels);  // block align
        header[33] = 0;
        header[34] = 16;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        return header;
    }

    public void stopRecording(){
        if (null != audioRecord) {
            audioRecord.stop();
        }
        isRecording = false;
    }

    public void terminateRecording(){
        audioRecord.release();
        audioRecord = null;
    }
}
