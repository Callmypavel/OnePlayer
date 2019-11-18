package one.peace.oneplayer.ui.music;


import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import one.peace.oneplayer.R;
import one.peace.oneplayer.databinding.ActivitySingerDetailBinding;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;
import one.peace.oneplayer.ui.base.BaseListActivity;
import one.peace.oneplayer.util.DataUtil;

/**
 * Created by ouyan on 2016/7/18.
 */

public class SingerDetailActivity extends BaseListActivity<MusicInfo, SingerDetailActivity.SingerDetailViewModel> {


    public static class SingerDetailViewModel extends BaseListActivity.BaseListViewModel<MusicInfo> {
        public SingerDetailViewModel() {

        }

        private MutableLiveData<SingerInfo> mSingerInfoMutableLiveData = new MutableLiveData<>();

        public SingerInfo getSingerInfo() {
            return mSingerInfoMutableLiveData.getValue();
        }

        public void setSingerInfo(SingerInfo singerInfo) {
            mSingerInfoMutableLiveData = new MutableLiveData<>();
            mSingerInfoMutableLiveData.setValue(singerInfo);
            getDatas().addAll(singerInfo.getMusicInfos());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_singer_detail;
    }

    @Override
    protected void onInitData(SingerDetailViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        ActivitySingerDetailBinding binding = (ActivitySingerDetailBinding)viewDataBinding;
        SingerInfo singerInfo = (SingerInfo) getFromIntent("singerInfo");
        if (singerInfo != null){
            viewModel.setSingerInfo(singerInfo);
        }
        binding.setSingeInfo(viewModel.getSingerInfo());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingerDetailActivity.this.finish();
            }
        });
    }


    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_song;
    }


    @Override
    public void onItemClick(View view, Object data, int position) {

    }

    @Override
    protected Class getViewModelClass() {
        return SingerDetailViewModel.class;
    }
}
