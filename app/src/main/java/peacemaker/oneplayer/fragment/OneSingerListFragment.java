package peacemaker.oneplayer.fragment;

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
import android.view.View;
import android.view.ViewGroup;

import peacemaker.oneplayer.activity.OneLocalMusicActivity;
import peacemaker.oneplayer.view.OneSingerViewHolder;
import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.MainActivity;
import peacemaker.oneplayer.activity.OneApplication;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.listener.OnItemHitListener;

import java.util.ArrayList;


/**
 * Created by ouyan on 2016/6/10.
 */

public class OneSingerListFragment extends OneFragment {
    private MainActivity mainActivity;
    private OneApplication oneApplication;
    private LinearLayoutManager linearLayoutManager;
    private OneSingerItemAdapter oneSingerItemAdapter;
    private boolean isNeedInit = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        oneApplication = (OneApplication) getActivity().getApplication();
        mainActivity = (MainActivity) getActivity();
//        oneSingerItemAdapter = new OneSingerItemAdapter(oneApplication.getSingerArrayList());
//        oneSingerItemAdapter.setOnItemHitListener(new OnItemHitListener() {
//            @Override
//            public void onItemHit(int position, Music music) {
//                ((MainActivity)getActivity()).toSecondItemActivity(music);
//
//            }
//        });

        if(isNeedInit) {
            //updateIndexView();
            isNeedInit = false;
        }

        //linearLayoutManager.scrollToPositionWithOffset(activity.singerSelectedPosition, activity.singerSelectedOffset);
    }
//    public void updateIndexView(){
//        mainActivity.binding.oneIndexView.setIndexedMusics(oneApplication.getIndexedSingerArrayList());
//        mainActivity.binding.oneIndexView.setRecyclerView(recyclerView,true);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_singerlist_fragment,container,false);
        Log.v("OneSingerListFragment","onCreateView");
        recyclerView = (RecyclerView) view.findViewById(R.id.one_fragment_recyclerView);
        initialize();
        return view;
    }


    public void initialize(){
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
            if(musicArrayList!=null) {
                if(musicArrayList.size()!=0) {
                    Music music = musicArrayList.get(position);
                    //music.setDisplayName(music.getDisplayName()+"(第"+position+"个)");
//                    holder.bind(music, music.getSecondItems().size());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = holder.getLayoutPosition();
                            if (onItemHitListener != null && position != -1) {
                                onItemHitListener.onItemHit(position, (Music) v.getTag());
                            }
                        }
                    });
                    return;
                }
            }
            Music music = new Music("找不到音乐");
            holder.nullBind(music);

        }

        @Override
        public int getItemCount() {
            if(musicArrayList!=null){
                return musicArrayList.size();
            }
            return 1;
        }

    }

}
