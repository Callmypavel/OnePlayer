package one.peace.oneplayer;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import one.peace.oneplayer.R;
import one.peace.oneplayer.databinding.ActivityMainBinding;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.util.PermissionUtil;

public class MainActivity extends BaseActivity<MainActivity.MainViewModel> {

    public static class MainViewModel extends ViewModel {

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
    protected void onInitData(MainViewModel viewModel, ViewDataBinding viewDataBinding) {
        PermissionUtil.requestAllPermission(this);
        ActivityMainBinding binding = (ActivityMainBinding)viewDataBinding;
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_music)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController( binding.navView, navController);
    }

}
