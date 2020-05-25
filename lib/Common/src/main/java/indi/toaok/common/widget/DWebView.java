package indi.toaok.common.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DWebView extends WebView {


    public DWebView(Context context) {
        this(context, null);
    }

    public DWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        this.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setSupportMultipleWindows(true);
        this.getSettings().setSupportZoom(false);
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadByBrowser(url);
            }
        });
        this.requestFocusFromTouch();
    }

    /*
     * 浏览器下载
     * */
    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }


    OnCustomScrollChangeListener mCustomScrollChangeListener;

    public OnCustomScrollChangeListener getCustomScrollChangeListener() {
        return mCustomScrollChangeListener;
    }

    public void setCustomScrollChangeListener(OnCustomScrollChangeListener customScrollChangeListener) {
        mCustomScrollChangeListener = customScrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCustomScrollChangeListener != null) {
            mCustomScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public interface OnCustomScrollChangeListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
