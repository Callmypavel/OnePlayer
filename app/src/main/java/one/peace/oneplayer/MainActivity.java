package one.peace.oneplayer;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.PagerAdapter;
import one.peace.oneplayer.R;
import one.peace.oneplayer.databinding.ActivityMainBinding;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.ui.base.BaseMultiplePageActivity;
import one.peace.oneplayer.ui.music.LocalMusicFragment;
import one.peace.oneplayer.util.MusicLoader;
import one.peace.oneplayer.util.PermissionUtil;

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
    protected void disableViewInteraction() {

    }

    @Override
    protected void enableViewInteraction() {

    }
}
