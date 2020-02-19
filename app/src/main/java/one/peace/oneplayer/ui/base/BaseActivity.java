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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import one.peace.oneplayer.util.LogTool;


/**
 * Created by pavel on 2019/11/18.
 */
public abstract class BaseActivity<T extends ViewModel> extends AppCompatActivity {
    protected ViewDataBinding mViewDataBinding;
    protected T mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = (T) ViewModelProviders.of(this).get(getViewModelClass());
        LogTool.log(this, "查看将要设置的layoutid" + getLayoutId());
        mViewDataBinding = DataBindingUtil.setContentView(this,getLayoutId());
        mViewDataBinding.setLifecycleOwner(this);
        onInitData(mViewModel, mViewDataBinding);
    }


    protected abstract Class getViewModelClass();
    protected abstract int getLayoutId();

    protected abstract void onInitData(T viewModel, ViewDataBinding viewDataBinding);


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

}
