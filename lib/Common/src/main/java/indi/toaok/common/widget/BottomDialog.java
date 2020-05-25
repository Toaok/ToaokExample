package indi.toaok.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import indi.toaok.common.R;

/**
 * @author Toaok
 * @version 1.0  2019/10/15.
 */
public class BottomDialog extends Dialog {


    public BottomDialog(Context context) {
        this(context, R.style.BottomDialogStyle);
    }

    public BottomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);

        setWindowPosition();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setWindowPosition();
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setWindowPosition();
    }

    private void setWindowPosition() {
        setCanceledOnTouchOutside(false);
        /**
         * 控制Dialog 大小
         */
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = -1;
        lp.height=-2;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }
}
