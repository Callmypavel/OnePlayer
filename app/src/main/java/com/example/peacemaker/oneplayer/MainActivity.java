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
public class MainActivity extends OneActivity implements View.OnClickListener {
    int transferNum = 0;
    //private Music lastPlayedMusic;

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
    SharedPreferences sharedPreferences;
    SerchingFragment serchingfragment;
    FragmentTransaction fragmentTransaction;
    Handler serchhandler;
    Boolean isVersionOpen = false;

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
    private OneSingerDetailFragment oneSingerDetailFragment;
    private OneAblumDetailFragment oneAblumDetailFragment;
    private ArrayList<Fragment> oneFragments;
    //private FragmentPagerAdapter fragmentPagerAdapter;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private int singerListIndex = -1;
    private int albumListIndex = -1;
    private Boolean isAblumDetail = false;
    private Boolean isSingerDetail = false;
    public Boolean isPlayView = false;
//    private ActionBarDrawerToggle toggle;
    private int currentFragmentPosition = singer;
//    public int albumSelectedPosition;
//    public int singerSelectedPosition;
//    public int albumSelectedOffset;
//    public int singerSelectedOffset;
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
//        if (Build.VERSION.SDK_INT > 19) {
        setContentView(R.layout.start_activity);
//        } else {
//            setContentView(R.layout.main_activity_low);
//        }
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
                tempmusicArrayList = oneMusicloader.loadLocalMusic();
                musicProvider = new MusicProvider(tempmusicArrayList);
                songArraylist = musicProvider.getSongs();
                singerArraylist = musicProvider.getSingers();
                albumArraylist = musicProvider.getAlbums();
                Message message = new Message();
                message.what = 0x111;
                handler.sendMessage(message);

                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
                //client = new GoogleApiClient.Builder(MainActivity.this).addApi(AppIndex.API).build();
            }
        });
        thread.start();
        //initialize();

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
        //Log.v("MainActivity","歌手列表抽查"+singerArraylist.get(0).getSecondItems());
        return singerArraylist;
    }
    public ArrayList<Music> getAlbumArraylist(){
        return albumArraylist;
    }
    public ArrayList<Music> getSongArraylist(){
        return songArraylist;
    }

    private void initialize() {
        //从intent获取数据
//        Bundle bundle = getIntent().getExtras();
//        lastPlayedMusic = (Music) bundle.get("lastPlayedMusic");
//        currentMusic = lastPlayedMusic;
//        currentPosition = (int) bundle.get("currentPosition");
//        Order = (int) bundle.get("OrderMode");
//        ArrayList<Music> musicArrayList = (ArrayList<Music>) bundle.get("musicArrayList");
        //musicProvider = (MusicProvider) bundle.get("musicProvider");
        currentPosition = 0;
        if(songArraylist!=null) {
            if(songArraylist.size()!=0) {
                currentMusic = songArraylist.get(0);
            }
        }
        Log.v("MainActivity", "传后检查提供者" + musicProvider);
        //for (Music singer : singerArraylist) {
            //Log.v("MainActivity","抽查歌手"+singer.getSecondItems());
            //Log.v("MainActivity","抽查歌手歌曲"+singer.getSecondItems().get(0).getDisplayName());
        //}
        tempmusicArrayList = songArraylist;
        musicNumber = tempmusicArrayList.size();
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return false;
//            }
//        });
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        themeColor = typedValue.data;
        // 注意setStatusBarBackgroundColor方法需要你将fitsSystemWindows设置为true才会生效
        //将ActionBar和drawer绑定
        ButterKnife.bind(this);
//        toolbar.setTitle("OnePlayer");
//        toolbar.setTitleTextColor(Color.WHITE);
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
//        toggle = new ActionBarDrawerToggle(this,
//                drawerLayout,
//                toolbar,
//                R.string.app_name,
//                R.string.app_name){
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                //super.onDrawerClosed(drawerView);
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                //super.onDrawerOpened(drawerView);
//            }
//        };
//        if(toggle!=null) {
//            drawerLayout.addDrawerListener(toggle);
//            toggle.syncState();
//            //toggle.setDrawerIndicatorEnabled(false);
//            //toolbar.setNavigationIcon(R.drawable.ic_menu_white_18dp);
//        }
//        (toolbar.getChildAt(0)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                //menuItem.setChecked(true);
                String title = menuItem.getTitle().toString();
                if (title.equals("深度扫描")){
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setCancelable(false)
//                        //.setIcon(R.mipmap.ic_launcher)
//                        .setTitle("深度扫描")
//                        .setMessage("是否要执行深度扫描？")
//                        .setPositiveButton("好！", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                System.out.println("扫描完毕更新view");
//
//                            }
//                        })
//                        .setNegativeButton("还是算了！", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .create().show();
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
                    closeNotification();
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



        //playlistView = (ListView)findViewById(R.id.playList);
        //seekBar = (SeekBar) findViewById(R.id.seekBar);

        drawerLayout.setStatusBarBackgroundColor(themeColor);
//        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int size = getResources().getDimensionPixelSize(R.dimen.shape_size);
//                outline.setOval(0,0,size,size);
//            }
//        };
        oneSeekBar.setOnOneSeekBarListener(new OnOneSeekBarListener() {
            @Override
            public void onSeekBarUpdated(float progress) {
                int targetTime = (int) (progress * totalTime);
                progressText.setText(ten2sixty(targetTime));
                if (!isNew) {
                    Log.v("MainActiivty","跳转进度"+targetTime);
                    if(currentColor==Color.WHITE) {
                        playButton.setImageResource(R.drawable.pause);
                    }else {
                        playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                    }
                    time = targetTime;
                    musicService.seekTo(time * 1000);
                    isPlaying = false;
                    play();
                } else {
                    oneSeekBar.setProgress(0);
                    progressText.setText(ten2sixty(0));
//                    ViewGroup.LayoutParams layoutParams = bottomProgress.getLayoutParams();
//                    layoutParams.width = 0;
//                    bottomProgress.setLayoutParams(layoutParams);
//                    bottomProgress.postInvalidate();
                }


            }
            @Override
            public void onButtonClick() {
                play();
            }
        });
        //imageView = (ImageView)findViewById(R.id.imageView);
        //imageView.setOutlineProvider(viewOutlineProvider);

//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("触摸监听");
//                switch (event.getActionMasked()){
//                    case MotionEvent.ACTION_DOWN:
//                        imageView.setTranslationZ(120);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        System.out.println("触摸抬起手指");
//                        imageView.setTranslationZ(0);
//                        break;
//                    default:
//                        return false;
//                }
//                return true;
//            }
//        });
//        databaseOperator = new DatabaseOperator(this, "OnePlayer.db");

        //5.0适用
//        if (Build.VERSION.SDK_INT > 19) {
//            localplaylistAdapter = new PlaylistAdapter(localmusicArrayList, getLayoutInflater());
//            netplaylistAdapter = new PlaylistAdapter(null, getLayoutInflater());
//            localmusicListFragment = new LocalMusicFragment();
//            localmusicListFragment.setOnOrderClickListener(new OnOrderClickListener() {
//                @Override
//                public void onOrderClick(int order) {
//                    OrderChanged(order);
//                }
//            });
//            netmusicListFragment = new NetMusicFragment();
//            localmusicListFragment.setOnIntializeCompleListener(new OnIntializeCompleListener() {
//                @Override
//                public void onInitComple() {
//                    OrderChanged(Order);
//                }
//            });
//            netplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    isNet = true;
//                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                        albumcontainer.setVisibility(View.VISIBLE);
//                        viewPager.setVisibility(View.GONE);
//                        return;
//                    }
//                    isNew = false;
//                    albumcontainer.setVisibility(View.VISIBLE);
//                    viewPager.setVisibility(View.GONE);
//                    if (isPlaying) {
//                        musicService.pause();
//                        isPlaying = false;
//                    }
//                    netcurrentPositon = position;
//                    Log.v("这个positon是我钦点的", position + "");
//                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                    currentMusic = netmusicArrayList.get(position).getUrl();
//                    netplaylistAdapter.url = currentMusic;
//                    netplaylistAdapter.notifyDataSetChanged();
//                    playingFragment.playAnimation();
//                    updateMusic();
//                    basetag.setVisibility(View.GONE);
//                    isAlbuming = true;
//                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                    isPlaying = true;
//                }
//            });
//
//        } else {
//            Log.v("MainActivity", "版本号" + Build.VERSION.SDK_INT);
//            localPlaylistAdapterlow = new PlaylistAdapterlow(localmusicArrayList, getLayoutInflater());
//            netPlaylistAdapterlow = new PlaylistAdapterlow(null, getLayoutInflater());
//            localmusicListFragmentLow = new LocalMusicFragmentLow();
//            netmusicListFragmentLow = new NetMusicFragmentLow();
//            localmusicListFragmentLow.setOnOrderClickListener(new OnOrderClickListener() {
//                @Override
//                public void onOrderClick(int order) {
//                    OrderChanged(order);
//                }
//            });
//            localmusicListFragmentLow.setOnIntializeCompleListener(new OnIntializeCompleListener() {
//                @Override
//                public void onInitComple() {
//                    OrderChanged(Order);
//                }
//            });
//            netmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    isNet = true;
//                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                        albumcontainer.setVisibility(View.VISIBLE);
//                        viewPager.setVisibility(View.GONE);
//                        return;
//                    }
//                    isNew = false;
//                    albumcontainer.setVisibility(View.VISIBLE);
//                    viewPager.setVisibility(View.GONE);
//                    if (isPlaying) {
//                        musicService.pause();
//                        isPlaying = false;
//                    }
//                    currentPosition = position;
//                    Log.v("这个positon是我钦点的", position + "");
//                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                    currentMusic = netmusicArrayList.get(position).getUrl();
//                    netPlaylistAdapterlow.url = currentMusic;
//                    netPlaylistAdapterlow.notifyDataSetChanged();
//                    playingFragment.playAnimation();
//                    updateMusic();
//                    basetag.setVisibility(View.GONE);
//                    isAlbuming = true;
//                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                    isPlaying = true;
//                }
//            });
//        }
//        playingFragment = new PlayingFragment();
        oneFragments = new ArrayList<>();
        oneSingerlistFragment = new OneSingerListFragment();
        oneAblumListFragment = new OneAblumListFragment();
        onePlayListFragment = new OnePlayListFragment();
//        oneSingerDetailFragment = new OneSingerDetailFragment();
//        oneAblumDetailFragment = new OneAblumDetailFragment();
        oneFragments.add(oneSingerlistFragment);
        oneFragments.add(oneAblumListFragment);
        oneFragments.add(onePlayListFragment);
//        oneFragments.add(oneSingerDetailFragment);
//        oneFragments.add(oneAblumDetailFragment);
        //fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.albumcontainer, playingFragment);
//        fragmentTransaction.commit();
        //musicListFragment.init(this);
//        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
////                if (Build.VERSION.SDK_INT > 19) {
//                if(oneFragments!=null) {
//                    if(isSingerDetail){
//                        if(position==0){
//                            position=3;
//                        }
//                    }
//                    if(isAblumDetail){
//                        if(position==1){
//                            position=4;
//                        }
//                    }
//                    Log.v("MainActivity","返回fragment序号"+position);
//                    return oneFragments.get(position);
//                }
////                } else {
////                    if (position == 0) {
////                        return localmusicListFragmentLow;
////                    } else return netmusicListFragmentLow;
////                }
//                return null;
//            }
//
//            @Override
//            public int getCount() {
//                return 3;
//            }
//
//            @Override
//            public int getItemPosition(Object object) {
//                if(isAblumDetail){
//                    if(object instanceof OneAblumListFragment){
//                        Log.v("MainActivity","进入专辑细节");
//                        return POSITION_NONE;
//                    }
//                }else {
//                    if (object instanceof OneAblumDetailFragment) {
//                        Log.v("MainActivity","退出专辑细节");
//                        return POSITION_NONE;
//                    }
//                }
//                if (isSingerDetail){
//                    if(object instanceof OneSingerListFragment){
//                        Log.v("MainActivity","进入歌手细节");
//                        return POSITION_NONE;
//                    }
//                }else {
//                    if(object instanceof OneSingerDetailFragment){
//                        Log.v("MainActivity","退出歌手细节");
//                        return POSITION_NONE;
//
//                    }
//                }
//                return POSITION_UNCHANGED;
//
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                Log.v("MainActivity","实例化item"+position);
//                return super.instantiateItem(container, position);
//            }
//        };
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
//                if (Build.VERSION.SDK_INT > 19) {
                if(oneFragments!=null) {
//                    if(isSingerDetail){
//                        if(position==0){
//                            position=3;
//                        }
//                    }
//                    if(isAblumDetail){
//                        if(position==1){
//                            position=4;
//                        }
//                    }
                    //Log.v("MainActivity","返回fragment序号"+position);
                    return oneFragments.get(position);
                }
//                } else {
//                    if (position == 0) {
//                        return localmusicListFragmentLow;
//                    } else return netmusicListFragmentLow;
//                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public int getItemPosition(Object object) {
//                if(isAblumDetail){
//                    if(object instanceof OneAblumListFragment){
//                        //Log.v("MainActivity","进入专辑细节");
//                        return POSITION_NONE;
//                    }
//                }else {
//                    if (object instanceof OneAblumDetailFragment) {
//                        Log.v("MainActivity","退出专辑细节");
//                        return POSITION_NONE;
//                    }
//                }
//                if (isSingerDetail){
//                    if(object instanceof OneSingerListFragment){
//                        Log.v("MainActivity","进入歌手细节");
//                        return POSITION_NONE;
//                    }
//                }else {
//                    if(object instanceof OneSingerDetailFragment){
//                        Log.v("MainActivity","退出歌手细节");
//                        return POSITION_NONE;
//
//                    }
//                }
                return POSITION_UNCHANGED;

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //Log.v("MainActivity","实例化item"+position);
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
                if (position == singer) {
                    currentFragmentPosition = singer;
                } else if(position==album){
                    currentFragmentPosition = album;
                } else if(position==song){
                    currentFragmentPosition = song;
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.v("页面滚动状态改变", state + "");
            }

        });
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        cursorWidth = dm.widthPixels / 3;
//        ViewGroup.LayoutParams layoutParams = cursor.getLayoutParams();
//        layoutParams.width = cursorWidth;
//        cursor.setLayoutParams(layoutParams);
//        Log.v("MainActivity","游标宽度"+cursorWidth);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称

        tabLayout.setupWithViewPager(viewPager);
