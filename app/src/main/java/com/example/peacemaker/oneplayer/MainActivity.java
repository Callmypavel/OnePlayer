package com.example.peacemaker.oneplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peacemaker.oneplayer.databinding.MainActivityBinding;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan_000 on 2015/8/14.
 */
public class MainActivity extends OneActivity{
    Boolean isNull = false;
    private OneApplication oneApplication;
    Handler handler;
    Boolean isAlbuming = false;
    Boolean isNotiClikable = true;
    Boolean isNet = false;
    Boolean isFirstTime = true;
    int netmusicNumber = 1;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    public NavigationView navigationView;
    @BindView(R.id.id_tablayout)
    public TabLayout tabLayout;
    @BindView(R.id.id_viewpager)
    public ViewPager viewPager;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.one_index_view)
    public IndexView indexView;
    public ProgressBar progressBar;
    public TextView updateText;
    private OnePlayListFragment onePlayListFragment;
    private OneAlbumListFragment oneAlbumListFragment;
    private OneSingerListFragment oneSingerlistFragment;
    private ArrayList<Fragment> oneFragments;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private int currentFragmentPosition = 0;
    private OneLogger oneLogger;
    private MainActivityBinding binding;
    private MusicProvider musicProvider;

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
        oneApplication = (OneApplication) getApplication();
        if(oneApplication.musicProvider==null) {
            setContentView(R.layout.start_activity);
        }else {
            oneApplication.setOneActivity(this);
            oneApplication.initReceiver();
            initialize();
            return;
        }
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==0x111){
                    oneApplication.setOneActivity(musicProvider,MainActivity.this);

                    initialize();
                    checkUpdate();
                }
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OneMusicloader oneMusicloader = new OneMusicloader(getContentResolver(),MainActivity.this);
                ArrayList<Music> tempmusicArrayList = oneMusicloader.loadLocalMusic();
                musicProvider = new MusicProvider(tempmusicArrayList);
                Message message = new Message();
                message.what = 0x111;
                handler.sendMessage(message);

            }
        });
        thread.start();
    }


    @Override
    protected void onRestart() {
        Log.v("MainActivity","onRestart()");
        super.onRestart();
        oneApplication.setOneActivity(this);
        refreshPlaylist();
        navigationView.getHeaderView(0).setBackgroundColor(oneApplication.getThemeColor());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity", "activity");
        //oneApplication.unRegister();
    }
    public void refreshPlaylist(){
        if(onePlayListFragment!=null) {
            onePlayListFragment.refresh();
        }
    }


    protected void initialize() {
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.main_activity);
        ButterKnife.bind(MainActivity.this);
        binding.setMusic(oneApplication.currentMusic);
        binding.setMusicState(oneApplication.musicState);
        binding.setHandler(new OneClickHandler());
        binding.setOneConfig(oneApplication.getOneConfig());
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int themeColor = typedValue.data;
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
                return PixelFormat.OPAQUE;
            }
        };
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
//        ((ViewGroup)toolbar.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        //toolbar.setPadding(0,OneStatusUtil.getStatusBarHeight(this),0,0);
//        ((ViewGroup)toolbar.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
//        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_48dp);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_menu_white_48dp);

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
                    //menuItem.setChecked(true);
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
                    //menuItem.setChecked(false);
                    if(oneLogger!=null){
                        oneLogger.stopLogging();
                    }
                    menuItem.setTitle("打印日志");
                }else if (title.equals("版本信息")){
                    //menuItem.setChecked(true);
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(false)
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
                }else if(title.equals("主题换肤")){
                    //menuItem.setChecked(true);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,OneColorSelectActivity.class);
                    startActivity(intent);
                }else if(title.equals("测试功能")){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this,TestActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        //drawerLayout.setStatusBarBackgroundColor(themeColor);

        oneFragments = new ArrayList<>();
        oneSingerlistFragment = new OneSingerListFragment();
        oneAlbumListFragment = new OneAlbumListFragment();
        onePlayListFragment = new OnePlayListFragment();
        oneFragments.add(oneSingerlistFragment);
        oneFragments.add(oneAlbumListFragment);
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
                Log.v("MainActivity","onPageSelected"+position);
                updateIndexView(position);
                //viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        navigationView.getHeaderView(0).setBackgroundColor(oneApplication.getThemeColor());
//        indexView.setRecyclerView(oneSingerlistFragment.getRecyclerView());
//        indexView.setIndexedMusics(oneApplication.getIndexedSongArrayList());
        super.initialize();

    }
    private void updateIndexView(int position){
        Log.v("MainActivity","isScrollBy更新位置"+position);
        if (position == 0) {
            currentFragmentPosition = 0;
            indexView.setIndexedMusics(oneApplication.getIndexedSingerArrayList());
            indexView.setRecyclerView(oneSingerlistFragment.getRecyclerView(),true);
        } else if(position==1){
            currentFragmentPosition = 1;
            indexView.setIndexedMusics(oneApplication.getIndexedAlbumArrayList());
            indexView.setRecyclerView(oneAlbumListFragment.getRecyclerView(),false);

        } else if(position==2){
            currentFragmentPosition = 2;
            indexView.setIndexedMusics(oneApplication.getIndexedSongArrayList());
            indexView.setRecyclerView(onePlayListFragment.getRecyclerView(),true);

        }
    }
    protected void disableListview(){
        tabLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);

    }
    protected void enableListview(){
        Log.v("MainActivity","enableListview()打开点击事件");
        tabLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        Log.v("MainActivity","按下返回键");
        if(drawerLayout!=null) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                return;
            }
        }
        if(isPlayView){
            quitPlayView();
            return;
        }
        super.onBackPressed();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String mode = bundle.getString("mode");
                if(mode!=null) {
                    if (mode.equals("searchCompled")) {
                        Toast.makeText(MainActivity.this, "扫描完毕", Toast.LENGTH_SHORT).show();
                        final ArrayList<Music> musicArrayList = bundle.getParcelableArrayList("musicArrayList");
                        if (musicArrayList != null) {
                            if (musicArrayList.size() != 0) {
                                if (isNull) {
                                    isNull = false;
                                    initialize();
                                } else {
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            oneApplication.setOneActivity(new MusicProvider(musicArrayList), MainActivity.this);
                                        }
                                    });
                                    thread.start();
                                }
                            }
                        }
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

//    public void stopButtons() {
//        oneSeekBar.setClickable(false);
//        nextButton.setClickable(false);
//        previousButton.setClickable(false);
//        isNotiClikable = false;
//    }
//
//    public void startButtons() {
//        oneSeekBar.setClickable(true);
//        nextButton.setClickable(true);
//        previousButton.setClickable(true);
//        isNotiClikable = true;
//    }

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
    public void toSingerDetail(){
        Log.v("MainActivity","toSingerDetail");
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,OneSingerDetailActivity.class);
        startActivity(intent);
    }
    public void toAblumDetail(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,OneAlbumDetailActivity.class);
        startActivity(intent);
    }
    public void toSecondItemActivity(Music music){
        LogTool.log("MainActivity","更新目标音乐"+music.getUrl());
        oneApplication.updateTargetMusic(music);
        if(currentFragmentPosition==0){
            toSingerDetail();
        }else if(currentFragmentPosition==1) {
            toAblumDetail();
        }
    }



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
