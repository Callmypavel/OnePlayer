package one.peace.oneplayer.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;
import one.peace.oneplayer.R;
import one.peace.oneplayer.databinding.ActivityMainBinding;
import one.peace.oneplayer.util.LogTool;

/**
 * Created by pavel on 2019/11/29.
 */
public abstract class BaseMultiplePageActivity extends BaseMusicControllActivity {
    private ArrayList<Fragment> fragments;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private ArrayList<String> titles;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private int currentFragmentPosition = 0;

    @Override
    protected void onInitData(ViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        tabLayout = findViewById(R.id.id_tablayout);
        viewPager = findViewById(R.id.id_viewpager);
        fragments = generateFragments();
        titles = generateTitles();
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (fragments != null) {
                    return fragments.get(position);
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                if (fragments != null) {
                    return fragments.size();
                } else {
                    return 0;
                }
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_UNCHANGED;

            }

            @Override
            public CharSequence getPageTitle(int position) {
                LogTool.log(this, "苍鹰 老虎 蝗虫" + LogTool.toString(titles) + position);
                if (titles != null) {
                    return titles.get(position);
                } else {
                    return super.getPageTitle(position);
                }

            }

        };
        viewPager.setAdapter(fragmentStatePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    protected abstract int getLayoutId();

    protected abstract ArrayList<Fragment> generateFragments();

    protected abstract ArrayList<String> generateTitles();

}