//        switch (Order) {
//            case -3:
//                tempmusicArrayList = songArraylist;
//                selectPage(song);
//                break;
//            case -1:
//                tempmusicArrayList = getLastPlayedSinger(singerArraylist,lastPlayedMusic.getArtist());
//                selectPage(singer);
//                break;
//            case -2:
//                tempmusicArrayList = getLastPlayedAlbum(albumArraylist,lastPlayedMusic.getAlbum());
//                selectPage(song);
//                break;
//        }


//        version.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isVersionOpen) {
//                    versionText.setVisibility(View.VISIBLE);
//                    versionText.setText("当前系统版本为:" + getAppVersionName(MainActivity.this));
//                    isVersionOpen = true;
//                } else {
//                    versionText.setVisibility(View.GONE);
//                    isVersionOpen = false;
//                }
//            }
//        });

        System.out.println("扫描后的数目" + tempmusicArrayList.size());
        if (tempmusicArrayList.size() == 0) {
            System.out.println("获得为空");
            Toast.makeText(this, "抱歉，没有歌曲", Toast.LENGTH_SHORT).show();
            isNull = true;
            oneSeekBar.setClickable(false);
            nextButton.setClickable(false);
            previousButton.setClickable(false);
            playModeButton.setClickable(false);
            System.out.println("朱军 地球太危险 老子先跑了");
            return;
        }
        //设置监听事件

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playModeButton.setOnClickListener(this);
        queueButton.setOnClickListener(this);
