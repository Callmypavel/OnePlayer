package one.peace.oneplayer.ui.music;


import android.graphics.Bitmap;
import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import one.peace.oneplayer.R;
import one.peace.oneplayer.databinding.ActivityAlbumDetailBinding;
import one.peace.oneplayer.music.entity.AlbumInfo;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.ui.base.BaseListActivity;
import one.peace.oneplayer.util.DataUtil;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.OneBitmapUtil;


/**
 * Created by ouyan on 2016/10/8.
 */

public class AlbumDetailActivity extends BaseListActivity<MusicInfo, AlbumDetailActivity.AlbumDetailViewModel> {

    public static class AlbumDetailViewModel extends BaseListActivity.BaseListViewModel<MusicInfo> {
        public AlbumDetailViewModel() {

        }

        private MutableLiveData<AlbumInfo> mAlbumInfoMutableLiveData = new MutableLiveData<>();

        public AlbumInfo getAlbumInfo() {
            return mAlbumInfoMutableLiveData.getValue();
        }

        public void setAlbumInfo(AlbumInfo albumInfo) {
            mAlbumInfoMutableLiveData = new MutableLiveData<>();
            mAlbumInfoMutableLiveData.setValue(albumInfo);
            getDatas().addAll(albumInfo.getMusicInfos());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void onInitData(AlbumDetailViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        ActivityAlbumDetailBinding binding = (ActivityAlbumDetailBinding)viewDataBinding;
        final AlbumInfo albumInfo = (AlbumInfo) getFromIntent("albumInfo");
        OneBitmapUtil.loadBitmapAsync(this, albumInfo.getAlbumBitmapUrl(), new OneBitmapUtil.LoadBitmapListener() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                albumInfo.setAlbumImage(bitmap);
            }
        });
        LogTool.log(this);
        if (albumInfo != null){
            viewModel.setAlbumInfo(albumInfo);
        }
        binding.setAlbumInfo(viewModel.getAlbumInfo());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumDetailActivity.this.finish();
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
        return AlbumDetailViewModel.class;
    }
}
