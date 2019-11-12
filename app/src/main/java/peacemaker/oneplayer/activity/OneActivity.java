package peacemaker.oneplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.entity.MusicState;
import peacemaker.oneplayer.listener.OnOneSeekBarListener;
import peacemaker.oneplayer.listener.OnReachMaxListener;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.tool.LogTool;
import peacemaker.oneplayer.view.OneLayout;
import peacemaker.oneplayer.view.OneSeekBar;
import peacemaker.oneplayer.view.OneWaveFromView;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.tool.OneStatusUtil;


/**
 * Created by ouyan on 2016/7/7.
 */

public abstract class OneActivity extends AppCompatActivity implements View.OnClickListener{
    protected OneApplication oneApplication;
    Handler handler;
//    protected boolean isPlayView = false;
    public OneSeekBar oneSeekBar;
    public OneLayout mainContentBar;
    public OneWaveFromView oneWaveFromView;
    public AppBarLayout appBarLayout;
    public CoordinatorLayout coordinatorLayout;
    private static int REQUEST_PERMISSION_CODE = 123;
    private MusicState musicState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 19) {
            setTheme(R.style.Oneplayer);
            Log.v("OneSingerDetailActivity", "我选择OnePlayer主题");
        } else {
            setTheme(R.style.One);
            Log.v("OneSingerDetailActivity", "我选择One主题");
        }
        oneApplication = (OneApplication) getApplication();
        this.musicState = oneApplication.musicState;
        OneStatusUtil.setStatusColor(this,oneApplication.getThemeColor());
    }
//    public void pause(){
//        oneSeekBar.setButtonBitmap(false);
//    }
//    public void start(){
//        oneSeekBar.setButt onBitmap(true);
//    }

    protected void initialize() {
        oneSeekBar = (OneSeekBar) getWindow().getDecorView().findViewById(R.id.one_seekbar);
        mainContentBar = (OneLayout) getWindow().getDecorView().findViewById(R.id.one_layout);
        oneWaveFromView = (OneWaveFromView) getWindow().getDecorView().findViewById(R.id.play_view_onewaveform);
        appBarLayout = (AppBarLayout) getWindow().getDecorView().findViewById(R.id.id_appbarlayout);
        LogTool.log(this,"父类Activity初始化View",oneSeekBar,mainContentBar,oneWaveFromView);
        oneSeekBar.setOnOneSeekBarListener(new OnOneSeekBarListener() {
            @Override
            public void onSeekBarUpdated(float progress) {
                Log.v("OneActivity","onSeekBarUpdated()progress:"+progress);
                if (!oneApplication.isNew()) {
                    oneApplication.seekTo(progress);
                } else {
                   oneApplication.unSeekable();
                }
            }
            @Override
            public void onButtonClick() {
                oneApplication.play();
                //oneSeekBar.setButtonBitmap(false);
            }
        });
        mainContentBar.setBottom_controll_bar((RelativeLayout) mainContentBar.getChildAt(0));
        mainContentBar.setOnReachMaxListener(new OnReachMaxListener() {
            @Override
            public void onReachMax() {
                disableListview();

                OneStatusUtil.setStatusColor(OneActivity.this,Color.argb(0,0,0,0));
            }

            @Override
            public void onReachMin() {
                musicState.setIsPlayView(false);
            }
        });
        initPermission();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(musicState.getIsPlayView()) {
            OneStatusUtil.setStatusColor(OneActivity.this, Color.argb(0,0,0,0));
        }else {
            OneStatusUtil.setStatusColor(OneActivity.this, oneApplication.getThemeColor());
        }
        oneApplication.musicState.setIsPlayView(oneApplication.musicState.getIsPlayView());
    }

    protected void disableListview(){
        Log.v("OneActivity","关闭");
    }
    protected void enableListview(){
        Log.v("OneActivity","开启");
    }