//        singerTagText.setOnClickListener(this);
//        albumTagText.setOnClickListener(this);
//        songTagText.setOnClickListener(this);
        bottomControllBar.setOnClickListener(this);
        playButton.setOnClickListener(this);
        //mainContentBar.setOnClickListener(this);
        mainContentBar.setBottom_controll_bar((RelativeLayout) mainContentBar.getChildAt(0));
        mainContentBar.setOnReachMaxListener(new OnReachMaxListener() {
            @Override
            public void onReachMax() {
                if(!isAblumDetail&&!isSingerDetail) {
                    disableListview();
                }
            }
        });
        //取得最后一次播放的音乐
        initHandler();
        initService();
        //initNotification(0);
        initReceiver();
        updateMusicInfo();
        updateSeekbar();
        initOnePlayer(currentMusic.getUrl());
        playMode = cycle;


    }

    public void setTempmusicArrayList(ArrayList<Music> musicArrayList){
        tempmusicArrayList = musicArrayList;
        musicNumber = musicArrayList.size();

    }
    private void updateMusicArrayList(ArrayList<Music> musicArrayList){
        musicProvider = new MusicProvider(musicArrayList);
        songArraylist = musicProvider.getSongs();
        singerArraylist = musicProvider.getSingers();
        albumArraylist = musicProvider.getAlbums();
        currentPosition = 0;
        setTempmusicArrayList(musicArrayList);

    }



