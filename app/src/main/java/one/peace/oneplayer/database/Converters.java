package one.peace.oneplayer.database;

import java.util.ArrayList;

import androidx.room.TypeConverter;
import one.peace.oneplayer.global.config.EnvironmentReverbConfig;
import one.peace.oneplayer.util.StringUtil;

/**
 * Created by pavel on 2020/1/6.
 */
public class Converters {

    @TypeConverter
    public static String convertEnvironmentReverbConfig(EnvironmentReverbConfig environmentalReverbConfig) {
        return StringUtil.entityToJson(environmentalReverbConfig);
    }

    @TypeConverter
    public static EnvironmentReverbConfig convertStringToEnvironmentReverbConfig(String storedValue) {
        return (EnvironmentReverbConfig) StringUtil.jsonToEntity(storedValue, EnvironmentReverbConfig.class);
    }

    @TypeConverter
    public static String convertIntList(ArrayList<Integer> integerArrayList) {
        return StringUtil.listToString(integerArrayList,",");
    }

    @TypeConverter
    public static ArrayList<Integer> convertStringToIntList(String storedValue) {
        return StringUtil.stringToIntList(storedValue);
    }

    @TypeConverter
    public static String convertShortArray(short[] shortArray) {
        return StringUtil.shortArrayToString(shortArray);
    }

    @TypeConverter
    public static short[] convertStringToShortArray(String storedValue) {
        return StringUtil.stringToShortArray(storedValue);
    }
}
