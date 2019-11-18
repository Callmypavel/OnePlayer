package one.peace.oneplayer.ui.base;

import android.app.Activity;
import android.view.View;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.peace.oneplayer.R;

/**
 * Created by pavel on 2019/11/18.
 */
public abstract class BaseListActivity<E, VM extends BaseListActivity.BaseListViewModel> extends BaseActivity<VM> implements UniversalAdapter.OnItemClickListener {
    protected RecyclerView recyclerView;
    protected UniversalAdapter universalAdapter;
    public static class BaseListViewModel<E> extends ViewModel {
        public BaseListViewModel() {

        }

        private MutableLiveData<ObservableArrayList> mDatas = new MutableLiveData<>();

        public ObservableArrayList<E> getDatas() {
            return mDatas.getValue();
        }

        public void setDatas(ObservableArrayList<E> datas) {
            mDatas.setValue(datas);
        }
    }

    @Override
    protected void onInitData(VM viewModel, ViewDataBinding viewDataBinding) {
        viewModel.setDatas(new ObservableArrayList());
        recyclerView = viewDataBinding.getRoot().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(generateLayoutManager());
        UniversalAdapter<E> adapter = new UniversalAdapter(getItemLayoutId());
        adapter.setDataSource(viewModel.getDatas());
        universalAdapter = adapter;
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Class getViewModelClass() {
        return BaseListActivity.BaseListViewModel.class;
    }

    protected abstract int getItemLayoutId();
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new LinearLayoutManager(this);
    }

    public UniversalAdapter getUniversalAdapter() {
        return universalAdapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
