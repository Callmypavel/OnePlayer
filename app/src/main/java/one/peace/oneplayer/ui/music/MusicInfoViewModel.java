package one.peace.oneplayer.ui.music;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import one.peace.oneplayer.base.BaseViewModel;
import one.peace.oneplayer.music.entity.MusicInfo;

/**
 * Created by pavel on 2019/11/14.
 */
public class MusicInfoViewModel extends BaseViewModel<MusicInfo> {

    @Override
    protected MusicInfo initializeBean() {
        return new MusicInfo();
    }
}
