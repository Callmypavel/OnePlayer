package peacemaker.oneplayer.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ouyan on 2016/6/24.
 */

public class OneLogger {
    private static boolean isLogging = false;
    public static void stopLogging(){
        isLogging = false;
    }

    public static void getLog(){
        isLogging = true;
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            final BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            final StringBuilder log=new StringBuilder();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;
                        while ((line = bufferedReader.readLine()) != null&&isLogging) {
                            log.append(line).append("\n");
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }
            });
            thread.start();
            output2File(log.toString());
        } catch (IOException e) {
        }
    }
    public static void output2File(String text){
        File logFile = new File("/mnt/sdcard/OneLog.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
