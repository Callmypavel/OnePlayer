package one.peace.oneplayer.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;


/**
 * Created by pavel on 2019/11/14.
 */
public abstract class BaseFragment<T extends ViewModel> extends Fragment{
    protected ViewDataBinding mViewDataBinding;
    protected T mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel = (T) ViewModelProviders.of(this).get(getViewModelClass());
        mViewDataBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        mViewDataBinding.setLifecycleOwner(this);
        onInitData(mViewModel, mViewDataBinding);
        return mViewDataBinding.getRoot();
    }

    protected abstract Class getViewModelClass();
    protected abstract int getLayoutId();

    protected abstract void onInitData(T viewModel, ViewDataBinding viewDataBinding);


    protected T getViewModel(){
        return mViewModel;
    }
}
