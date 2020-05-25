package indi.toaok.common.utils;

import java.io.File;

import indi.toaok.utils.core.AppUtils;
import indi.toaok.utils.core.PathUtils;

/**
 * 外部存储
 */
public class FilePathUtil {

    static final String sBasePath = PathUtils.getExternalStoragePath();

    public static String getBasePath() {
        return sBasePath + "/" + AppUtils.getAppName() + "/";
    }

    private static String getBaseDirPath(String childDir) {
        return getBasePath() + childDir;
    }

    public static String getCacheCropPath() {
        return checkAndMkdirs(getBaseDirPath("cache/crop/"));
    }

    public static String getCacheImagePath() {
        return checkAndMkdirs(getBaseDirPath("cache/images/"));
    }

    public static String getCacheCropImageFilePath() {
        return getCacheCropPath() + System.currentTimeMillis() + ".jpg";
    }

    public static String getCacheImageFilePath() {
        return getCacheImagePath() + System.currentTimeMillis() + ".jpg";
    }

    public static String getImageFilePath() {
        return getImagePath() + System.currentTimeMillis() + ".jpg";
    }

    public static String getCacheWebPath() {
        return checkAndMkdirs(getBaseDirPath("cache/web/"));
    }

    public static String getCache() {
        return checkAndMkdirs(getBaseDirPath("cache/"));
    }

    public static String getImagePath() {
        return checkAndMkdirs(getBaseDirPath("images/"));
    }

    public static String getSplashImagePath() {
        return checkAndMkdirs(getBaseDirPath("images/splash/"));
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @return
     */
    public static String checkAndMkdirs(String dir) {
        File file = new File(dir);
        if (file.exists() == false) {
            file.mkdirs();
        }
        return dir;
    }

    public static String getRandomImageName() {
        return System.currentTimeMillis() + ".jpg";
    }

}