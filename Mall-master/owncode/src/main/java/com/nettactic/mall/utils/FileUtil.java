package com.nettactic.mall.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.nettactic.mall.constants.MyConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings("deprecation")
public class FileUtil {

    // 图片缓存路径
    public static final String MATERIAL_PATH = FileUtil.getSDPath() + "/"
            + MyConstants.CACHE_DIR + "/";

    /**
     * 获取次文件夹大小
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0.0;
        }
    }

    /**
     * 获取 文件最后修改时间
     *
     * @param file
     * @return
     */
    public static long getlastModified(File file) {
        long lastModified = file.lastModified();
        return lastModified;
    }

    /**
     * 找到文件夹中 最早的 文件
     *
     * @param file
     */
    public static File getOldFile(File file) {
        File OldFile = null;
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                long min = getlastModified(files[0]);
                for (int i = 0; i < files.length; i++) {
                    if (getlastModified(files[i]) < min) {// 判断最小值
                        min = getlastModified(files[i]);
                    }
                }
                for (int j = 0; j < files.length; j++) {
                    if (min == getlastModified(files[j])) {
                        OldFile = files[j];
                    }
                }
            }
        }
        return OldFile;
    }

    /**
     * 当文件超过上限 删除最早的文件
     */
    public static void deleteOldFile(File file, double Max) {
        double dirSize = getDirSize(file);
        if (dirSize > Max) {
            File oldFile = getOldFile(file);
            deleteFile(oldFile);
        }
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 把URL 加密成文件名
     *
     * @param url
     * @return
     */
    public static String getenURl(String url) {
        String enUrl = null;
        try {
            enUrl = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return enUrl;
    }

    /**
     * 根据名字删除SD中文件
     *
     * @param dir
     * @param name
     * @param er
     */
    public static void deleteFromSD(String dir, String name, String er) {
        String filePath = FileUtil.getFilePath(dir, name, er);
        File file = new File(filePath);
        deleteFile(file);
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    /**
     * 获取SD中文件 的路径
     *
     * @param dir
     * @param name
     * @param er
     */
    public static String getFilePath(String dir, String name, String er) {
        String picName = name + er;
        String fileName = getSDPath() + "/" + dir + "/";
        String path = fileName + picName;
        return path;
    }

    /**
     * 从sd卡读文件
     *
     * @param dir  路径
     * @param name 名字
     * @param er   后缀
     * @return
     */
    public static String readFile(String dir, String name, String er) {
        FileInputStream inputStream = null;
        String Str = null;
        try {
            if (isSdCardExist()) {
                File fileDir = new File(getSDPath() + "/" + dir + "/" + "/"
                        + name + er);
                inputStream = new FileInputStream(fileDir);
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                Str = new String(b);
                return Str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return Str;
    }

    /**
     * 把String 保存到SD中的方法
     *
     * @param Str
     */
    public static void saveFile(String Str, String dir, String name, String er) {
        try {
            File file = new File(getSDPath() + "/" + dir);
            file.mkdirs();
            if (!file.exists()) {
                file.mkdir();
            }
            FileWriter fw = new FileWriter(getSDPath() + "/" + dir + "/" + "/"
                    + name + er);

            fw.flush();
            fw.write(Str);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片Jpeg到SD卡的方法
     *
     * @param bm
     * @param dir
     * @param name
     * @param er
     * @return
     */
    public static void saveBitmap2Jpeg(Bitmap bm, String dir, String name,
                                       String er) {
        String picName = name + er;
        String fileName = getSDPath() + "/" + dir + "/";
        File f = new File(fileName, picName);
        f.mkdirs();
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片Png到SD卡的方法
     *
     * @param bm
     * @param dir
     * @param name
     * @param er
     * @return
     */
    public static void saveBitmap2Png(Bitmap bm, String dir, String name,
                                      String er) {
        String picName = name + er;
        String fileName = getSDPath() + "/" + dir + "/";

        File f = new File(fileName, picName);
        f.mkdirs();
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 99, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过SD卡路径获取图片
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath) {
        // BitmapFactory.Options opt = new BitmapFactory.Options();
        // opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // opt.inPurgeable = true;
        // opt.inInputShareable = true;
        // // 获取资源图片
        // return BitmapFactory.decodeFile(imagePath, opt);
        Bitmap btp = null;
        InputStream is = null;
        try {
            is = new FileInputStream(imagePath);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inSampleSize = 1;
            opts.inInputShareable = true;
            btp = BitmapFactory.decodeStream(is, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return btp;

    }

    /**
     * 通过SD卡路径获取图片
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath, int width, int height) {
        Bitmap bitmap = YSYBitmapUtil.getBitmap(imagePath, width, height);

        return bitmap;

    }

    /**
     * 通过SD卡路径获取图片
     *
     * @param imagePath
     * @return
     */

    public static Bitmap getSDCardImg02(String imagePath) {
        Bitmap btp = null;
        try {
            InputStream is = new FileInputStream(imagePath);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inSampleSize = 30;
            opts.inInputShareable = true;
            btp = BitmapFactory.decodeStream(is, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return btp;
    }

    /**
     * 获取手机SD卡路径的方法
     */
    public static String getSDPath() {
        File sdDir = null;
        if (isSdCardExist()) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 判断SDCard是否存在
     *
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断SDCard是否存在
     *
     * @return
     */
    public static String getWorksDir(String works_id) {
        String works_dir = MyConstants.WORK_DIR + "/" + works_id;
        return works_dir;
    }

    /**
     * 根据 works_id
     * <p>
     * 删除保存单个作品的文件夹
     */
    public static void deleteWorks(String works_id) {
        String worksDir = FileUtil.getWorksDir(works_id);
        String path = FileUtil.getSDPath() + "/" + worksDir;
        FileUtil.deleteFile(new File(path));
    }

    /**
     * 根据 works_id
     * <p>
     * 删除保存单个作品的文件夹
     */
    public static void deleteOrderList() {
        String worksDir = MyConstants.ORDERLIST;
        String path = FileUtil.getSDPath() + "/" + worksDir;
        FileUtil.deleteFile(new File(path));
    }

    /**
     * 获取资源图片
     *
     * @param context
     * @param resource
     * @return
     */
    public static Bitmap getResourceImg(Context context, int resource) {
        Bitmap bitmap = YSYBitmapUtil.getBitmap(context, resource);

        return bitmap;

    }

    /**
     * 获取资源图片
     *
     * @param context
     * @param resource
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getResourceImg(Context context, int resource,
                                        int width, int height) {
        Bitmap bitmap = YSYBitmapUtil.getBitmap(context, resource, width,
                height);

        return bitmap;

    }

    /**
     * 根据 url 获取 图片缓存路径
     *
     * @param url
     * @return
     */
    public static String getUrl2Path(String url) {
        // 素材
        String MATERIAL_URL = "http://120.76.21.33/"
                + "upload/Elements/";
        String materialUrl = MATERIAL_URL;
//		String materialUrl = MyURL.MATERIAL_URL;
        String materialPath = FileUtil.MATERIAL_PATH;
        String path = url.replaceAll(materialUrl, materialPath);

        return path;

    }


}