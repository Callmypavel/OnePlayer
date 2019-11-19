package peacemaker.oneplayer.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ouyan on 2018/2/2.
 */

public class FileOperator {
    public static void writeBitmapToFile(Bitmap bitmap,String fileName,String path){
        try {
            checkFile(fileName,path);
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void writeStreamToFile(InputStream inputStream,String fileName,String path){
        try {
            checkFile(fileName,path);
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            byte buffer[] = new byte[1024];
            int read;
            while ((read=inputStream.read(buffer))>0){
                fileOutputStream.write(buffer,0,read);
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static Bitmap getBitmapFromFile(String fileName){
        try {
            File file = new File(fileName);
            if (!file.exists()){
                return null;
            }
            //LogTool.log("FileOperator","文件名"+fileName);

            //先获取宽高
           // FileInputStream fileInputStream = new FileInputStream(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName,options);
            //LogTool.log("FileOperator","预读宽度"+options.outWidth);

            int scale = options.outWidth/400;
            if(scale<1){
                scale = 1;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
            //LogTool.log("FileOperator","预读宽度"+options.outWidth+"最后文件中读出的bitmap宽度"+bitmap.getWidth());
            //LogTool.log("FileOperator","最后文件中读出的bitmap大小"+bitmap.getByteCount());
//            fileInputStream.close();
            return bitmap;
        }catch (Exception e){
            LogTool.log("FileOperator","文件读取异常");
            e.printStackTrace();
        }
        return null;

    }

    public static void checkFile(String fileName,String path){
        File pathFile = new File(path);
        File logFile = new File(path+fileName);
        if(!pathFile.exists()){
            boolean result = pathFile.mkdirs();
            LogTool.log("FileOperator","目录"+logFile.getAbsolutePath()+"不存在，创建目录"+(result?"成功":"失败"));
        }
        if (!logFile.exists())
        {
            try
            {
                boolean result = logFile.createNewFile();
                LogTool.log("FileOperator","文件"+logFile.getAbsolutePath()+"不存在，创建目录"+(result?"成功":"失败"));
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