//    protected void banClick(){
//        oneSeekBar.setClickable(false);
//        nextButton.setClickable(false);
//        previousButton.setClickable(false);
//        playModeButton.setClickable(false);
//    }

    public void setWaveData(byte[] data){
        oneWaveFromView.setData(data);

    }

//    public void selectMusic(Music music, int position) {
//        oneApplication.selectMusic(music,position);
//    }
//    public void updateMusicInfo(MusicState musicState, Bitmap bitmap){
//        if(oneWaveFromView!=null) {
//            oneWaveFromView.setPaintColor(musicState.getMusicColor());
//        }
//        if(oneSeekBar!=null) {
//            if (musicState.getIsWhite()) {
//                Log.v("OneActivity","updateMusicInfo()绘制白色");
//                oneSeekBar.setColorInt(Color.WHITE);
//            } else {
//                Log.v("OneActivity","updateMusicInfo()绘制黑色");
//                oneSeekBar.setColorInt(Color.BLACK);
//            }
//            oneSeekBar.setButtonBitmap(true);
//        }
//        if(bottomAblumImageView!=null) {
//            bottomAblumImageView.setImageBitmap(bitmap);
//            playViewAblumImageView.setImageBitmap(bitmap);
//        }
//        Log.v("OneActivity","updateMusicInfo()检查imageView"+bottomAblumImageView+bitmap);
//    }
//    public void updateSeekbar() {
//        try {
//            totalTime = Integer.parseInt(oneApplication.currentMusic.getDuration()) / 1000;
//        }catch (Exception e){
//            Log.v("MainActivity","获取时间出错");
//            e.printStackTrace();
//            totalTime = 0;
//        }
//        oneSeekBar.setDegree(0);
//    }

    public void queue() {
        quitPlayView();

    }
    public void toSoundEffect(View v){
        Intent intent = new Intent();
        intent.setClass(this,SoundEffectActivity.class);
        startActivity(intent);
    }
    public void toPlayView(){
        Log.v("OneActivity","toPlayView()");
        mainContentBar.toMaxHeight();
        musicState.setIsPlayView(true);
    }

    public void quitPlayView(){
        Log.v("OneActivity","quitPlayView()");
        mainContentBar.toMinHeight();
        musicState.setIsPlayView(false);
        enableListview();
        OneStatusUtil.setStatusColor(this,oneApplication.getThemeColor());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_item:
                //点击歌曲列表中的item
                OneApplication.selectMusic((Music)view.findViewById(R.id.MusicName).getTag(),(int)view.getTag(),null);
                break;
        }
    }

    protected void initPermission() {
        if(Build.VERSION.SDK_INT>=23) {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo;
            try {
                // 参数2必须是PackageManager.GET_PERMISSIONS
                packageInfo = pm.getPackageInfo("peacemaker.oneplayer", PackageManager.GET_PERMISSIONS);
                String[] permissions = packageInfo.requestedPermissions;
                if (permissions != null) {
                    ActivityCompat.requestPermissions(this, permissions, 667);
                }
            }catch(PackageManager.NameNotFoundException exception){
                exception.printStackTrace();
            }
        }
    }

    public abstract void onPermissionGranted(Boolean isPermissionUpdated);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION_CODE){
            if (grantResults.length>0) {
                ArrayList<String> permittedPermissions = new ArrayList<>();
                for (int i=0;i<permissions.length;i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        permittedPermissions.add(permissions[i]);
                    }
                }
                PackageManager pm = getPackageManager();
                PackageInfo packageInfo;
                try {
                    packageInfo = pm.getPackageInfo("peacemaker.oneplayer", PackageManager.GET_PERMISSIONS);
                    String requiredPermissions[] = packageInfo.requestedPermissions;
                    OneApplication.isPermissionGranted = true;
                    for (String requiredPermission : requiredPermissions) {
                        if (!permittedPermissions.contains(requiredPermission)) {
                            OneApplication.isPermissionGranted = false;
                        }
                    }
                    onPermissionGranted(OneApplication.isPermissionGranted);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}
