package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/30.
 */

public class SearchActivity extends Activity {
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private Handler searchHandler;
    @BindView(R.id.search_info_text)
    public TextView searchInfoText;
    @BindView(R.id.search_stop_button)
    public Button searchStopButton;
    @BindView(R.id.search_path_text)
    public TextView searchPathText;
    private OneMusicloader oneMusicloader;
    private boolean isStopped = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT > 19) {
//            setTheme(R.style.Oneplayer);
//        } else {
//            setTheme(R.style.One);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        initialize();
    }
    private void initialize(){
        searchHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==0x125){
                    if(!isStopped) {
                        String name = msg.getData().getString("name");
                        searchPathText.setText(name);
                    }
                }else if(msg.what==0x126){
                    Music music = msg.getData().getParcelable("music");
                    if(music!=null) {
                        musicArrayList.add(music);
                    }
                }else if(msg.what==0x127){
                    searchInfoText.setText("扫描完成");
                    searchStopButton.setText("返回");
                }
                return false;
            }
        });
        searchStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchStopButton.getText().toString();
                if(text.equals("开始扫描")){
                    isStopped=false;
                    oneMusicloader = new OneMusicloader(searchHandler);
                    oneMusicloader.getdeepLoad(new File("/mnt/sdcard"));
                    searchInfoText.setText("扫描中...");
                    searchStopButton.setText("停止扫描");
                }else if(text.equals("返回")){
                    onBackPressed();
                }else if(text.equals("停止扫描")){
                    isStopped = true;
                    oneMusicloader.stopLoading();
                    musicArrayList.clear();
                    searchInfoText.setText("扫描中止");
                    searchPathText.setText("");
                    searchStopButton.setText("开始扫描");
                    musicArrayList.clear();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(musicArrayList.size()==0) {
            super.onBackPressed();
        }else {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("musicArrayList",musicArrayList);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(SearchActivity.this,MainActivity.class);
            startActivity(intent);
            SearchActivity.this.finish();
        }
    }
}
