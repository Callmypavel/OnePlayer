package peacemaker.oneplayer.fragment;

/**
 * Created by peace on 2017/11/28.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import peacemaker.oneplayer.R;


public class OneFragment extends Fragment{

    public RecyclerView recyclerView;
    public RecyclerView getRecyclerView(){
        return this.recyclerView;
    }


}
