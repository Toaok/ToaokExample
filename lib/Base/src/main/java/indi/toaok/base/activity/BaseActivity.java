package indi.toaok.base.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import indi.toaok.base.R;
import indi.toaok.utils.core.AdaptScreenUtils;
import indi.toaok.utils.core.BarUtils;
import indi.toaok.utils.core.KeyboardUtils;
import indi.toaok.utils.core.LogUtils;

/**
 * @author hpp
 * @version 1.0  2019/6/13.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected final String TAG = this.getClass().getSimpleName();

    protected boolean isShowLog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isShowLog) {
            LogUtils.i(TAG, "onCreate");
        }
        TypedValue colorPrimaryDark = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, colorPrimaryDark, true);
        BarUtils.setStatusBarLightMode(this, isLightColor(colorPrimaryDark.data));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShowLog) {
            LogUtils.i(TAG, "onStart");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isShowLog) {
            LogUtils.i(TAG, "onRestart");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShowLog) {
            LogUtils.i(TAG, "onResume");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isShowLog) {
            LogUtils.i(TAG, "onPause");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isShowLog) {
            LogUtils.i(TAG, "onStop");
        }
    }

    @Override
    public void finish() {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.hideSoftInput(this);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isShowLog) {
            LogUtils.i(TAG, "onDestroy");
        }
    }

    /**
     * 上次点击时间
     */
    private long lastClick = 0;

    /**
     * 判断是否快速点击
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 200) {
            lastClick = now;
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (isFastClick()) return;
    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    protected boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 375);
    }
}
