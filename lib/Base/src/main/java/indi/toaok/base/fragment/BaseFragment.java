package indi.toaok.base.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected static final String TAG = BaseFragment.class.getSimpleName();

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
        if (now - lastClick >= 300) {
            lastClick = now;
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (isFastClick()) return;
    }
}
