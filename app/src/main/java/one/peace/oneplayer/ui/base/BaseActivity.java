package one.peace.oneplayer.ui.base;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import one.peace.oneplayer.base.OneApplication;
import one.peace.oneplayer.global.config.Config;
import one.peace.oneplayer.ui.music.SoundEffectActivity;
import one.peace.oneplayer.util.DialogUtil;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.PermissionUtil;
import one.peace.oneplayer.util.StringUtil;
import one.peace.oneplayer.util.ViewTool;


/**
 * Created by pavel on 2019/11/18.
 */
public abstract class BaseActivity<T extends ViewModel> extends AppCompatActivity {
    protected ViewDataBinding mViewDataBinding;
    protected T mViewModel;
    protected Config mConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = (T) ViewModelProviders.of(this).get(getViewModelClass());
        LogTool.log(this, "查看将要设置的layoutid" + getLayoutId());
        mViewDataBinding = DataBindingUtil.setContentView(this,getLayoutId());
        mViewDataBinding.setLifecycleOwner(this);
        onInitData(mViewModel, mViewDataBinding);
        Config.getInstance(this, new Config.ConfigListener() {
            @Override
            public void onConfigLoaded(Config config) {
                mConfig = config;
//                mViewDataBinding.setVariable(BR.config,config);
//                ViewTool.setStatusColor(BaseActivity.this, mConfig.getThemeColor());
            }
        });

        PermissionUtil.requestAllPermissions(this, new PermissionUtil.OnPermissionStateListener() {
            @Override
            public void onPermissionAllGranted(boolean isPermissionAllGranted) {
                if (isPermissionAllGranted){
                    //所有权限都已获取
                    ArrayList<Fragment> fragments = getFragments();
                    if (fragments != null){
                        for (Fragment fragment : fragments) {
                            if (fragment instanceof BaseFragment){
                                BaseFragment baseFragment = (BaseFragment)fragment;
                                baseFragment.onPermissionAllGranted();
                            }
                        }
                    }
                }
            }
        });
    }


    protected abstract Class getViewModelClass();
    protected abstract int getLayoutId();

    protected abstract void onInitData(T viewModel, ViewDataBinding viewDataBinding);

    protected ArrayList<Fragment> getFragments(){
        return null;
    }


    protected T getViewModel(){
        return mViewModel;
    }

    public Object getFromIntent(String key){
        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getBundleExtra("data");
            if (bundle != null){
                return bundle.get(key);
            }
        }
        return null;
    }

    public Intent setIntoIntent(String key,Object value){
        Intent intent = getIntent();
        if (intent == null){
            intent = new Intent();
        }
        Bundle bundle = intent.getBundleExtra("data");
        if (bundle == null){
            bundle = new Bundle();
        }
        if(value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable)value);
        }else if(value instanceof ArrayList) {
            try {
                ArrayList<Parcelable> arrayList = (ArrayList<Parcelable>)value;
                bundle.putParcelableArrayList(key, arrayList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        intent.putExtra("data",bundle);
        return intent;
    }

    public void jumpToActivity(Class targetActivityClass,@NonNull Intent intent){
        intent.setClass(this,targetActivityClass);
        startActivity(intent);
    }

    public void jumpToActivity(Class targetActivityClass,String key,Object value){
        Intent intent = setIntoIntent(key,value);
        intent.setClass(this,targetActivityClass);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onPermissionResult(this,requestCode, grantResults);
    }


}