//    private ArrayList<Music> getLastPlayedAlbum(ArrayList<Music> albumArraylist, String albumName){
//        ArrayList<Music> defaultList = new ArrayList<>();
//        for(Music album : albumArraylist){
//            if(album.getDisplayName().equals(albumName)){
//                return album.getSecondItems();
//            }
//        }
//        return defaultList;
//    }
//    private ArrayList<Music> getLastPlayedSinger(ArrayList<Music> singerArraylist,String albumName){
//        ArrayList<Music> defaultList = new ArrayList<>();
//        for(Music singer : singerArraylist){
//            if(singer.getDisplayName().equals(albumName)){
//                return singer.getSecondItems();
//            }
//        }
//        return defaultList;
//    }



    private void toPlayView(){
        Log.v("MainActivity","toPlayView()");
        mainContentBar.toMaxHeight();
        //disableListview();
        isPlayView = true;
    }
    private void quitPlayView(){
        Log.v("MainActivity","quitPlayView()");
        if(!isSingerDetail&&!isAblumDetail) {
            enableListview();
        }
        mainContentBar.toMinHeight();
        isPlayView = false;
    }
    private void disableListview(){
        //Log.v("MainActivity","disableListview()取消点击事件");
//        if(isSingerDetail){
//            oneSingerDetailFragment.disable();
//        }else {
//            oneSingerlistFragment.disable();
//        }
//        if(isAblumDetail){
//            oneAblumDetailFragment.disable();
//        }else {
//            oneAblumListFragment.disable();
//        }
//        onePlayListFragment.disable();
//        albumTagText.setVisibility(View.GONE);
//        singerTagText.setVisibility(View.GONE);
//        songTagText.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
//        viewPager.setClickable(false);
        viewPager.setVisibility(View.GONE);

    }
    private void enableListview(){
        Log.v("MainActivity","enableListview()打开点击事件");
//        if(isSingerDetail){
//            oneSingerDetailFragment.enable();
//        }else {
//            oneSingerlistFragment.enable();
//        }
//        if(isAblumDetail){
//            oneAblumDetailFragment.enable();
//        }else {
//            oneAblumListFragment.enable();
//        }
//        onePlayListFragment.enable();
//        albumTagText.setVisibility(View.VISIBLE);
//        singerTagText.setVisibility(View.VISIBLE);
//        songTagText.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
//        viewPager.setClickable(true);
        viewPager.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        Log.v("MainActivity","按下返回键");
//        if (isAlbuming) {
//            //albumcontainer.setVisibility(View.GONE);
//            //viewPager.setVisibility(View.VISIBLE);
//            //basetag.setVisibility(View.VISIBLE);
//            isAlbuming = false;
//            queueButton.setImageResource(R.drawable.ic_album_white_48dp);
//        } else {
//            if (isSingerTwo) {
//                OrderChanged(-2);
//                isSingerTwo = false;
//            } else if (isAlbumTwo) {
//                OrderChanged(-3);
//                isAlbumTwo = false;
//            } else {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        if(isPlayView){
            quitPlayView();
            return;
        }
        if(currentFragmentPosition==singer){
            if(isSingerDetail){
                quitSingerDetail();
                return;
            }
        }else if (currentFragmentPosition==album){
            if(isAblumDetail){
                quitAblumDetail();
                return;
            }
        }
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setCancelable(false);
//            //builder.setIcon(R.mipmap.ic_launcher);
//            builder.setTitle("退出Oneplayer");
//            builder.setMessage("确定要退出OnePlayer吗？(希望音乐在后台播放直接点击home键即可)");
//            builder.setPositiveButton("嗯，退出", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    closeNotification();
//                    finish();
//                    //onDestroy();
//                }
//            });
//            builder.setNegativeButton("还不退呢", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    arg0.dismiss();
//                }
//            });
//            builder.create().show();
//            }
//        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //initNotification(R.drawable.ic_pause_circle_outline_black_48dp,null);
        Toast.makeText(MainActivity.this, "扫描完毕", Toast.LENGTH_SHORT).show();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            final ArrayList<Music> musicArrayList = bundle.getParcelableArrayList("musicArrayList");
            if(musicArrayList!=null){
                if(musicArrayList.size()!=0) {
//                    Log.v("MainActivity", "onNewIntent噪音检查" + musicArrayList.size());
//                    for(Music music : musicArrayList) {
//                        Log.v("MainActivity", "onNewIntent噪音检查" + music.getDisplayName());
//                    }
                    if (isNull) {
                        isNull = false;
                        initialize();
                    } else {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                songArraylist = musicArrayList;
                                updateMusicArrayList(songArraylist);
                            }
                        });
                        thread.start();

                    }
                }
            }
        }

    }

    public void updateMusic() {
        //playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
        time = 0;
        initOnePlayer(currentMusic.getUrl());
        musicService.setOnePlayer(onePlayer);
        musicService.startService();
        if(!visualizer.getEnabled()){
            visualizer.setEnabled(true);
        }
        isPlaying = true;
//        if (!isNet) {
//            saveLastPlayed();
//        }
        updateMusicInfo();
        updateSeekbar();
        //initNotification(R.drawable.ic_pause_circle_outline_black_48dp,null);
    }

    public void initOnePlayer(String path) {
        //Log.v("MainActivity", "initOnePlayer我要用它" + path);
        if (onePlayer == null) {
            onePlayer = new OnePlayer();
            onePlayer.setOnCompleListener(new OnCompleListener() {
                @Override
                public void onComple() {
                    if(playMode!=looping) {
                        next();
                    }else {
                        currentMusic = tempmusicArrayList.get(currentPosition);
                        updateMusic();
                    }
                }
            });
            onePlayer.setOnPrepareListener(new OnPrepareListener() {
                @Override
                public void onPrepare(int duration) {
                    if (timer == null) {
                        initTimer();
                    }
                    isPlaying = true;
                    durationText.setText(ten2sixty(duration / 1000));
                    //seekBar.setMax(duration / 1000);
                    oneSeekBar.setButtonBitmap(true);
                    if(currentColor==Color.WHITE) {
                        playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                        initNotification(false,null);
                    }else {
                        playButton.setImageResource(R.drawable.pause);
                        initNotification(false,null);
                    }

                    startButtons();
                }
            });
            onePlayer.setOnBufferedUpdateListener(new OnBuffedUpdateListener() {
                @Override
                public void onBuffedUpdate(int percent) {
                    Log.v("主activity", percent + " " + percent * totalTime / 100);
                    //seekBar.setSecondaryProgress(percent * totalTime / 100);
                }
            });
            onePlayer.setOnOneErrorListener(new OnOneErrorListener() {
                @Override
                public void setError(int what, int extra) {
                    Toast.makeText(MainActivity.this,"what:"+what+",extra:"+extra,Toast.LENGTH_LONG).show();
                }
            });
        }
        //原地址
        onePlayer.init(path);
        //onePlayer.init("http://192.168.1.103/OnePlayer/Fall%20Out%20Boy%20-%20The%20Phoenix.mp3");

    }
