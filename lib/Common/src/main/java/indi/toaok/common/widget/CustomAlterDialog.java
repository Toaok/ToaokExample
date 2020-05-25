package indi.toaok.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import indi.toaok.common.R;
import indi.toaok.utils.core.SizeUtils;


/**
 * @author Toaok
 * @version 1.0  2019/10/15.
 */
public class CustomAlterDialog extends Dialog {
    public CustomAlterDialog(Context context) {
        super(context);

    }

    public CustomAlterDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder implements View.OnClickListener {
        CustomAlterDialog dialog;
        private Context context;
        private CharSequence title;
        private CharSequence content;
        private CharSequence positiveText;
        private CharSequence negativeText;
        private OnCustomDialogListener positiveButtonClickListener;
        private OnCustomDialogListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPositiveButton(CharSequence positiveButtonText, OnCustomDialogListener listener) {
            this.positiveText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence negativeButtonText, OnCustomDialogListener listener) {
            this.negativeText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setContent(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }


        TextView mTitle;
        TextView mContent;
        TextView mPositiveBtn;
        View mCenterLine;
        TextView mNegativeBtn;


        /**
         * Create the custom dialog
         */
        public CustomAlterDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new CustomAlterDialog(context, R.style.CenterDialogStyle);
            View layout = inflater.inflate(R.layout.view_custom_alter_dialog, null);
            dialog.setContentView(layout);

            mTitle = layout.findViewById(R.id.tv_dialog_title);
            mContent = layout.findViewById(R.id.tv_dialog_content);
            mPositiveBtn = layout.findViewById(R.id.tv_positive_btn);
            mCenterLine = layout.findViewById(R.id.center_line);
            mNegativeBtn = layout.findViewById(R.id.tv_negative_btn);


            if (!TextUtils.isEmpty(title)) {
                mTitle.setText(title);
            } else {
                mTitle.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(content)) {
                mContent.setText(content);
                mContent.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                mContent.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(positiveText) && positiveButtonClickListener != null) {
                mPositiveBtn.setText(positiveText);
            } else {
                mPositiveBtn.setVisibility(View.GONE);
                mCenterLine.setVisibility(View.GONE);
            }
            mPositiveBtn.setOnClickListener(this);

            if (!TextUtils.isEmpty(negativeText) && negativeButtonClickListener != null) {
                mNegativeBtn.setText(negativeText);
            } else {
                mNegativeBtn.setVisibility(View.GONE);
                mCenterLine.setVisibility(View.GONE);
            }
            mNegativeBtn.setOnClickListener(this);

            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            /**
             * 控制Dialog 大小
             */
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.height = -2;
            lp.width = (int) SizeUtils.dp2px(290f);
            dialogWindow.setAttributes(lp);
            return dialog;
        }


        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_positive_btn) {
                if (positiveButtonClickListener != null) {
                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            } else if (id == R.id.tv_negative_btn) {
                if (negativeButtonClickListener != null) {
                    negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            }
        }
    }

    public interface OnCustomDialogListener {
        void onClick(Dialog dialog, int what);
    }
}