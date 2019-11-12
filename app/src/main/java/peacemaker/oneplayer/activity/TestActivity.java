package peacemaker.oneplayer.activity;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.databinding.OneLayoutBinding;
import peacemaker.oneplayer.databinding.TestActivityBinding;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.tool.JsonUtil;
import peacemaker.oneplayer.tool.LogTool;
import peacemaker.oneplayer.tool.OneStatusUtil;
import peacemaker.oneplayer.tool.OneWebChromeClient;
import peacemaker.oneplayer.tool.OneWebViewClient;
import peacemaker.oneplayer.tool.StringUtil;

import java.util.ArrayList;


/**
 * Created by ouyan on 2016/9/26.
 */

public class TestActivity extends AppCompatActivity {
    private OneApplication oneApplication;
//    @BindView(R.id.webview)
//    public WebView webView;
//    @BindView(R.id.search_button)
//    public ImageButton searchButton;
//    @BindView(R.id.search_bar)
//    public EditText searchBar;
    private TestActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(TestActivity.this, R.layout.test_activity);

        oneApplication = (OneApplication) getApplication();
        //oneApplication.setOneConfig(DatabaseOperator.loadConfig(this));
        binding.setOneConfig(oneApplication.getOneConfig());
        OneStatusUtil.setStatusColor(this, oneApplication.getThemeColor());
        //toolbar.setNavigationIcon(DrawableTool.getBackArrowDrawable(this));
        LogTool.log(this,"sdk版本"+Build.VERSION.SDK_INT);
        if(Build.VERSION.SDK_INT >= 23){
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            LogTool.log(this,"权限码"+permission);
            LogTool.log(this,"获取码"+PackageManager.PERMISSION_GRANTED);
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},666);
                return;
            }
        }
        WebSettings webSettings = binding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        binding.webview.setWebViewClient(new OneWebViewClient());
        binding.webview.setWebChromeClient(new OneWebChromeClient());
        binding.webview.loadUrl(StringUtil.getUrl_background());
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(message.what==StringUtil.successCode){
                    Bundle bundle = message.getData();
                    String result = bundle.getString(StringUtil.result);
                    ArrayList<Music> musics = JsonUtil.getSearchResult(result);
                    LogTool.log(this,"搜索结果数目"+musics.size());
                }
                return false;
            }
        });
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = binding.searchBar.getText().toString();
                JsonUtil.sendContent(
                        StringUtil.getString(TestActivity.this,R.string.searchJson,word),
                        StringUtil.getUrl_background(),
                        TestActivity.this,
                        handler
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==666){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//允许
                // Permission Granted
                LogTool.log(this,"成功获取权限");
                Toast.makeText(this,"成功获取权限！",Toast.LENGTH_SHORT).show();
            }else{
                LogTool.log(this,"不给老子权限");
                OneApplication.destroy();
            }

        }
    }
}
