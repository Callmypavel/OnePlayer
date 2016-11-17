package com.example.peacemaker.oneplayer;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.provider.MediaStore;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by ouyan on 2016/10/20.
 */

public class MusicDecoder {
    private String url;
    private static MediaExtractor mediaExtractor;
    private static MediaFormat mediaFormat;
    private MediaCodec mediaCodec;
    private byte[] chunk;
    public MusicDecoder(String url){
        this.url = url;
        //this.chunk = decode();
    }

    public byte[] getChunk() {
        return chunk;
    }

    public static int getSampleRate(){
        if(mediaFormat==null){
            LogTool.log("MusicDecoder","getSampleRate()mediaFormat为空");
            return 1;
        }
        return mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
    }
    public static int getFormat(){
        return AudioFormat.ENCODING_PCM_16BIT;
    }
    public static void asynchronousDecode(MediaCodec.Callback callback,String url) throws IOException{
        if(Build.VERSION.SDK_INT>=21){
            mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(url);
            mediaFormat = mediaExtractor.getTrackFormat(0);
            MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
            MediaCodec mediaCodec = MediaCodec.createByCodecName(mediaCodecList.findDecoderForFormat(mediaFormat));
            mediaCodec.setCallback(callback);
            mediaCodec.start();
            LogTool.log("MusicDecoder","decode()解码开始");

        }
    }
//    public void synchronousDecode(String url){
//        if(Build.VERSION.SDK_INT>=21) {
//            MediaExtractor mediaExtractor = new MediaExtractor();
//            mediaExtractor.setDataSource(url);
//            mediaFormat = mediaExtractor.getTrackFormat(0);
//            MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
//            MediaCodec codec = MediaCodec.createByCodecName(mediaCodecList.findDecoderForFormat(mediaFormat));
//            codec.configure(mediaFormat, null,null,0);
//            MediaFormat outputFormat = codec.getOutputFormat(); // option B
//            codec.start();
//            for (; ; ) {
//                int inputBufferId = codec.dequeueInputBuffer(1000);
//                if (inputBufferId >= 0) {
//                    ByteBuffer inputBuffer = codec.getInputBuffer(…);
//                    // fill inputBuffer with valid data
//                    …
//                    codec.queueInputBuffer(inputBufferId,0,sampleSize,presentationTimeUs,sawInputEOS?MediaCodec.BUFFER_FLAG_END_OF_STREAM:0);
//                }
//                int outputBufferId = codec.dequeueOutputBuffer(…);
//                if (outputBufferId >= 0) {
//                    ByteBuffer outputBuffer = codec.getOutputBuffer(outputBufferId);
//                    MediaFormat bufferFormat = codec.getOutputFormat(outputBufferId); // option A
//                    // bufferFormat is identical to outputFormat
//                    // outputBuffer is ready to be processed or rendered.
//                    …
//                    codec.releaseOutputBuffer(outputBufferId, …);
//                } else if (outputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
//                    // Subsequent data will conform to new format.
//                    // Can ignore if using getOutputFormat(outputBufferId)
//                    outputFormat = codec.getOutputFormat(); // option B
//                }
//            }
//            codec.stop();
//            codec.release();
//        }
//    }
//    short[] getSamplesForChannel(MediaCodec codec, int bufferId, int channelIx) {
//        ByteBuffer outputBuffer = codec.getOutputBuffer(bufferId);
//        MediaFormat format = codec.getOutputFormat(bufferId);
//        ShortBuffer samples = outputBuffer.order(ByteOrder.nativeOrder()).asShortBuffer();
//        int numChannels = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
//        if (channelIx < 0 || channelIx >= numChannels) {
//            return null;
//        }
//        short[] res = new short[samples.remaining() / numChannels];
//        for (int i = 0; i < res.length; ++i) {
//            res[i] = samples.get(i * numChannels + channelIx);
//        }
//        return res;
//    }

    public byte[] decode1(){
        mediaExtractor = new MediaExtractor();
        try {
            mediaExtractor.setDataSource(url);
            //获取媒体格式
            mediaFormat = mediaExtractor.getTrackFormat(0);
            //获取mime
            String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
            mediaCodec = MediaCodec.createDecoderByType(mime);
            //配置codec
            mediaCodec.configure(mediaFormat,null,null,0);
            mediaCodec.start();
            mediaExtractor.selectTrack(0);
            //在1秒内完成输入队列解析
            int inputBufferIndex = mediaCodec.dequeueInputBuffer(1000);
            if(inputBufferIndex>=0){
                ByteBuffer buffer;
                if(Build.VERSION.SDK_INT>=21){
                    buffer = mediaCodec.getInputBuffer(inputBufferIndex);
                }else {
                    ByteBuffer[] buffers = mediaCodec.getInputBuffers();
                    buffer = buffers[inputBufferIndex];
                }
                //读取samplesize
                int sampleSize = mediaExtractor.readSampleData(buffer,0);
                long presentationTimeUs = 0;
                boolean sawInputEOS = false;
                if(sampleSize<0){
                    sampleSize=0;
                    sawInputEOS = true;
                }else {
                    presentationTimeUs = mediaExtractor.getSampleTime();
                }
                mediaCodec.queueInputBuffer(inputBufferIndex,0,sampleSize,presentationTimeUs,sawInputEOS?MediaCodec.BUFFER_FLAG_END_OF_STREAM:0);
                if(!sawInputEOS){
                    mediaExtractor.advance();
                }
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufIndex = mediaCodec.dequeueOutputBuffer(bufferInfo,1000);
            if(outputBufIndex>0){
                ByteBuffer buffer;
                if(Build.VERSION.SDK_INT>=21){
                    buffer = mediaCodec.getOutputBuffer(outputBufIndex);
                }else {
                    ByteBuffer[] buffers = mediaCodec.getOutputBuffers();
                    buffer = buffers[outputBufIndex];
                }
                byte[] chunk = new byte[bufferInfo.size];
                //马上读一次
                buffer.get(chunk);
                buffer.clear();
                if(chunk.length>0){
                    return chunk;
                }
                mediaCodec.release();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
