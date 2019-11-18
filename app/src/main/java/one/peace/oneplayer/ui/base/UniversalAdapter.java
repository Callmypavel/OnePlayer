package one.peace.oneplayer.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import one.peace.oneplayer.BR;
import one.peace.oneplayer.util.LogTool;

/**
 * Created by ouyan on 2017/12/24.
 */

public class UniversalAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected OnItemClickListener mOnItemClickListener;
    protected ObservableArrayList<T> dataSource;
    protected OnBindHandler onBindHandler;
    private int layoutId;

    public interface OnBindHandler<T> {
        void onBinded(T data);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,Object data,int position);
    }

    public void setOnBindHandler(OnBindHandler onBindHandler) {
        this.onBindHandler = onBindHandler;
    }

    public ObservableArrayList<T> getDataSource() {
        return dataSource;
    }

    public void setDataSource(@NonNull ObservableArrayList<T> dataSource) {
        this.dataSource = dataSource;
        dataSource.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> dataSource) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<T> dataSource, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
                LogTool.logCrime(this, "更换");
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> dataSource, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart + 1, itemCount);
                LogTool.logCrime(this, "插入");
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> dataSource, int fromPosition, int toPosition, int itemCount) {
                notifyItemRangeChanged(fromPosition, toPosition);
                LogTool.logCrime(this, "移动");
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> dataSource, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
                LogTool.logCrime(this, "移除");
            }
        });
    }

    public UniversalAdapter(int layoutId) {
        this.layoutId = layoutId;
    }


    class BaseViewHolder extends RecyclerView.ViewHolder {
        public ViewDataBinding binding;

        public BaseViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = new BaseViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false));
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
        baseViewHolder.binding.setVariable(BR.item, dataSource.get(position));
        baseViewHolder.binding.setVariable(BR.onClickListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v,v.getTag(),position);
                }
            }
        });
        baseViewHolder.binding.executePendingBindings();
        if (onBindHandler != null) {
            onBindHandler.onBinded(dataSource.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return layoutId;
    }
}
