package one.peace.oneplayer;

import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.android.material.navigation.NavigationView;

import one.peace.oneplayer.ui.base.BaseMultiplePageActivity;
import one.peace.oneplayer.ui.music.LocalMusicFragment;
import one.peace.oneplayer.util.LogTool;

public class MainActivity extends BaseMultiplePageActivity {

    public static class MainViewModel extends ViewModel{

    }

    @Override
    protected Class getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected ArrayList<Fragment> generateFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalMusicFragment(this));
        return fragments;
    }

    @Override
    protected ArrayList<String> generateTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("音乐");
        return titles;
    }

    @Override
    protected void onInitData(ViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                LogTool.log(this,"看看菜单item各项"+LogTool.toString(menuItem));
                if ("主题换肤".contentEquals(menuItem.getTitle())){
                    LogTool.log(this,"点击主题换肤");
                }
                return false;
            }
        });
    }

    @Override
    protected void disableViewInteraction() {

    }

    @Override
    protected void enableViewInteraction() {

    }
}
