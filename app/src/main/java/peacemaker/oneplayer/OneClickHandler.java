package peacemaker.oneplayer;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.OneActivity;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.entity.IndexedMusic;
import peacemaker.oneplayer.tool.ColorUtil;
import peacemaker.oneplayer.tool.OneBitmapUtil;
import peacemaker.oneplayer.tool.SocketUtil;
import peacemaker.oneplayer.tool.StringUtil;
import peacemaker.oneplayer.tool.VoiceUtil;
import peacemaker.oneplayer.view.IndexView;
import peacemaker.oneplayer.view.OneGameView;
import peacemaker.oneplayer.view.OneSeekBar;
import peacemaker.oneplayer.view.OneWaveFromView;

import java.util.ArrayList;

/**
 * Created by ouyan on 2016/7/7.
 */

public class OneClickHandler {
    @BindingAdapter("android:bitmapSrc")
    public static void setBitmapSrc(ImageView imageView, Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
    @BindingAdapter("android:isWhite")
    public static void setIsWhite(OneSeekBar oneSeekBar, boolean isWhite){
            if (isWhite) {
                Log.v("OneActivity","updateMusicInfo()绘制白色");
                oneSeekBar.setColorInt(Color.WHITE);
            } else {
                Log.v("OneActivity","updateMusicInfo()绘制黑色");
                oneSeekBar.setColorInt(Color.BLACK);
            }
            oneSeekBar.setButtonBitmap(true);
    }
    @BindingAdapter("android:seekBarColor")
    public static void setSeekBarColor(OneSeekBar oneSeekBar, int color){
        oneSeekBar.setColorInt(color);
        oneSeekBar.setButtonBitmap(true);
    }
    @BindingAdapter("android:isPlaying")
    public static void setIsPlaying(OneSeekBar oneSeekBar, boolean isPlaying){
        oneSeekBar.setButtonBitmap(isPlaying);
    }
    @BindingAdapter("android:paintColor")
    public static void setPaintColor(OneWaveFromView oneWaveFromView, int color){
        oneWaveFromView.setPaintColor(color);
    }
    @BindingAdapter("android:percentage")
    public static void setPercentage(OneSeekBar oneSeekBar, float percentage){
        oneSeekBar.setProgress(percentage);
    }
    @BindingAdapter("android:waveformdata")
    public static void setWaveformdata(OneWaveFromView oneWaveFromView, byte[] data){
        oneWaveFromView.setData(data);
    }
    @BindingAdapter("android:waveformdata")
    public static void setWaveformdata(OneGameView oneGameView, byte[] data){
        oneGameView.setData(data);
    }
    @BindingAdapter("android:bitmapImage")
    public static void setImageResource(ImageView imageView,Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
    @BindingAdapter("android:index")
    public static void setIndex(IndexView indexView, ArrayList<IndexedMusic> indexedMusics){
        indexView.setIndexedMusics(indexedMusics);
    }
    @BindingAdapter("android:displayValue")
    public static void setValue(TextView textView, int value){
        Context context = textView.getContext();
        String text = "";
        switch (textView.getTag().toString()){
            case "redTag":
                text = StringUtil.getString(context,R.string.color_red,value);
                break;
            case "greenTag":
                text = StringUtil.getString(context,R.string.color_green,value);
                break;
            case "blueTag":
                text = StringUtil.getString(context,R.string.color_blue,value);
                break;
            case "alphaTag":
                text = StringUtil.getString(context,R.string.alpha,value);
                break;
            case "radiusTag":
                text = StringUtil.getString(context,R.string.radius,value);
                break;
        }
        textView.setText(text);
    }
    @BindingAdapter("colorFilter")
    public static void setHintColor(ImageButton imageButton, int hintColor){
        imageButton.setColorFilter(hintColor);
    }
    @BindingAdapter("android:colorFilter")
    public static void setViewHintColor(ImageView imageView, int color){
        imageView.setColorFilter( color);
    }
    @BindingAdapter("android:highlightColor")
    public static void setHighlightColor(IndexView indexView, int highlightColor){
        indexView.setHighlightColor(highlightColor);
    }
    @BindingAdapter({"android:blurBitmap","android:blurRadius"})
    public static void setBlusRadius(View view, Bitmap bitmap,int radius){
        //LogTool.log("OneClickHandler","查看高斯半径和bitmap是否为空"+bitmap+","+radius);
        if(bitmap==null||radius==0){
            return;
        }
        Bitmap bitmap1 = OneBitmapUtil.getBluredBitmap(bitmap,radius,view.getContext());
        view.setBackground(new BitmapDrawable(view.getContext().getResources(),bitmap1));

    }

//    @BindingAdapter("android:progress")
//    public static void setProgress(SeekBar seekBar, int progress){
//        seekBar.setProgress(progress);
//    }


    public void onButtonClick(View view){
        OneApplication oneApplication = (OneApplication)(view.getContext().getApplicationContext());
        switch (view.getId()){
            case R.id.play_view_previous:
                oneApplication.previous();
                break;
            case R.id.play_view_next:
                Log.v("OneClickHandler","下一首");
                oneApplication.next();
                break;
            case R.id.play_view_queue:
                oneApplication.queue();
                break;
            case R.id.play_view_playmode:
                oneApplication.changePlayMode();
                break;
            case R.id.bottom_play_button:
                Log.v("OneClickHandler","播放按键");
                oneApplication.play();
                break;
            case R.id.bottom_controll_bar:
                Log.v("OneClickHandler","查看上下文"+view.getContext());
                ((OneActivity)view.getContext()).toPlayView();
                break;
            case R.id.down_button:
                ((OneActivity)view.getContext()).quitPlayView();
                break;
            case R.id.recognize_button:
                if (!VoiceUtil.isRecognizaionStarted) {
                    VoiceUtil.startRecognization();
                    ((Button)view).setText("停止");
                }else {
                    VoiceUtil.stopRecognization();
                    ((Button)view).setText("开始");
                }
                break;
            case R.id.reconnect_button:
                SocketUtil.reConnect();
                //VoiceUtil.getOneRecorder().stopRecording();
                break;
        }
    }
}

