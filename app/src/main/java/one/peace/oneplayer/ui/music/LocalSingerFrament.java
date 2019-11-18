package one.peace.oneplayer.ui.music;

import android.view.View;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;
import one.peace.oneplayer.ui.base.BaseActivity;
import one.peace.oneplayer.ui.base.BaseListFragment;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.MusicLoader;

public class LocalSingerFrament extends BaseListFragment<SingerInfo, BaseListFragment.BaseListViewModel> implements MusicLoader.LoadMusicListener {
    private IndexedEntities<MusicInfo> indexedEntities;
    private ArrayList<MusicInfo> musicDataSource = new ArrayList<>();
    private ObservableArrayList<SingerInfo> singerInfos;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_singer;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        if (singerInfos == null) {
            indexedEntities = new IndexedEntities<MusicInfo>(MusicInfo.class, "artist", musicDataSource, new IndexedEntities.IndexInfoChangedListener() {
                @Override
                public void indexInfoUpdated(Object indexValue, Object entity, int position, int newSize) {
                    SingerInfo singerInfo = (SingerInfo) viewModel.getDatas().get(position);
                    singerInfo.setSongsNumber(newSize);
                    singerInfo.getMusicInfos().add((MusicInfo)entity);
                    //LogTool.log(this,"行天之道"+LogTool.toString(singerInfo));
                }

                @Override
                public void indexInfoAdded(Object indexValue, Object entity, int position) {
                    MusicInfo musicInfo = (MusicInfo) entity;
                    SingerInfo singerInfo = new SingerInfo(musicInfo.getArtist());
                    singerInfo.setSongsNumber(1);
                    //LogTool.log(this,"走私一切"+LogTool.toString(singerInfo)); albumInfo.setAlbumBitmapUrl(musicInfo.getUrl());
                    ObservableArrayList<MusicInfo> musicInfos = new ObservableArrayList<>();
                    musicInfos.add(musicInfo);
                    singerInfo.setMusicInfos(musicInfos);
                    viewModel.getDatas().add(position, singerInfo);
                    if (singerInfos == null) {
                        singerInfos = new ObservableArrayList<>();
                    }
                    singerInfos.add(singerInfo);
                }
            });
            musicDataSource = null;
            LogTool.log(this, "CAST OFF!");
        } else {
            viewModel.getDatas().addAll(singerInfos);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_singer;
    }

    @Override
    public void loadPass(String fileName) {

    }

    @Override
    public void loadFound(MusicInfo musicInfo) {
        if (indexedEntities == null) {
            musicDataSource.add(musicInfo);
            //LogTool.log(this,"加野"+LogTool.toString(musicInfo));
        } else {
            if (indexedEntities.getIndexInfoChangedListener() != null) {
                indexedEntities.addNew(musicInfo);
                //LogTool.log(this,"加野"+LogTool.toString(musicInfo));
            } else {
                musicDataSource.add(musicInfo);
                //LogTool.log(this,"偷东西"+LogTool.toString(musicInfo));
            }
        }
    }

    @Override
    public void loadFinished() {

    }

    @Override
    public void loadProgressChanged(int progress) {

    }

    @Override
    public void onItemClick(View view, Object data, int position) {
        SingerInfo singerInfo = (SingerInfo)data;
        BaseActivity activity = (BaseActivity) getActivity();
        activity.jumpToActivity(SingerDetailActivity.class,"singerInfo",singerInfo);
    }
}
