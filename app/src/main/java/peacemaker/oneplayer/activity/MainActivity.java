package peacemaker.oneplayer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import peacemaker.oneplayer.databinding.MainActivityBinding;
import peacemaker.oneplayer.databinding.SimpleListFragmentBinding;
import peacemaker.oneplayer.entity.AlbumInfo;
import peacemaker.oneplayer.entity.Data;
import peacemaker.oneplayer.entity.IndexedMusic;
import peacemaker.oneplayer.entity.SingerInfo;
import peacemaker.oneplayer.fragment.OneAlbumListFragment;
import peacemaker.oneplayer.fragment.OneFragment;
import peacemaker.oneplayer.fragment.OneHomePageFragment;
import peacemaker.oneplayer.fragment.OneMymusicFragment;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.OneClickHandler;
import peacemaker.oneplayer.fragment.OnePlayListFragment;
import peacemaker.oneplayer.fragment.OneSingerListFragment;
import peacemaker.oneplayer.tool.*;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.view.GridViewDecoration;
import peacemaker.oneplayer.view.IndexView;
import peacemaker.oneplayer.view.ListViewDecorarion;
import peacemaker.oneplayer.view.OneListView;
import peacemaker.oneplayer.view.SimpleListFragment;
import peacemaker.oneplayer.view.UniversalAdapter;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.SAXParser;


/**
 * Created by ouyan_000 on 2015/8/14.
 */
public class MainActivity extends OneActivity implements View.OnClickListener{
    Boolean isNull = false;
    private OneApplication oneApplication;
    Handler handler;
    Boolean isAlbuming = false;
    Boolean isNotiClikable = true;
    Boolean isNet = false;
    Boolean isFirstTime = true;
    int netmusicNumber = 1;
//    @BindView(R.id.drawer_layout)
//    public DrawerLayout drawerLayout;
//    @BindView(R.id.navigation_view)
//    public NavigationView navigationView;
//    @BindView(R.id.id_tablayout)
//    private TabLayout tabLayout;
//    @BindView(R.id.id_viewpager)
//    private ViewPager viewPager;
//    @BindView(R.id.toolbar)
//    private Toolbar toolbar;
//    @BindView(R.id.one_index_view)
//    public IndexView indexView;
    public ProgressBar progressBar;
    public TextView updateText;
    private static final int SONG_INDEX = 0;
    private static final int ALBUM_INDEX = 1;
    private static final int SINGER_INDEX = 2;
    private int currentFragmentPosition;
//    private OneHomePageFragment oneHomePageFragment;
//    private OneMymusicFragment oneMymusicFragment;
//    private ArrayList<OneFragment> oneFragments;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private OneLogger oneLogger;
    public MainActivityBinding binding;
    private MusicProvider musicProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        oneApplication = (OneApplication) getApplication();
        if(oneApplication.musicProvider==null) {
            setContentView(R.layout.start_activity);
        }else {
            initialize();
            oneApplication.setOneActivity(this);
            oneApplication.initReceiver();
            return;
        }
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==0x111){
                    initialize();
                    oneApplication.setOneActivity(musicProvider,MainActivity.this);


                    //checkUpdate();
                }
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OneMusicloader oneMusicloader = new OneMusicloader(getContentResolver(),MainActivity.this);
                ObservableArrayList<Music> tempmusicArrayList = oneMusicloader.loadLocalMusic();
                musicProvider = new MusicProvider(tempmusicArrayList);
                Message message = new Message();
                message.what = 0x111;
                handler.sendMessage(message);

            }
        });
        thread.start();
    }
//    public void toSecondItemActivity(Music music){
//        LogTool.log("MainActivity","更新目标音乐"+music.getUrl());
//        oneApplication.updateTargetMusic(music);
//        Intent intent = new Intent();
//        if(currentFragmentPosition==SINGER_INDEX){
//            intent.setClass(MainActivity.this,OneSingerDetailActivity.class);
//        }else if(currentFragmentPosition==ALBUM_INDEX) {
//            intent.setClass(MainActivity.this,OneAlbumDetailActivity.class);
//        }
//        startActivity(intent);
//    }


    @Override
    public void onPermissionGranted(Boolean isPermissionUpdated) {
        if (!isPermissionUpdated){
            initPermission();
        }
    }

    @Override
    protected void onRestart() {
        Log.v("MainActivity","onRestart()");
        super.onRestart();
        oneApplication.setOneActivity(this);
        //refreshPlaylist();
        binding.navigationView.getHeaderView(0).setBackgroundColor(oneApplication.getThemeColor());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity", "activity");
        //oneApplication.unRegister();
    }
