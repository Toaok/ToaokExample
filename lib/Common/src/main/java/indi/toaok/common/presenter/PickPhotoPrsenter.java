package indi.toaok.common.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import indi.toaok.common.R;
import indi.toaok.common.listener.IOnActivityResult;
import indi.toaok.common.utils.FilePathUtil;
import indi.toaok.common.utils.FileProviderUtils;
import indi.toaok.common.utils.PermissionUtils;
import indi.toaok.common.utils.PhotoPickUtil;
import indi.toaok.common.utils.SystemPhotoPathUtil;
import indi.toaok.pluto.core.dialog.dialog.listener.OnOperItemClickL;
import indi.toaok.pluto.core.dialog.dialog.widget.ActionSheetDialog;
import indi.toaok.pluto.core.luban.Luban;
import indi.toaok.pluto.core.luban.OnCompressListener;
import indi.toaok.utils.core.LogUtils;


/**
 * @author hpp
 * @version 1.0  2019/5/29.
 */
public class PickPhotoPrsenter implements IOnActivityResult {
    AppCompatActivity mActivity;

    private Uri uriTakePhoto;
    private File outputFile;
    private String outputFilePath;
    private File cacheFile;
    private String cacheFilePath;
    ActionSheetDialog pickImageDialog;
    String[] dialogItems = {"相机", "相册"};
    RxPermissions mRxPermissions;

    OnPickPhotoListenerForFile mOnPickPhotoListenerForFile;
    OnPickPhotoListenerForUri mOnPickPhotoListenerForUri;

    private boolean isToCrop = true;

    public PickPhotoPrsenter(AppCompatActivity activity) {
        mActivity = activity;
        mRxPermissions = new RxPermissions(mActivity);
    }

    public PickPhotoPrsenter(AppCompatActivity activity, String outputFilePath) {
        this.outputFilePath = outputFilePath;
        mActivity = activity;
        mRxPermissions = new RxPermissions(mActivity);
    }

    public PickPhotoPrsenter(AppCompatActivity activity, File outputFile) {
        this.outputFile = outputFile;
        mActivity = activity;
        mRxPermissions = new RxPermissions(mActivity);

        initPickDialog();
    }

    private void initPickDialog() {
        pickImageDialog = new ActionSheetDialog(mActivity, dialogItems, null)
                .isTitleShow(false)
                .layoutAnimation(null)
                .showAnim(null);
        pickImageDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (outputFile == null) {
                    outputFile = getOutputFile(true);
                }
                switch (position) {
                    case 0:
                        //相机
                        mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(granted -> {
                                    if (granted) {
                                        uriTakePhoto = PhotoPickUtil.takePickByCamera(mActivity, FilePathUtil.getCacheImageFilePath());
                                    } else {
                                        PermissionUtils.showPermissionDialog(mActivity,
                                                R.string.permissions_camera_storage);
                                    }
                                });
                        break;
                    case 1:
                        //相册
                        mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(granted -> {
                                    if (granted) {
                                        PhotoPickUtil.takePicFromGallery(mActivity);
                                    } else {
                                        PermissionUtils.showPermissionDialog(mActivity, R.string.permissions_camera_storage);
                                    }
                                });
                        break;
                }
                pickImageDialog.dismiss();
            }
        });
    }


    public void setPickPhotoListener(OnPickPhotoListenerForFile pickPhotoListener) {
        mOnPickPhotoListenerForFile = pickPhotoListener;
    }

    public void setPickPhotoListener(OnPickPhotoListenerForUri onPickPhotoListenerForUri) {
        mOnPickPhotoListenerForUri = onPickPhotoListenerForUri;
    }

    public void setToCrop(boolean toCrop) {
        isToCrop = toCrop;
    }

    public boolean isToCrop() {
        return isToCrop;
    }


    /**
     * 裁剪压缩后图片的路径
     *
     * @param isCreate
     * @return
     */
    private File getOutputFile(boolean isCreate) {
        if (outputFile == null && isCreate) {
            if (TextUtils.isEmpty(outputFilePath)) {
                outputFilePath = FilePathUtil.getImageFilePath();
            }
            outputFile = new File(outputFilePath);
        }
        return outputFile;
    }

    public File getOutputFile() {
        return getOutputFile(false);
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }


    /**
     * 裁剪后的缓存
     *
     * @return
     */
    public File getCacheFile() {
        if (cacheFile == null) {
            if (TextUtils.isEmpty(cacheFilePath)) {
                cacheFilePath = FilePathUtil.getCacheCropImageFilePath();
            }
            cacheFile = new File(cacheFilePath);
        }
        return cacheFile;
    }

    public String getCacheFilePath() {

        return cacheFilePath;
    }

    public ActionSheetDialog getPickImageDialog() {
        if (pickImageDialog == null) {
            initPickDialog();
        }
        return pickImageDialog;
    }

    /**
     * 必须在{@link Activity#onActivityResult(int, int, Intent)}中调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 拍照返回
                case PhotoPickUtil.REQUESTCODE_SYS_CAMERA:
                    if (uriTakePhoto != null) {
                        if (isToCrop) {
                            PhotoPickUtil.toCrop(mActivity, uriTakePhoto, getCacheFile());
                        } else {
                            if (mOnPickPhotoListenerForUri != null) {
                                mOnPickPhotoListenerForUri.onPickPhotoResult(uriTakePhoto);
                            }
                        }
                    }
                    break;
                // 相册选择返回
                case PhotoPickUtil.REQUESTCODE_SYS_PICK_IMAGE:
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        String url = SystemPhotoPathUtil.getPath(mActivity.getApplicationContext(), data.getData());
                        uriTakePhoto = FileProviderUtils.uriFromFile(mActivity, new File(url));
                    } else {
                        uriTakePhoto = data.getData();
                    }
                    if (isToCrop) {
                        PhotoPickUtil.toCrop(mActivity, uriTakePhoto, getCacheFile());
                    } else {
                        if (mOnPickPhotoListenerForUri != null) {
                            mOnPickPhotoListenerForUri.onPickPhotoResult(uriTakePhoto);
                        }
                    }
                    break;
                // 调用系统裁剪
                case PhotoPickUtil.REQUESTCODE_SYS_CROP:
                    if (mOnPickPhotoListenerForFile != null) {
                        Luban.with(mActivity)
                                .load(cacheFile)
                                .ignoreBy(100)
                                .setTargetDir(FilePathUtil.getImagePath())
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                        LogUtils.i("onStart:开始鲁班压缩 ");
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        // TODO 压缩成功后调用，返回压缩后的图片文件
                                        mOnPickPhotoListenerForFile.onPickPhotoResult(file);
                                        outputFile = file;
                                        LogUtils.i("onSuccess: 鲁班压缩成功 ：" + file.getAbsolutePath());
                                        try {
                                            int size = new FileInputStream(file).available();
                                            LogUtils.i("tag", "鲁班压缩 size--->:" + "byte:" + size + "    kb:"
                                                    + (float) size / 1024);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // TODO 当压缩过程出现问题时调用
                                        LogUtils.i("onError: 鲁班压缩出错");
                                    }
                                })
                                .launch();
                    }
                    break;
            }
        }
    }


    public interface OnPickPhotoListenerForFile {
        void onPickPhotoResult(File outputFile);
    }

    public interface OnPickPhotoListenerForUri {
        void onPickPhotoResult(Uri outputUri);
    }
}
