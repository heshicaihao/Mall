package com.nettactic.mall.net;

import java.io.File;

import android.os.Environment;
/**
 * 读取本地文件
 * @author heshicaihao
 *
 */
public class ReadPropertyFile {
	
    private static final String FILE_PATH_FLAG_TEST = "heshicaihaotest";
    
    public static boolean isTestEnvironment() {
        if (isFileExit(FILE_PATH_FLAG_TEST)) {
            return true;
        }
        return false;
    }
    
    public static boolean isFileExit(String file) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardDir = ReadPropertyFile.getExternalStorageDirectory();
            File f = new File(sdCardDir + file);
            if (f.exists()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public static String getExternalStorageDirectory() {
        String rootpath = "";
        try {
            rootpath = Environment.getExternalStorageDirectory().getPath();
            if (!rootpath.endsWith(File.separator)) {
                rootpath += File.separator;
            }
        } catch (Exception e) {
            rootpath = "";
        }
        return rootpath;
    }

    
    

}
