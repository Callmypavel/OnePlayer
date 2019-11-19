package peacemaker.oneplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import peacemaker.oneplayer.R;
import peacemaker.oneplayer.entity.Music;
import peacemaker.oneplayer.fragment.OneAlbumListFragment;
import peacemaker.oneplayer.fragment.OneHomePageFragment;
import peacemaker.oneplayer.fragment.OnePlayListFragment;
import peacemaker.oneplayer.fragment.OneSingerListFragment;
import peacemaker.oneplayer.tool.LogTool;
import peacemaker.oneplayer.view.IndexView;

import java.util.ArrayList;


/**
 * Created by ouyang on 2017/2/16.
 */

public class OneLocalMusicActivity extends OneActivity {
//    @BindView(R.id.id_tablayout)
    public TabLayout tabLayout;
//    @BindView(R.id.id_viewpager)
    public ViewPager viewPager;
//    @BindView(R.id.one_index_view)
    public IndexView indexView;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private OnePlayListFragment onePlayListFragment;
    private OneAlbumListFragment oneAlbumListFragment;
    private OneSingerListFragment oneSingerlistFragment;
    private ArrayList<Fragment> oneFragments;
    private int currentFragmentPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oneFragments = new ArrayList<>();
        oneSingerlistFragment = new OneSingerListFragment();
        oneAlbumListFragment = new OneAlbumListFragment();
        onePlayListFragment = new OnePlayListFragment();
        oneFragments.add(oneSingerlistFragment);
        oneFragments.add(oneAlbumListFragment);
        oneFragments.add(onePlayListFragment);
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(oneFragments!=null) {
                    return oneFragments.get(position);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_UNCHANGED;

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0){
                    return "歌手";
                }else if(position==1) {
                    return "专辑";
                }else if(position==2){
                    return "歌曲";
                }else {
                    return super.getPageTitle(position);
                }
            }
        };
        viewPager.setAdapter(fragmentStatePagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v("MainActivity","onPageSelected"+position);
                updateIndexView(position);
                //viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onPermissionGranted(Boolean isPermissionUpdated) {
        if (!isPermissionUpdated){
            initPermission();
        }
    }

    private void updateIndexView(int position){
        Log.v("MainActivity","isScrollBy更新位置"+position);
        if (position == 0) {
            currentFragmentPosition = 0;
            indexView.setIndexedMusics(oneApplication.getIndexedSingerArrayList());
            indexView.setRecyclerView(oneSingerlistFragment.getRecyclerView(),true);
        } else if(position==1){
            currentFragmentPosition = 1;
            indexView.setIndexedMusics(oneApplication.getIndexedAlbumArrayList());
            indexView.setRecyclerView(oneAlbumListFragment.getRecyclerView(),false);

        } else if(position==2){
            currentFragmentPosition = 2;
            indexView.setIndexedMusics(oneApplication.getIndexedSongArrayList());
            indexView.setRecyclerView(onePlayListFragment.getRecyclerView(),true);

        }
    }
//    public void toSecondItemActivity(Music music){
//        LogTool.log("MainActivity","更新目标音乐"+music.getUrl());
//        oneApplication.updateTargetMusic(music);
//        if(currentFragmentPosition==0){
//            toSingerDetail();
//        }else if(currentFragmentPosition==1) {
//            toAlbumDetail();
//        }
//    }
    public void toSingerDetail(){
        Log.v("MainActivity","toSingerDetail");
        Intent intent = new Intent();
        intent.setClass(OneLocalMusicActivity.this,OneSingerDetailActivity.class);
        startActivity(intent);
    }
    public void toAlbumDetail(){
        Intent intent = new Intent();
        intent.setClass(OneLocalMusicActivity.this,OneAlbumDetailActivity.class);
        startActivity(intent);
    }
}
