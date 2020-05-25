package indi.toaok.imageloder.core;

import android.content.Context;
import android.widget.ImageView;

import indi.toaok.imageloder.core.glide.GlideImageLoderStrategy;


/**
 * Created by sj on 10/16/16.
 */

public class ImageLoder {

    /**
     * 指定当前图片加载器 Glide
     * @return
     */
    protected static BaseImageLoderStrategy getLoader() {
        return GlideImageLoderStrategy.getInstance();
    }

    /**
     * 自定义加载
     * @param imageView
     * @param imageUrl
     * @param imageLoderConfig
     * @param listener
     */
    public static void loadImage(ImageView imageView, String imageUrl, ImageLoderConfig imageLoderConfig, LoaderListener listener) {
        getLoader().loadImage(imageView, imageUrl, imageLoderConfig, listener);
    }

    /**
     * 加载基础图片
     * @param imageView
     * @param imageUrl
     */
    public static void loadImage(ImageView imageView, String imageUrl){
        getLoader().loadImage(imageView, imageUrl);
    }

    /**
     * 加载基础图片
     * @param imageView
     * @param resourceId
     */
    public static void loadImage(ImageView imageView, Integer resourceId){
        getLoader().loadImage(imageView, resourceId);
    }
    /**
     * 先加载缩略图，再加载大图
     * @param imageView
     * @param imageUrl
     * @param thumbnailUrl
     */
    public static void loadImage(ImageView imageView, String imageUrl, String thumbnailUrl){
        getLoader().loadImage(imageView, imageUrl, thumbnailUrl);
    }

    /**
     * 加载圆图
     * @param imageView
     * @param imageUrl
     */
    public static void loadRoundedImage(ImageView imageView, String imageUrl,float cornersRadius){
        getLoader().loadRoundedImage(imageView, imageUrl,cornersRadius);
    }

    public static void loadRoundedImage(ImageView imageView, String imageUrl,float cornersRadius,LoaderListener loaderListener){
        getLoader().loadRoundedImage(imageView, imageUrl,cornersRadius,loaderListener);
    }


    /**
     * 加载Gif
     * @param imageView
     * @param imageUrl
     */
    public static void loadGif(ImageView imageView, String imageUrl) {
        getLoader().loadGif(imageView, imageUrl);
    }

    /**
     * 下载网络图片并保存
     * @param context
     * @param uri
     */
    public static void downloadImage(Context context, String uri, String savePath) {
        getLoader().downloadImage(context, null, uri, savePath, null, false);
    }

    /**
     *下载网络图片并保存
     *
     * @param context
     * @param uri
     * @param savePath
     * @param listener
     */
    public static void downloadImage(Context context, String uri, String savePath, LoaderListener listener) {
        getLoader().downloadImage(context, null, uri, savePath, null, false, listener);
    }
}


