package peacemaker.oneplayer.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peacemaker.oneplayer.BR;
import peacemaker.oneplayer.tool.LogTool;

/**
 * Created by ouyan on 2017/12/24.
 */

public class UniversalAdapter<T> extends RecyclerView.Adapter<UniversalAdapter.BaseViewHolder> {

    protected View.OnClickListener onClickListener;
    protected View.OnLongClickListener onLongClickListener;
    protected OnBindHandler onBindHandler;
    protected ObservableArrayList<T> dataSource;
    private boolean hasHeadView = false;
    private int layoutId;

    public interface OnBindHandler<T>{
        void onBind(T data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnBindHandler(OnBindHandler onBindHandler) {
        this.onBindHandler = onBindHandler;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public ObservableArrayList<T> getDataSource() {
        return dataSource;
    }

    public void setDataSource(@NonNull ObservableArrayList<T> dataSource) {
        this.dataSource = dataSource;
        if (dataSource!=null) {
            dataSource.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
                @Override
                public void onChanged(ObservableList<T> dataSource) {

                }

                @Override
                public void onItemRangeChanged(ObservableList<T> dataSource, int positionStart, int itemCount) {
                    notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeInserted(ObservableList<T> dataSource, int positionStart, int itemCount) {
                    notifyItemRangeInserted(positionStart + 1, itemCount);
                    LogTool.log(this, "新值插入" + positionStart);
                }

                @Override
                public void onItemRangeMoved(ObservableList<T> dataSource, int fromPosition, int toPosition, int itemCount) {
                    notifyItemRangeChanged(fromPosition, toPosition);
                }

                @Override
                public void onItemRangeRemoved(ObservableList<T> dataSource, int positionStart, int itemCount) {
                    notifyItemRangeRemoved(positionStart, itemCount);
                }
            });
        }
    }

    public UniversalAdapter(int layoutId) {
        this.layoutId = layoutId;
    }


    class BaseViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder{
        public B binding;
        public BaseViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),viewType,parent,false));
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.binding.setVariable(BR.item, dataSource.get(position));
        holder.binding.setVariable(BR.onClickListener ,onClickListener);
        holder.binding.getRoot().setTag(position);
        if (onBindHandler!=null){
            onBindHandler.onBind(dataSource.get(position));
        }
        holder.binding.executePendingBindings();

    }



    @Override
    public int getItemCount() {
        if(dataSource!=null){
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutId;
    }
}
