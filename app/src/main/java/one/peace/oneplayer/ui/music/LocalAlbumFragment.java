package one.peace.oneplayer.ui.music;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.AlbumInfo;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.ui.base.BaseListFragment;

import one.peace.oneplayer.ui.base.UniversalAdapter;
import one.peace.oneplayer.util.ExecutorServiceUtil;
import one.peace.oneplayer.util.MusicLoader;
import one.peace.oneplayer.util.OneBitmapUtil;

public class LocalAlbumFragment extends BaseListFragment<AlbumInfo, BaseListFragment.BaseListViewModel> implements MusicLoader.LoadMusicListener {
    private IndexedEntities<MusicInfo> indexedEntities;
    private ArrayList<MusicInfo> musicDataSource = new ArrayList<>();
    private ObservableArrayList<AlbumInfo> albumInfos;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_album;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        if (albumInfos == null) {
            indexedEntities = new IndexedEntities<MusicInfo>(MusicInfo.class, "album", musicDataSource, new IndexedEntities.IndexInfoChangedListener() {
                @Override
                public void indexInfoUpdated(Object indexValue, Object entity, int position, int newSize) {
                    AlbumInfo albumInfo = (AlbumInfo) viewModel.getDatas().get(position);
                    albumInfo.setSongsNumber(newSize);
                }

                @Override
                public void indexInfoAdded(Object indexValue, Object entity, int position) {
                    MusicInfo musicInfo = (MusicInfo) entity;
                    AlbumInfo albumInfo = new AlbumInfo(musicInfo.getAlbum());
                    albumInfo.setSongsNumber(1);
                    albumInfo.setSingerName(musicInfo.getArtist());
                    albumInfo.setAlbumBitmapUrl(musicInfo.getUrl());
                    viewModel.getDatas().add(position, albumInfo);
                    if (albumInfos == null) {
                        albumInfos = new ObservableArrayList<>();
                    }
                    albumInfos.add(position, albumInfo);
                }
            });
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
        } else {
            viewModel.getDatas().addAll(albumInfos);
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadPass(String fileName) {

    }

    @Override
    public void loadFound(MusicInfo musicInfo) {
        if (indexedEntities == null) {
            musicDataSource.add(musicInfo);
        } else {
            if (indexedEntities.getIndexInfoChangedListener() != null) {
                indexedEntities.addNew(musicInfo);
            } else {
                musicDataSource.add(musicInfo);
            }
        }

    }

    @Override
    public void loadFinished() {

    }

    @Override
    public void loadProgressChanged(int progress) {

    }
}