//    private Bitmap getCurrentBitmap(){
//        return currentMusic.getSmallAlbumArt(this);
//    }



    public void initService() {
        Log.v("主Activity", "初始化服务");

        Intent i = new Intent(MainActivity.this, MusicService.class);
        i.putExtra("isPlaying", isPlaying);
        i.putExtra("playMode", playMode);
        i.putExtra("currentMusic", currentMusic);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.v("主Activity", "初始化服务完毕");
    }


    public void playMode() {
        int i = transferNum % 3;
        switch (i) {
            case 0:
                //playByLooping();
                playMode = looping;
                if(currentColor==Color.WHITE) {
                    playModeButton.setImageResource(R.drawable.ic_repeat_one_white_48dp);
                }else {
                    playModeButton.setImageResource(R.drawable.repeat_one);
                }
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //playByRandom();
                playMode = random;
                if(currentColor==Color.WHITE) {
                    playModeButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
                }else {
                    playModeButton.setImageResource(R.drawable.shuffle);
                }
                Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //playByCycle();
                playMode = cycle;
                if(currentColor==Color.WHITE) {
                    playModeButton.setImageResource(R.drawable.ic_repeat_white_48dp);
                }else {
                    playModeButton.setImageResource(R.drawable.repeat);
                }
                Toast.makeText(this, "列表循环", Toast.LENGTH_SHORT).show();
                break;
        }
        transferNum += 1;
    }

    public void queue() {
        quitPlayView();
//        if (viewPager.getVisibility() == View.GONE) {
//            viewPager.setVisibility(View.VISIBLE);
//            //albumcontainer.setVisibility(View.GONE);
//            basetag.setVisibility(View.VISIBLE);
//            isAlbuming = false;
//            queueButton.setImageResource(R.drawable.ic_album_white_48dp);
//        } else {
//            viewPager.setVisibility(View.GONE);
//            //albumcontainer.setVisibility(View.VISIBLE);
//            basetag.setVisibility(View.GONE);
//            isAlbuming = true;
//            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//        }

    }

    //    public void setUrls(){
//        int i = musicArrayList.size();
//        String[] urls = new String[i];
//        for (int j=0;j<i;j++){
//            urls[j] = musicArrayList.get(j).getUrl();
//        }
//        onePlayer.setUrls(urls);
//    }
    public void updateSeekbar() {
        System.out.println("更新seekbar");
//        if(!isNet){
        try {
            totalTime = Integer.parseInt(currentMusic.getDuration()) / 1000;
        }catch (Exception e){
            Log.v("MainActivity","获取时间出错");
            e.printStackTrace();
            totalTime = 0;
        }
//            seekBar.setMax(Integer.parseInt(tempmusicArrayList.get(currentPosition).getDuration()) / 1000);
//        }
        progressText.setText(ten2sixty(0));
        durationText.setText(ten2sixty(totalTime));
//        seekBar.setSecondaryProgress(0);
        oneSeekBar.setDegree(0);
//        ViewGroup.LayoutParams layoutParams = bottomProgress.getLayoutParams();
//        layoutParams.width = 0;
//        bottomProgress.setLayoutParams(layoutParams);
//        bottomProgress.postInvalidate();

    }

    public String ten2sixty(int ten) {
        String sixty;
        String minute = ten / 60 + "";
        String second = ten % 60 + "";
        if (ten / 60 >= 0 && ten / 60 < 10) {
            minute = "0" + minute;
        }
        if (ten % 60 >= 0 && ten % 60 < 10) {
            second = "0" + second;
        }
        sixty = minute + ":" + second;
        return sixty;
    }

    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            this.onStop();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    public void updateToolbar() {
//        //getActionBar().setSubtitle(musicArrayList.get(currentPosition).getArtist());
////        getSupportActionBar().setTitle("");
////        if (!isNet) {
////            ((TextView) findViewById(R.id.toolbar_title)).setText(tempmusicArrayList.get(currentPosition).getDisplayName());
////            Log.v("MainActivity", "updateToolbar当前temp" + tempmusicArrayList.size() + tempmusicArrayList.get(currentPosition).getDisplayName());
////            Log.v("MainActivity", "updateToolbar当前position" + currentPosition + tempmusicArrayList.get(currentPosition).getArtist());
////            ((TextView) findViewById(R.id.toolbar_subtitle)).setText(tempmusicArrayList.get(currentPosition).getArtist());
////        } else {
////            ((TextView) findViewById(R.id.toolbar_title)).setText(netmusicArrayList.get(netcurrentPositon).getDisplayName());
////            ((TextView) findViewById(R.id.toolbar_subtitle)).setText(netmusicArrayList.get(netcurrentPositon).getArtist());
////        }
//
//    }
    private void updateMusicInfo(){
        currentBitmap = currentMusic.getMiddleAlbumArt(this);
        Palette.generateAsync(currentBitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                musicColor = palette.getDarkVibrantColor(Color.WHITE);
                bottomControllBar.setBackgroundColor(musicColor);
                playViewControllBar.setBackgroundColor(musicColor);
                topToolBar.setBackgroundColor(musicColor);
                oneWaveFromView.setPaintColor(musicColor);
                if(!ColorUtil.getContrast(musicColor,currentColor)){
                    if (currentColor==Color.WHITE){
                        currentColor = Color.BLACK;
                    }else {
                        currentColor = Color.WHITE;
                    }
                }
                initColor();

            }
        });
        bottomAblumImageView.setImageBitmap(currentBitmap);
        bottomSingerNameText.setText(currentMusic.getArtist());
        bottomSongNameText.setText(currentMusic.getDisplayName());
        playViewAblumImageView.setImageBitmap(currentBitmap);
        topToolBarSingerNameText.setText(currentMusic.getArtist());
        topToolBarSongNameText.setText(currentMusic.getDisplayName());

    }
    private void initColor(){
        progressText.setTextColor(currentColor);
        durationText.setTextColor(currentColor);
        bottomSingerNameText.setTextColor(currentColor);
        bottomSongNameText.setTextColor(currentColor);
        topToolBarSingerNameText.setTextColor(currentColor);
        topToolBarSongNameText.setTextColor(currentColor);
        oneSeekBar.setColorInt(currentColor);
        oneSeekBar.setButtonBitmap(isPlaying);
        mainContentBar.setBackgroundColor(currentColor);
        if(currentColor==Color.WHITE){
            //bottomProgress.setBackgroundColor(currentColor);
            if(isPlaying){
                playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
            }else {
                playButton.setImageResource(R.drawable.ic_play_circle_fill_white_48dp);
            }
            previousButton.setImageResource(R.drawable.ic_skip_previous_white_48dp);
            nextButton.setImageResource(R.drawable.ic_skip_next_white_48dp);
            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
            int i = transferNum % 3;
            switch (i) {
                case 0:
                    playModeButton.setImageResource(R.drawable.ic_repeat_white_48dp);
                    break;
                case 1:
                    playModeButton.setImageResource(R.drawable.ic_repeat_one_white_48dp);
                    break;
                case 2:
                    playModeButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
                    break;
            }
        }else if (currentColor==Color.BLACK){
            //bottomProgress.setBackgroundColor(Color.GRAY);
            if(isPlaying){
                playButton.setImageResource(R.drawable.pause);
            }else {
                playButton.setImageResource(R.drawable.play);
            }
            previousButton.setImageResource(R.drawable.previous);
            nextButton.setImageResource(R.drawable.next);
            queueButton.setImageResource(R.drawable.queue);
            int i = transferNum % 3;
            switch (i) {
                case 0:
                    playModeButton.setImageResource(R.drawable.repeat);
                    break;
                case 1:
                    playModeButton.setImageResource(R.drawable.repeat_one);
                    break;
                case 2:
                    playModeButton.setImageResource(R.drawable.shuffle);
                    break;
            }
        }
        if(!isNew) {
            initNotification(false, currentBitmap);
        }else {
            initNotification(true, currentBitmap);
        }
    }


