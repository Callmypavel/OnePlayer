package com.example.peacemaker.oneplayer;

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
    private boolean isLogging = false;
    public void stopLogging(){
        isLogging = false;
    }

    public void getLog(){
        isLogging = true;
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            StringBuilder log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null&&isLogging) {
                log.append(line).append("\n");
            }

            output2File(log.toString());
        } catch (IOException e) {
        }
    }
    public void output2File(String text){
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