//    public void refreshPlaylist(){
//        if(onePlayListFragment!=null) {
//            onePlayListFragment.refresh();
//        }
//    }
    private void checkPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            LogTool.log(this,"权限码"+permission);
            LogTool.log(this,"获取码"+PackageManager.PERMISSION_GRANTED);
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},666);
                return;
            }
        }
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


    protected void initialize() {

        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.main_activity);
        binding.setMusic(oneApplication.currentMusic);
        binding.setMusicState(oneApplication.musicState);
        binding.setHandler(new OneClickHandler());
        LogTool.log("MainActivity","高斯模糊初始值"+oneApplication.getOneConfig().getBlurRadius());
        binding.setOneConfig(oneApplication.getOneConfig());
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int themeColor = typedValue.data;
        final Bitmap bitmap = OneBitmapUtil.zoomImg(this,R.drawable.ic_menu_white_48dp,15);
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
        ((Toolbar)binding.getRoot().findViewById(R.id.toolbar)).setNavigationIcon(drawable);
        setSupportActionBar(binding.toolbar);
//        ((ViewGroup)toolbar.getChildAt(0)).getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
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

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                    oneApplication.destroy();
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
        checkPermission();
//        oneFragments = new ArrayList<>();
//        oneFragments.add(new OneAlbumListFragment());
//        oneFragments.add(new OneSingerListFragment());
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SimpleListFragment().setupWithOnRecyclerViewDataSetListener(new SimpleListFragment.OnRecyclerViewDataSetListener() {
                @Override
                public void OnRecyclerViewDataSet(SimpleListFragmentBinding binding, SimpleListFragment simpleListFragment) {
                    UniversalAdapter<Music> adapter = new UniversalAdapter(R.layout.item_music);
                    adapter.setDataSource(musicProvider.getSongs());
                    adapter.setOnClickListener(MainActivity.this);
                    binding.recyclerview.setAdapter(adapter);
                    binding.recyclerview.addItemDecoration(new ListViewDecorarion(1));
                    binding.recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            },"歌曲"
        ));
        fragments.add(new SimpleListFragment().setupWithOnRecyclerViewDataSetListener(new SimpleListFragment.OnRecyclerViewDataSetListener() {
            @Override
            public void OnRecyclerViewDataSet(SimpleListFragmentBinding binding, SimpleListFragment simpleListFragment) {
                UniversalAdapter<AlbumInfo> adapter = new UniversalAdapter(R.layout.item_albumlist);
                adapter.setDataSource(musicProvider.getAlbums());
                adapter.setOnClickListener(MainActivity.this);
                adapter.setOnBindHandler(new UniversalAdapter.OnBindHandler() {
                    @Override
                    public void onBind(Object data) {
                        final AlbumInfo albumInfo = (AlbumInfo)data;
                        Bitmap bitmap = OneImageCache.getImageCache().getBitmapFromCache(albumInfo.getAlbumBitmapId());
                        if (bitmap == null) {
                            executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    MusicProvider.loadAlbumImage(albumInfo);

                                }
                            });
                        } else {
                            albumInfo.setAlbumImage(bitmap);
                            //Log.v("OneAblumListFragment","缓存取bitmap"+bitmap);
                        }
                    }
                });
                binding.recyclerview.setAdapter(adapter);
                binding.recyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                binding.recyclerview.addItemDecoration(new GridViewDecoration(2,2));
            }},"专辑"
        ));
        fragments.add(new SimpleListFragment().setupWithOnRecyclerViewDataSetListener(new SimpleListFragment.OnRecyclerViewDataSetListener() {
                @Override
                public void OnRecyclerViewDataSet(SimpleListFragmentBinding binding, SimpleListFragment simpleListFragment) {
                    UniversalAdapter<SingerInfo> adapter = new UniversalAdapter(R.layout.item_singer);
                    adapter.setDataSource(musicProvider.getSingers());
                    adapter.setOnClickListener(MainActivity.this);
                    binding.recyclerview.setAdapter(adapter);
                    binding.recyclerview.addItemDecoration(new ListViewDecorarion(2));
                }
            },"歌手"
        ));


        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(fragments!=null) {
                    return fragments.get(position);
                }
                return null;
            }

            @Override
            public int getCount() {
                return fragments.size();
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
                    return "歌曲";
                }else if(position==1) {
                    return "专辑";
                }else if(position==2) {
                    return "歌手";
                }else {
                   return super.getPageTitle(position);
                }
            }
        };
        binding.idViewpager.setAdapter(fragmentStatePagerAdapter);
        binding.idViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v("MainActivity","onPageSelected"+position);
