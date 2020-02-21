package one.peace.oneplayer.ui.music;

import android.view.View;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.AlbumInfo;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.ui.base.BaseListFragment;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.MusicLoader;

public class LocalSingerFrament extends BaseListFragment<SingerInfo, BaseListFragment.BaseListViewModel> implements IndexedEntities.IndexInfoChangedListener {
    private ObservableArrayList<SingerInfo> tempSingerInfos = new ObservableArrayList<>();

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_singer;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        if (tempSingerInfos != null) {
            viewModel.getDatas().addAll(tempSingerInfos);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_singer;
    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        SingerInfo singerInfo = (SingerInfo) data;
        BaseActivity activity = (BaseActivity) getActivity();
        activity.jumpToActivity(SingerDetailActivity.class, "singerInfo", singerInfo);
    }

    @Override
    public void indexInfoUpdated(Object indexValue, Object entity, int position, int newSize) {
        SingerInfo singerInfo;
        if (getViewModel() != null) {
            singerInfo = (SingerInfo) getViewModel().getDatas().get(position);
        } else {
            singerInfo = tempSingerInfos.get(position);
        }
        singerInfo.setSongsNumber(newSize);
        MusicInfo musicInfo = (MusicInfo) entity;
        singerInfo.getMusicInfos().add(musicInfo);
        musicInfo.setSingerInfo(singerInfo);
        //LogTool.log(this, "歌手更新:" + singerInfo.getSingerName() + "," + musicInfo.getDisplayName());
    }

    @Override
    public void indexInfoAdded(Object indexValue, Object entity, int position) {
        MusicInfo musicInfo = (MusicInfo) entity;
        SingerInfo singerInfo = new SingerInfo(musicInfo.getArtist());
        singerInfo.setSongsNumber(1);
        ObservableArrayList<MusicInfo> musicInfos = new ObservableArrayList<>();
        musicInfos.add(musicInfo);
        musicInfo.setSingerInfo(singerInfo);
        singerInfo.setMusicInfos(musicInfos);
        //LogTool.log(this, "歌手增加:" + singerInfo.getSingerName() + "," + musicInfo.getDisplayName());
        if (getViewModel() != null) {
            getViewModel().getDatas().add(position, singerInfo);
        } else {
            tempSingerInfos.add(position, singerInfo);
        }
    }
}
