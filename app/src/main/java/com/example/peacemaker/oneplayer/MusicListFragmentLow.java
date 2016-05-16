package com.example.peacemaker.oneplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by 请叫我保尔 on 2015/9/27.
 */
public abstract class MusicListFragmentLow extends Fragment {
    OnItemClickListener onItemClickListener;
    OnOrderClickListener onOrderClickListener;
    OnIntializeCompleListener onIntializeCompleListener;
    public abstract void setOnOrderClickListener(OnOrderClickListener onOrderClickListener);
    public abstract ListView getListView();
    public abstract void init();
    public abstract void setOnItemClickListener(OnItemClickListener onItemClickListener);
    public abstract void setOnIntializeCompleListener(OnIntializeCompleListener onIntializeCompleListener);
}