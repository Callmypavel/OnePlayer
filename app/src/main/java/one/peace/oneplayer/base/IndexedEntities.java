package one.peace.oneplayer.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import one.peace.oneplayer.util.ReflectUtil;
import one.peace.oneplayer.util.StringUtil;

public abstract class IndexedEntities<E> extends ArrayList<E> {
    private HashMap<Object,ArrayList<Integer>> indices;
    private ArrayList<E> dataSource;
    private Method getMethod;
    private Comparator<E> comparator;
    private String attributeName;

    public IndexedEntities(Class clazz,String attributeName){
        indices = new HashMap<>();
        getMethod = ReflectUtil.getGetMethod(clazz,attributeName);
        comparator = new Comparator<E>() {
            @Override
            public int compare(E lhs, E rhs) {
                if(lhs!=null&&rhs!=null) {
                    return StringUtil.mandarin2Pinyin(ReflectUtil.invokeGetMethod(lhs,getMethod)).toLowerCase()
                            .compareTo(StringUtil.mandarin2Pinyin(ReflectUtil.invokeGetMethod(rhs,getMethod)).toLowerCase());
                }else {
                    return 0;
                }
            }
        };
    }

    public void initialize(ArrayList<E> entities){
        if (entities == null || entities.size() == 0){
            return null;
        }
        dataSource = entities;
        for (int i = 0; i < dataSource.size(); i++) {
            E data = dataSource.get(i);
            Music music1 = new Music(music.getArtist());
            ArrayList<Music> secondItems;
            Object valueToIndex = ReflectUtil.invokeGetMethod(data,getMethod);
            if (indices.get(valueToIndex) == null){
                //害没有这个索引下的条目
            }
            if (index == -1) {
                Object valueToIndex = ReflectUtil.invokeGetMethod(data,getMethod);
                indices.put(getIndexByAttribute())
            } else {
                // Log.v("MusicProvider", "更新歌手" + music.getArtist() + "的" + music.getDisplayName());
                singers.get(singerIndex).addSecondItem(music);
            }
            Music music2 = new Music(music.getAlbum());
            int albumIndex = getIndex(albums, music2);
            if (albumIndex == -1) {
                //Log.v("MusicProvider", "新增专辑" + music.getArtist() + "的" + music.getAlbum());
                secondItems = new ArrayList<>();
                secondItems.add(music);
                music2.setSecondItems(secondItems);
                albums.add(music2);
            } else {
                //Log.v("MusicProvider", "更新专辑" + music.getArtist() + "的" + music.getAlbum());
                albums.get(albumIndex).addSecondItem(music);
            }
        }
    }

    public abstract Object getIndexByAttribute(Object attributeValue);


    private int getIndex(E data,ArrayList<E> datas){
        int index = -1;
        Object valueToIndex = ReflectUtil.invokeGetMethod(data,getMethod);
        if(datas == null){
            return index;
        }
        for(int i=0;i<datas.size();i++){
            if (ReflectUtil.invokeGetMethod(datas.get(i),getMethod).equals(valueToIndex)){
                index = i;
                return index;
            }
        }
        return index;
    }

}
