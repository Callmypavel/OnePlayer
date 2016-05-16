package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 请叫我保尔 on 2015/9/27.
 */
public abstract class MusicListFragment extends Fragment {


//    public void init(Context context){
//        this.context = context;
//    }
    OnOrderClickListener onOrderClickListener;
    OnIntializeCompleListener onIntializeCompleListener;
    public abstract void setOnOrderClickListener(OnOrderClickListener onOrderClickListener);
    public abstract RecyclerView getRecyclerView();
    public abstract void init();
    public abstract void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener);

}
