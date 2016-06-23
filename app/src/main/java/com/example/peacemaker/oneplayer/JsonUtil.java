package com.example.peacemaker.oneplayer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class JsonUtil {
	JSONObject jsonObject = null;
	private HttpClient httpClient;
	private HttpPost httpPost;
	private HttpResponse httpResponse;
	private HttpURLConnection httpURLConnection;
	private StringBuilder stringBuilder;
	private BufferedReader bufferedReader;
	Handler handler;
	Boolean stop = false;
	public JsonUtil(Handler handler) {
		this.handler = handler;

	}
	public JsonUtil() {
	}

	void sendJson(final JSONObject jsonObjectin, final String url) {
		Log.v("JsonUrl","检查url"+url);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!stop) {
					jsonObject = Connect(jsonObjectin, url);
					Log.v("JsonUtil", jsonObject + "线程中的json");
					if(jsonObject!=null) {
						Bundle bundle = new Bundle();
						Log.v("JsonUtil","bundle发送检查"+jsonObject.toString());
						bundle.putString("json", jsonObject.toString());
						Message message = new Message();
						message.setData(bundle);
						handler.sendMessage(message);
					}else{
						handler.sendEmptyMessage(0X001);
					}
				}
			}
		});
		thread.start();
		return ;

	}
	public JSONObject Connect(JSONObject jsonObjectin, String url){
		try {
			Log.v("巴斯特林克","死大头");
			Log.v("巴斯头临客",jsonObjectin.toString());
			URL url1 = new URL(url);
			httpURLConnection = (HttpURLConnection)url1.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(jsonObjectin.toString().getBytes());
//			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//
//			objectOutputStream.writeObject(jsonObjectin.toString());
            Log.v("巴斯特实验",httpURLConnection.getInputStream()+"");
			bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			stringBuilder = new StringBuilder();
			for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
					.readLine()) {

				stringBuilder.append(s);
			}
			Log.v("状态码", httpURLConnection.getResponseCode() + "");
			Log.v(" 我看看", stringBuilder.toString());
			jsonObject = new JSONObject(stringBuilder.toString());
			//jsonObject = new JSONObject(stringBuilder.toString());
		} catch (Exception e) {
			Log.v("巴斯特林克","失败");
			e.printStackTrace();

		}
        stop = true;
        httpURLConnection.disconnect();
		//Log.v(" 传回前看一看", jsonObject.toString());
		return jsonObject;
	}
    public JSONObject Connect2(JSONObject jsonObjectin, String url){
        try{
        httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonObjectin.toString());
        httpPost.setEntity(stringEntity);
            httpClient = new DefaultHttpClient();
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                bufferedReader = new BufferedReader(new InputStreamReader(
                        httpResponse.getEntity().getContent()));
                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
                        .readLine()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(s);
                }
                Log.v("这个锅","不是我背的！");
                Log.v("检查大锅",stringBuilder.toString());
            }
			Log.v("这个锅","由我来背！");
        }catch (Exception e){
            e.printStackTrace();
        }
        stop = true;
            return jsonObject;
    }

	public static HashMap<String, String> jsonObject2HashMap(
			JSONObject jsonObject) {
		if (jsonObject != null) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			Iterator<String> iterator = jsonObject.keys();
			try {
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = jsonObject.getString(key);
					hashMap.put(key, value);
				}
			} catch (Exception e) {
				Log.w("apputil", "jsonobject2hasmap");
			}
			return hashMap;
		}
		return null;
	}
	public static Bitmap getBitmap(JSONObject jsonObject){
		try {
			String url = jsonObject.getString("pictureUrl");
			URLConnection connection = new java.net.URL(url).openConnection();
			InputStream is;
			is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bm = BitmapFactory.decodeStream(bis);
			is.close();
			bis.close();
			return bm;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1){
			e1.printStackTrace();
		}
		return null;
	}
	public static ArrayList<Music> jsonObject2musicArraylist(JSONObject jsonObject){
		int num;Music Music;
		ArrayList<Music> musicArraylist = new ArrayList<Music>();
		try {
			num = jsonObject.getInt("number");
			for(int i=0;i<num;i++){
				System.out.println("开始便");
				System.out.println(jsonObject.toString());
				System.out.println(jsonObject.getJSONObject("json"+(i+1)).toString());
				JSONObject jsonObject1 = jsonObject.getJSONObject("json"+(i+1));
				//Music = new Music(jsonObject1.getString("title"),jsonObject1.getString("pictureUrl"),jsonObject1.getString("contentId"),jsonObject1.getString("url"));
				//musicArraylist.add(Music);
			}
			System.out.println("尝试成功而返回");
			return musicArraylist;
		} catch (JSONException e1) {
			System.out.println("json转换异常");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("还是要返回");
		return musicArraylist;
		
	}

	public static String bitmap2String(Bitmap bitmap) {
		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}
	 public static Bitmap String2bitmap(String st){  
	    Bitmap bitmap = null;  
	    try{  
	       byte[] bitmapArray;  
	       bitmapArray = Base64.decode(st, Base64.DEFAULT);  
	       bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);  
	       return bitmap;  
	     }catch (Exception e){  
	       return null;  
	   }  
 }  


}
