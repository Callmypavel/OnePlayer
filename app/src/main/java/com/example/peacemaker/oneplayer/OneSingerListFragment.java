package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ouyan on 2016/6/10.
 */

public class OneSingerListFragment extends Fragment {
    @BindView(R.id.one_singer_list)
    public ListView oneSingerListView;
    private int selectedPosition;
    private MainActivity activity;
    private boolean isEnable = true;
    private OneSingerItemAdapter oneSingerItemAdapter;

    @Override
    public void onAttach(Context context) {
        Log.v("OneSingerListFragment","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.v("OneSingerListFragment","onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        activity.singerSelectedPosition = selectedPosition;
        Log.v("OneSingerListFragment","onDestroy()死去"+selectedPosition);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.v("OneSingerListFragment","onDestroyView");
        super.onDestroyView();
    }
    public void disable(){
//        if(oneSingerListView!=null) {
//            oneSingerListView.setEnabled(false);
//            oneSingerListView.setClickable(false);
//        }
        isEnable = false;
        oneSingerItemAdapter.notifyDataSetChanged();
    }
    public void enable(){
//        if(oneSingerListView!=null) {
//            oneSingerListView.setEnabled(true);
//            oneSingerListView.setClickable(true);
//        }
        isEnable = true;
        oneSingerItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        if(oneSingerItemAdapter!=null){
            oneSingerItemAdapter.addAll(activity.getSingerArraylist());
        }
        oneSingerItemAdapter.notifyDataSetChanged();
        selectedPosition = activity.singerSelectedPosition;
        Log.v("OneSingerListFragment","onActivityCreated()复活"+selectedPosition);
        oneSingerListView.setSelection(selectedPosition);
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
        oneSingerItemAdapter = new OneSingerItemAdapter(getActivity());
        oneSingerListView.setAdapter(oneSingerItemAdapter);
        oneSingerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(oneSingerItemAdapter!=null) {
                    Music music = oneSingerItemAdapter.getItem(position);
                    activity.itemSelected(music,position);
                }
            }
        });
        oneSingerListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.v("OneSingerListFragment","滚动状态改变"+scrollState+","+AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    selectedPosition = oneSingerListView.getFirstVisiblePosition();
                    //Log.v("OneSingerListFragment","停止滚动"+scrolledX+","+scrolledY);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Log.v("OneSingerListFragment","滚动监听1--firstVisibleItem:"+firstVisibleItem+","+visibleItemCount+","+totalItemCount);
                //Log.v("OneSingerListFragment","滚动监听2--firstVisibleItem:"+oneSingerListView.getScrollX()+","+oneSingerListView.getScrollY());

            }
        });
    }
    private class OneSingerItemAdapter extends ArrayAdapter<Music> {
        private Activity activity;

        @Override
        public boolean areAllItemsEnabled() {
            return isEnable;
        }

        public OneSingerItemAdapter(Activity context) {
            super(context, R.layout.one_singerlist_fragment, new ArrayList<Music>());
            activity = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Music music = getItem(position);
            OneViewHolder oneViewHolder = new OneViewHolder();
            return oneViewHolder.getHolderView(convertView,activity,parent,music);
        }



        public class OneViewHolder{
            private TextView singerTextView;
            private TextView songsnumberTextView;
            private ImageView singerImageView;

            View getHolderView(View convertView,Activity activity,ViewGroup parent,Music music) {
                OneViewHolder oneViewHolder;
                if(convertView==null){
                    convertView = activity.getLayoutInflater().inflate(R.layout.one_singerlist_item,parent,false);
                    oneViewHolder = new OneViewHolder();
                    oneViewHolder.singerTextView = ButterKnife.findById(convertView,R.id.one_singer_item_singername);
                    oneViewHolder.songsnumberTextView = ButterKnife.findById(convertView,R.id.one_singer_item_songsnumber);
                    oneViewHolder.singerImageView = ButterKnife.findById(convertView,R.id.one_singer_item_singerimage);
                    convertView.setTag(oneViewHolder);
                }else {
                    oneViewHolder = (OneViewHolder) convertView.getTag();
                }
                oneViewHolder.singerTextView.setText(music.getDisplayName());
                if(music.getSecondItems()!=null) {
                    oneViewHolder.songsnumberTextView.setText(music.getSecondItems().size()+"首歌曲");
                }
                return convertView;
            }


        }

    }

}
