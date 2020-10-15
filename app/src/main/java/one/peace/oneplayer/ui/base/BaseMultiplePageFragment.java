package one.peace.oneplayer.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;
import one.peace.oneplayer.R;
import one.peace.oneplayer.util.LogTool;

public abstract class BaseMultiplePageFragment extends BaseFragment {
    private ArrayList<Fragment> fragments;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private ArrayList<String> titles;
    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private int currentFragmentPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_page, container, false);
        tabLayout = view.findViewById(R.id.id_fragment_tablayout);
        viewPager = view.findViewById(R.id.id_fragment_viewpager);
        fragments = generateFragments();
        titles = generateTitles();
        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
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
        //防止销毁
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(fragmentStatePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInit();
    }

    protected abstract ArrayList<Fragment> generateFragments();

    protected abstract ArrayList<String> generateTitles();

    protected abstract void onInit();

    @Override
    protected Class getViewModelClass() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onInitData(ViewModel viewModel, ViewDataBinding viewDataBinding) {

    }
}
