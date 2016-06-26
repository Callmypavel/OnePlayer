package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.text.Collator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by 请叫我保尔 on 2015/10/23.
 */
public class StartActivity extends Activity implements Runnable {
    Boolean isStop = false;
    final static int initFinished = 0x130;
    ArrayList<Music> musicArrayList;
    ArrayList<String> singers = new ArrayList<>();
    ArrayList<ArrayList<Music>> singerArrayLists  = new ArrayList<ArrayList<Music>>();
    ArrayList<String> albums = new ArrayList<>();
    ArrayList<ArrayList<Music>> albumArrayLists = new ArrayList<ArrayList<Music>>();
    MusicProvider musicProvider;
    int musicNumber;
    Handler handler;
    private Music lastPlayedMusic ;
    int currentPosition = 0;
    int OrderMode = -3;
    int FirstPosition = 0;
    String singer;
    String song;
    OneLogger oneLogger;
    final static int startMainActivity = 0x131;
    final static int godie = 0x132;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>19){
            setTheme(R.style.Oneplayer);
        }else{
            setTheme(R.style.One);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        //oneLogger = new OneLogger();
        //oneLogger.getLog();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==initFinished){
                    Intent intent = new Intent();
                    //intent.putParcelableArrayListExtra("musicArraylist", musicArrayList);
//                    intent.putExtra("lastPlayedMusic", lastPlayedMusic);
//                    intent.putExtra("currentPosition", currentPosition);
                    //Log.v("StartActivity", "onCreate检查模式" + OrderMode);
                    //intent.putExtra("FirstPosition", FirstPosition);
                    //Log.v("StartActivity", "onCreate检查第一彩蛋" + FirstPosition);
//                    intent.putExtra("OrderMode", OrderMode);
                    //Log.v("StartActivity", "onCreate检查第一彩蛋" + FirstPosition);
                    intent.putExtra("musicProvider", musicProvider);
                    Log.v("StartActivity", "传前检查提供者" + musicProvider);
                    intent.setClass(StartActivity.this, MainActivity.class);
                    Log.v("获取完成", "开始新的旅途");
                    //oneLogger.stopLogging();
                    //oneLogger = null;
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        new Thread(this).start();
    }

    @Override
    public void run() {
        if(!isStop){
            init();
            //isStop = true;
        }
    }
    public void init()  {
        TextView textView = (TextView)findViewById(R.id.entering);
        textView.setVisibility(View.VISIBLE);
        //从数据库中获得音乐
//        DatabaseOperator databaseOperator = new DatabaseOperator(this, "OnePlayer.db");
//        musicArrayList = databaseOperator.getMusics();
        //如果从数据库中取不到歌曲，启动扫描加载
//        if (musicArrayList.size() == 0) {
            OneMusicloader oneMusicloader = new OneMusicloader(getContentResolver());
            musicArrayList = oneMusicloader.loadLocalMusic();
//            databaseOperator.saveMusics(musicArrayList);
//        }
//        Comparator<Music> comparator = new Comparator<Music>() {
//            @Override
//            public int compare(Music lhs, Music rhs) {
//                return  Mandarin2Pinyin(lhs.getDisplayName()).toLowerCase().compareTo(Mandarin2Pinyin(rhs.getDisplayName()).toLowerCase());
//            }
//        };
//        Collections.sort(musicArrayList, comparator);
//        //初始化按歌手排列
//        singers.add("初始化用");
//        ArrayList<Music> temp = new ArrayList<>();
//        Music tempmusic = new Music();
//        tempmusic.setArtist("初始化用");
//        temp.add(tempmusic);
//        singerArrayLists.add(temp);
//        for (Music music : musicArrayList){
//            int singerLocation = 0;
//            Boolean isSame = false;
//            for(int i = singers.size();i>0;i--){
//                String singer=singers.get(i-1);
//                if(singer.equals(music.getArtist())){
//                    singerLocation = i-1;
//                    isSame = true;
//                }
//            }
//            if(!isSame){
//                singers.add(music.getArtist());
//                ArrayList<Music> temp1 = new ArrayList<>();
//                temp1.add(music);
//                singerArrayLists.add(temp1);
//            }else {
//
//                singerArrayLists.get(singerLocation).add(music);
//            }
//        }
//        singers.remove(0);
//        singerArrayLists.remove(0);
//
//        //初始化按专辑排列
//        albums.add("初始化用");
//        tempmusic.setAlbum("初始化用");
//        temp.add(tempmusic);
//        albumArrayLists.add(temp);
//        for (Music music : musicArrayList){
//            int albumLocation = 0;
//            Boolean isSame = false;
//            for(int i = albums.size();i>0;i--){
//                String album=albums.get(i-1);
//                if(album.equals(music.getAlbum())){
//                    albumLocation = i-1;
//                    isSame = true;
//                }
//            }
//            if(!isSame){
//                albums.add(music.getAlbum());
//                ArrayList<Music> temp1 = new ArrayList<>();
//                temp1.add(music);
//                albumArrayLists.add(temp1);
//            }else {
//
//                albumArrayLists.get(albumLocation).add(music);
//            }
//        }
//        albums.remove(0);
//        albumArrayLists.remove(0);
//
////        Comparator<String> comparator1 = new Comparator<String>() {
////            @Override
////            public int compare(String lhs, String rhs) {
////                return  Mandarin2Pinyin(lhs).toLowerCase().compareTo(Mandarin2Pinyin(rhs).toLowerCase());
////            }
////        };
////        Collections.sort(albums, comparator1);
////        Collections.sort(singers, comparator1);
//        for(ArrayList<Music> musics : singerArrayLists){
//            Collections.sort(musics, comparator);
//        }
//        for(ArrayList<Music> musics : albumArrayLists){
//            Collections.sort(musics, comparator);
//        }
////        Log.v("StartActivity", "检查歌手数量" + singers.size());
////        for (String singer : singers){
////            Log.v("StartActivity","检查歌手们"+singer);
////        }
////        Log.v("StartActivity","检查音乐歌手数量"+singerArrayLists.size());
////        for(ArrayList<Music> musicArrayList1 : singerArrayLists){
////            Log.v("StartActivity","检查歌手音乐数量"+musicArrayList1.size());
////            for(Music music : musicArrayList1){
////                Log.v("StartActivity","检查朱军音乐们"+music.getDisplayName());
////            }
////        }
////        Log.v("StartActivity", "检查专辑数量" + albums.size());
////        for (String album : albums){
////            Log.v("StartActivity","检查专辑们"+album);
////        }
////        Log.v("StartActivity","检查音乐专辑数量"+albumArrayLists.size());
////        for(ArrayList<Music> musicArrayList1 : albumArrayLists){
////            Log.v("StartActivity","检查专辑音乐数量"+musicArrayList1.size());
////            for(Music music : musicArrayList1){
////                Log.v("StartActivity","检查朱军音乐们"+music.getDisplayName());
////            }
////        }
        //取得最后一次播放的音乐
        //SharedPreferences sharedPreferences = getSharedPreferences("Last", Activity.MODE_PRIVATE);
//        JSONObject jsonObject = databaseOperator.getLastPlayed();
//        try {
//            currentPosition = jsonObject.getInt("currentPosition");
//            String artist = jsonObject.getString("artist");
//            String duration = jsonObject.getString("duration");
//            String ablum = jsonObject.getString("ablum");
//            String displayName = jsonObject.getString("displayName");
//            String url = jsonObject.getString("url");
//            lastPlayedMusic = new Music(artist,duration,ablum,displayName,url,true);
//            //FirstPosition = jsonObject.getInt("FirstPosition");
//            OrderMode = jsonObject.getInt("OrderMode");
//            Log.v("StartActivity","init检查模式"+OrderMode);
//        }catch (JSONException e){
//            e.printStackTrace();
//            Log.v("StartActivity","json异常");
//        }
//        if(lastPlayedMusic==null){
//            if(musicArrayList.size()!=0){
//                lastPlayedMusic = musicArrayList.get(0);
//            }
//        }
        musicProvider = new MusicProvider(musicArrayList);
        isStop = true;
        Message message = new Message();
        message.what = initFinished;
        handler.sendMessage(message);
    }
//    public String Mandarin2Pinyin(String src){
//        char[] t1 ;
//        t1=src.toCharArray();
//        String[] t2;
//        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
//        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//        String t4="";
//        int t0=t1.length;
//        try {
//            for (int i=0;i<t0;i++)
//            {
//            //判断是否为汉字字符
//                if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
//                {
//                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], outputFormat);
//                    t4+=t2[0];
//                }
//                else
//                    t4+=java.lang.Character.toString(t1[i]);
//            }
//            return t4;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return t4;
//    }


}
