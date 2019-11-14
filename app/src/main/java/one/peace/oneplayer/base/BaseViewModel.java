package one.peace.oneplayer.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by pavel on 2019/11/14.
 */
public abstract class BaseViewModel<T extends BaseBean> extends ViewModel {
    public MutableLiveData<T> mLiveData;
    protected T bean;
    public BaseViewModel(){
        mLiveData = new MutableLiveData<>();
        bean = initializeBean();
        bean.setViewModel(this);
        mLiveData.setValue(bean);
    }
    public void updateData(){
        if(mLiveData == null){
            mLiveData = new MutableLiveData<>();
        }
        mLiveData.setValue(mLiveData.getValue());
    }

    public T getBean(){
        return bean;
    }

    protected abstract T initializeBean();
}
