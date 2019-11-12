package peacemaker.oneplayer.tool;

import android.databinding.ObservableArrayList;

import java.util.ArrayList;

/**
 * Created by peace on 2018/5/8.
 */

public class EntityConverter {
    public static ObservableArrayList ArraylistToObservableOne(ArrayList arrayList){
        ObservableArrayList observableArrayList = new ObservableArrayList();
        for (Object object: arrayList) {
            observableArrayList.add(object);
        }
        return observableArrayList;
    }
}
