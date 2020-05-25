package indi.toaok.imageloder.core;

import android.graphics.Bitmap;

/**
 * @author Toaok
 * @version 1.0  2018/11/7.
 */
public interface LoaderListener {
    void onSuccess(Bitmap bitmap);

    void onError();
}
