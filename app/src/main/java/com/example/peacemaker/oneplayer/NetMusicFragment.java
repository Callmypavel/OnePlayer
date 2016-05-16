package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 请叫我保尔 on 2015/10/26.
 */
public class NetMusicFragment extends MusicListFragment  {
    LayoutInflater layoutInflater;
    RecyclerView playrecycleView;
    Context context;
    MainActivity activity;
    Boolean isInit = false;
    Button button;
    Handler handler;
    @Override
    public void init() {
        Log.v("NetMusicFragment","初始化");
        playrecycleView = (RecyclerView)activity.findViewById(R.id.netlist);
        button = (Button)activity.findViewById(R.id.net_tips);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("正在连接服务器...");
            }
        });
        playrecycleView.setAdapter(activity.netplaylistAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        Log.v("LocalMusicFragment", linearLayoutManager + "");
        playrecycleView.setLayoutManager(linearLayoutManager);
        playrecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if(msg.what==0x001){
                        if(!activity.isFirstTime) {
                            activity.isNet = false;
                            Toast.makeText(activity, "服务器连接失败", Toast.LENGTH_SHORT).show();
                            button.setClickable(true);
                            Thread.sleep(1000);
                            button.setText("服务器连接失败，点击重试");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getNet();
                                    button.setText("正在连接服务器...");
                                    button.setClickable(false);
                                }
                            });
                        }
                    }else{
                        activity.isNet = true;
                        String json = (String)msg.getData().get("json");
                        if(json!=null) {
                            button.setVisibility(View.GONE);
                            Log.v("NetMusicFragment","json"+json);
                            ArrayList<Music> musicArrayList = new ArrayList<Music>();
                            JSONObject response = new JSONObject(json);

                            Log.v("回应", response + "");
//                            int musicNum = response.getInt("musicNum");
//                            for(int i = 0;i<musicNum;i++){
//                                JSONObject jsonObject = new JSONObject(response.getString("music" + i));
//                                Music music = new Music();
//                                music.setAlbum(jsonObject.getString("album"));
//                                music.setDisplayName(jsonObject.getString("song"));
//                                Log.v("NetMusicFragment", "歌名" + jsonObject.getString("song"));
//                                music.setArtist(jsonObject.getString("singer"));
//                                music.setUrl(jsonObject.getString("url"));
//                                Log.v("NetMusicFragment", "歌手" + jsonObject.getString("singer"));
//                                Log.v("NetMusicFragment", "music检查" + music.getDisplayName()+music.getArtist());
//                                musicArrayList.add(music);
//                                for(Music music1 : musicArrayList) {
//
//                                    Log.v("NetMusicFragment列表检查" , music1.getArtist()+music1.getDisplayName());
//                                }
//                            }
                            JSONArray jsonArray = response.getJSONArray("RECORDS");
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Music music = new Music();
                                music.setAlbum(jsonObject.getString("album"));
                                music.setDisplayName(jsonObject.getString("song"));
                                Log.v("NetMusicFragment", "歌名" + jsonObject.getString("song"));
                                music.setArtist(jsonObject.getString("singer"));
                                music.setUrl(jsonObject.getString("url"));
                                Log.v("NetMusicFragment", "歌手" + jsonObject.getString("singer"));
                                Log.v("NetMusicFragment", "music检查" + music.getDisplayName()+music.getArtist());
                                musicArrayList.add(music);
                                for(Music music1 : musicArrayList) {

                                    Log.v("NetMusicFragment列表检查" , music1.getArtist()+music1.getDisplayName());
                                }
                            }

                            activity.netmusicNumber = musicArrayList.size();
                            activity.netmusicArrayList = musicArrayList;
                            activity.netplaylistAdapter.setDatasource(musicArrayList);
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                isInit = true;
                return false;
            }
        });
       getNet();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("view初始化");
        this.layoutInflater = inflater;
        View view = inflater.inflate(R.layout.net_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity)getActivity();
        init();
    }
    @Override
    public RecyclerView getRecyclerView() {
        return playrecycleView;
    }

    @Override
    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener) {

    }

    @Override
    public void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener) {

    }

    public void getNet(){
        JsonUtil jsonUtil = new JsonUtil(handler);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", "musicList");
        }catch (JSONException e){
            e.printStackTrace();
        }
        jsonUtil.sendJson(jsonObject, Data.url);
    }
}