//    public void saveLastPlayed() {
//        databaseOperator = new DatabaseOperator(this, "OnePlayer.db");
//        if (sharedPreferences == null) {
//            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
//        }
//        Log.v("MainActivity", "储存位置" + currentPosition);
//        Log.v("MainActivity", "储存url" + currentMusic);
//        databaseOperator.saveLastplayed(currentPosition, currentMusic,"Last");
//        databaseOperator = null;
//    }

//    public void saveOrderMode(int OrderMode) {
//        Log.v("MainActivity", "saveOrderMode()我钦点存储" + OrderMode);
//        databaseOperator = new DatabaseOperator(this, "OnePlayer.db");
//        if (sharedPreferences == null) {
//            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
//        }
//        databaseOperator.saveOrderMode(OrderMode,"Last");
//        databaseOperator = null;
//    }

//    public void saveFirstPosition(int Position) {
//        databaseOperator = new DatabaseOperator(this, "OnePlayer.db");
//        if (sharedPreferences == null) {
//            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
//        }
//        databaseOperator.saveFirstPosition(sharedPreferences, Position);
//        databaseOperator = null;
//    }

    public void initReceiver() {
        //注册播放广播
        playReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                play();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("playButton");
        registerReceiver(playReceiver, filter);

        //上一首广播
        previousReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNotiClikable) {
                    previous();
                }
            }
        };
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("previousButton");
        registerReceiver(previousReceiver, filter1);

        //下一首广播
        nextReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNotiClikable) {
                    next();
                }
            }
        };
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("nextButton");
        registerReceiver(nextReceiver, filter2);

//        BroadcastReceiver openReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.v("接收到变化",intent.getAction());
//                openApk(MainActivity.this, null);
//            }
//        };
//        IntentFilter filter3 = new IntentFilter();
//        filter3.addAction(Intent.ACTION_PACKAGE_REPLACED);
//        filter3.addAction(Intent.ACTION_PACKAGE_ADDED);
//        filter3.addAction(Intent.ACTION_PACKAGE_CHANGED);
//        filter3.addAction(Intent.ACTION_PACKAGE_REMOVED);
//        filter3.addDataScheme("package");
//        filter3
//        registerReceiver(openReceiver, filter3);
//        Log.v("安装广播已注册", "");


    }
    public void setMusicProgress(int time){

    }
    public void setMusicDuration(int duration){

    }

    protected void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //处理开始走时信息
                if (msg.what == 1) {
                    //Log.v("MainActivity","handler查看进度"+time+","+totalTime);
                    progressText.setText(ten2sixty(time));
                    float progress = time * 1.0f / totalTime;
                    //seekBar.setProgress(time);
                    oneSeekBar.setProgress(time * 1.0f / totalTime);

                }
                return false;
            }
        });
    }

    public void initTimer() {
        time = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isPlaying) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    if (time != totalTime) {
                        time += 1;
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
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


    @Override
    public void onClick(View v) {
        Log.v("MainActivity","onClick()查看id"+v.getId());
        switch (v.getId()) {
            case R.id.play_view_previous:
                previous();
                break;
            case R.id.play_view_next:
                next();
                break;
//            case R.id.playandpause:
//                play();
//                break;
//            case R.id.update:
//                checkUpdate();
//                break;
            case R.id.play_view_queue:
                queue();
                break;
            case R.id.play_view_playmode:
                playMode();
                break;
//            case R.id.singer_tag:
//                selectPage(0);
//                break;
//            case R.id.album_tag:
//                selectPage(1);
//                break;
//            case R.id.song_tag:
//                selectPage(2);
//                break;
//            case R.id.main_content_bar:
//                Log.v("MainActivity","点击MainContenBar");
//                toPlayView();
//                break;
            case R.id.bottom_play_button:
                play();
                break;
            case R.id.bottom_controll_bar:
                Log.v("MainActivity","点击BottomControllBar");
                toPlayView();
                break;

        }
    }

//    private void moveCursor(int start, int end) {
//        //Log.v("MainActivity","游标移动从"+start+"到"+end);
//        TranslateAnimation animation = new TranslateAnimation(start, end, 0, 0);
//        animation.setFillAfter(true);// True:停留在停止位置
//        animation.setDuration(300);
//        cursor.startAnimation(animation);
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
    public void toSingerDetail(Music music){
        Log.v("MainActivity","toSingerDetail");
        isSingerDetail = true;
        target = music;
        oneApplication.setTargetMusic(music);
        oneSingerDetailFragment = new OneSingerDetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,oneSingerDetailFragment).commit();
        disableListview();
        toolbar.setTitle(music.getDisplayName());
        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_arrow_back_white_18dp,8);
    }
    public void toAblumDetail(Music music){
        isAblumDetail = true;
        target = music;
        oneApplication.setTargetMusic(music);
        oneAblumDetailFragment = new OneAblumDetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,oneAblumDetailFragment).commit();
        disableListview();
    }
    public Music getDetailTarget(){
        return target;
    }

    public void quitAblumDetail(){
        enableListview();
        isAblumDetail = false;
        getSupportFragmentManager().beginTransaction().remove(oneAblumDetailFragment).commit();
        toolbar.setTitle("OnePlayer");
//        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_menu_white_18dp,12);
//        Drawable drawable = new Drawable() {
//            Paint paint = new Paint();
//            @Override
//            public void draw(Canvas canvas) {
//                canvas.drawBitmap(bitmap,canvas.getWidth()/2-bitmap.getWidth()/2,canvas.getHeight()/2-bitmap.getHeight()/2,paint);
//            }
//
//            @Override
//            public void setAlpha(int alpha) {
//
//            }
//
//            @Override
//            public void setColorFilter(ColorFilter colorFilter) {
//
//            }
//
//            @Override
//            public int getOpacity() {
//                return 0;
//            }
//        };
//        toolbar.setNavigationIcon(drawable);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
        setSupportActionBar(toolbar);
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            Log.v("MainActivity","检查主题色"+themeColor);
            //window.setStatusBarColor(Color.TRANSPARENT);
            //window.setStatusBarColor(themeColor);
        }
    }
    private void quitSingerDetail(){
        Log.v("MainActivity","quitSingerDetail");
        enableListview();
        isSingerDetail = false;
        getSupportFragmentManager().beginTransaction().remove(oneSingerDetailFragment).commit();
        //toggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitle("OnePlayer");
        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_menu_white_18dp,12);
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
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        //if(oneSingerlistFragment==null) {
        //oneSingerlistFragment = new OneSingerListFragment();
        //}
        //oneFragments.set(0,oneSingerlistFragment);
        //fragmentPagerAdapter.notifyDataSetChanged();
        //fragmentStatePagerAdapter.notifyDataSetChanged();
    }



