package com.example.peacemaker.oneplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ListView;

/**
 * Created by 请叫我保尔 on 2015/10/26.
 */
public class LocalMusicFragmentLow extends MusicListFragmentLow implements View.OnClickListener {
    LayoutInflater layoutInflater;
    MainActivity activity;
    TextView single;
    TextView singer;
    TextView album;
    ListView playlistView;

    @Override
    public void init() {
        playlistView = (ListView)activity.findViewById(R.id.playlist_low);
        onIntializeCompleListener.onInitComple();
        single =(TextView)activity.findViewById(R.id.order_single);
        singer =(TextView)activity.findViewById(R.id.order_singer);
        album =(TextView)activity.findViewById(R.id.order_album);
        single.setOnClickListener(this);
        singer.setOnClickListener(this);
        album.setOnClickListener(this);
        playlistView.setAdapter(activity.localPlaylistAdapterlow);
        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(view,position);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("view初始化");
        this.layoutInflater = inflater;
        View view = inflater.inflate(R.layout.local_list_fragment_low, container, false);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_single:
                onOrderClickListener.onOrderClick(-1);
                break;
            case R.id.order_singer:
                onOrderClickListener.onOrderClick(-2);
                break;
            case R.id.order_album:
                onOrderClickListener.onOrderClick(-3);
                break;
        }
    }
    @Override
    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener){
        this.onOrderClickListener = onOrderClickListener;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener) {
        this.onIntializeCompleListener = onIntializeCompleListener;
    }
}
