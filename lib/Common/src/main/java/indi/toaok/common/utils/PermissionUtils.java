package indi.toaok.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import indi.toaok.utils.Utils;
import indi.toaok.utils.core.AppUtils;
import indi.toaok.utils.core.LogUtils;

/**
 * @author Toaok
 * @version 1.0  2018/11/9.
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";

    private static WeakReference<Context> wrContext;

    private static void initContext(Context context) {
        if (wrContext == null) {
            wrContext = new WeakReference<>(context);
        } else {
            LogUtils.d(TAG, "context:" + context);
            LogUtils.d(TAG, "wrContext:" + wrContext.get());
            if (wrContext.get() != context) {
                wrContext = new WeakReference<>(context);
            }
        }
    }

    /**
     * 判断是否开启通知权限
     *
     * @param context {@link Context}
     * @return boolean
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        initContext(context);

       /* if (Build.VERSION.SDK_INT >= 26) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }*/

        if (Build.VERSION.SDK_INT >= 19) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(wrContext.get());
            return manager.areNotificationsEnabled();
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) wrContext.get().getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = wrContext.get().getApplicationInfo();
        String pkg = wrContext.get().getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class<?> appOpsClass;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod;
            checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            if (((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) != AppOpsManager.MODE_ALLOWED)) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通知权限弹框
     *
     * @param context {@link Context}
     * @param message {@link String} 提示信息
     */
    public static void notificationPermissionDialog(Context context, String message) {
        initContext(context);
        AlertDialog notificationDialog = new AlertDialog.Builder(wrContext.get())
                .setMessage(message)
                .setNegativeButton("取消", (dialog, which) -> dialog.cancel()).setPositiveButton("设置", (dialog, which) -> PermissionUtils.openToNotificationSetting(wrContext.get()))
                .setCancelable(false)
                .create();
        notificationDialog.setCanceledOnTouchOutside(true);
        notificationDialog.show();
    }

    /**
     * 跳转到通知设置界面
     *
     * @param context {@link Context}
     */
    private static void openToNotificationSetting(Context context) {
        initContext(context);
        try {
            Intent intent = new Intent();

            if (Build.VERSION.SDK_INT >= 26) {
                //android 8.0引导
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", wrContext.get().getPackageName());
            } else if (Build.VERSION.SDK_INT >= 21) {
                //android 5.0-7.0
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", wrContext.get().getPackageName());
                intent.putExtra("app_uid", wrContext.get().getApplicationInfo().uid);
            } else if (Build.VERSION.SDK_INT < 21) {
                //其他
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", wrContext.get().getPackageName(), null));
            }
            wrContext.get().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            //下面这种方案是直接跳转到当前应用的设置界面。
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", wrContext.get().getPackageName(), null);
            intent.setData(uri);
            wrContext.get().startActivity(intent);
        }
    }


    /**
     * 自定义打开GPS提示弹框
     *
     * @param context
     * @param resId
     * @return
     */
    public static void showOpenGpsSettingDialog(final Context context, final @StringRes int resId) {
        getOpenGpsSettingDialog(context, resId).show();
    }

    /**
     * 自定义打开GPS提示弹框
     *
     * @param context
     * @param resId
     * @return
     */
    public static AlertDialog getOpenGpsSettingDialog(final Context context, final @StringRes int resId) {
        initContext(context);
        return new AlertDialog.Builder(wrContext.get())
                .setMessage(getPermissionPrompt(resId))
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                }).setPositiveButton("设置", (dialog, which) -> {
                    dialog.dismiss();
                    openGpsSettings(wrContext.get());
                })
                .setCancelable(false)
                .create();
    }


    /**
     * 打卡GPS设置页面
     *
     * @param context
     */
    public static void openGpsSettings(Context context) {
        initContext(context);
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        wrContext.get().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 自定义权限提示弹框
     *
     * @param context {@link Context}
     * @param resId   {@link StringRes} 提示信息
     */
    public static void showPermissionDialog(Context context, @StringRes int resId) {
        getPermissionDialog(context, resId, new Object[]{}).show();
    }

    /**
     * 自定义权限提示弹框
     *
     * @param context {@link Context}
     * @param resId   {@link StringRes} 提示信息 自定义缺省信息
     */
    public static void showPermissionDialog(Context context, @StringRes int resId, Object... args) {
        getPermissionDialog(context, resId, args).show();
    }

    /**
     * 自定义权限提示弹框
     *
     * @param context {@link Context}
     * @param resId   {@link StringRes} 提示信息
     * @return {@link AlertDialog}
     */
    public static AlertDialog getPermissionDialog(Context context, @StringRes int resId) {
        return getPermissionDialog(context, resId, new Object[]{});
    }

    /**
     * 自定义权限提示弹框
     *
     * @param context {@link Context}
     * @param resId   {@link StringRes} 提示信息
     * @param args    {@link String... } 提示信息缺省值
     * @return {@link AlertDialog}
     */
    private static AlertDialog getPermissionDialog(final Context context, final @StringRes int resId, Object... args) {
        initContext(context);
        if (args != null) {
            return getPermissionDialog(context, getPermissionPrompt(resId, args));
        } else {
            return getPermissionDialog(context, getPermissionPrompt(resId));
        }
    }

    private static AlertDialog getPermissionDialog(final Context context, final String message) {
        initContext(context);
        return new AlertDialog.Builder(wrContext.get())
                .setMessage(message)
                .setNegativeButton("取消", (dialog, which) -> {
                    dialog.dismiss();
                }).setPositiveButton("设置", (dialog, which) -> {
                    dialog.dismiss();
                    openToAppInfoSetting(wrContext.get());
                })
                .setCancelable(false)
                .create();
    }

    /**
     * 跳转到权限设置界面
     *
     * @param context {@link Context}
     */
    @SuppressLint("ObsoleteSdkInt")
    private static void openToAppInfoSetting(Context context) {
        initContext(context);
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", wrContext.get().getPackageName(), null));
        } else {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", wrContext.get().getPackageName());
        }
        wrContext.get().startActivity(localIntent);
    }

    @SuppressLint("ResourceType")
    private static String getPermissionPrompt(@StringRes int resId) {
        return Utils.getApp().getString(resId, AppUtils.getAppName(), AppUtils.getAppName());
    }

    @SuppressLint("ResourceType")
    private static String getPermissionPrompt(@StringRes int resId, Object... args) {
        return getPermissionPrompt(Utils.getApp().getString(resId), args);
    }

    @SuppressLint("ResourceType")
    private static String getPermissionPrompt(String format, Object... args) {
        return String.format(format, args);
    }
}
