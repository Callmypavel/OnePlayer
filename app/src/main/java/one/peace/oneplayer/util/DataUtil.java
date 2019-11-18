package one.peace.oneplayer.util;

import android.os.Parcelable;

import java.util.ArrayList;

import androidx.databinding.ObservableArrayList;

public class DataUtil {
    public static final String TAG = "DataUtil";
    public static ObservableArrayList convertToObservaleVersion(ArrayList arrayList) {
        ObservableArrayList observableArrayList = new ObservableArrayList();
        for (Object object : arrayList) {
            observableArrayList.add(object);
        }
        return observableArrayList;
    }
    public static ObservableArrayList convertToObservaleArrayList(Object[] array) {
        ObservableArrayList observableArrayList = new ObservableArrayList();
        for (Object object : array) {
            observableArrayList.add(object);
        }
        return observableArrayList;
    }
    public static Parcelable[] convertToParcelableArray(ObservableArrayList observableArrayList) {
        Parcelable[] array = new Parcelable[observableArrayList.size()];
        LogTool.log(TAG,"战兔");
        for (int i = 0; i < observableArrayList.size(); i++) {
            LogTool.log(TAG,"战兔");
           array[i] = (Parcelable) observableArrayList.get(i);
        }
        LogTool.log(TAG,"战兔");
        return array;
    }
}
