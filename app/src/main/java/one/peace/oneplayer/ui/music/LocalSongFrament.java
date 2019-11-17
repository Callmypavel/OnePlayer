package one.peace.oneplayer.ui.music;

import android.view.View;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import one.peace.oneplayer.R;
import one.peace.oneplayer.base.IndexedEntities;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.music.entity.SingerInfo;
import one.peace.oneplayer.ui.base.BaseFragment;
import one.peace.oneplayer.ui.base.BaseListFragment;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.MusicLoader;

public class LocalSongFrament extends BaseListFragment<MusicInfo, BaseListFragment.BaseListViewModel> implements MusicLoader.LoadMusicListener {
    private ObservableArrayList<MusicInfo> musicInfos;

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_song;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        if (musicInfos != null) {
            getViewModel().getDatas().addAll(musicInfos);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_song;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadPass(String fileName) {

    }

    @Override
    public void loadFound(MusicInfo musicInfo) {
        getViewModel().getDatas().add(musicInfo);
        if (musicInfos == null) {
            musicInfos = new ObservableArrayList<>();
        }
        musicInfos.add(musicInfo);
        //LogTool.log(this,"我day到了！"+LogTool.toString(musicInfo));
    }

    @Override
    public void loadFinished() {

    }

    @Override
    public void loadProgressChanged(int progress) {

    }
}