//    public String Mandarin2Pinyin(String src) {
//        char[] t1;
//        t1 = src.toCharArray();
//        String[] t2;
//        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
//        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//        String t4 = "";
//        int t0 = t1.length;
//        try {
//            for (int i = 0; i < t0; i++) {
//                //判断是否为汉字字符
//                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
//                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], outputFormat);
//                    t4 += t2[0];
//                } else
//                    t4 += Character.toString(t1[i]);
//            }
//            return t4;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return t4;
//    }

//    public void OrderChanged(int i) {
//        Log.v("MainActivity", "OrderChanged模式代码:" + i);
//        if (Build.VERSION.SDK_INT > 19) {
//            recyclerView = localmusicListFragment.getRecyclerView();
//            switch (i) {
//                case -1:
//                    Order = -1;
//                    localplaylistAdapter.setDatasource(tempmusicArrayList);
//                    if (recyclerView != null) {
//                        recyclerView.setAdapter(localplaylistAdapter);
//                    }
//                    localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                                albumcontainer.setVisibility(View.VISIBLE);
//                                viewPager.setVisibility(View.GONE);
//                                return;
//                            }
//                            isNew = false;
//                            albumcontainer.setVisibility(View.VISIBLE);
//                            viewPager.setVisibility(View.GONE);
//                            if (isPlaying) {
//                                musicService.pause();
//                                isPlaying = false;
//                            }
//                            playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                            tempmusicArrayList = localmusicArrayList;
//                            saveOrderMode(-1);
//                            musicNumber = tempmusicArrayList.size();
//                            currentPosition = position;
//                            currentMusic = tempmusicArrayList.get(position).getUrl();
//                            Log.v("MainActivity", "OrderChanged当前音乐" + currentMusic);
//                            notifiPositionChanged();
//                            playingFragment.playAnimation();
//                            updateMusic();
//                            basetag.setVisibility(View.GONE);
//                            isAlbuming = true;
//                            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                            isPlaying = true;
//                        }
//                    });
//                    break;
//                case -2:
//
//                    Order = -2;
//                    localplaylistAdapter.setDataSource(singers);
//                    localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            isSingerTwo = true;
//                            Log.v("MainActivity", "一级菜单点击" + position);
//                            saveFirstPosition(position);
//
//                            tempmusicArrayList = singerArrayLists.get(position);
//                            musicNumber = tempmusicArrayList.size();
//                            localplaylistAdapter.setDatasource(tempmusicArrayList);
//                            localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                                        albumcontainer.setVisibility(View.VISIBLE);
//                                        viewPager.setVisibility(View.GONE);
//                                        return;
//                                    }
//                                    saveOrderMode(-2);
//                                    Log.v("MainActivity", "二级菜单点击" + position);
//                                    isNew = false;
//                                    albumcontainer.setVisibility(View.VISIBLE);
//                                    viewPager.setVisibility(View.GONE);
//                                    if (isPlaying) {
//                                        musicService.pause();
//                                        isPlaying = false;
//                                    }
//                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                                    currentPosition = position;
//                                    currentMusic = tempmusicArrayList.get(position).getUrl();
//
//                                    localplaylistAdapter.url = currentMusic;
//                                    localplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
//                                    localplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
//                                    localplaylistAdapter.notifyDataSetChanged();
//                                    playingFragment.playAnimation();
//                                    updateMusic();
//                                    basetag.setVisibility(View.GONE);
//                                    isAlbuming = true;
//                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                                    isPlaying = true;
//                                }
//                            });
//                            Log.v("MainActivity", "二级菜单初始化完毕");
//                            if (recyclerView != null) {
//                                Log.v("MainActivity", "一级适配器设立");
//                                recyclerView.setAdapter(localplaylistAdapter);
//                            }
//                        }
//                    });
//                    if (recyclerView != null) {
//                        Log.v("MainActivity", "二级适配器设立");
//                        recyclerView.setAdapter(localplaylistAdapter);
//                    }
//                    break;
//                case -3:
//
//                    Order = -3;
//                    localplaylistAdapter.setDataSource(albums);
//                    localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            isAlbumTwo = true;
//                            tempmusicArrayList = albumArrayLists.get(position);
//                            musicNumber = tempmusicArrayList.size();
//                            localplaylistAdapter.setDatasource(tempmusicArrayList);
//                            saveFirstPosition(position);
//                            localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                                        albumcontainer.setVisibility(View.VISIBLE);
//                                        viewPager.setVisibility(View.GONE);
//                                        return;
//                                    }
//                                    saveOrderMode(-3);
//                                    isNew = false;
//                                    albumcontainer.setVisibility(View.VISIBLE);
//                                    viewPager.setVisibility(View.GONE);
//                                    if (isPlaying) {
//                                        musicService.pause();
//                                        isPlaying = false;
//                                    }
//                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                                    currentPosition = position;
//                                    currentMusic = tempmusicArrayList.get(position).getUrl();
//                                    localplaylistAdapter.url = currentMusic;
//                                    localplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
//                                    localplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
//                                    localplaylistAdapter.notifyDataSetChanged();
//                                    playingFragment.playAnimation();
//                                    updateMusic();
//                                    basetag.setVisibility(View.GONE);
//                                    isAlbuming = true;
//                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                                    isPlaying = true;
//                                }
//                            });
//                            if (recyclerView != null) {
//                                recyclerView.setAdapter(localplaylistAdapter);
//                            }
//                        }
//                    });
//                    if (recyclerView != null) {
//                        recyclerView.setAdapter(localplaylistAdapter);
//                    }
//                    break;
//            }
//        } else {
//            listView = localmusicListFragmentLow.getListView();
//            switch (i) {
//                case -1:
//
//                    Order = -1;
//
//                    localPlaylistAdapterlow.setDatasource(tempmusicArrayList);
//                    if (listView != null) {
//                        listView.setAdapter(localPlaylistAdapterlow);
//                    }
//                    localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            if (tempmusicArrayList.get(position).getUrl() == currentMusic && !isNew) {
//                                playingFragment.playAnimation();
//                                albumcontainer.setVisibility(View.VISIBLE);
//                                viewPager.setVisibility(View.GONE);
//                                basetag.setVisibility(View.GONE);
//                                ;
//                                isPlaying = true;
//                                return;
//                            }
//                            saveOrderMode(-1);
//                            isNew = false;
//                            albumcontainer.setVisibility(View.VISIBLE);
//                            viewPager.setVisibility(View.GONE);
//                            if (isPlaying) {
//                                musicService.pause();
//                                isPlaying = false;
//                            }
//                            playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                            tempmusicArrayList = localmusicArrayList;
//                            musicNumber = tempmusicArrayList.size();
//                            currentPosition = position;
//                            currentMusic = tempmusicArrayList.get(position).getUrl();
//                            notifiPositionChanged();
//                            playingFragment.playAnimation();
//                            updateMusic();
//                            basetag.setVisibility(View.GONE);
//                            isAlbuming = true;
//                            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                            isPlaying = true;
//                        }
//                    });
//                    break;
//                case -2:
//
//                    Order = -2;
//                    Log.v("MainActivity", "按歌手排序");
//                    localPlaylistAdapterlow.setDataSource(singers);
//                    localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            isSingerTwo = true;
//                            Log.v("MainActivity", "一级菜单点击" + position);
//                            tempmusicArrayList = singerArrayLists.get(position);
//                            musicNumber = tempmusicArrayList.size();
//                            localPlaylistAdapterlow.setDatasource(tempmusicArrayList);
//                            saveFirstPosition(position);
//                            localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                                        albumcontainer.setVisibility(View.VISIBLE);
//                                        viewPager.setVisibility(View.GONE);
//                                        return;
//                                    }
//                                    saveOrderMode(-2);
//                                    Log.v("MainActivity", "二级菜单点击" + position);
//                                    isNew = false;
//                                    albumcontainer.setVisibility(View.VISIBLE);
//                                    viewPager.setVisibility(View.GONE);
//                                    if (isPlaying) {
//                                        musicService.pause();
//                                        isPlaying = false;
//                                    }
//                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                                    currentPosition = position;
//                                    currentMusic = tempmusicArrayList.get(position).getUrl();
//                                    notifiPositionChanged();
//                                    playingFragment.playAnimation();
//                                    updateMusic();
//                                    basetag.setVisibility(View.GONE);
//                                    isAlbuming = true;
//                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                                    isPlaying = true;
//                                }
//                            });
//                            if (listView != null) {
//                                listView.setAdapter(localPlaylistAdapterlow);
//                            }
//                        }
//                    });
//                    if (listView != null) {
//                        listView.setAdapter(localPlaylistAdapterlow);
//                    }
//                    break;
//                case -3:
//
//                    Order = -3;
//                    localPlaylistAdapterlow.setDataSource(albums);
//                    localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            isAlbumTwo = true;
//                            tempmusicArrayList = albumArrayLists.get(position);
//                            musicNumber = tempmusicArrayList.size();
//                            localPlaylistAdapterlow.setDatasource(tempmusicArrayList);
//                            saveFirstPosition(position);
//                            localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
//                                        albumcontainer.setVisibility(View.VISIBLE);
//                                        viewPager.setVisibility(View.GONE);
//                                        return;
//                                    }
//                                    saveOrderMode(-3);
//                                    isNew = false;
//                                    albumcontainer.setVisibility(View.VISIBLE);
//                                    viewPager.setVisibility(View.GONE);
//                                    if (isPlaying) {
//                                        musicService.pause();
//                                        isPlaying = false;
//                                    }
//                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                                    currentPosition = position;
//                                    currentMusic = tempmusicArrayList.get(position).getUrl();
//                                    notifiPositionChanged();
//                                    playingFragment.playAnimation();
//                                    updateMusic();
//                                    basetag.setVisibility(View.GONE);
//                                    isAlbuming = true;
//                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
//                                    isPlaying = true;
//                                }
//                            });
//                            if (listView != null) {
//                                listView.setAdapter(localPlaylistAdapterlow);
//                            }
//                        }
//                    });
//                    if (listView != null) {
//                        listView.setAdapter(localPlaylistAdapterlow);
//                    }
//                    break;
//            }
//        }
//
//    }
//    private void setOrderMode(){
//        switch (Order) {
//            case -3:
//                tempmusicArrayList = songArraylist;
//                break;
//            case -1:
//                tempmusicArrayList = singerArraylist.ge;
//                break;
//            case -2:
//                tempmusicArrayList = getLastPlayedAlbum(albumArraylist,lastPlayedMusic.getAlbum());
//                break;
//        }
//    }
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
//            switch (currentFragmentPosition){
//                case 0:
//                    Order = -1;
//                    break;
//                case 1:
//                    Order = -2;
//                    break;
//                case 2:
//                    Order = -3;
//                    break;
//            }
//            setOrderMode();
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
