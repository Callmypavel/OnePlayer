package com.example.peacemaker.oneplayer;

import android.util.Log;
import android.view.View;

/**
 * Created by ouyan on 2016/7/7.
 */

public class OneHandler {
    public void onButtonClick(View view){
        OneApplication oneApplication = (OneApplication)(view.getContext().getApplicationContext());
        switch (view.getId()){
            case R.id.music_item:
                break;
            case R.id.play_view_previous:
                oneApplication.previous();
                break;
            case R.id.play_view_next:
                oneApplication.next();
                break;
            case R.id.play_view_queue:
                oneApplication.queue();
                break;
            case R.id.play_view_playmode:
                oneApplication.changePlayMode();
                break;
            case R.id.bottom_play_button:
                oneApplication.play();
                break;
            case R.id.bottom_controll_bar:
                oneApplication.toPlayView();
                break;

            }
        }
}

