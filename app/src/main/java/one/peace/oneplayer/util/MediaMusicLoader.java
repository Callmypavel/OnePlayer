package one.peace.oneplayer.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

import one.peace.oneplayer.music.entity.MusicInfo;

public class MediaMusicLoader extends MusicLoader {
    private Context mContext;
    private ContentResolver contentResolver;
    private Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String sortOrder = MediaStore.Audio.Media.DATA;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    public MediaMusicLoader(Context context){
        mContext = context;
    }
    @Override
    protected void loadMusic() {
        if(contentResolver == null){
            contentResolver = mContext.getContentResolver();
        }
        Cursor cursor = contentResolver.query(contentUri, projection, selection, selectionArgs, sortOrder);
        if(cursor!=null) {
            int count = cursor.getCount();
            int current = 0;
            while (cursor.moveToNext()) {
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setArtist(artist);
                musicInfo.setDuration(duration);
                musicInfo.setSize(size);
                musicInfo.setId(id);
                musicInfo.setAlbum(album);
                musicInfo.setDisplayName(displayName);
                musicInfo.setUrl(url);
                musicInfo.setPlayable(true);
                scanFound(musicInfo);
                current += 1;
                scanProgressChanged((int)(current*100.f/count));
            }
            scanFinished();
            cursor.close();
        }
    }
}
