package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.graphics.Palette;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
//import android.support.v4.widget.DrawerLayout;

/**
 * Created by ouyan_000 on 2015/8/14.
 */
public class MainActivity extends OneActivity{
    int transferNum = 0;
    private int currentColor = Color.WHITE;
    private int cursorWidth;
    private int time;
    private int totalTime;
    Boolean isNull = false;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isLogging = false;
    private OneApplication oneApplication;
    Handler handler;
    Boolean isAlbuming = false;
    Boolean isNotiClikable = true;
    Boolean isNet = false;
    Boolean isFirstTime = true;
    int netmusicNumber = 1;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.id_viewpager)
    public ViewPager viewPager;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    public ProgressBar progressBar;
    private OnePlayListFragment onePlayListFragment;
    private OneAblumListFragment oneAblumListFragment;
    private OneSingerListFragment oneSingerlistFragment;
    private ArrayList<Fragment> oneFragments;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    public Boolean isPlayView = false;
    private int currentFragmentPosition = singer;
    private OneLogger oneLogger;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > 19) {
            setTheme(R.style.Oneplayer);
            Log.v("MainActivity", "我选择OnePlayer主题");
        } else {
            setTheme(R.style.One);
            Log.v("MainActivity", "我选择One主题");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        oneApplication = (OneApplication) getApplication();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==0x111){
                    setContentView(R.layout.main_activity);
                    initialize();
                    checkUpdate();
                }
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OneMusicloader oneMusicloader = new OneMusicloader(getContentResolver());
                ArrayList<Music> tempmusicArrayList = oneMusicloader.loadLocalMusic();
                MusicProvider musicProvider = new MusicProvider(tempmusicArrayList);
                oneApplication.setOneActivity(musicProvider,MainActivity.this);
                Message message = new Message();
                message.what = 0x111;
                handler.sendMessage(message);

            }
        });
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //initNotification(0,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("摧毁", "activity");
        if(playReceiver!=null) {
            unregisterReceiver(playReceiver);
            unregisterReceiver(nextReceiver);
            unregisterReceiver(previousReceiver);
        }
        unbindService(serviceConnection);
    }
    public ArrayList<Music> getSingerArraylist(){
        return singerArraylist;
    }
    public ArrayList<Music> getAlbumArraylist(){
        return albumArraylist;
    }
    public ArrayList<Music> getSongArraylist(){
        return songArraylist;
    }

    private void initialize() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int themeColor = typedValue.data;
        ButterKnife.bind(this);
        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_menu_white_48dp,12);
        Drawable drawable = new Drawable() {
            Paint paint = new Paint();
            @Override
            public void draw(Canvas canvas) {

                canvas.drawBitmap(bitmap,canvas.getWidth()/2-bitmap.getWidth()/2,canvas.getHeight()/2-bitmap.getHeight()/2,paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                String title = menuItem.getTitle().toString();
                if (title.equals("深度扫描")){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,SearchActivity.class);
                    startActivity(intent);
                }else if (title.equals("检查更新")){
                    checkUpdate();
                }else if (title.equals("打印日志")){
                    menuItem.setChecked(true);
                    if(oneLogger==null) {
                        oneLogger = new OneLogger();
                    }
                    oneLogger.getLog();
                    menuItem.setTitle("正在打印");
                }else if (title.equals("版本更新")){
                    checkUpdate();
                }else if (title.equals("退出应用")){
                    oneApplication.closeNotification();
                    finish();
                }else if (title.equals("正在打印")){
                    menuItem.setChecked(false);
                    if(oneLogger!=null){
                        oneLogger.stopLogging();
                    }
                    menuItem.setTitle("打印日志");
                }else if (title.equals("版本信息")){
                    menuItem.setChecked(true);
                    final  android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(false)
                        //.setIcon(R.mipmap.ic_launcher)
                        .setTitle("版本信息")
                        .setMessage("当前版本:"+getAppVersionName(MainActivity.this))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                menuItem.setChecked(false);

                            }
                        })
                        .create().show();
                }
                return false;
            }
        });

        drawerLayout.setStatusBarBackgroundColor(themeColor);

        oneFragments = new ArrayList<>();
        oneSingerlistFragment = new OneSingerListFragment();
        oneAblumListFragment = new OneAblumListFragment();
        onePlayListFragment = new OnePlayListFragment();
        oneFragments.add(oneSingerlistFragment);
        oneFragments.add(oneAblumListFragment);
        oneFragments.add(onePlayListFragment);
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(oneFragments!=null) {
                    return oneFragments.get(position);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_UNCHANGED;

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0){
                    return "歌手";
                }else if(position==1) {
                    return "专辑";
                }else if(position==2){
                    return "歌曲";
                }else {
                   return super.getPageTitle(position);
                }
            }
        };
        viewPager.setAdapter(fragmentStatePagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentFragmentPosition = 0;
                } else if(position==1){
                    currentFragmentPosition = 1;
                } else if(position==2){
                    currentFragmentPosition = 2;
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        //取得最后一次播放的音乐
        initHandler();
        initService();
        initReceiver();
        updateMusicInfo();
        updateSeekbar();


    }
