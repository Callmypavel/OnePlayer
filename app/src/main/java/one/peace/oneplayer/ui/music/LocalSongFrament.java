package one.peace.oneplayer.ui.music;

import android.view.View;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;
import one.peace.oneplayer.music.player.OnePlayer;
import one.peace.oneplayer.ui.base.BaseFragment;
import one.peace.oneplayer.ui.base.BaseListFragment;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.MusicLoader;

public class LocalSongFrament extends BaseListFragment<MusicInfo, BaseListFragment.BaseListViewModel> implements MusicLoader.LoadMusicListener {
    private ObservableArrayList<MusicInfo> tempMusicInfos = new ObservableArrayList<>();
    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_song;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        if (tempMusicInfos != null) {
            viewModel.getDatas().addAll(tempMusicInfos);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_song;
    }

    @Override
    public void loadPass(String fileName) {

    }

    @Override
    public void loadFound(MusicInfo musicInfo) {
        if (getViewModel() != null) {
            LogTool.log(this,"新增单曲"+musicInfo.getDisplayName());
            getViewModel().getDatas().add(musicInfo);
        }else {
            //先放在临时区
            LogTool.log(this,"临时新增单曲"+musicInfo.getDisplayName());
            tempMusicInfos.add(musicInfo);
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
        LogTool.log(this,"即将播放"+((MusicInfo)data).getUrl());
        OnePlayer.getInstance(getContext()).setPlayList(getViewModel().getDatas());
        OnePlayer.getInstance(getContext()).selectMusic(position);
        OnePlayer.getInstance(getContext()).play();
    }
}
