package indi.toaok.imageloder.core;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

/**
 * @author Toaok
 * @version 1.0  2018/9/10.
 */
public interface BaseImageLoderStrategy {

    void loadImage(ImageView imageView,Integer resourceId);

    /**
     * 自定义加载
     *
     * @param imageView
     * @param imageUrl
     * @param imageLoderConfig
     * @param listener
     */
    void loadImage(ImageView imageView, String imageUrl, ImageLoderConfig imageLoderConfig, LoaderListener listener);

    /**
     * 加载基础图片
     *
     * @param imageView
     * @param imageUrl
     */
    void loadImage(ImageView imageView, String imageUrl);

    /**
     * 先加载缩略图，再加载大图
     *
     * @param imageView
     * @param imageUrl
     * @param thumbnailUrl
     */
    void loadImage(ImageView imageView, String imageUrl, String thumbnailUrl);

    /**
     * 加载圆角图
     *
     * @param imageView
     * @param imageUrl
     */
    void loadRoundedImage(ImageView imageView, String imageUrl,float cornersRadius);

    /**
     * 加载圆角图
     *
     * @param imageView
     * @param imageUrl
     */
    void loadRoundedImage(ImageView imageView, String imageUrl,float cornersRadius,LoaderListener listener);

    /**
     * 加载圆图
     *
     * @param imageView
     * @param imageUrl
     */
    void loadCircleImage(ImageView imageView, String imageUrl);

    /**
     * 加载Gif
     *
     * @param imageView
     * @param imageUrl
     */
    void loadGif(ImageView imageView, String imageUrl);

    /**
     *
     * @param context
     * @param handler
     * @param uri
     * @param savePath
     * @param name
     * @param isInsertMedia
     */
    void downloadImage(Context context, Handler handler, String uri, String savePath, String name, boolean isInsertMedia);

    /**
     * @param context
     * @param handler
     * @param uri
     * @param savePath
     * @param name
     * @param isInsertMedia
     * @param loaderListener
     */
    void downloadImage(Context context, Handler handler, String uri, String savePath, String name, boolean isInsertMedia, LoaderListener loaderListener);
}
