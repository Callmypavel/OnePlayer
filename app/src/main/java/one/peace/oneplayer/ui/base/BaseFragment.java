package one.peace.oneplayer.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import one.peace.oneplayer.BR;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.BaseBean;
import one.peace.oneplayer.base.BaseViewModel;

/**
 * Created by pavel on 2019/11/14.
 */
public abstract class BaseFragment<T extends BaseViewModel,E extends BaseBean> extends Fragment implements View.OnClickListener {
    protected ViewDataBinding mViewDataBinding;
    protected T mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = (T) ViewModelProviders.of(this).get(getViewModelClass());
        mViewDataBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        mViewDataBinding.setLifecycleOwner(this);
        onSetData(mViewDataBinding);
        onInitData(mViewModel);
        return mViewDataBinding.getRoot();
    }

    protected abstract Class getViewModelClass();
    protected abstract int getLayoutId();
    protected abstract void onInitData(T viewModel);
    protected abstract void onSetData(ViewDataBinding viewDataBinding);
    protected T getViewModel(){
        return mViewModel;
    }
    protected E getEntity(){
        return (E) mViewModel.getBean();
    }
}
