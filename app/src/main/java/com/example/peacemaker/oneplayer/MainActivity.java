package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    int transferNum = 0;
    private Music currentMusic;
    //private Music lastPlayedMusic;
    int musicNumber;
    public int currentPosition;
    int playMode = cycle;
    final static int cycle = 1;
    final static int looping = 2;
    final static int random = 3;
    private static int singer = 0;
    private static int album = 1;
    private static int song = 2;
    private int currentColor = Color.WHITE;
    private int cursorWidth;
    private int time;
    private int totalTime;
    Boolean isNull = false;
    private Timer timer;
    private TimerTask timerTask;
    private boolean isLogging = false;
    Handler handler;
    SharedPreferences sharedPreferences;
    private ArrayList<Music> tempmusicArrayList;
    OnePlayer onePlayer;
    MusicService musicService;
    SerchingFragment serchingfragment;
    FragmentTransaction fragmentTransaction;
    Handler serchhandler;
    Boolean isVersionOpen = false;
    Boolean isNew = true;
    Boolean isAlbuming = false;
    Boolean isNotiClikable = true;
    Boolean isNet = false;
    Boolean isFirstTime = true;
    private Bitmap currentBitmap;
    private int musicColor = Color.WHITE;
//    private int Order;
    int netmusicNumber = 1;
    Visualizer visualizer;
    MusicProvider musicProvider;
    BroadcastReceiver playReceiver;
    BroadcastReceiver previousReceiver;
    BroadcastReceiver nextReceiver;
    private Music target;
    private ArrayList<Music> albumArraylist;
    private ArrayList<Music> singerArraylist;
    private ArrayList<Music> songArraylist;
    @BindView(R.id.one_seekbar)
    OneSeekBar oneSeekBar;
    @BindView(R.id.play_view_previous)
    public ImageButton previousButton;
    @BindView(R.id.play_view_next)
    public ImageButton nextButton;
    @BindView(R.id.play_view_playmode)
    public ImageButton playModeButton;
    @BindView(R.id.play_view_queue)
    public ImageButton queueButton;
    public TextView updateText;
    @BindView(R.id.cursor)
    public ImageView cursor;
    @BindView(R.id.deepload)
    public RelativeLayout deepload;
    @BindView(R.id.version)
    public RelativeLayout version;
    @BindView(R.id.update)
    public RelativeLayout update;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.print_log)
    public RelativeLayout printLayout;
    @BindView(R.id.print_log_text)
    public TextView printLayoutTextView;
    @BindView(R.id.basetag)
    public RelativeLayout basetag;
    @BindView(R.id.play_view_duration)
    public TextView durationText;
    @BindView(R.id.play_view_progress)
    public TextView progressText;
    @BindView(R.id.versiontext)
    public TextView versionText;
    @BindView(R.id.song_tag)
    public TextView songTagText;
    @BindView(R.id.singer_tag)
    public TextView singerTagText;
    @BindView(R.id.album_tag)
    public TextView albumTagText;
    @BindView(R.id.bottom_song_name)
    public TextView bottomSongNameText;
    @BindView(R.id.bottom_singer_name)
    public TextView bottomSingerNameText;
    @BindView(R.id.bottom_album_image)
    public ImageView bottomAblumImageView;
    @BindView(R.id.bottom_controll_bar)
    public RelativeLayout bottomControllBar;
    @BindView(R.id.top_tool_bar_song_name)
    public TextView topToolBarSongNameText;
    @BindView(R.id.top_tool_bar_singer_name)
    public TextView topToolBarSingerNameText;
    @BindView(R.id.top_tool_bar)
    public RelativeLayout topToolBar;
    @BindView(R.id.play_view_album)
    public ImageView playViewAblumImageView;
    @BindView(R.id.play_view_controll_bar)
    public RelativeLayout playViewControllBar;
    @BindView(R.id.bottom_play_button)
    public ImageButton playButton;
    @BindView(R.id.main_content_bar)
    public OneLayout mainContentBar;
    @BindView(R.id.id_viewpager)
    public ViewPager viewPager;
    @BindView(R.id.play_view_onewaveform)
    public OneWaveFromView oneWaveFromView;
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
    private int currentFragmentPosition = singer;
    public int albumSelectedPosition;
    public int singerSelectedPosition;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            musicService = ((MusicService.MusicBinder) service).getMusicService();
            System.out.println("接受音乐服务" + musicService);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    DatabaseOperator databaseOperator;
    Boolean isPlaying = false;
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
        setContentView(R.layout.main_activity);
