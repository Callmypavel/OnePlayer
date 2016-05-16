package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;

/**
 * Created by 请叫我保尔 on 2015/10/26.
 */
public class LocalMusicFragment extends MusicListFragment implements View.OnClickListener {
    RecyclerView playrecycleView;
    LayoutInflater layoutInflater;
    Context context;
    MainActivity activity;
    LinearLayoutManager linearLayoutManager;
    TextView single;
    TextView singer;
    TextView album;
    ListView playlistView;
    OnIntializeCompleListener onIntializeCompleListener;
    @Override
    public void init() {
        System.out.println("fragment初始化");
        playrecycleView = (RecyclerView)activity.findViewById(R.id.playList);
        onIntializeCompleListener.onInitComple();
        Log.v("LocalMusicFragment", "已经初始化了 辣鸡");
        single =(TextView)activity.findViewById(R.id.order_single);
        singer =(TextView)activity.findViewById(R.id.order_singer);
        album =(TextView)activity.findViewById(R.id.order_album);
        single.setOnClickListener(this);
        singer.setOnClickListener(this);
        album.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(activity);
        playrecycleView.setLayoutManager(linearLayoutManager);
        playrecycleView.setAdapter(activity.localplaylistAdapter);
        playrecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        Log.v("LocalMusicFragment本地庆典", playrecycleView.getLayoutManager() + "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("view初始化");
        this.layoutInflater = inflater;
        View view = inflater.inflate(R.layout.local_list_fragment, container, false);
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
    public void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener){
        this.onIntializeCompleListener = onIntializeCompleListener;
    }
}