//    private void disableListview(){
//        tabLayout.setVisibility(View.GONE);
//        toolbar.setVisibility(View.GONE);
//        viewPager.setVisibility(View.GONE);
//
//    }
//    private void enableListview(){
//        Log.v("MainActivity","enableListview()打开点击事件");
//        tabLayout.setVisibility(View.VISIBLE);
//        toolbar.setVisibility(View.VISIBLE);
//        viewPager.setVisibility(View.VISIBLE);
//    }

    public void onBackPressed() {
        Log.v("MainActivity","按下返回键");
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        if(isPlayView){
            quitPlayView();
            return;
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(MainActivity.this, "扫描完毕", Toast.LENGTH_SHORT).show();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            final ArrayList<Music> musicArrayList = bundle.getParcelableArrayList("musicArrayList");
            if(musicArrayList!=null){
                if(musicArrayList.size()!=0) {
                    if (isNull) {
                        isNull = false;
                        initialize();
                    } else {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                oneApplication.setMusicProvider(new MusicProvider(musicArrayList));
                            }
                        });
                        thread.start();

                    }
                }
            }
        }

    }


    public void setMusicProgress(int time){

    }
    public void setMusicDuration(int duration){

    }


    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("peacemaker.oneplayer", 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public void checkUpdate() {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if (msg.what == 0x001) {
                        if (!isFirstTime) {
                            Toast.makeText(MainActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                        }
                        isFirstTime = false;
                    } else {
                        String json = (String) msg.getData().get("json");
                        if (json != null) {
                            JSONObject response = new JSONObject(json);
                            Log.v("回应", response + "");
                            Boolean isUpdate = response.getBoolean("isUpdate");
                            Log.v("升级没有", isUpdate + "蛤蛤");
                            if (isUpdate) {
                                //有更新
                                Log.v("有更新", "蛤蛤");
                                String url = response.getString("url");
                                update(url);
                            } else {
                                Log.v("老子不更新啦", "哈哈哈");
                                Toast.makeText(MainActivity.this, "没有更新呢！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        isFirstTime = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        JsonUtil jsonUtil = new JsonUtil(handler);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "checkUpdate");
            jsonObject.put("model", Build.MODEL);
            jsonObject.put("sdk", Build.VERSION.SDK_INT);
            jsonObject.put("system", Build.VERSION.RELEASE);
            jsonObject.put("app", Data.appName);
            jsonObject.put("appVersion", getAppVersionName(MainActivity.this));
            jsonUtil.sendJson(jsonObject, Data.url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(final String url) {
        //ProgressBar progressBar;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                //.setIcon(R.mipmap.ic_launcher)
                .setTitle("有更新！")
                .setMessage("是否要安装新版OnePlayer？")
                .setPositiveButton("好！", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final AlertDialog.Builder downloadbuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View v = inflater.inflate(R.layout.update, null);
                        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
                        updateText = (TextView) v.findViewById(R.id.updateprogress);
                        downloadbuilder.setView(v);
                        Handler handler = new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                if (msg.what == 0x128) {

                                    int progress = msg.getData().getInt("progress");
                                    progressBar.setProgress(progress);
                                    updateText.setText(progress + "/100");
                                }
                                if (msg.what == 0x129) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    File apkfile = new File(Environment.getExternalStorageDirectory().getPath(), "OnePlayer.apk");
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                                    startActivity(i);
                                }
                                return false;
                            }
                        });
                        new Downloader().Download(url, handler);
                        downloadbuilder.create().show();
                    }
                })


                .setNegativeButton("还是算了！", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    public ArrayList<Music> getTempmusicArrayList() {
        return this.tempmusicArrayList;
    }

    private void openApk(Context context, String url) {
        Log.v("打开", "apk");
        PackageManager manager = context.getPackageManager();
        // 这里的是你下载好的文件路径
        PackageInfo info = manager.getPackageArchiveInfo(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "OnePlayer.apk", PackageManager.GET_ACTIVITIES);
        if (info != null) {
            Intent intent = manager.getLaunchIntentForPackage(info.applicationInfo.packageName);
            startActivity(intent);
        }
    }

    public void stopButtons() {
        oneSeekBar.setClickable(false);
        nextButton.setClickable(false);
        previousButton.setClickable(false);
        isNotiClikable = false;
    }

    public void startButtons() {
        oneSeekBar.setClickable(true);
        nextButton.setClickable(true);
        previousButton.setClickable(true);
        isNotiClikable = true;
    }
    private void selectPage(int page){
        switch (page){
            case 0:
                viewPager.setCurrentItem(singer);
                currentFragmentPosition = singer;
                break;
            case 1:
                viewPager.setCurrentItem(album);
                currentFragmentPosition = album;
                break;
            case 2:
                viewPager.setCurrentItem(song);
                currentFragmentPosition = song;
                break;
            default:break;
        }
        //saveOrderMode(page);
    }


    public ArrayList<Music> getInternetMusiclist() {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if (msg.what == 0x001) {
                        Toast.makeText(MainActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    } else {
                        String json = (String) msg.getData().get("json");
                        if (json != null) {
                            JSONObject response = new JSONObject(json);
                            response.getString("musicList");
//                            if (isUpdate) {
//                                //有更新
//                                Log.v("有更新", "蛤蛤");
//                                String url = response.getString("url");
//                                update(url);
//                            }else {
//                                Log.v("老子不更新啦","哈哈哈");
//                                Toast.makeText(MainActivity.this,"没有更新呢！",Toast.LENGTH_SHORT).show();
//                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        JsonUtil jsonUtil = new JsonUtil(handler);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "getInternetMusiclist");
            jsonUtil.sendJson(jsonObject, Data.url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void toSingerDetail(Music music){
        Log.v("MainActivity","toSingerDetail");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,OneAblumDetailActivity.class);
        startActivity(intent);
    }
    public void toAblumDetail(Music music){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,OneAblumDetailActivity.class);
        startActivity(intent);
    }
    public Music getDetailTarget(){
        return target;
    }

    public void changeTemp(int mode){
        if(mode==0){
            setTempmusicArrayList(singerArraylist.get(singerListIndex).getSecondItems());
        }else if(mode==1){
            setTempmusicArrayList(albumArraylist.get(albumListIndex).getSecondItems());
        }else if(mode==2){
            setTempmusicArrayList(songArraylist);
        }
    }

    public void itemSelected(Music music,int position) {
        Log.v("MainActivity","itemSelected选择位置"+music.getUrl());
        if (music.isPlayable()) {
            selectMusic(music,position);
        } else {
            switch (currentFragmentPosition){
                case 0:
                    singerListIndex = position;
                    toSingerDetail(music);
                    break;
                case 1:
                    albumListIndex = position;
                    toAblumDetail(music);
                    break;
            }
        }
    }

    public void selectMusic(Music music,int position) {
        Log.v("MainActivity","selectMusic查看来播放的音乐"+music.getUrl());
        if (music.equals(currentMusic) && !isNew) {
            return;
        }
//        switch (currentFragmentPosition){
//            case 0:if(isSingerDetail){
//                    oneSingerDetailFragment.setSelectedPosition(position);
//                    }
//                break;
//            case 1:if(isAblumDetail){
//                    oneAblumDetailFragment.setSelectedPosition(position);
//                    }
//                break;
//            case 2:break;
//        }
        currentPosition = tempmusicArrayList.indexOf(music);
        onePlayListFragment.setSelectedPosition(songArraylist.indexOf(music));
        //saveOrderMode(-3);
        isNew = false;
        //albumcontainer.setVisibility(View.VISIBLE);
        //viewPager.setVisibility(View.GONE);
        if (isPlaying) {
            musicService.pause();
            isPlaying = false;
        }
        if(currentColor==Color.WHITE) {
            playButton.setImageResource(R.drawable.pause);
        }else {
            playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
        }
        //currentPosition = position;
        currentMusic = music;
        //playingFragment.playAnimation();
        updateMusic();
        toPlayView();
        //basetag.setVisibility(View.GONE);
        //isAlbuming = true;
        //queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
        //isPlaying = true;
    }
//    private int findPosition(ArrayList<Music> musicArrayList,Music music){
//        for(Music music1 : musicArrayList){
//            if (music1.getDisplayName().equals(music.getDisplayName())){
//                if(music1.getArtist().equals(music.getArtist())){
//                    if(music1.getAlbum().equals(music.getAlbum())){
//                        return musicArrayList.indexOf(music1);
//                    }
//                }
//            }
//        }
//        return -1;
//    }
//
//    private void toOnePlayList() {
//        if (onePlayListFragment == null) {
//
//        }
//    }

    //    private void navigateToBrowser(String mediaId) {
//        LogHelper.d(TAG, "navigateToBrowser, mediaId=" + mediaId);
//        MediaBrowserFragment fragment = getBrowseFragment();
//
//        if (fragment == null || !TextUtils.equals(fragment.getMediaId(), mediaId)) {
//            fragment = new MediaBrowserFragment();
//            fragment.setMediaId(mediaId);
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(
//                    R.animator.slide_in_from_right, R.animator.slide_out_to_left,
//                    R.animator.slide_in_from_left, R.animator.slide_out_to_right);
//            transaction.replace(R.id.container, fragment, FRAGMENT_TAG);
//            // If this is not the top level media (root), we add it to the fragment back stack,
//            // so that actionbar toggle and Back will work appropriately:
//            if (mediaId != null) {
//                transaction.addToBackStack(null);
//            }
//            transaction.commit();
//        }
//    }
    public void notifiPositionChanged() {
        ////
        // // TODO: Make sure this auto-generated URL is correct.
//        if (!isNet) {
//            if (Build.VERSION.SDK_INT > 19) {
//                localplaylistAdapter.url = currentMusic;
//                localplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
//                localplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
//                localplaylistAdapter.notifyDataSetChanged();
//            } else {
//                localPlaylistAdapterlow.url = currentMusic;
//                localPlaylistAdapterlow.singer = tempmusicArrayList.get(currentPosition).getArtist();
//                localPlaylistAdapterlow.album = tempmusicArrayList.get(currentPosition).getAlbum();
//                localPlaylistAdapterlow.notifyDataSetChanged();
//            }
//        } else {
//            if (Build.VERSION.SDK_INT > 19) {
//                netplaylistAdapter.url = currentMusic;
//                netplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
//                netplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
//                netplaylistAdapter.notifyDataSetChanged();
//            } else {
//                netPlaylistAdapterlow.url = currentMusic;
//                netPlaylistAdapterlow.singer = tempmusicArrayList.get(currentPosition).getArtist();
//                netPlaylistAdapterlow.album = tempmusicArrayList.get(currentPosition).getAlbum();
//                netPlaylistAdapterlow.notifyDataSetChanged();
//            }
//        }
    }

//    public void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener) {
//        this.onIntializeCompleListener = onIntializeCompleListener;
//    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }
}
