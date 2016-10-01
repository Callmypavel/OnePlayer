package com.example.peacemaker.oneplayer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ouyan_000 on 2015/8/22.
 */
public class DatabaseOperator extends SQLiteOpenHelper {
    private MainActivity mainActivity;
    private final static int VERSION = 1;
    private final static String CREATE_Music = "create table if not exists Music(_id integer primary key autoincrement,artist text not null,duration text not null,album text not null,displayName text not null,url text not null,identifier text not null)";
    private final static String CREATE_Last = "create table if not exists Last(_id integer primary key autoincrement,artist text not null,duration text not null,album text not null,displayName text not null,url text not null,position integer not null)";
    public DatabaseOperator(Context context, String name) {
        super(context, name, null, 1);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        System.out.println("创建测试");
        db.execSQL(CREATE_Music);
        db.execSQL(CREATE_Last);
        System.out.println("创建测试完毕");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
    public void insert(ContentValues values,String tableName){
        // 获取SQLiteDatabase实例
        Boolean isSame = false;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("Music",new String[]{"identifier"},null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!isSame&&!cursor.isLast()){
                //System.out.println(values.get("identifier")+"和"+cursor.getString(cursor.getColumnIndex("identifier")));
                if(values.get("identifier").equals(cursor.getString(cursor.getColumnIndex("identifier")))){
                    System.out.println("找到同名不插入和和");
                    isSame = true;
                }
                    cursor.moveToNext();

            }

            if(!isSame){
                System.out.println(values.get("identifier")+"对比"+cursor.getString(cursor.getColumnIndex("identifier")));
                Long i = db.insert(tableName, "ab", values);
            }
        }else {
            System.out.println("全部插入");
            db.insert(tableName, "ab", values);
        }
        System.out.println("插入完毕");
        db.close();
    }

    public void saveMusics(ArrayList<Music> Musics){
       int i = 0;
        System.out.println("开始保存 请祖国人民放心");
        for (Music music : Musics) {
            i+=1;
            Log.v("Database","savemusics次数"+i);
            Log.v("Database","savemusics名字"+music.getDisplayName());
            String artist = music.getArtist();
            String duration = music.getDuration();
            //String size = music.getSize();
            //String id = music.getId();
            String album = music.getAlbum();
            String displayName = music.getDisplayName();
            String url = music.getUrl();
            String identifier = getIdentifier(displayName,artist,duration);
            int id = music.getId();
            ContentValues values = new ContentValues();
            values.put("artist", artist);
            values.put("duration", duration);
            //values.put("size", size);
            values.put("id", id);
            values.put("album", album);
            values.put("displayName", displayName);
            values.put("url", url);
            values.put("identifier",identifier);
            Log.v("DatabaseOperator","本地储存音乐"+url);
            insert(values, "Music");
        }
    }
    public ArrayList<Music> getMusics(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Music", null, null, null, null, null, null) ;
        ArrayList<Music> Musics = new ArrayList<>();
        while(cursor.moveToNext()){
            String artist = cursor.getString(cursor.getColumnIndex("artist"));
            String duration = cursor.getString(cursor.getColumnIndex("duration"));
            String album = cursor.getString(cursor.getColumnIndex("album"));
            String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String id = cursor.getInt(cursor.getColumnIndex("url"))+"";
            Log.v("DatabaseOperator","本地读取音乐"+url);
            Musics.add(new Music(artist,duration,album,displayName,url,id,true));
        }
        return Musics;
    }
    public String findUrlByPosition(int position,ArrayList<Music> musicArrayList) {
//        System.out.println("我要找"+position);
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query("Music", new String[]{"url"}, null, null, null, null, null);
//        cursor.moveToPosition(position);
//        String url = cursor.getString(cursor.getColumnIndex("url"));
//        System.out.println("然而我给你返回"+url);
        return musicArrayList.get(position).getUrl();
    }

    public String getIdentifier(String name,String artist,String duration){
        String identifier = name+artist+duration;
        return identifier;
    }
    public void saveLastplayed(int currentPosition,Music music,String tableName){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("currentMusic",currentMusic);
//        editor.putInt("currentPosition", currentPosition);
//        editor.commit();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("Last",null,null,null,null,null,null);
        ContentValues values = new ContentValues();
        values.put("artist", music.getArtist());
        values.put("duration", music.getDuration());
        values.put("album", music.getAlbum());
        values.put("displayName", music.getDisplayName());
        values.put("url", music.getUrl());
        values.put("position",currentPosition);
        if(cursor.moveToFirst()){
            db.replace(tableName,"ab",values);
        }else {
            db.insert(tableName,"ab",values);
        }
        db.close();
    }
    public static OneConfig loadConfig(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Config",0);
        int themeColor = sharedPreferences.getInt("ThemeColor",Color.parseColor("#fd4c5b"));
        OneConfig oneConfig = new OneConfig(themeColor);
        return oneConfig;
    }
    public static void saveConfig(OneConfig oneConfig,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Config",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ThemeColor", oneConfig.getThemeColor());
        editor.apply();
    }
//    public void saveOrderMode(int OrderMode,String tableName,SharedPreferences sharedPreferences){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("OrderMode", OrderMode);
//        editor.commit();
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor = db.query("Last",null,null,null,null,null,null);
//        ContentValues values = new ContentValues();
//        values.put("OrderMode",OrderMode);
//        if(cursor.moveToFirst()){
//            db.replace(tableName,"ab",values);
//        }else {
//            db.insert(tableName,"ab",values);
//        }
//        //db.update(tableName,values,null,null);
//        db.close();
//    }
    public JSONObject getLastPlayed(){
        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("currentMusic", sharedPreferences.getString("currentMusic", ""));
//            jsonObject.put("currentPosition", sharedPreferences.getInt("currentPosition", 0));
//            jsonObject.put("OrderMode", sharedPreferences.getInt("OrderMode", -1));
//            jsonObject.put("FirstPosition", sharedPreferences.getInt("FirstPosition", 0));
//            Log.v("DatabaseOperator","getLastPlayed()"+sharedPreferences.getString("currentMusic", "")+
//                    sharedPreferences.getInt("currentPosition", 0)+sharedPreferences.getInt("OrderMode", -1));
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("Last", null, null, null, null, null, null) ;
        if(cursor.moveToFirst()) {
            try {
                jsonObject.put("currentPosition", cursor.getString(cursor.getColumnIndex("position")));
                //jsonObject.put("OrderMode", cursor.getString(cursor.getColumnIndex("OrderMode")));
            }catch (Exception e){

            }

        }
        return jsonObject;
    }
    public void saveFirstPosition(SharedPreferences sharedPreferences,int FirstPosition){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("FirstPosition", FirstPosition);
        editor.commit();
    }
    public static String bitmap2String(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }
    public static Bitmap String2bitmap(String st){
        Bitmap bitmap = null;
        try{
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,bitmapArray.length);
            return bitmap;
        }catch (Exception e){
            return null;
        }
    }




}
