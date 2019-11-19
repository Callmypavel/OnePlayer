package peacemaker.oneplayer.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peacemaker.oneplayer.databinding.SimpleListFragmentBinding;

import peacemaker.oneplayer.R;



/**
 * Created by ouyan on 2017/12/17.
 */

public class SimpleListFragment extends Fragment {
    private SimpleListFragmentBinding binding;
    private OnRecyclerViewConfigureListener onRecyclerViewConfigureListener;
    private OnRecyclerViewDataSetListener onRecyclerViewDataSetListener;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public interface OnRecyclerViewDataSetListener{
        void OnRecyclerViewDataSet(SimpleListFragmentBinding binding, SimpleListFragment simpleListFragment);
    }
    public interface OnRecyclerViewConfigureListener{
        void OnRecyclerViewConfigure(RecyclerView recyclerView);
    }

    public void refresh(){
        binding.recyclerview.refresh();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.baby_fragment,container,false);
        binding = SimpleListFragmentBinding.inflate(inflater,container,false);
        //recyclerView = (RecyclerView) ((ViewGroup)view).getChildAt(0);
        if(onRecyclerViewDataSetListener!=null){
            onRecyclerViewDataSetListener.OnRecyclerViewDataSet(binding,this);
        }
        if(onRecyclerViewConfigureListener!=null){
            onRecyclerViewConfigureListener.OnRecyclerViewConfigure((RecyclerView) binding.getRoot().findViewById(R.id.recyclerview));
        }
        return binding.getRoot();
    }

    public void setOnRecyclerViewConfigureListener(OnRecyclerViewConfigureListener onRecyclerViewConfigureListener){
        this.onRecyclerViewConfigureListener = onRecyclerViewConfigureListener;
    }

    public void setOnRecyclerViewDataSetListener(OnRecyclerViewDataSetListener onRecyclerViewDataSetListener) {
        this.onRecyclerViewDataSetListener = onRecyclerViewDataSetListener;
    }

    public SimpleListFragment setupWithOnRecyclerViewDataSetListener(OnRecyclerViewDataSetListener onRecyclerViewDataSetListener, String tag){
        setOnRecyclerViewDataSetListener(onRecyclerViewDataSetListener);
        setTitle(tag);
        return this;
    }
    public RecyclerView getRecyclerView(){
        return binding.recyclerview;
    }


}
