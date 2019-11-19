package peacemaker.oneplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.activity.OneLocalMusicActivity;


/**
 * Created by ouyang on 2017/2/16.
 */

public class OneMymusicFragment extends Fragment {
    public Button localMusicEntryButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_mymusic_fragment,container,false);
        localMusicEntryButton = (Button)view.findViewById(R.id.local_music_entry_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localMusicEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), OneLocalMusicActivity.class);
                startActivity(intent);
            }
        });
    }
}
