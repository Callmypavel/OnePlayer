package peacemaker.oneplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import peacemaker.oneplayer.databinding.SearchActivityBinding;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.tool.OneMusicloader;
import peacemaker.oneplayer.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ouyan on 2016/6/30.
 */

public class SearchActivity extends Activity {
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private Handler searchHandler;
//    @BindView(R.id.search_info_text)
//    public TextView searchInfoText;
//    @BindView(R.id.search_stop_button)
//    public Button searchStopButton;
//    @BindView(R.id.search_path_text)
//    public TextView searchPathText;
//    @BindView(R.id.search_exit_button)
//    public TextView exitButton;
    private OneMusicloader oneMusicloader;
    private SearchActivityBinding binding;
    private boolean isStopped = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT > 19) {
//            setTheme(R.style.Oneplayer);
//        } else {
//            setTheme(R.style.One);
//        }
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.search_activity);
        initialize();
    }
    private void initialize(){
        searchHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==0x125){
                    if(!isStopped) {
                        String name = msg.getData().getString("name");
                        binding.searchPathText.setText(name);
                    }
                }else if(msg.what==0x126){
                    Music music = msg.getData().getParcelable("music");
                    if(music!=null) {
                        musicArrayList.add(music);
                    }
                }else if(msg.what==0x127){
                    binding.searchInfoText.setText("扫描完成");
                    binding.searchStopButton.setText("返回");
                }
                return false;
            }
        });
        binding.searchStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.searchStopButton.getText().toString();
                if(text.equals("开始扫描")){
                    isStopped=false;
                    oneMusicloader = new OneMusicloader(searchHandler);
                    oneMusicloader.getdeepLoad(new File("/mnt/sdcard"));
                    binding.searchInfoText.setText("扫描中...");
                    binding.searchStopButton.setText("停止扫描");
                }else if(text.equals("返回")){
                    onBackPressed();
                }else if(text.equals("停止扫描")){
                    isStopped = true;
                    oneMusicloader.stopLoading();
                    binding.searchInfoText.setText("扫描中止");
                    binding.searchPathText.setText("");
                    binding.searchStopButton.setText("开始扫描");
                    musicArrayList.clear();
                }
            }
        });
        binding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStopped = true;
                if(oneMusicloader!=null) {
                    oneMusicloader.stopLoading();
                }
                musicArrayList.clear();
                SearchActivity.this.finish();
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
            bundle.putString("mode","searchCompled");
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.setClass(SearchActivity.this,MainActivity.class);
            startActivity(intent);
            SearchActivity.this.finish();
        }
    }
}
