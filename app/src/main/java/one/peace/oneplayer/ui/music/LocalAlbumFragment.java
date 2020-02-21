package one.peace.oneplayer.ui.music;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.peace.oneplayer.MainActivity;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.AlbumInfo;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.ui.base.BaseListFragment;

import one.peace.oneplayer.ui.base.UniversalAdapter;
import one.peace.oneplayer.util.ExecutorServiceUtil;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.MusicLoader;
import one.peace.oneplayer.util.OneBitmapUtil;

public class LocalAlbumFragment extends BaseListFragment<AlbumInfo, BaseListFragment.BaseListViewModel> implements IndexedEntities.IndexInfoChangedListener {
    private ObservableArrayList<AlbumInfo> tempAlbumInfos = new ObservableArrayList<>();

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_album;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        getUniversalAdapter().setOnBindHandler(new UniversalAdapter.OnBindHandler() {
            @Override
            public void onBinded(Object data) {
                final AlbumInfo albumInfo = (AlbumInfo) data;
                OneBitmapUtil.loadBitmapAsync(getContext(), albumInfo.getAlbumBitmapUrl(), new OneBitmapUtil.LoadBitmapListener() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap) {
                        albumInfo.setAlbumImage(bitmap);
                    }
                });
            }
        });
        if (tempAlbumInfos != null) {
            viewModel.getDatas().addAll(tempAlbumInfos);
        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_album;
    }

    @Override
    protected RecyclerView.LayoutManager generateLayoutManager() {
        getRecyclerView().addItemDecoration(new SpacesItemDecoration(16));
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        AlbumInfo albumInfo = (AlbumInfo)data;
        BaseActivity activity = (BaseActivity) getActivity();
        activity.jumpToActivity(AlbumDetailActivity.class,"albumInfo",albumInfo);
    }

    @Override
    public void indexInfoUpdated(Object indexValue, Object entity, int position, int newSize) {
        AlbumInfo albumInfo;
        if (getViewModel() != null) {
            albumInfo = (AlbumInfo) getViewModel().getDatas().get(position);
        } else {
            albumInfo = tempAlbumInfos.get(position);
        }
        albumInfo.setSongsNumber(newSize);
        MusicInfo musicInfo = (MusicInfo) entity;
        albumInfo.getMusicInfos().add(musicInfo);
        musicInfo.setAlbumInfo(albumInfo);
        //LogTool.log(this, "专辑更新:" + albumInfo.getAlbumName() + "," + musicInfo.getDisplayName());
    }

    @Override
    public void indexInfoAdded(Object indexValue, Object entity, int position) {
        MusicInfo musicInfo = (MusicInfo) entity;
        AlbumInfo albumInfo = new AlbumInfo(musicInfo.getAlbum());
        albumInfo.setSongsNumber(1);
        albumInfo.setSingerName(musicInfo.getArtist());
        albumInfo.setAlbumBitmapUrl(musicInfo.getUrl());
        musicInfo.setAlbumInfo(albumInfo);
        ObservableArrayList<MusicInfo> musicInfos = new ObservableArrayList<>();
        musicInfos.add(musicInfo);
        albumInfo.setMusicInfos(musicInfos);
        if (getViewModel() != null) {
            getViewModel().getDatas().add(position, albumInfo);
        } else {
            tempAlbumInfos.add(position, albumInfo);
        }
        //LogTool.log(this, "专辑增加:" + albumInfo.getAlbumName() + "," + musicInfo.getDisplayName());
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.bottom = space;
            if (parent.getChildLayoutPosition(view) % 2 == 0) {
                outRect.left = 0;
            }
        }
    }

}
