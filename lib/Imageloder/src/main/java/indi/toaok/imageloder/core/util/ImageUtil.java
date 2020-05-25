package indi.toaok.imageloder.core.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;


/**
 * Created by sj on 14/12/2016.
 */

public class ImageUtil {

    private static final String TAG = "ImageLoader";

    /**
     * 保存图片成功
     */
    public static final int SAVE_IMAGE_SUCCESS = 2000;

    /**
     * 保存图片失败
     */
    public static final int SAVE_IMAGE_FAIL = 2002;
    /**
     * 图片质量，100表示最高
     */
    private static final int BITMAP_QUALITY = 95;

    /**
     * 保存网络下载的图片到指定地址，并插入相册可见
     *
     * @param context
     * @param handler
     * @param bitmap
     * @param url
     * @param name
     */
    public static void saveImageAndRefresh(Context context, Handler handler, Bitmap bitmap, String url, String savePath, String name, boolean isRefresh) {
        if (bitmap != null) {
            String path;
            // 有sd写入到sd
            if (FileUtil.isExistSdcard()) {
                path = saveBitmapToSDcard(context, bitmap, url, savePath, null, isRefresh);
            }
            // 无sd写入到相册
            else {
                path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, name, "");
            }
            if (path != null) {
                // 通知相册刷新
                if (isRefresh) {
                    if (!FileUtil.isVersionUpKitkat()) {
                        try {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                        } catch (SecurityException e) {
                            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()}, new String[]{"image/*"}, null);
                        }
                    } else {
                        MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()}, new String[]{"image/*"}, null);
                    }
                }
                if (handler != null) {
                    handler.sendEmptyMessage(SAVE_IMAGE_SUCCESS);
                }
            }
        } else {
            if (handler != null) {
                handler.sendEmptyMessage(SAVE_IMAGE_FAIL);
            }
        }
    }

    /**
     * 保存网络图片 bitmap 到文件
     *
     * @param context
     * @param bm        不能为空
     * @param url       不能为空
     * @param savePath  不能为空
     * @param name      文件名为空则 使用url哈希化为文件名
     * @param isRefresh
     * @return
     */
    public static String saveBitmapToSDcard(Context context, Bitmap bm, String url, String savePath, String name, boolean isRefresh) {
        if (bm == null || TextUtils.isEmpty(url)||TextUtils.isEmpty(savePath)) {
            return null;
        }

        String filename = TextUtils.isEmpty(name) ? FileUtil.convertUrlToFileName(url) : name;
        File file = new File(FileUtil.checkAndMkdirs(savePath) + filename);
        if (file.exists()) {
            Log.e(TAG, "save bitmap but file exists");
            return file.getAbsolutePath();
        }
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException" + (e == null ? "" : e.getMessage()));
        } catch (IOException e) {
            Log.e(TAG, "IOException" + (e == null ? "" : e.getMessage()));
        }

        if (isRefresh) {
            ContentValues values = new ContentValues(7);
            values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            ContentResolver contentResolver = context.getContentResolver();

            try {
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    public static Bitmap getCacheImage(String filepath,String uri) {
        File file = FileUtil.getCacheFileFromUrl(filepath,uri);
        if (file != null) {
            return BitmapFactory.decodeFile(file.getPath());
        }
        return null;
    }

    /**
     * Return the round bitmap.
     *
     * @param src The source of bitmap.
     * @return the round bitmap
     */
    public static Bitmap toRound(final Bitmap src) {
        return toRound(src, 0, 0, false);
    }

    /**
     * Return the round bitmap.
     *
     * @param src     The source of bitmap.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the round bitmap
     */
    public static Bitmap toRound(final Bitmap src, final boolean recycle) {
        return toRound(src, 0, 0, recycle);
    }

    /**
     * Return the round bitmap.
     *
     * @param src         The source of bitmap.
     * @param borderSize  The size of border.
     * @param borderColor The color of border.
     * @return the round bitmap
     */
    public static Bitmap toRound(final Bitmap src,
                                 @IntRange(from = 0) int borderSize,
                                 @ColorInt int borderColor) {
        return toRound(src, borderSize, borderColor, false);
    }

    /**
     * Return the round bitmap.
     *
     * @param src         The source of bitmap.
     * @param recycle     True to recycle the source of bitmap, false otherwise.
     * @param borderSize  The size of border.
     * @param borderColor The color of border.
     * @return the round bitmap
     */
    public static Bitmap toRound(final Bitmap src,
                                 @IntRange(from = 0) int borderSize,
                                 @ColorInt int borderColor,
                                 final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        int size = Math.min(width, height);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        float center = size / 2f;
        RectF rectF = new RectF(0, 0, width, height);
        rectF.inset((width - size) / 2f, (height - size) / 2f);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        matrix.preScale((float) size / width, (float) size / height);
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        Canvas canvas = new Canvas(ret);
        canvas.drawRoundRect(rectF, center, center, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            float radius = center - borderSize / 2f;
            canvas.drawCircle(width / 2f, height / 2f, radius, paint);
        }
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }
}
