package one.peace.oneplayer.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import one.peace.oneplayer.util.ReflectUtil;
import one.peace.oneplayer.util.StringUtil;

public class IndexedEntities<E> {
    private ArrayList<IndexedEntity> indices;
    private ArrayList<E> dataSource;
    private Method getMethod;
    private Comparator<E> comparator;
    private IndexInfoChangedListener indexInfoChangedListener;

    public interface IndexInfoChangedListener {
        void indexInfoUpdated(Object indexValue, Object entity, int position, int newSize);

        void indexInfoAdded(Object indexValue, Object entity, int position);
    }

    class IndexedEntity {
        public Object key;
        public ArrayList<E> values;
    }

    public IndexedEntities(Class clazz, String attributeName, ArrayList<E> entities, IndexInfoChangedListener indexInfoChangedListener) {
        indices = new ArrayList<>();
        getMethod = ReflectUtil.getGetMethod(clazz, attributeName);
        comparator = new Comparator<E>() {
            @Override
            public int compare(E lhs, E rhs) {
                if (lhs != null && rhs != null) {
                    return StringUtil.mandarin2Pinyin(ReflectUtil.invokeGetMethod(lhs, getMethod)).toLowerCase()
                            .compareTo(StringUtil.mandarin2Pinyin(ReflectUtil.invokeGetMethod(rhs, getMethod)).toLowerCase());
                } else {
                    return 0;
                }
            }
        };
        this.indexInfoChangedListener = indexInfoChangedListener;
        initialize(entities);
    }

    public void setIndexInfoChangedListener(IndexInfoChangedListener indexInfoChangedListener) {
        this.indexInfoChangedListener = indexInfoChangedListener;
    }

    public IndexInfoChangedListener getIndexInfoChangedListener() {
        return indexInfoChangedListener;
    }

    public ArrayList<E> getDataSource() {
        return dataSource;
    }

    public void initialize(ArrayList<E> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        dataSource = entities;
        for (int i = 0; i < dataSource.size(); i++) {
            E data = dataSource.get(i);
            update(data);
        }
    }

    private void update(E newEntity) {
        Object valueToIndex = ReflectUtil.invokeGetMethod(newEntity, getMethod);
        IndexedEntity indexedEntity;
        int index = getIndex(valueToIndex);
        if (index == -1) {
            //害没有这个索引下的条目
            indexedEntity = new IndexedEntity();
            indexedEntity.key = valueToIndex;
            indexedEntity.values = new ArrayList<>();
            indexedEntity.values.add(newEntity);
            int position = getPositionToInsert(valueToIndex);
            indices.add(position, indexedEntity);
            if (indexInfoChangedListener != null) {
                indexInfoChangedListener.indexInfoAdded(valueToIndex, newEntity, position);
            }
        } else {
            //已有索引
            indexedEntity = indices.get(index);
            indexedEntity.values.add(newEntity);
            if (indexInfoChangedListener != null) {
                indexInfoChangedListener.indexInfoUpdated(valueToIndex, newEntity, index, indexedEntity.values.size());
            }
        }
    }

    public void addNew(E newEntity) {
        if (dataSource == null) {
            dataSource = new ArrayList<>();
        }
        dataSource.add(newEntity);
        update(newEntity);
    }

    public List<String> getAllIndexName() {
        List<String> result = new ArrayList<>();
        for (IndexedEntity indexedEntity : indices) {
            result.add(indexedEntity.key.toString());
        }
        return result;
    }


    private int getIndex(Object key) {
        int index = -1;
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i).key.equals(key)) {
                index = i;
                return index;
            }
        }
        return index;
    }

    private int getPositionToInsert(Object key) {
        int index = 0;
        for (int i = 0; i < indices.size(); i++) {
            if (StringUtil.mandarin2Pinyin(key).compareTo(StringUtil.mandarin2Pinyin(indices.get(i).key)) > 0) {
                return index;
            }
        }
        return index;
    }

}
