package indi.toaok.utils.core;

import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

import indi.toaok.utils.Utils;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/27
 *     desc  : utils about clean
 * </pre>
 */
public final class CleanUtils {

    private CleanUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Clean the internal cache.
     * <p>directory: /data/data/package/cache</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalCache() {
        return deleteFilesInDir(Utils.getApp().getCacheDir());
    }

    /**
     * Clean the internal files.
     * <p>directory: /data/data/package/files</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalFiles() {
        return deleteFilesInDir(Utils.getApp().getFilesDir());
    }

    /**
     * Clean the internal databases.
     * <p>directory: /data/data/package/databases</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalDbs() {
        return deleteFilesInDir(new File(Utils.getApp().getFilesDir().getParent(), "databases"));
    }

    /**
     * Clean the internal database by name.
     * <p>directory: /data/data/package/databases/dbName</p>
     *
     * @param dbName The name of database.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalDbByName(final String dbName) {
        return Utils.getApp().deleteDatabase(dbName);
    }

    /**
     * Clean the internal shared preferences.
     * <p>directory: /data/data/package/shared_prefs</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalSp() {
        return deleteFilesInDir(new File(Utils.getApp().getFilesDir().getParent(), "shared_prefs"));
    }

    /**
     * Clean the external cache.
     * <p>directory: /storage/emulated/0/android/data/package/cache</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanExternalCache() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && deleteFilesInDir(Utils.getApp().getExternalCacheDir());
    }

    /**
     * Clean the custom directory.
     *
     * @param dirPath The path of directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanCustomDir(final String dirPath) {
        return deleteFilesInDir(dirPath);
    }

    /**
     * Clean the custom directory.
     *
     * @param dir The directory.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanCustomDir(final File dir) {
        return deleteFilesInDir(dir);
    }

    public static boolean deleteFilesInDir(final String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static boolean deleteFilesInDir(final File dir) {
        if (dir == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    private static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KiB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MiB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GiB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TiB";
    }


    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        if (files == null) {
            return 0;
        }
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
