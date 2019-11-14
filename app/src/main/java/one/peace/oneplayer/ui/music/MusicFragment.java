package one.peace.oneplayer.ui.music;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import one.peace.oneplayer.R;
import one.peace.oneplayer.databinding.FragmentMusicBinding;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.ui.base.BaseFragment;
import one.peace.oneplayer.util.LogTool;

/**
 * Created by pavel on 2019/11/14.
 */
public class MusicFragment extends BaseFragment<MusicInfoViewModel,MusicInfo> {

    @Override
    public Class getViewModelClass() {
        return MusicInfoViewModel.class;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    protected void onInitData(MusicInfoViewModel viewModel) {
        getEntity().setArtist("测试");
        getEntity().setDisplayName("测试歌手");
    }

    @Override
    protected void onSetData(ViewDataBinding viewDataBinding) {
        FragmentMusicBinding fragmentMusicBinding = ((FragmentMusicBinding)viewDataBinding);
        fragmentMusicBinding.setMusicInfoViewModel(getViewModel());
        fragmentMusicBinding.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_button:
                LogTool.log(this,"点击改变按键");
                getEntity().setArtist("周杰伦");
                getEntity().setDisplayName("不该");
                break;
        }
    }
}
