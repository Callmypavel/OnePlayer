package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.LogRecord;

/**
 * Created by 请叫我保尔 on 2015/9/26.
 */
public class SerchingFragment extends Fragment  {
    public TextView folder;
    public TextView filename;
    public ArrayList<Music> musicArrayList;
    OneMusicloader oneMusicloader;
    Handler handler;

    File file = new File("/mnt/sdcard");
    //三个一般必须重载的方法
    public void init(Handler handler){
        System.out.println("接到棒棒");
        this.handler = handler;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("ExampleFragment--onCreate");


    }

    @Override
    public void onStart() {
        super.onStart();
        final Handler searchhandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==0x126){
                    String name = msg.getData().getString("name");
                    filename.setText(name);
                }
                return false;
            }
        });
        oneMusicloader = new OneMusicloader(searchhandler);
        final AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                musicArrayList = oneMusicloader.getdeepLoad(file);
                System.out.println("刚接到的"+musicArrayList);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                System.out.println("执行完毕");
                SerchingFragment.this.onDetach();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("music",musicArrayList);
                message.what = 0x127;
                message.setData(bundle);
                handler.sendMessage(message);
                System.out.println("扫描后的" + musicArrayList);
                System.out.println("肥皂已扔 蛤蛤");
            }
        };
        asyncTask.execute();
        System.out.println("onStart()结束了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        System.out.println("ExampleFragment--onCreateView");
        final View view = inflater.inflate(R.layout.search_dialog,container, false);
        folder = (TextView) view.findViewById(R.id.folder);
        filename = (TextView) view.findViewById(R.id.file);
        System.out.println("检查搜索view"+view);
        return view;

    }

    @Override
    public void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        System.out.println("ExampleFragment--onPause");
    }



    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        System.out.println("ExampleFragment--onResume");
    }

    @Override
    public void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
        System.out.println("ExampleFragment--onStop");
    }



}