//                bindToIndexView(oneFragments.get(position).getRecyclerView());
                currentFragmentPosition = position;
//                switch (position){
//                    case 0:updateIndexViewData(oneApplication.getIndexedSongArrayList());break;
//                    case 1:updateIndexViewData(oneApplication.getIndexedAlbumArrayList());break;
//                    case 2:updateIndexViewData(oneApplication.getIndexedSingerArrayList());break;
//                }


                //viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        binding.idTablayout.setTabMode(TabLayout.MODE_FIXED);
        binding.idTablayout.setupWithViewPager(binding.idViewpager);

        binding.navigationView.getHeaderView(0).setBackgroundColor(oneApplication.getThemeColor());
        //indexView.setRecyclerView(oneFragments.get(SONG_INDEX).getRecyclerView(),true);
        //updateIndexViewData(oneApplication.getIndexedSongArrayList());
        super.initialize();

    }
//
//    public void bindToIndexView(RecyclerView recyclerView){
//        //binding.oneIndexView.setRecyclerView(recyclerView,true);
//    }
//    private void updateIndexViewData(ArrayList<IndexedMusic> data){
//        //binding.oneIndexView.setIndexedMusics(data);
//    }

    protected void disableListview(){
        binding.idTablayout.setVisibility(View.GONE);
        binding.toolbar.setVisibility(View.GONE);
        binding.idViewpager.setVisibility(View.GONE);
        //binding.oneIndexView.setVisibility(View.GONE);
    }
    protected void enableListview(){
        Log.v("MainActivity","enableListview()打开点击事件");
        binding.idTablayout.setVisibility(View.VISIBLE);
        binding.toolbar.setVisibility(View.VISIBLE);
        binding.idViewpager.setVisibility(View.VISIBLE);
        //binding.oneIndexView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        Log.v("MainActivity","按下返回键");
        if(binding.drawerLayout!=null) {
            if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
                return;
            }
        }
        if(oneApplication.musicState.getIsPlayView()){
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
                        final ObservableArrayList<Music> musicArrayList = EntityConverter.ArraylistToObservableOne(bundle.getParcelableArrayList("musicArrayList"));
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
        JsonUtil jsonUtil = new JsonUtil();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "checkUpdate");
            jsonObject.put("model", Build.MODEL);
            jsonObject.put("sdk", Build.VERSION.SDK_INT);
            jsonObject.put("system", Build.VERSION.RELEASE);
            jsonObject.put("app", Data.appName);
            jsonObject.put("appVersion", getAppVersionName(MainActivity.this));
            jsonUtil.sendJson(jsonObject, Data.url,handler);

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
        JsonUtil jsonUtil = new JsonUtil();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "getInternetMusiclist");
            jsonUtil.sendJson(jsonObject, Data.url,handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.singer_item:
                //点击歌手列表中的item
                IntentUtil.toActivity(this,OneSingerDetailActivity.class,"singerId",((SingerInfo)view.getTag()).getSingerName());
                break;
            case R.id.album_item:
                //点击专辑列表中的item
                IntentUtil.toActivity(this,OneAlbumDetailActivity.class,"albumId",((AlbumInfo)view.getTag()).getAlbumName());
                break;
        }
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
