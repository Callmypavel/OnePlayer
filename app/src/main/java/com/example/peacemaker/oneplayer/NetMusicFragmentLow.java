package com.example.peacemaker.oneplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 请叫我保尔 on 2015/10/26.
 */
public class NetMusicFragmentLow extends MusicListFragmentLow {
    LayoutInflater layoutInflater;
    MainActivity activity;
    Boolean isInit = false;
    ListView playlistView;
    Handler handler;
    Button button;
    @Override
    public void init() {
        System.out.println("fragment初始化");
        //this.playlistAdapter = playlistAdapter;
        //activity.databaseOperator = new DatabaseOperator(activity,"OnePlayer.db");
        playlistView = (ListView)activity.findViewById(R.id.netlist_low);
        button = (Button)activity.findViewById(R.id.net_tips_low);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setText("正在连接服务器...");
            }
        });
        //playlistView.setAdapter(activity.netPlaylistAdapterlow);
        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(view, position);
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if(msg.what==0x001){
                        if(!activity.isFirstTime) {
                            activity.isNet = false;
                            Toast.makeText(activity, "服务器连接失败", Toast.LENGTH_SHORT).show();
                            button.setClickable(true);
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
                            ArrayList<Music> musicArrayList = new ArrayList<Music>();
                            JSONObject response = new JSONObject(json);

                            Log.v("回应", response + "");
                            int musicNum = response.getInt("musicNum");
                            for(int i = 0;i<musicNum;i++){
                                JSONObject jsonObject = new JSONObject(response.getString("music" + i));

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
                           // activity.netmusicArrayList = musicArrayList;
                           // activity.netPlaylistAdapterlow.setDatasource(musicArrayList);
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
        View view = inflater.inflate(R.layout.net_list_fragment_low, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity)getActivity();
        init();
    }
    @Override
    public ListView getListView() {
        return playlistView;
    }

    @Override
    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener) {

    }

    @Override
    public void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener) {

    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
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
