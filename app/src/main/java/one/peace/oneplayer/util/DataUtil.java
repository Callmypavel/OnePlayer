package one.peace.oneplayer.util;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;

public class DataUtil {
    public static ObservableArrayList convertToObservaleVersion(ArrayList arrayList) {
        ObservableArrayList observableArrayList = new ObservableArrayList();
        for (Object object : arrayList) {
            observableArrayList.add(object);
        }
        return observableArrayList;
    }
}
