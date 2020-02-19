package one.peace.oneplayer.ui.music;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.work.WorkerParameters;
import one.peace.oneplayer.music.entity.MusicInfo;
import one.peace.oneplayer.ui.base.BaseMultiplePageFragment;
import one.peace.oneplayer.util.LoaderManager;
import one.peace.oneplayer.util.LocalMusicLoader;
import one.peace.oneplayer.util.LogTool;
import one.peace.oneplayer.util.MediaMusicLoader;

public class LocalMusicFragment extends BaseMultiplePageFragment {
    private LocalSongFrament localSongFrament;
    private LocalAlbumFragment localAlbumFragment;
    private LocalSingerFrament localSingerFrament;

    @Override
    protected ArrayList<Fragment> generateFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        localSongFrament = new LocalSongFrament();
        localAlbumFragment = new LocalAlbumFragment();
        localSingerFrament = new LocalSingerFrament();
        fragments.add(localSongFrament);
        fragments.add(localAlbumFragment);
        fragments.add(localSingerFrament);
        return fragments;
    }

    @Override
    protected ArrayList<String> generateTitles() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("单曲");
        titles.add("专辑");
        titles.add("歌手");
        return titles;
    }

    @Override
    protected void onInit() {
        LogTool.log(this, "你应该只调一遍");
        MediaMusicLoader.initHandler();
        MediaMusicLoader.addLoadMusicListener(localSongFrament);
        MediaMusicLoader.addLoadSingerAndAlbumListeners(localSingerFrament, localAlbumFragment);
        LoaderManager loaderManager = new LoaderManager(MediaMusicLoader.class);
        loaderManager.startLoading();
    }


}
