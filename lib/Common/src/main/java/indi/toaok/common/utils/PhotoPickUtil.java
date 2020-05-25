package indi.toaok.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

import indi.toaok.utils.core.BarUtils;
import indi.toaok.utils.core.ImageUtil;
import indi.toaok.utils.core.ScreenUtils;
import indi.toaok.utils.core.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hpp
 * @version 1.0  2019/5/16.
 */
public class PhotoPickUtil {

    // 系统文件选择返回
    public static final int REQUESTCODE_SYS_PICK_IMAGE = 901;
    // 系统相机拍照返回
    public static final int REQUESTCODE_SYS_CAMERA = 902;
    // 裁剪
    public static final int REQUESTCODE_SYS_CROP = 903;

    /**
     * 相册
     *
     * @param context
     */
    public static void takePicFromGallery(Context context) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        ((Activity) context).startActivityForResult(photoPickerIntent, REQUESTCODE_SYS_PICK_IMAGE);
    }

    /**
     * 拍照
     *
     * @param context
     * @param absolutePath
     * @return
     */
    public static Uri takePickByCamera(Context context, String absolutePath) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (TextUtils.isEmpty(absolutePath)) {
                    return null;
                }
                File file = new File(absolutePath);

                Uri mUri = FileProviderUtils.uriFromFile(context, file);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                ((Activity) context).startActivityForResult(intent, REQUESTCODE_SYS_CAMERA);
                return mUri;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "没有找到储存目录", Toast.LENGTH_LONG).show();
                return null;
            }
        } else {
            Toast.makeText(context, "没有找到储存目录", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    /**
     * 调用系统裁剪
     *
     * @param context
     * @param photoUri
     */
    public static void toCrop(Context context, Uri photoUri, File outputFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        FileProviderUtils.setIntentDataAndType(context, intent, "image/*", photoUri, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        Uri outputUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);//将裁剪的图片存储到文件

        intent.putExtra("return-data", false);//不直接返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ((Activity) context).startActivityForResult(intent, REQUESTCODE_SYS_CROP);
    }

    /**
     * 获取当前屏幕截图，不包含状态栏（Status Bar）。
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = BarUtils.getStatusBarHeight();
        int width = ScreenUtils.getScreenWidth();
        int height = ScreenUtils.getScreenHeight();
        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return ret;
    }


    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }


    /**
     * 保存图片到相册
     * @param context
     * @param target 要保存的图片
     * @param dir 保存的目录
     * @param fileName 保存的文件名
     */
    public static void saveImgToGallery(final Context context, final Bitmap target, final String dir, final String fileName) {
        Observable.create((ObservableOnSubscribe<File>) emitter -> {
            String targeFilePath = dir + fileName;
            ImageUtil.save(target, targeFilePath, Bitmap.CompressFormat.JPEG);
            File targetFile = new File(targeFilePath);
            //扫描媒体库
            String extension = MimeTypeMap.getFileExtensionFromUrl(targetFile.getAbsolutePath());
            String mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{targetFile.getAbsolutePath()},
                    new String[]{mimeTypes}, null);
            emitter.onNext(targetFile);
        })
                .subscribeOn(Schedulers.io()) //发送事件在io线程
                .observeOn(AndroidSchedulers.mainThread())//最后切换主线程提示结果
                .subscribe(file -> ToastUtils.showShort("保存图片成功"),
                        throwable -> ToastUtils.showShort("保存失败"));
    }

}