//        } else {
//            setContentView(R.layout.main_activity_low);
//        }
        initialize();
        checkUpdate();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        Bundle bundle = getIntent().getExtras();
//        lastPlayedMusic = (Music) bundle.get("lastPlayedMusic");
//        currentMusic = lastPlayedMusic;
//        currentPosition = (int) bundle.get("currentPosition");
//        Order = (int) bundle.get("OrderMode");
        ArrayList<Music> musicArrayList = (ArrayList<Music>) bundle.get("musicArrayList");
        //musicProvider = (MusicProvider) bundle.get("musicProvider");
        musicProvider = new MusicProvider(musicArrayList);
        songArraylist = musicProvider.getSongs();
        singerArraylist = musicProvider.getSingers();
        albumArraylist = musicProvider.getAlbums();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return false;
//            }
//        });
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int color = typedValue.data;
        // 注意setStatusBarBackgroundColor方法需要你将fitsSystemWindows设置为true才会生效
        //将ActionBar和drawer绑定
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
//        toggle.syncState();
//        drawerLayout.setDrawerListener(toggle);

        //playlistView = (ListView)findViewById(R.id.playList);
        //seekBar = (SeekBar) findViewById(R.id.seekBar);
        ButterKnife.bind(this);
        drawerLayout.setStatusBarBackgroundColor(color);
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
        databaseOperator = new DatabaseOperator(this, "OnePlayer.db");

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
        oneSingerDetailFragment = new OneSingerDetailFragment();
        oneAblumDetailFragment = new OneAblumDetailFragment();
        oneFragments.add(oneSingerlistFragment);
        oneFragments.add(oneAblumListFragment);
        oneFragments.add(onePlayListFragment);
        oneFragments.add(oneSingerDetailFragment);
        oneFragments.add(oneAblumDetailFragment);
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
                    if(isSingerDetail){
                        if(position==0){
                            position=3;
                        }
                    }
                    if(isAblumDetail){
                        if(position==1){
                            position=4;
                        }
                    }
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
                if(isAblumDetail){
                    if(object instanceof OneAblumListFragment){
                        //Log.v("MainActivity","进入专辑细节");
                        return POSITION_NONE;
                    }
                }else {
                    if (object instanceof OneAblumDetailFragment) {
                        Log.v("MainActivity","退出专辑细节");
                        return POSITION_NONE;
                    }
                }
                if (isSingerDetail){
                    if(object instanceof OneSingerListFragment){
                        Log.v("MainActivity","进入歌手细节");
                        return POSITION_NONE;
                    }
                }else {
                    if(object instanceof OneSingerDetailFragment){
                        Log.v("MainActivity","退出歌手细节");
                        return POSITION_NONE;

                    }
                }
                return POSITION_UNCHANGED;

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                //Log.v("MainActivity","实例化item"+position);
                return super.instantiateItem(container, position);
            }
        };
        DisplayMetrics dm = getResources().getDisplayMetrics();
        cursorWidth = dm.widthPixels / 3;
        ViewGroup.LayoutParams layoutParams = cursor.getLayoutParams();
        layoutParams.width = cursorWidth;
        cursor.setLayoutParams(layoutParams);
        Log.v("MainActivity","游标宽度"+cursorWidth);
        viewPager.setAdapter(fragmentStatePagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == singer) {
                    moveCursor(currentFragmentPosition*cursorWidth, 0);
                    singerTagText.setTextColor(Color.WHITE);
                    albumTagText.setTextColor(Color.BLACK);
                    songTagText.setTextColor(Color.BLACK);
                    currentFragmentPosition = singer;
                } else if(position==album){
                    moveCursor(currentFragmentPosition*cursorWidth, cursorWidth);
                    albumTagText.setTextColor(Color.WHITE);
                    singerTagText.setTextColor(Color.BLACK);
                    songTagText.setTextColor(Color.BLACK);
                    currentFragmentPosition = album;
                } else if(position==song){
                    moveCursor(currentFragmentPosition*cursorWidth, 2*cursorWidth);
                    songTagText.setTextColor(Color.WHITE);
                    singerTagText.setTextColor(Color.BLACK);
                    albumTagText.setTextColor(Color.BLACK);
                    currentFragmentPosition = song;
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.v("页面滚动状态改变", state + "");
            }
        });
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

        deepload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false)
                        //.setIcon(R.mipmap.ic_launcher)
                        .setTitle("深度扫描")
                        .setMessage("是否要执行深度扫描？")
                        .setPositiveButton("好！", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                v.setClickable(false);
                                //activity.drawerLayout.closeDrawer(Gravity.LEFT);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                serchhandler = new Handler(new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        if (msg.what == 0x128) {
                                            Log.v("MainActivity","handleMessage()0x128");
                                            v.setClickable(true);
                                            musicProvider = msg.getData().getParcelable("provider");
                                            songArraylist = musicProvider.getSongs();
                                            singerArraylist = musicProvider.getSingers();
                                            albumArraylist = musicProvider.getAlbums();
                                            tempmusicArrayList = songArraylist;
//                                            if (databaseOperator == null) {
//                                                databaseOperator = new DatabaseOperator(MainActivity.this, "OnePlayer.db");
//                                            }
//                                            databaseOperator.saveMusics(tempmusicArrayList);
                                            if (isNull && tempmusicArrayList.size() != 0) {
                                                isNull = false;
                                                initialize();
                                            } else {
                                                Toast.makeText(MainActivity.this, "扫描完毕", Toast.LENGTH_SHORT).show();
                                                //localplaylistAdapter.setDatasource(localmusicArrayList);
                                                //localmusicListFragment.getRecyclerView().setAdapter(localplaylistAdapter);
                                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.remove(serchingfragment);
                                                fragmentTransaction.commit();
                                            }
                                        }
                                        return false;
                                    }
                                });
                                serchingfragment = new SerchingFragment();
                                serchingfragment.init(serchhandler);
                                fragmentTransaction.add(R.id.container, serchingfragment);
                                fragmentTransaction.commit();
                                //测试深度扫描
                                System.out.println("扫描完毕更新view");

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
        });
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVersionOpen) {
                    versionText.setVisibility(View.VISIBLE);
                    versionText.setText("当前系统版本为:" + getAppVersionName(MainActivity.this));
                    isVersionOpen = true;
                } else {
                    versionText.setVisibility(View.GONE);
                    isVersionOpen = false;
                }
            }
        });
        final OneLogger oneLogger = new OneLogger();
        printLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(printLayoutTextView.getText().toString().equals("打印日志")) {
                    oneLogger.getLog();
                    printLayoutTextView.setText("正在打印");
                }else {
                    oneLogger.stopLogging();
                    printLayoutTextView.setText("打印日志");
                }
            }
        });
        update.setOnClickListener(this);
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
        singerTagText.setOnClickListener(this);
        albumTagText.setOnClickListener(this);
        songTagText.setOnClickListener(this);
        bottomControllBar.setOnClickListener(this);
        playButton.setOnClickListener(this);
        //mainContentBar.setOnClickListener(this);
        mainContentBar.setBottom_controll_bar((RelativeLayout) mainContentBar.getChildAt(0));
        mainContentBar.setOnReachMaxListener(new OnReachMaxListener() {
            @Override
            public void onReachMax() {
                disableListview();
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
        visualizer = new Visualizer(onePlayer.mediaPlayer.getAudioSessionId());
        visualizer.setCaptureSize(64);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                //Log.v("MainActivity", "initialize()捕获到波数据" + waveform[0] + waveform[1]);

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                //Log.v("MainActivity", "onFftDataCapture()捕获到fft数据" + fft[0] + fft[1]);
                //playingFragment.setWaveData(fft);
                setWaveData(fft);
            }
        }, Visualizer.getMaxCaptureRate()/2, false, true);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int stopPosition;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                stopPosition = progress;
