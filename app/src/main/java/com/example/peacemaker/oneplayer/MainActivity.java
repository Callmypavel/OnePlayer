package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
//import android.support.v4.widget.DrawerLayout;

import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ouyan_000 on 2015/8/14.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    int transferNum = 0;
    String currentMusic;
    String lastPlayedMusic;
    int musicNumber;
    public int currentPosition;
    int playMode = cycle;
    final static int cycle = 1;
    final static int looping = 2;
    final static int random = 3;
    int time;
    int totalTime;
    int FirstPositon;
    int netcurrentPositon;
    CardView cardView;
    Boolean isNull = false;
    private Timer timer;
    private TimerTask timerTask;
    RecyclerView recyclerView;
    ListView listView;
    Handler handler;
    SharedPreferences sharedPreferences;
    ArrayList<Music> localmusicArrayList;
    ArrayList<Music> netmusicArrayList;
    ArrayList<String> singers ;
    ArrayList<ArrayList<Music>> singerArrayLists;
    ArrayList<String> albums ;
    ArrayList<ArrayList<Music>> albumArrayLists;
    ArrayList<Music> tempmusicArrayList ;
    ArrayList<Music> tempmusicArrayList2 ;
    DrawerLayout drawerLayout;
    PlaylistAdapter localplaylistAdapter;
    PlaylistAdapter netplaylistAdapter;
    PlaylistAdapterlow localPlaylistAdapterlow;
    PlaylistAdapterlow netPlaylistAdapterlow;
    RelativeLayout basetag;
    MusicService musicService;
    TextView durationText;
    TextView progressText;
    TextView versionText;
    TextView local;
    TextView internet;
    OnePlayer onePlayer;
    TextView updateText;
    ImageButton previousButton;
    ImageButton playButton;
    ImageButton nextButton;
    ImageButton playModeButton;
    ImageButton queueButton;
    ImageView cursor;
    RelativeLayout deepload;
    RelativeLayout version;
    RelativeLayout update;
    RelativeLayout albumcontainer;
    SeekBar seekBar;
    ViewPager viewPager;
    SerchingFragment serchingfragment;
    PlayingFragment playingFragment;
    FragmentTransaction fragmentTransaction;
    Handler serchhandler;
    Boolean isVersionOpen = false;
    Boolean isNew = true;
    Boolean isAlbuming = false;
    ProgressBar progressBar;
    Boolean isNotiClikable = true;
    Boolean isNet = false;
    Boolean isFirstTime = true;
    Boolean isAlbumTwo = false;
    Boolean isSingerTwo = false;
    String initSinger;
    String initSong;
    int Order;
    int netmusicNumber = 1;
    OnIntializeCompleListener onIntializeCompleListener;
    BroadcastReceiver playReceiver;
    BroadcastReceiver previousReceiver;
    BroadcastReceiver nextReceiver;
    CircleImageView circleImageView;
    MusicListFragment localmusicListFragment ;
    MusicListFragment netmusicListFragment ;
    MusicListFragmentLow localmusicListFragmentLow;
    MusicListFragmentLow netmusicListFragmentLow;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            musicService = ((MusicService.MusicBinder)service).getMusicService();
            System.out.println("接受音乐服务" + musicService);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    DatabaseOperator databaseOperator;
    Boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>19){
            setTheme(R.style.Oneplayer);
            Log.v("MainActivity","我选择OnePlayer主题");
        }else{
            setTheme(R.style.One);
            Log.v("MainActivity", "我选择One主题");
        }

        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>19){
            setContentView(R.layout.main_activity);
        }else{
            setContentView(R.layout.main_activity_low);
        }
        initialize();
        checkUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNotification(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("摧毁","activity");
        unregisterReceiver(playReceiver);
        unregisterReceiver(nextReceiver);
        unregisterReceiver(previousReceiver);
        unbindService(serviceConnection);
    }

    public void initialize(){
        //从intent获取数据
        final Bundle bundle = getIntent().getExtras();
        localmusicArrayList = (ArrayList<Music>)bundle.get("musicArraylist");
        lastPlayedMusic = (String)bundle.get("lastPlayedMusic");
        Log.v("MainActivity", "intialize检查一番" + lastPlayedMusic);
        currentMusic = lastPlayedMusic;
        currentPosition = (int)bundle.get("currentPosition");
        Log.v("MainActivity", "intialize检查位置" + currentPosition);
        FirstPositon = (int)bundle.get("FirstPosition");
        singerArrayLists = (ArrayList<ArrayList<Music>>)bundle.get("singerArrayLists");
        albumArrayLists = (ArrayList<ArrayList<Music>>)bundle.get("albumArrayLists");
        singers = (ArrayList<String>) bundle.get("singers");
        albums = (ArrayList<String>) bundle.get("albums");
        Order = (int)bundle.get("OrderMode");
        switch (Order){
            case -1:tempmusicArrayList = localmusicArrayList;break;
            case -2:tempmusicArrayList = singerArrayLists.get(FirstPositon);break;
            case -3:tempmusicArrayList = albumArrayLists.get(FirstPositon);break;
        }
        musicNumber = tempmusicArrayList.size();
        Log.v("MainActivity", "intialize" + bundle.getInt("OrderMode"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                return false;
//            }
//        });
        setSupportActionBar(toolbar);



        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int color = typedValue.data;
        // 注意setStatusBarBackgroundColor方法需要你将fitsSystemWindows设置为true才会生效
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(color);
        //将ActionBar和drawer绑定
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
//        toggle.syncState();
//        drawerLayout.setDrawerListener(toggle);

        previousButton = (ImageButton)findViewById(R.id.previous);

        playButton = (ImageButton)findViewById(R.id.playandpause);

        queueButton = (ImageButton)findViewById(R.id.queue);
        playModeButton = (ImageButton)findViewById(R.id.playmode);
        nextButton = (ImageButton)findViewById(R.id.next);
        playModeButton = (ImageButton)findViewById(R.id.playmode);
        //playlistView = (ListView)findViewById(R.id.playList);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        versionText = (TextView)findViewById(R.id.versiontext);
        durationText = (TextView)findViewById(R.id.duration);
        progressText = (TextView)findViewById(R.id.progress);
        local = (TextView)findViewById(R.id.local);
        internet = (TextView)findViewById(R.id.internet);
//        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                int size = getResources().getDimensionPixelSize(R.dimen.shape_size);
//                outline.setOval(0,0,size,size);
//            }
//        };
        albumcontainer = (RelativeLayout)findViewById(R.id.albumcontainer);
        deepload = (RelativeLayout)findViewById(R.id.deepload);
        version = (RelativeLayout)findViewById(R.id.version);
        update = (RelativeLayout)findViewById(R.id.update);
        basetag = (RelativeLayout)findViewById(R.id.basetag);
        cursor = (ImageView)findViewById(R.id.cursor);
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
        databaseOperator = new DatabaseOperator(this,"OnePlayer.db");

        //5.0适用
        if(Build.VERSION.SDK_INT>19) {
            Log.v("MainActivity","版本号"+Build.VERSION.SDK_INT);
            localplaylistAdapter = new PlaylistAdapter(localmusicArrayList, getLayoutInflater());
            netplaylistAdapter = new PlaylistAdapter(null, getLayoutInflater());
            localmusicListFragment = new LocalMusicFragment();
            localmusicListFragment.setOnOrderClickListener(new OnOrderClickListener() {
                @Override
                public void onOrderClick(int order) {
                    OrderChanged(order);
                }
            });
            netmusicListFragment = new NetMusicFragment();
            localmusicListFragment.setOnIntializeCompleListener(new OnIntializeCompleListener() {
                @Override
                public void onInitComple() {
                    OrderChanged(Order);
                }
            });
            netplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    isNet = true;
                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                        albumcontainer.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.GONE);
                        return;
                    }
                    isNew = false;
                    albumcontainer.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    if (isPlaying) {
                        musicService.pause();
                        isPlaying = false;
                    }
                    netcurrentPositon = position;
                    Log.v("这个positon是我钦点的", position + "");
                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                    currentMusic = netmusicArrayList.get(position).getUrl();
                    netplaylistAdapter.url = currentMusic;
                    netplaylistAdapter.notifyDataSetChanged();
                    playingFragment.playAnimation();
                    updateMusic();
                    basetag.setVisibility(View.GONE);
                    isAlbuming = true;
                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                    isPlaying = true;
                }
            });

        }else {
            Log.v("MainActivity","版本号"+Build.VERSION.SDK_INT);
            localPlaylistAdapterlow = new PlaylistAdapterlow(localmusicArrayList, getLayoutInflater());
            netPlaylistAdapterlow = new PlaylistAdapterlow(null, getLayoutInflater());
            localmusicListFragmentLow = new LocalMusicFragmentLow();
            netmusicListFragmentLow = new NetMusicFragmentLow();
            localmusicListFragmentLow.setOnOrderClickListener(new OnOrderClickListener() {
                @Override
                public void onOrderClick(int order) {
                    OrderChanged(order);
                }
            });
            localmusicListFragmentLow.setOnIntializeCompleListener(new OnIntializeCompleListener() {
                @Override
                public void onInitComple() {
                    OrderChanged(Order);
                }
            });
            netmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    isNet = true;
                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                        albumcontainer.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.GONE);
                        return;
                    }
                    isNew = false;
                    albumcontainer.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    if (isPlaying) {
                        musicService.pause();
                        isPlaying = false;
                    }
                    currentPosition = position;
                    Log.v("这个positon是我钦点的", position + "");
                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                    currentMusic = netmusicArrayList.get(position).getUrl();
                    netPlaylistAdapterlow.url = currentMusic;
                    netPlaylistAdapterlow.notifyDataSetChanged();
                    playingFragment.playAnimation();
                    updateMusic();
                    basetag.setVisibility(View.GONE);
                    isAlbuming = true;
                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                    isPlaying = true;
                }
            });
        }
        playingFragment = new PlayingFragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.albumcontainer, playingFragment);
        fragmentTransaction.commit();
        //musicListFragment.init(this);
        viewPager = (ViewPager)findViewById(R.id.id_viewpager);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                if(Build.VERSION.SDK_INT>19) {
                    if (position == 0) {
                        return localmusicListFragment;
                    } else if(position == 1) {
                        return netmusicListFragment;
                    }
                }else{
                    if (position == 0) {
                        return localmusicListFragmentLow;
                    } else return netmusicListFragmentLow;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    moveCursor(0, local.getWidth());
                    local.setTextColor(Color.parseColor("#000000"));
                    internet.setTextColor(Color.parseColor("#FFFFFF"));
                }else{
                    moveCursor(local.getWidth(), 0);
                    local.setTextColor(Color.parseColor("#FFFFFF"));
                    internet.setTextColor(Color.parseColor("#000000"));
                    isNet = false;
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v("页面滚动状态改变", state + "");
            }
        });
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
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                serchhandler = new Handler(new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        System.out.println("第一次接到 手斜");
                                        if (msg.what == 0x127) {
                                            System.out.println("接到 手斜");
                                            v.setClickable(true);
                                            localmusicArrayList = msg.getData().getParcelableArrayList("music");
                                            Comparator<Music> comparator = new Comparator<Music>() {
                                                @Override
                                                public int compare(Music lhs, Music rhs) {
                                                    return  Mandarin2Pinyin(lhs.getDisplayName()).toLowerCase().compareTo(Mandarin2Pinyin(rhs.getDisplayName()).toLowerCase());
                                                }
                                            };
                                            Collections.sort(localmusicArrayList, comparator);
                                            if (databaseOperator == null) {
                                                databaseOperator = new DatabaseOperator(MainActivity.this, "OnePlayer.db");
                                            }
                                            databaseOperator.saveMusics(localmusicArrayList);
                                            if (isNull&&localmusicArrayList.size()!=0) {
                                                isNull = false;
                                                initialize();
                                            } else {
                                                Toast.makeText(MainActivity.this, "扫描完毕", Toast.LENGTH_SHORT).show();
                                                localplaylistAdapter.setDatasource(localmusicArrayList);
                                                localmusicListFragment.getRecyclerView().setAdapter(localplaylistAdapter);
                                                fragmentTransaction = getFragmentManager().beginTransaction();
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
        update.setOnClickListener(this);
        System.out.println("扫描后的数目" + localmusicArrayList.size());
        if(localmusicArrayList.size()==0){
            System.out.println("获得为空");
            Toast.makeText(this, "抱歉，没有歌曲", Toast.LENGTH_SHORT).show();
            isNull = true;
            playButton.setClickable(false);
            nextButton.setClickable(false);
            previousButton.setClickable(false);
            playModeButton.setClickable(false);
            System.out.println("朱军 地球太危险 老子先跑了");
            return;
        }
        //设置监听事件

        previousButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playModeButton.setOnClickListener(this);
        queueButton.setOnClickListener(this);
        local.setOnClickListener(this);
        internet.setOnClickListener(this);
        //取得最后一次播放的音乐
        initHandler();
        initService();
        initNotification(0);
        initReceiver();
        updateToolbar();
        updateSeekbar();
        initOnePlayer(currentMusic);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int stopPosition;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stopPosition = progress;
                progressText.setText(ten2sixty(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                playButton.setClickable(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playButton.setClickable(true);
                if (!isNew) {
                    System.out.println("直接seekto");
                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                    musicService.seekTo(stopPosition * 1000);
                    isPlaying = false;
                    play();
                } else {
                    seekBar.setProgress(0);
                    progressText.setText(ten2sixty(0));
                }
                time = stopPosition;
            }
        });

        playMode = cycle;


    }



    public void play(){
        //开始播放
        if(!isPlaying) {
            playingFragment.playAnimation();
            if(isNew) {
                albumcontainer.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                basetag.setVisibility(View.GONE);
                isAlbuming = true;
                queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                notifiPositionChanged();

                System.out.println("初次启动");
                sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
                saveLastPlayed();
                if (handler == null) {
                    initHandler();
                }
                isPlaying = true;
                playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                initNotification(R.drawable.ic_pause_circle_fill_white_48dp);
                musicService.setOnePlayer(onePlayer);
                musicService.startService();
                isNew = false;
            }else {
                musicService.play();
                isPlaying = true;
                playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                initNotification( R.drawable.ic_pause_circle_fill_white_48dp);
            }
        }else {
            playingFragment.stopAnimation();
            //暂停播放
            System.out.println("暂停播放");
            playButton.setImageResource(R.drawable.ic_play_circle_fill_white_48dp);
            initNotification( R.drawable.ic_play_circle_fill_white_48dp);
            isPlaying = false;
            musicService.pause();
        }

    }

    public void next(){
        stopButtons();
        isNew = false;
        playingFragment.playAnimation();
        if(isPlaying){
            musicService.pause();
            isPlaying = false;
        }
        if(!isNet){
            switch (playMode){
                case random:
                    currentPosition = onePlayer.getRandomPosition(musicNumber);
                    break;
                default:
                    Log.v("MainActivity","当前位置"+currentPosition+" "+musicNumber);
                    currentPosition = (currentPosition + 1) % musicNumber;
                    Log.v("MainActivity","当前位置"+currentPosition);
                    break;
            }
            currentMusic = tempmusicArrayList.get(currentPosition).getUrl();
        }else {
            switch (playMode){
                case random:
                    netcurrentPositon = onePlayer.getRandomPosition(netmusicNumber);
                    break;
                default:
                    netcurrentPositon = (netcurrentPositon + 1) % netmusicNumber;
                    break;
            }
            currentMusic = netmusicArrayList.get(currentPosition).getUrl();
        }
        notifiPositionChanged();
        updateMusic();
    }
    public void previous(){
        stopButtons();
        isNew = false;
        playingFragment.playAnimation();
        if(isPlaying){
            musicService.pause();
            isPlaying = false;
        }
        if(!isNet) {
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
            currentMusic = tempmusicArrayList.get(currentPosition).getUrl();
        }else {
            switch (playMode){
                case random:
                    netcurrentPositon = onePlayer.getRandomPosition(netmusicNumber);
                    break;
                default:
                    netcurrentPositon = (netcurrentPositon - 1) % netmusicNumber;
                    break;
            }
            currentMusic = netmusicArrayList.get(currentPosition).getUrl();
        }
        notifiPositionChanged();
        updateMusic();

    }
    public void onBackPressed() {
        if(isAlbuming) {
            albumcontainer.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            basetag.setVisibility(View.VISIBLE);
            isAlbuming = false;
            queueButton.setImageResource(R.drawable.ic_album_white_48dp);
        }else {
            if(isSingerTwo){
                OrderChanged(-2);
                isSingerTwo=false;
            }else if(isAlbumTwo){
                OrderChanged(-3);
                isAlbumTwo=false;
            }else {
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
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initNotification(R.drawable.ic_pause_circle_fill_white_48dp);
    }

    public void updateMusic(){
        playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
        time = 0;
        initOnePlayer(currentMusic);
        musicService.setOnePlayer(onePlayer);
        musicService.startService();
        isPlaying = true;
        if(!isNet){
            saveLastPlayed();
        }
        updateToolbar();
        updateSeekbar();
        initNotification(R.drawable.ic_pause_circle_fill_white_48dp);
    }
    public void initOnePlayer(String path){
        Log.v("MainActivity", "initOnePlayer我要用它" + path);
        if(onePlayer==null) {
            onePlayer = new OnePlayer();
        }
        //原地址
        onePlayer.init(path);
        //onePlayer.init("http://192.168.1.103/OnePlayer/Fall%20Out%20Boy%20-%20The%20Phoenix.mp3");
        onePlayer.setOnCompleListener(new OnCompleListener() {
            @Override
            public void onComple() {
                next();
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
                seekBar.setMax(duration / 1000);
                playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                startButtons();
            }
        });
        onePlayer.setOnBufferedUpdateListener(new OnBuffedUpdateListener() {
            @Override
            public void onBuffedUpdate(int percent) {
                Log.v("主activity", percent + " " + percent * totalTime / 100);
                seekBar.setSecondaryProgress(percent * totalTime / 100);
            }
        });
    }
    public void initNotification(int resId){
        System.out.println("初始化通知栏");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        if(!isNet){
            remoteViews.setTextViewText(R.id.noti_singer, tempmusicArrayList.get(currentPosition).getArtist());
            remoteViews.setTextViewText(R.id.noti_name, tempmusicArrayList.get(currentPosition).getDisplayName());
        }else{
            remoteViews.setTextViewText(R.id.noti_singer, netmusicArrayList.get(netcurrentPositon).getArtist());
            remoteViews.setTextViewText(R.id.noti_name, netmusicArrayList.get(netcurrentPositon).getDisplayName());
        }

        if(resId!=0){
            remoteViews.setImageViewResource(R.id.noti_playandpause, resId);
        }
        //播放键点击事件
        Intent intent1 = new Intent("playButton");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.noti_playandpause,pendingIntent1);
        //下一首点击事件
        Intent intent2 = new Intent("nextButton");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,0,intent2,0);
        remoteViews.setOnClickPendingIntent(R.id.noti_next,pendingIntent2);
        //上一首点击事件
        Intent intent3 = new Intent("previousButton");
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this,0,intent3,0);
        remoteViews.setOnClickPendingIntent(R.id.noti_previous,pendingIntent3);

        //remoteViews.setProgressBar(R.id.noti_seekBar, Integer.parseInt(musicArrayList.get(currentPosition).getDuration()) / 1000, 0, false);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("title")
                .setContentText("describe")
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();
        notificationManager.notify(0, notification);
        System.out.println("初始化通知栏完毕");


    }
    public void closeNotification(){
        System.out.println("调用关闭通知");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }
    public void initService(){
        Log.v("主Activity","初始化服务");

        Intent i = new Intent(MainActivity.this, MusicService.class);
        i.putExtra("isPlaying", isPlaying);
        i.putExtra("playMode", playMode);
        i.putExtra("currentMusic", currentMusic);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.v("主Activity", "初始化服务完毕");
    }


    public void playMode(){
        int i = transferNum % 3;
        switch (i){
            case 0:
                //playByLooping();
                playMode = looping;
                playModeButton.setImageResource(R.drawable.ic_repeat_one_white_48dp);
                Toast.makeText(this,"单曲循环",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //playByRandom();
                playMode = random;
                playModeButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
                Toast.makeText(this,"随机播放",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //playByCycle();
                playMode = cycle;
                playModeButton.setImageResource(R.drawable.ic_repeat_white_48dp);
                Toast.makeText(this,"列表循环",Toast.LENGTH_SHORT).show();
                break;
        }
        transferNum+=1;
    }
    public void queue(){
        if(viewPager.getVisibility()==View.GONE) {
            viewPager.setVisibility(View.VISIBLE);
            albumcontainer.setVisibility(View.GONE);
            basetag.setVisibility(View.VISIBLE);
            isAlbuming = false;
            queueButton.setImageResource(R.drawable.ic_album_white_48dp);
        }else {
            viewPager.setVisibility(View.GONE);
            albumcontainer.setVisibility(View.VISIBLE);
            basetag.setVisibility(View.GONE);
            isAlbuming = true;
            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
        }

    }
//    public void setUrls(){
//        int i = musicArrayList.size();
//        String[] urls = new String[i];
//        for (int j=0;j<i;j++){
//            urls[j] = musicArrayList.get(j).getUrl();
//        }
//        onePlayer.setUrls(urls);
//    }
    public void updateSeekbar(){
        System.out.println("更新seekbar");
        if(!isNet){
            totalTime = Integer.parseInt(tempmusicArrayList.get(currentPosition).getDuration()) / 1000;
            seekBar.setMax(Integer.parseInt(tempmusicArrayList.get(currentPosition).getDuration()) / 1000);
        }
        progressText.setText(ten2sixty(0));
        durationText.setText(ten2sixty(totalTime));
        seekBar.setSecondaryProgress(0);
    }
    public String ten2sixty(int ten){
        String sixty;
        String minute = ten/60+"";
        String second = ten%60+"";
        if(ten/60>=0&&ten/60<10){
            minute = "0"+minute;
        }
        if(ten%60>=0&&ten%60<10){
            second = "0"+second;
        }
        sixty = minute+":"+second;
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
    public void updateToolbar(){
        //getActionBar().setSubtitle(musicArrayList.get(currentPosition).getArtist());
        getSupportActionBar().setTitle("");
        if (!isNet) {
            ((TextView) findViewById(R.id.toolbar_title)).setText(tempmusicArrayList.get(currentPosition).getDisplayName());
            Log.v("MainActivity", "updateToolbar当前temp" + tempmusicArrayList.size()+tempmusicArrayList.get(currentPosition).getDisplayName());
            Log.v("MainActivity", "updateToolbar当前position" + currentPosition+tempmusicArrayList.get(currentPosition).getArtist());
            ((TextView) findViewById(R.id.toolbar_subtitle)).setText(tempmusicArrayList.get(currentPosition).getArtist());
        } else {
            ((TextView) findViewById(R.id.toolbar_title)).setText(netmusicArrayList.get(netcurrentPositon).getDisplayName());
            ((TextView) findViewById(R.id.toolbar_subtitle)).setText(netmusicArrayList.get(netcurrentPositon).getArtist());
        }

    }
    public void saveLastPlayed(){
        databaseOperator = new DatabaseOperator(this,"OnePlayer.db");
        if(sharedPreferences==null){
            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
        }
        Log.v("MainActivity","储存位置"+currentPosition);
        Log.v("MainActivity","储存url"+currentMusic);
        databaseOperator.saveLastplayed(currentPosition, currentMusic, sharedPreferences);
        databaseOperator = null;
    }
    public void saveOrderMode(int OrderMode){
        Log.v("MainActivity","saveOrderMode()我钦点存储"+OrderMode);
        databaseOperator = new DatabaseOperator(this,"OnePlayer.db");
        if(sharedPreferences==null){
            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
        }
        databaseOperator.saveOrderMode(sharedPreferences, OrderMode);
        databaseOperator = null;
    }
    public void saveFirstPosition(int Position){
        databaseOperator = new DatabaseOperator(this,"OnePlayer.db");
        if(sharedPreferences==null){
            sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
        }
        databaseOperator.saveFirstPosition(sharedPreferences, Position);
        databaseOperator = null;
    }
    public void initReceiver(){
        //注册播放广播
        playReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    play();
            }};
        IntentFilter filter = new IntentFilter();
        filter.addAction("playButton");
        registerReceiver(playReceiver, filter);

        //上一首广播
        previousReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isNotiClikable){
                    previous();
                }
            }};
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("previousButton");
        registerReceiver(previousReceiver, filter1);

        //下一首广播
        nextReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isNotiClikable) {
                    next();
                }
            }};
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
    protected void initHandler(){
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //处理开始走时信息
                if(msg.what==1) {
                    progressText.setText(ten2sixty(time));
                    seekBar.setProgress(time);
                }
                return false;
            }
        }){

        };
    }
    public void initTimer(){
        time = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isPlaying) {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    if(time!=totalTime) {
                        time += 1;
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    public  String getAppVersionName(Context context) {
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
    public void checkUpdate(){
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if(msg.what==0x001){
                        if(!isFirstTime) {
                            Toast.makeText(MainActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                        }
                        isFirstTime = false;
                    }else{
                        String json = (String)msg.getData().get("json");
                        if(json!=null) {
                            JSONObject response = new JSONObject(json);
                            Log.v("回应", response + "");
                            Boolean isUpdate = response.getBoolean("isUpdate");
                            Log.v("升级没有", isUpdate + "蛤蛤");
                            if (isUpdate) {
                                //有更新
                                Log.v("有更新", "蛤蛤");
                                String url = response.getString("url");
                                update(url);
                            }else {
                                Log.v("老子不更新啦","哈哈哈");
                                Toast.makeText(MainActivity.this,"没有更新呢！",Toast.LENGTH_SHORT).show();
                            }
                        }
                        isFirstTime = false;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });
        JsonUtil jsonUtil = new JsonUtil(handler);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "checkUpdate");
            jsonObject.put("model", android.os.Build.MODEL);
            jsonObject.put("sdk", Build.VERSION. SDK_INT);
            jsonObject.put("system", android.os.Build.VERSION.RELEASE);
            jsonObject.put("app", Data.appName);
            jsonObject.put("appVersion",getAppVersionName(MainActivity.this));
            jsonUtil.sendJson(jsonObject,Data.url);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void update(final String url){
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

    public void stopButtons() {
        playButton.setClickable(false);
        nextButton.setClickable(false);
        previousButton.setClickable(false);
        isNotiClikable = false;

    }

    public void startButtons() {
        playButton.setClickable(true);
        nextButton.setClickable(true);
        previousButton.setClickable(true);
        isNotiClikable = true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous : previous(); break;
            case R.id.next : next(); break;
            case R.id.playandpause : play(); break;
            case R.id.update : checkUpdate(); break;
            case R.id.queue : queue();break;
            case R.id.playmode : playMode();break;
            case R.id.local :
                viewPager.setCurrentItem(0);
                isNet = false;
                local.setTextColor(Color.parseColor("#FFFFFF"));
                internet.setTextColor(Color.parseColor("#000000"));

                break;
            case R.id.internet :
                viewPager.setCurrentItem(1);
                local.setTextColor(Color.parseColor("#000000"));
                internet.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }
    public void moveCursor(int start,int end){
        TranslateAnimation animation = new TranslateAnimation(start, end, 0, 0);
        animation.setFillAfter(true);// True:停留在停止位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }
    public ArrayList<Music> getInternetMusiclist(){
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if(msg.what==0x001){
                        Toast.makeText(MainActivity.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                    }else{
                        String json = (String)msg.getData().get("json");
                        if(json!=null) {
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

                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });
        JsonUtil jsonUtil = new JsonUtil(handler);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "getInternetMusiclist");
            jsonUtil.sendJson(jsonObject,Data.url);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String Mandarin2Pinyin(String src){
        char[] t1 ;
        t1=src.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4="";
        int t0=t1.length;
        try {
            for (int i=0;i<t0;i++)
            {
                //判断是否为汉字字符
                if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
                {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], outputFormat);
                    t4+=t2[0];
                }
                else
                    t4+=java.lang.Character.toString(t1[i]);
            }
            return t4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t4;
    }
    public void OrderChanged(int i) {
        Log.v("MainActivity","OrderChanged模式代码:"+i);
        if(Build.VERSION.SDK_INT>19){
            recyclerView = localmusicListFragment.getRecyclerView();
            switch (i) {
                case -1:
                    Order = -1;
                    localplaylistAdapter.setDatasource(tempmusicArrayList);
                    if (recyclerView!=null){
                        recyclerView.setAdapter(localplaylistAdapter);
                    }
                    localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                                albumcontainer.setVisibility(View.VISIBLE);
                                viewPager.setVisibility(View.GONE);
                                return;
                            }
                            isNew = false;
                            albumcontainer.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            if (isPlaying) {
                                musicService.pause();
                                isPlaying = false;
                            }
                            playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                            tempmusicArrayList = localmusicArrayList;
                            saveOrderMode(-1);
                            musicNumber = tempmusicArrayList.size();
                            currentPosition = position;
                            currentMusic = tempmusicArrayList.get(position).getUrl();
                            Log.v("MainActivity","OrderChanged当前音乐"+currentMusic);
                            notifiPositionChanged();
                            playingFragment.playAnimation();
                            updateMusic();
                            basetag.setVisibility(View.GONE);
                            isAlbuming = true;
                            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                            isPlaying = true;
                        }
                    });
                    break;
                case -2:

                    Order = -2;
                    localplaylistAdapter.setDataSource(singers);
                    localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            isSingerTwo = true;
                            Log.v("MainActivity", "一级菜单点击" + position);
                            saveFirstPosition(position);

                            tempmusicArrayList = singerArrayLists.get(position);
                            musicNumber = tempmusicArrayList.size();
                            localplaylistAdapter.setDatasource(tempmusicArrayList);
                            localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                                        albumcontainer.setVisibility(View.VISIBLE);
                                        viewPager.setVisibility(View.GONE);
                                        return;
                                    }
                                    saveOrderMode(-2);
                                    Log.v("MainActivity", "二级菜单点击" + position);
                                    isNew = false;
                                    albumcontainer.setVisibility(View.VISIBLE);
                                    viewPager.setVisibility(View.GONE);
                                    if (isPlaying) {
                                        musicService.pause();
                                        isPlaying = false;
                                    }
                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                                    currentPosition = position;
                                    currentMusic = tempmusicArrayList.get(position).getUrl();

                                    localplaylistAdapter.url = currentMusic;
                                    localplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
                                    localplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
                                    localplaylistAdapter.notifyDataSetChanged();
                                    playingFragment.playAnimation();
                                    updateMusic();
                                    basetag.setVisibility(View.GONE);
                                    isAlbuming = true;
                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                                    isPlaying = true;
                                }
                            });
                            Log.v("MainActivity", "二级菜单初始化完毕" );
                            if (recyclerView != null) {
                                Log.v("MainActivity", "一级适配器设立" );
                                recyclerView.setAdapter(localplaylistAdapter);
                            }
                        }
                    });
                    if (recyclerView!=null){
                        Log.v("MainActivity", "二级适配器设立" );
                        recyclerView.setAdapter(localplaylistAdapter);
                    }
                    break;
                case -3:

                    Order = -3;
                    localplaylistAdapter.setDataSource(albums);
                    localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            isAlbumTwo = true;
                            tempmusicArrayList = albumArrayLists.get(position);
                            musicNumber = tempmusicArrayList.size();
                            localplaylistAdapter.setDatasource(tempmusicArrayList);
                            saveFirstPosition(position);
                            localplaylistAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                                        albumcontainer.setVisibility(View.VISIBLE);
                                        viewPager.setVisibility(View.GONE);
                                        return;
                                    }
                                    saveOrderMode(-3);
                                    isNew = false;
                                    albumcontainer.setVisibility(View.VISIBLE);
                                    viewPager.setVisibility(View.GONE);
                                    if (isPlaying) {
                                        musicService.pause();
                                        isPlaying = false;
                                    }
                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                                    currentPosition = position;
                                    currentMusic = tempmusicArrayList.get(position).getUrl();
                                    localplaylistAdapter.url = currentMusic;
                                    localplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
                                    localplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
                                    localplaylistAdapter.notifyDataSetChanged();
                                    playingFragment.playAnimation();
                                    updateMusic();
                                    basetag.setVisibility(View.GONE);
                                    isAlbuming = true;
                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                                    isPlaying = true;
                                }
                            });
                            if (recyclerView != null) {
                                recyclerView.setAdapter(localplaylistAdapter);
                            }
                        }
                    });
                    if (recyclerView!=null){
                        recyclerView.setAdapter(localplaylistAdapter);
                    }
                    break;
            }
        }else{
            listView = localmusicListFragmentLow.getListView();
            switch (i) {
                case -1:

                    Order = -1;

                    localPlaylistAdapterlow.setDatasource(tempmusicArrayList);
                    if(listView!=null){
                        listView.setAdapter(localPlaylistAdapterlow);
                    }
                    localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (tempmusicArrayList.get(position).getUrl() == currentMusic && !isNew) {
                                playingFragment.playAnimation();
                                albumcontainer.setVisibility(View.VISIBLE);
                                viewPager.setVisibility(View.GONE);
                                basetag.setVisibility(View.GONE);
                                ;
                                isPlaying = true;
                                return;
                            }
                            saveOrderMode(-1);
                            isNew = false;
                            albumcontainer.setVisibility(View.VISIBLE);
                            viewPager.setVisibility(View.GONE);
                            if (isPlaying) {
                                musicService.pause();
                                isPlaying = false;
                            }
                            playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                            tempmusicArrayList = localmusicArrayList;
                            musicNumber = tempmusicArrayList.size();
                            currentPosition = position;
                            currentMusic = tempmusicArrayList.get(position).getUrl();
                            notifiPositionChanged();
                            playingFragment.playAnimation();
                            updateMusic();
                            basetag.setVisibility(View.GONE);
                            isAlbuming = true;
                            queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                            isPlaying = true;
                        }
                    });
                    break;
                case -2:

                    Order = -2;
                    Log.v("MainActivity", "按歌手排序");
                    localPlaylistAdapterlow.setDataSource(singers);
                    localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            isSingerTwo = true;
                            Log.v("MainActivity", "一级菜单点击" + position);
                            tempmusicArrayList = singerArrayLists.get(position);
                            musicNumber = tempmusicArrayList.size();
                            localPlaylistAdapterlow.setDatasource(tempmusicArrayList);
                            saveFirstPosition(position);
                            localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                                        albumcontainer.setVisibility(View.VISIBLE);
                                        viewPager.setVisibility(View.GONE);
                                        return;
                                    }
                                    saveOrderMode(-2);
                                    Log.v("MainActivity", "二级菜单点击" + position);
                                    isNew = false;
                                    albumcontainer.setVisibility(View.VISIBLE);
                                    viewPager.setVisibility(View.GONE);
                                    if (isPlaying) {
                                        musicService.pause();
                                        isPlaying = false;
                                    }
                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                                    currentPosition = position;
                                    currentMusic = tempmusicArrayList.get(position).getUrl();
                                    notifiPositionChanged();
                                    playingFragment.playAnimation();
                                    updateMusic();
                                    basetag.setVisibility(View.GONE);
                                    isAlbuming = true;
                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                                    isPlaying = true;
                                }
                            });
                            if (listView != null) {
                                listView.setAdapter(localPlaylistAdapterlow);
                            }
                        }
                    });
                    if(listView!=null){
                        listView.setAdapter(localPlaylistAdapterlow);
                    }
                    break;
                case -3:

                    Order = -3;
                    localPlaylistAdapterlow.setDataSource(albums);
                    localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            isAlbumTwo = true;
                            tempmusicArrayList = albumArrayLists.get(position);
                            musicNumber = tempmusicArrayList.size();
                            localPlaylistAdapterlow.setDatasource(tempmusicArrayList);
                            saveFirstPosition(position);
                            localmusicListFragmentLow.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (tempmusicArrayList.get(position).getUrl().equals(currentMusic) && !isNew) {
                                        albumcontainer.setVisibility(View.VISIBLE);
                                        viewPager.setVisibility(View.GONE);
                                        return;
                                    }
                                    saveOrderMode(-3);
                                    isNew = false;
                                    albumcontainer.setVisibility(View.VISIBLE);
                                    viewPager.setVisibility(View.GONE);
                                    if (isPlaying) {
                                        musicService.pause();
                                        isPlaying = false;
                                    }
                                    playButton.setImageResource(R.drawable.ic_pause_circle_fill_white_48dp);
                                    currentPosition = position;
                                    currentMusic = tempmusicArrayList.get(position).getUrl();
                                    notifiPositionChanged();
                                    playingFragment.playAnimation();
                                    updateMusic();
                                    basetag.setVisibility(View.GONE);
                                    isAlbuming = true;
                                    queueButton.setImageResource(R.drawable.ic_queue_music_white_48dp);
                                    isPlaying = true;
                                }
                            });
                            if (listView != null) {
                                listView.setAdapter(localPlaylistAdapterlow);
                            }
                        }
                    });
                    if(listView!=null){
                        listView.setAdapter(localPlaylistAdapterlow);
                    }
                    break;
            }
        }

    }
    public void notifiPositionChanged(){
        if(!isNet) {
            if (Build.VERSION.SDK_INT > 19) {
                localplaylistAdapter.url = currentMusic;
                localplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
                localplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
                localplaylistAdapter.notifyDataSetChanged();
            } else {
                localPlaylistAdapterlow.url = currentMusic;
                localPlaylistAdapterlow.singer = tempmusicArrayList.get(currentPosition).getArtist();
                localPlaylistAdapterlow.album = tempmusicArrayList.get(currentPosition).getAlbum();
                localPlaylistAdapterlow.notifyDataSetChanged();
            }
        }else{
            if (Build.VERSION.SDK_INT > 19) {
                netplaylistAdapter.url = currentMusic;
                netplaylistAdapter.singer = tempmusicArrayList.get(currentPosition).getArtist();
                netplaylistAdapter.album = tempmusicArrayList.get(currentPosition).getAlbum();
                netplaylistAdapter.notifyDataSetChanged();
            } else {
                netPlaylistAdapterlow.url = currentMusic;
                netPlaylistAdapterlow.singer = tempmusicArrayList.get(currentPosition).getArtist();
                netPlaylistAdapterlow.album = tempmusicArrayList.get(currentPosition).getAlbum();
                netPlaylistAdapterlow.notifyDataSetChanged();
            }
        }
    }
    public void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener){
        this.onIntializeCompleListener = onIntializeCompleListener;
    }


}
