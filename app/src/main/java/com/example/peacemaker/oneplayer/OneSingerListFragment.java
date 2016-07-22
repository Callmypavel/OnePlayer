package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.peacemaker.oneplayer.MainActivity;
import com.example.peacemaker.oneplayer.Music;
import com.example.peacemaker.oneplayer.R;
import com.example.peacemaker.oneplayer.databinding.OneSingerlistItemBinding;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/10.
 */

public class OneSingerListFragment extends Fragment {
    @BindView(R.id.one_singer_recycler)
    public RecyclerView recyclerView;
    private OneApplication oneApplication;
    private LinearLayoutManager linearLayoutManager;
    private OneSingerItemAdapter oneSingerItemAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        oneApplication = (OneApplication) getActivity().getApplication();
        oneSingerItemAdapter = new OneSingerItemAdapter(oneApplication.getSingerArrayList());
        oneSingerItemAdapter.setOnItemHitListener(new OnItemHitListener() {
            @Override
            public void onItemHit(int position, Music music) {
                oneApplication.selectMusic(music,position);
            }
        });
        linearLayoutManager = new LinearLayoutManager(oneApplication);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(oneSingerItemAdapter);
        final Paint paint = new Paint();
        paint.setColor(Color.rgb(192,192,192));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                final int left = parent.getPaddingLeft()+20;
                final int right = parent.getMeasuredWidth() - parent.getPaddingRight()-20;
                final int childSize = parent.getChildCount() ;
                for(int i=0 ; i <childSize ; i ++){
                    final View child = parent.getChildAt(i) ;
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                    final int top = child.getBottom() + layoutParams.bottomMargin ;
                    final int bottom = top + 2 ;
                    c.drawRect(left,top,right,bottom,paint);
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        //linearLayoutManager.scrollToPositionWithOffset(activity.singerSelectedPosition, activity.singerSelectedOffset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_singerlist_fragment,container,false);
        Log.v("OneSingerListFragment","onCreateView");
        ButterKnife.bind(this,view);
        initialize();
        return view;
    }


    public void initialize(){

    }
    private class OneSingerItemAdapter extends RecyclerView.Adapter<OneSingerViewHolder>{
        private ArrayList<Music> musicArrayList;
        private OnItemHitListener onItemHitListener;
        public OneSingerItemAdapter(ArrayList<Music> musicArrayList){
            this.musicArrayList = musicArrayList;
        }
        public void setOnItemHitListener(OnItemHitListener onItemHitListener){
            this.onItemHitListener = onItemHitListener;
        }

        @Override
        public OneSingerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            OneSingerViewHolder oneSingerViewHolder = OneSingerViewHolder.createHolder(LayoutInflater.from(parent.getContext()),parent);
            return oneSingerViewHolder;
        }

        @Override
        public void onBindViewHolder(final OneSingerViewHolder holder, int position) {
            Music music = musicArrayList.get(position);
            holder.bind(music, music.getSecondItems().size());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    if(onItemHitListener!=null&&position!=-1){
                        onItemHitListener.onItemHit(position,(Music)v.getTag());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if(musicArrayList!=null){
                return musicArrayList.size();
            }
            return 0;
        }

    }

}
