package one.peace.oneplayer.ui.base;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.peace.oneplayer.R;

public abstract class BaseListFragment<E, VM extends BaseListFragment.BaseListViewModel> extends BaseFragment<VM> implements UniversalAdapter.OnItemClickListener {
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
            //LogTool.logCrime(this,"捕捉"+datas.size());
        }
    }

    @Override
    protected void onInitData(VM viewModel, ViewDataBinding viewDataBinding) {
        if (viewModel.getDatas() == null) {
            //LogTool.log(this,"重放数据");
            viewModel.setDatas(new ObservableArrayList());
        }
        recyclerView = viewDataBinding.getRoot().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(generateLayoutManager());
        UniversalAdapter<E> adapter = new UniversalAdapter(getItemLayoutId());
        adapter.setDataSource(viewModel.getDatas());
        universalAdapter = adapter;
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        //LogTool.log(this,"月读！"+viewModel.getDatas());
    }

    @Override
    protected void onPermissionAllGranted() {

    }

    @Override
    protected Class getViewModelClass() {
        return BaseListFragment.BaseListViewModel.class;
    }

    protected abstract int getItemLayoutId();
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public UniversalAdapter getUniversalAdapter() {
        return universalAdapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