//                progressText.setText(ten2sixty(progress));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                isPlaying = false;
//                playButton.setClickable(false);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                playButton.setClickable(true);
//                if (!isNew) {
//                    System.out.println("直接seekto");
//                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
//                    musicService.seekTo(stopPosition * 1000);
//                    isPlaying = false;
//                    play();
//                } else {
//                    seekBar.setProgress(0);
//                    progressText.setText(ten2sixty(0));
//                }
//                time = stopPosition;
//            }
//        });

        playMode = cycle;


    }

    public void setWaveData(byte[] data){
        oneWaveFromView.setData(data);

    }
    public void setTempmusicArrayList(ArrayList<Music> musicArrayList){
        tempmusicArrayList = musicArrayList;
        musicNumber = musicArrayList.size();
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


    private void play() {
        //开始播放
        if (!isPlaying) {
            //playingFragment.playAnimation();
            visualizer.setEnabled(true);
            if (isNew) {
                //albumcontainer.setVisibility(View.VISIBLE);
                //viewPager.setVisibility(View.GONE);
                //basetag.setVisibility(View.GONE);
                //isAlbuming = true;
                //queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                //notifiPositionChanged();
                //System.out.println("初次启动");
                //sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
                //saveLastPlayed();
                if (handler == null) {
                    initHandler();
                }
                isPlaying = true;
                Log.v("MainActivity","初始化开始播放");
                oneSeekBar.setButtonBitmap(true);
                if(currentColor==Color.WHITE) {
                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                    initNotification(false,null);
                }else {
                    playButton.setImageResource(R.drawable.pause);
                    initNotification(false,null);
                }

                musicService.setOnePlayer(onePlayer);
                musicService.startService();
                isNew = false;
            } else {
                musicService.play();
                isPlaying = true;
                oneSeekBar.setButtonBitmap(true);
                Log.v("MainActivity","请开始播放");
                if(currentColor==Color.WHITE) {
                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                    initNotification(false,null);
                }else {
                    playButton.setImageResource(R.drawable.pause);
                    initNotification(false,null);
                }

            }
        } else {
            if (visualizer != null) {
                visualizer.setEnabled(false);
            }
            //playingFragment.stopAnimation();
            //暂停播放
            System.out.println("暂停播放");
            if(currentColor==Color.WHITE) {
                playButton.setImageResource(R.drawable.ic_play_circle_fill_white_48dp);
                initNotification(true,null);
            }else {
                playButton.setImageResource(R.drawable.play);
                initNotification(true,null);
            }

            oneSeekBar.setButtonBitmap(false);
            isPlaying = false;
            musicService.pause();
        }

    }

    public void next() {
        stopButtons();
        isNew = false;
        //playingFragment.playAnimation();
        if (isPlaying) {
            musicService.pause();
            isPlaying = false;
        }
        //if (!isNet) {
            switch (playMode) {
                case random:
                    currentPosition = onePlayer.getRandomPosition(musicNumber);
                    break;

                default:
                    Log.v("MainActivity", "当前位置" + currentPosition + " " + musicNumber);
                    currentPosition = (currentPosition + 1) % musicNumber;
                    Log.v("MainActivity", "当前位置" + currentPosition);
                    break;
            }
            currentMusic = tempmusicArrayList.get(currentPosition);
            onePlayListFragment.setSelectedPosition(songArraylist.indexOf(currentMusic));
//        } else {
//            switch (playMode) {
//                case random:
//                    netcurrentPositon = onePlayer.getRandomPosition(netmusicNumber);
//                    break;
//                default:
//                    netcurrentPositon = (netcurrentPositon + 1) % netmusicNumber;
//                    break;
//            }
//            currentMusic = netmusicArrayList.get(currentPosition).getUrl();
//        }
        notifiPositionChanged();
        updateMusic();
    }

    public void previous() {
        stopButtons();
        isNew = false;
        //playingFragment.playAnimation();
        if (isPlaying) {
            musicService.pause();
            isPlaying = false;
        }
//        if (!isNet) {
            switch (playMode) {
                case random:
                    currentPosition = onePlayer.getRandomPosition(musicNumber);
                    break;
                default:
                    currentPosition = currentPosition - 1;
                    if (currentPosition < 0) {
                        currentPosition += musicNumber;
                    }
                    break;
            }
            //onePlayListFragment.setSelectedPosition(currentPosition);
            currentMusic = tempmusicArrayList.get(currentPosition);
            onePlayListFragment.setSelectedPosition(songArraylist.indexOf(currentMusic));
//        } else {
//            switch (playMode) {
//                case random:
//                    netcurrentPositon = onePlayer.getRandomPosition(netmusicNumber);
//                    break;
//                default:
//                    netcurrentPositon = (netcurrentPositon - 1) % netmusicNumber;
//                    break;
//            }
//            currentMusic = netmusicArrayList.get(currentPosition).getUrl();
//        }
        notifiPositionChanged();
        updateMusic();

    }
    private void toPlayView(){
        Log.v("MainActivity","toPlayView()");
        mainContentBar.toMaxHeight();
        //disableListview();
        isPlayView = true;
    }
    private void quitPlayView(){
        Log.v("MainActivity","quitPlayView()");
        enableListview();
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
        albumTagText.setVisibility(View.GONE);
        singerTagText.setVisibility(View.GONE);
        songTagText.setVisibility(View.GONE);
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
        albumTagText.setVisibility(View.VISIBLE);
        singerTagText.setVisibility(View.VISIBLE);
        songTagText.setVisibility(View.VISIBLE);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            //builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("退出Oneplayer");
            builder.setMessage("确定要退出OnePlayer吗？(希望音乐在后台播放直接点击home键即可)");
            builder.setPositiveButton("嗯，退出", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    closeNotification();
                    finish();
                    //onDestroy();
                }
            });
            builder.setNegativeButton("还不退呢", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            builder.create().show();
//            }
//        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //initNotification(R.drawable.ic_pause_circle_outline_black_48dp,null);
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

    private void initNotification(boolean isPlayState,Bitmap bitmap) {
        if (tempmusicArrayList == null||tempmusicArrayList.size() == 0) {
            return;
        }
        //Bitmap bitmap = getCurrentBitmap();
        //System.out.println("初始化通知栏");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
//        if (!isNet) {
        remoteViews.setTextViewText(R.id.noti_singer,currentMusic.getArtist());
        remoteViews.setTextViewText(R.id.noti_name, currentMusic.getDisplayName());
        remoteViews.setInt(R.id.noti_singer,"setTextColor",currentColor);
        remoteViews.setInt(R.id.noti_name,"setTextColor",currentColor);
        remoteViews.setInt(R.id.noti_background,"setBackgroundColor",musicColor);
        if(bitmap!=null) {
            remoteViews.setImageViewBitmap(R.id.noti_album_image, bitmap);
        }
//        } else {
//            remoteViews.setTextViewText(R.id.noti_singer, netmusicArrayList.get(netcurrentPositon).getArtist());
//            remoteViews.setTextViewText(R.id.noti_name, netmusicArrayList.get(netcurrentPositon).getDisplayName());
//        }

//        if (isPlayState) {
//            remoteViews.setImageViewResource(R.id.noti_playandpause, resId);
//        }
        if(currentColor==Color.WHITE){
            remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.ic_skip_previous_white_48dp);
            remoteViews.setImageViewResource(R.id.noti_next, R.drawable.ic_skip_next_white_48dp);
            if (isPlayState) {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_play_circle_fill_white_48dp);
            }else {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_pause_circle_fill_white_48dp);
            }
        }else {
            remoteViews.setImageViewResource(R.id.noti_previous, R.drawable.previous);
            remoteViews.setImageViewResource(R.id.noti_next, R.drawable.next);
            if (isPlayState) {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_play_circle_outline_black_48dp);
            }else {
                remoteViews.setImageViewResource(R.id.noti_playandpause, R.drawable.ic_pause_circle_outline_black_48dp);
            }
        }
        //播放键点击事件
        Intent intent1 = new Intent("playButton");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_playandpause, pendingIntent1);
        //下一首点击事件
        Intent intent2 = new Intent("nextButton");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, pendingIntent2);
        //上一首点击事件
        Intent intent3 = new Intent("previousButton");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, 0, intent3, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_previous, pendingIntent3);

        //remoteViews.setProgressBar(R.id.noti_seekBar, Integer.parseInt(musicArrayList.get(currentPosition).getDuration()) / 1000, 0, false);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("title")
                .setContentText("describe")
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.start_small)
                .setOngoing(true)
                .build();
        notification.priority = Notification.PRIORITY_HIGH;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(0, notification);
        //System.out.println("初始化通知栏完毕");


    }

    public void closeNotification() {
        System.out.println("调用关闭通知");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

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
//        Bitmap albumBitmap = currentMusic.getAlbumBitmap(this);
//        Bitmap smallBitmap = currentMusic.getSmallAlbumArt(albumBitmap,this);
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

    public void saveFirstPosition(int Position) {
        databaseOperator = new DatabaseOperator(this, "OnePlayer.db");
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
        }
        databaseOperator.saveFirstPosition(sharedPreferences, Position);
        databaseOperator = null;
    }

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

    protected void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //处理开始走时信息
                if (msg.what == 1) {
                    //Log.v("MainActivity","handler查看进度"+time+","+totalTime);
                    progressText.setText(ten2sixty(time));
                    float progress = time * 1.0f / totalTime;
//                    ViewGroup.LayoutParams layoutParams = bottomProgress.getLayoutParams();
//                    layoutParams.width = (int)progress*3*cursorWidth;
//                    bottomProgress.setLayoutParams(layoutParams);
//                    bottomProgress.postInvalidate();
                    //seekBar.setProgress(time);
                    oneSeekBar.setProgress(time * 1.0f / totalTime);

                }
                return false;
            }
        }) {

        };
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
                singerTagText.setTextColor(Color.parseColor("#FFFFFF"));
                albumTagText.setTextColor(Color.parseColor("#000000"));
                songTagText.setTextColor(Color.parseColor("#000000"));
                moveCursor(currentFragmentPosition*cursorWidth,singer*cursorWidth);
                currentFragmentPosition = singer;
                break;
            case 1:
                viewPager.setCurrentItem(album);
                singerTagText.setTextColor(Color.parseColor("#000000"));
                albumTagText.setTextColor(Color.parseColor("#FFFFFF"));
                songTagText.setTextColor(Color.parseColor("#000000"));
                moveCursor(currentFragmentPosition*cursorWidth,album*cursorWidth);
                currentFragmentPosition = album;
                break;
            case 2:
                viewPager.setCurrentItem(song);
                singerTagText.setTextColor(Color.parseColor("#000000"));
                albumTagText.setTextColor(Color.parseColor("#000000"));
                songTagText.setTextColor(Color.parseColor("#FFFFFF"));
                moveCursor(currentFragmentPosition*cursorWidth,song*cursorWidth);
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
            case R.id.update:
                checkUpdate();
                break;
            case R.id.play_view_queue:
                queue();
                break;
            case R.id.play_view_playmode:
                playMode();
                break;
            case R.id.singer_tag:
                selectPage(0);
                break;
            case R.id.album_tag:
                selectPage(1);
                break;
            case R.id.song_tag:
                selectPage(2);
                break;
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

    private void moveCursor(int start, int end) {
        //Log.v("MainActivity","游标移动从"+start+"到"+end);
        TranslateAnimation animation = new TranslateAnimation(start, end, 0, 0);
        animation.setFillAfter(true);// True:停留在停止位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
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
        isSingerDetail = true;
        getSupportFragmentManager().beginTransaction().remove(oneSingerlistFragment).commit();
        //if(oneSingerDetailFragment==null){
        oneSingerDetailFragment = new OneSingerDetailFragment();
        //}
        target = music;
        //fragmentPagerAdapter.notifyDataSetChanged();
        fragmentStatePagerAdapter.notifyDataSetChanged();
    }
    public void toAblumDetail(Music music){
        isAblumDetail = true;
        getSupportFragmentManager().beginTransaction().remove(oneAblumListFragment).commit();
        //if(oneAblumDetailFragment==null){
        //oneAblumDetailFragment = new OneAblumDetailFragment();
        //}
        target = music;
        //fragmentPagerAdapter.notifyDataSetChanged();
        fragmentStatePagerAdapter.notifyDataSetChanged();
    }
    public Music getDetailTarget(){
        return target;
    }

    private void quitAblumDetail(){
        isAblumDetail = false;
        getSupportFragmentManager().beginTransaction().remove(oneAblumDetailFragment).commit();
        //if(oneAblumListFragment==null) {
        oneAblumListFragment = new OneAblumListFragment();
        //}
        //oneFragments.set(1,oneAblumListFragment);
        //fragmentPagerAdapter.notifyDataSetChanged();
        fragmentStatePagerAdapter.notifyDataSetChanged();
    }
    private void quitSingerDetail(){
        Log.v("MainActivity","quitSingerDetail");
        isSingerDetail = false;
        getSupportFragmentManager().beginTransaction().remove(oneSingerDetailFragment).commit();
        //if(oneSingerlistFragment==null) {
        oneSingerlistFragment = new OneSingerListFragment();
        //}
        //oneFragments.set(0,oneSingerlistFragment);
        //fragmentPagerAdapter.notifyDataSetChanged();
        fragmentStatePagerAdapter.notifyDataSetChanged();
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
        //Log.v("MainActivity","itemSelected选择位置"+music.getUrl());
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
