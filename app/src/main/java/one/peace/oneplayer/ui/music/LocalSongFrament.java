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
    private ObservableArrayList<MusicInfo> dataSource;
    @Override
    protected int getItemLayoutId() {
        return R.layout.item_local_song;
    }

    @Override
    protected void onInitData(final BaseListFragment.BaseListViewModel viewModel, ViewDataBinding viewDataBinding) {
        super.onInitData(viewModel, viewDataBinding);
        LogTool.log(this,"天照！"+viewModel.getDatas());
        if (musicInfos != null) {
            viewModel.getDatas().addAll(musicInfos);
            musicInfos = null;
        }else {
            //临时的为空
            if (dataSource != null){
                //数据源不为空
                viewModel.getDatas().addAll(dataSource);
            }
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
        if(getViewModel() != null){
            getViewModel().getDatas().add(musicInfo);
        }else {
            if (musicInfos == null) {
                musicInfos = new ObservableArrayList<>();
            }
            musicInfos.add(musicInfo);
        }

        //LogTool.log(this,"我day到了！"+LogTool.toString(musicInfo));
    }

    @Override
    public void loadFinished() {
        if(dataSource == null) {
            dataSource = new ObservableArrayList<>();
        }
        if(getViewModel().getDatas() != null){
            dataSource.addAll(getViewModel().getDatas());
            return;
        }
        if(musicInfos != null){
            dataSource.addAll(musicInfos);
        }
    }

    @Override
    public void loadProgressChanged(int progress) {

    }

    @Override
    public void onItemClick(View view, Object data, int position) {

    }
}