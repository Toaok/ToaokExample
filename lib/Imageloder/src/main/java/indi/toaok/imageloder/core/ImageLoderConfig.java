package indi.toaok.imageloder.core;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;

/**
 * @author Toaok
 * @version 1.0  2018/10/31.
 */
public class ImageLoderConfig {

    public static final int CENTER_CROP = 0;
    public static final int FIT_CENTER = 1;
    public static final int CENTER_INSIDE = 2;

    private Builder mBuilder;

    private ImageLoderConfig(Builder builder) {
        placeHolderDrawable = builder.placeHolderDrawable;
        errorDrawable = builder.errorDrawable;
        placeHolderResId = builder.placeHolderResId;
        errorResId = builder.errorResId;
        crossFade = builder.crossFade;
        croossDuration = builder.croossDuration;
        mDisplaySize = builder.mDisplaySize;
        cropType = builder.cropType;
        asGif = builder.asGif;
        asBitmap = builder.asBitmap;
        skipMemoryCache = builder.skipMemoryCache;
        diskCacheStrategy = builder.diskCacheStrategy;
        loadPriority = builder.loadPriority;
        thumbnail = builder.thumbnail;
        thumbnailUrl = builder.thumbnailUrl;
        viewTarget = builder.viewTarget;
        notificationTarget = builder.notificationTarget;
        appWidgetTarget = builder.appWidgetTarget;
        animResId = builder.animResId;
        cropCircle = builder.cropCircle;
        cornersRadius = builder.cornersRadius;
        borderSize = builder.borderSize;
        borderColor = builder.borderColor;
        grayscale = builder.grayscale;
        blur = builder.blur;
        rotate = builder.rotate;
        rotateDegree = builder.rotateDegree;
        tag = builder.tag;
        mBuilder = builder;
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public void setBuilder(Builder builder) {
        mBuilder = builder;
    }

    /**
     * 使用注解代替Enum,限定类型
     * 图片裁剪类型
     */
    @IntDef({CENTER_CROP, FIT_CENTER, CENTER_INSIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CropType {
    }


    /**
     * 默认占位资源
     */
    private @DrawableRes
    Integer placeHolderResId;

    /**
     * 错误时显示的占位资源
     */
    private @DrawableRes
    Integer errorResId;


    /**
     * 默认占位资源
     */
    private
    Drawable placeHolderDrawable;

    /**
     * 错误时显示的占位资源
     */
    private
    Drawable errorDrawable;

    /**
     * 是否使用淡入淡出动画
     */
    private boolean crossFade;

    /**
     * 淡入淡出动画持续时间
     */
    private int croossDuration;


    /**
     * 图片最终显示在ImageView上的宽高度像素
     */
    private DisplaySize mDisplaySize;

    /**
     * 裁剪类型(0 CENTER_CROP ,1 FIT_CENTER,2 CENTER_INSIDE)
     * 默认CENTER_CROP
     */
    private @CropType
    int cropType;

    /**
     * true,强制显示gif动画，如果url不是gif的资源，那么会回调error()
     */
    private boolean asGif;

    /**
     * true,强制显示常规图片，如果url是gif的资源，则加载第一帧作为图片
     */
    private boolean asBitmap;

    /**
     * true,跳过内存缓存，默认false
     */
    private boolean skipMemoryCache;

    /**
     * 硬盘缓存策略，默认所有类型
     */
    private MDiskCacheStrategy diskCacheStrategy;

    /**
     * 加载优先级策略
     */
    private LoadPriority loadPriority;

    /**
     * 设置缩略图的缩放比例0.0f-1.0f,
     * 如果缩略图币全尺寸图先加载完，就显示缩略图，否则就不显示
     */
    private float thumbnail;

    /**
     * 设置缩略图的url,
     * 如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
     */
    private String thumbnailUrl;

    /**
     * 指定ViewTarget对象，可以在Target回调方法中处理bitmap,同时该构造方法中还可以指定size
     */
    private CustomViewTarget<? extends View, Drawable> viewTarget;

    /**
     * 设置通知栏加载大图片的target
     */
    private NotificationTarget notificationTarget;

    /**
     * 设置加载小部件图片的target
     */
    private AppWidgetTarget appWidgetTarget;

    /**
     * 图片加载完成后的动画效果，在异步加载资源完成时会执行该动画
     */
    private Integer animResId;

//    private ViewPropertyAnimator.Animator animator;

    /**
     * 圆形裁剪
     */
    private boolean cropCircle;
    /**
     * 圆角处理
     */
    private float cornersRadius;

    /**
     * 边框大小
     */
    private int borderSize;
    /**
     * 边框颜色
     */
    private int borderColor;

    /**
     * 灰度处理
     */
    private boolean grayscale;
    /**
     * 高斯模糊处理
     */
    private boolean blur;
    /**
     * 旋转图片
     */
    private boolean rotate;
    /**
     * 默认旋转
     */
    private int rotateDegree;
    /**
     * 唯一标识
     */
    private String tag;


    /**
     * 加载优先级策略
     */
    public enum LoadPriority {
        LOW(Priority.LOW),
        NORMAL(Priority.NORMAL),
        HIGH(Priority.HIGH),
        IMMEDIATE(Priority.IMMEDIATE);

        Priority priority;

        LoadPriority(Priority priority) {
            this.priority = priority;
        }

        public Priority getPriority() {
            return priority;
        }
    }

    /**
     * 硬盘缓存策略
     */
    public enum MDiskCacheStrategy {
        NONE(DiskCacheStrategy.NONE),//不使用硬盘缓存
        DATA(DiskCacheStrategy.DATA),//缓存原始分辨率的图片
        RESOURCE(DiskCacheStrategy.RESOURCE),//缓存转换后的图片
        ALL(DiskCacheStrategy.ALL);//缓存所有类型的图片

        DiskCacheStrategy diskCacheStrategy;

        MDiskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
            this.diskCacheStrategy = diskCacheStrategy;
        }

        public DiskCacheStrategy getStrategy() {
            return diskCacheStrategy;
        }
    }

    /**
     * 图片最终显示在ImageView上的宽高度像素
     */
    public static class DisplaySize {
        private final int width;
        private final int height;

        public DisplaySize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }


    public Integer getPlaceHolderResId() {
        return placeHolderResId;
    }

    public Integer getErrorResId() {
        return errorResId;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public int getCroossDuration() {
        return croossDuration;
    }

    public DisplaySize getDisplaySize() {
        return mDisplaySize;
    }

    public int getCropType() {
        return cropType;
    }

    public boolean isAsGif() {
        return asGif;
    }

    public boolean isAsBitmap() {
        return asBitmap;
    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache;
    }

    public MDiskCacheStrategy getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public LoadPriority getLoadPriority() {
        return loadPriority;
    }

    public float getThumbnail() {
        return thumbnail;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public CustomViewTarget<? extends View, Drawable> getViewTarget() {
        return viewTarget;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public AppWidgetTarget getAppWidgetTarget() {
        return appWidgetTarget;
    }

    public Integer getAnimResId() {
        return animResId;
    }

    public boolean isCropCircle() {
        return cropCircle;
    }

    public float getCornersRadius() {
        return cornersRadius;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public boolean isGrayscale() {
        return grayscale;
    }

    public boolean isBlur() {
        return blur;
    }

    public boolean isRotate() {
        return rotate;
    }

    public int getRotateDegree() {
        return rotateDegree;
    }

    public String getTag() {
        return tag;
    }

    public Drawable getPlaceHolderDrawable() {
        return placeHolderDrawable;
    }

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    /**
     * Builder类
     */
    public static final class Builder {
        private Drawable placeHolderDrawable;
        private Drawable errorDrawable;
        private Integer placeHolderResId;
        private Integer errorResId;
        private boolean crossFade;
        private int croossDuration;
        private DisplaySize mDisplaySize;
        private int cropType = CENTER_CROP;
        private boolean asGif;
        private boolean asBitmap;
        private boolean skipMemoryCache;
        private MDiskCacheStrategy diskCacheStrategy = MDiskCacheStrategy.ALL;
        private LoadPriority loadPriority = LoadPriority.HIGH;
        private float thumbnail;
        private String thumbnailUrl;
        private CustomViewTarget<? extends View, Drawable> viewTarget;
        private NotificationTarget notificationTarget;
        private AppWidgetTarget appWidgetTarget;
        private Integer animResId;
        private boolean cropCircle;
        private float cornersRadius;

        private int borderSize = 0;

        private int borderColor = Color.WHITE;

        private boolean grayscale;
        private boolean blur;
        private boolean rotate;
        private int rotateDegree = 90;
        private String tag;

        public Builder() {
        }

        public Builder placeHolderDrawable(Drawable val) {
            placeHolderDrawable = val;
            return this;
        }

        public Builder errorDrawable(Drawable val) {
            errorDrawable = val;
            return this;
        }

        public Builder placeHolderResId(Integer val) {
            placeHolderResId = val;
            return this;
        }

        public Builder errorResId(Integer val) {
            errorResId = val;
            return this;
        }

        public Builder crossFade(boolean val) {
            crossFade = val;
            return this;
        }

        public Builder croossDuration(int val) {
            croossDuration = val;
            return this;
        }

        public Builder mDisplaySize(DisplaySize val) {
            mDisplaySize = val;
            return this;
        }

        public Builder cropType(int val) {
            cropType = val;
            return this;
        }

        public Builder asGif(boolean val) {
            asGif = val;
            return this;
        }

        public Builder asBitmap(boolean val) {
            asBitmap = val;
            return this;
        }

        public Builder skipMemoryCache(boolean val) {
            skipMemoryCache = val;
            return this;
        }

        public Builder diskCacheStrategy(MDiskCacheStrategy val) {
            diskCacheStrategy = val;
            return this;
        }

        public Builder loadPriority(LoadPriority val) {
            loadPriority = val;
            return this;
        }

        public Builder thumbnail(float val) {
            thumbnail = val;
            return this;
        }

        public Builder thumbnailUrl(String val) {
            thumbnailUrl = val;
            return this;
        }

        public Builder viewTarget(CustomViewTarget<? extends View, Drawable> val) {
            viewTarget = val;
            return this;
        }

        public Builder notificationTarget(NotificationTarget val) {
            notificationTarget = val;
            return this;
        }

        public Builder appWidgetTarget(AppWidgetTarget val) {
            appWidgetTarget = val;
            return this;
        }

        public Builder animResId(Integer val) {
            animResId = val;
            return this;
        }

        public Builder cropCircle(boolean val) {
            cropCircle = val;
            return this;
        }

        public Builder cornersRadius(float val) {
            cornersRadius = val;
            return this;
        }

        public Builder borderSize(int val) {
            borderSize = val;
            return this;
        }

        public Builder borderColor(int val) {
            borderColor = val;
            return this;
        }

        public Builder grayscale(boolean val) {
            grayscale = val;
            return this;
        }

        public Builder blur(boolean val) {
            blur = val;
            return this;
        }

        public Builder rotate(boolean val) {
            rotate = val;
            return this;
        }

        public Builder rotateDegree(int val) {
            rotateDegree = val;
            return this;
        }

        public Builder tag(String val) {
            tag = val;
            return this;
        }

        public ImageLoderConfig build() {
            return new ImageLoderConfig(this);
        }
    }
}
