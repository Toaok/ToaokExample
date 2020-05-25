package indi.toaok.imageloder.core.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import indi.toaok.imageloder.R;
import indi.toaok.imageloder.core.BaseImageLoderStrategy;
import indi.toaok.imageloder.core.ImageLoder;
import indi.toaok.imageloder.core.ImageLoderConfig;
import indi.toaok.imageloder.core.LoaderListener;
import indi.toaok.imageloder.core.util.ImageUtil;

/**
 * Glide 加载器，当前工程使用Glide 作为图片Loader
 * 全局网络图片加载 请使用 {@link ImageLoder}
 * <p>
 * 如需要使用其他图片加载引擎 ， 请自定义ImageLoaderStrategy
 * Created by sj on 10/17/16.
 */

public class GlideImageLoderStrategy implements BaseImageLoderStrategy {

    // default config
    public final static ImageLoderConfig defaultConfigBuilder = new ImageLoderConfig.Builder().
            cropType(ImageLoderConfig.CENTER_CROP)
            .asBitmap(true)
            .placeHolderResId(R.drawable.bg_image_placeholder)
            .errorResId(R.drawable.bg_image_placeholder)
            .diskCacheStrategy(ImageLoderConfig.MDiskCacheStrategy.DATA)
            .loadPriority(ImageLoderConfig.LoadPriority.HIGH)
            .build();

    private GlideImageLoderStrategy() {
    }

    public static GlideImageLoderStrategy getInstance() {
        return LazyHolder.strategy;
    }

    private static class LazyHolder {
        static final GlideImageLoderStrategy strategy = new GlideImageLoderStrategy();
    }

    @SuppressLint("CheckResult")
    private static void setListener(RequestBuilder<Bitmap> request, final LoaderListener listener) {
        request.listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                listener.onError();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                listener.onSuccess(resource);
                return false;
            }
        });
    }

    @Override
    public void loadImage(ImageView imageView, Integer resourceId) {
        final Context context = imageView.getContext().getApplicationContext();
        RequestBuilder builder;
        RequestOptions options;
        options = new RequestOptions()
                .placeholder(defaultConfigBuilder.getPlaceHolderResId())
                .error(defaultConfigBuilder.getErrorResId())
                .priority(Priority.HIGH);
        builder = Glide.with(context).load(resourceId).apply(options);
        builder.into(imageView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadImage(final ImageView imageView, String imageUrl, ImageLoderConfig config, LoaderListener listener) {
        final Context context = imageView.getContext().getApplicationContext();
        if (null == config) {
            config = defaultConfigBuilder;
        }
        try {
            RequestBuilder builder;
            RequestOptions options;

            if (config.getPlaceHolderDrawable() != null && config.getErrorDrawable() != null) {
                options = new RequestOptions()
                        .placeholder(config.getPlaceHolderDrawable())
                        .error(config.getErrorDrawable())
                        .priority(Priority.HIGH);
            } else {
                options = new RequestOptions()
                        .placeholder(config.getPlaceHolderResId())
                        .error(config.getErrorResId())
                        .priority(Priority.HIGH);
            }

            if (config.isAsGif()) {
                //gif类型
                RequestBuilder<GifDrawable> request = Glide.with(context).asGif().load(imageUrl);
                if (config.getCropType() == ImageLoderConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
                builder = request;
            } else if (config.isAsBitmap()) {
                //bitmap 类型
                RequestBuilder<Bitmap> request = Glide.with(context).asBitmap().load(imageUrl);
                if (config.getCropType() == ImageLoderConfig.CENTER_CROP) {
                    options.centerCrop();
                } else if (config.getCropType() == ImageLoderConfig.CENTER_INSIDE) {//实现centerInside效果(imageview scaletype设置后，此处要使用request.dontTransform设置才生效)
                    options.dontTransform();
                } else {
                    options.fitCenter();
                }
                builder = request;
            } else if (config.isCrossFade()) {
                RequestBuilder request = Glide.with(context).load(imageUrl);
                if (config.getCropType() == ImageLoderConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
                builder = request;
            } else {
                RequestBuilder request = Glide.with(context).load(imageUrl);
                if (config.getCropType() == ImageLoderConfig.CENTER_CROP) {
                    options.centerCrop();
                } else {
                    options.fitCenter();
                }
                builder = request;
            }

            options.diskCacheStrategy(config.getDiskCacheStrategy().getStrategy())
                    .skipMemoryCache(config.isSkipMemoryCache())
                    .priority(config.getLoadPriority().getPriority())
                    .dontAnimate();
            //缓存设置
            if (null != config.getDisplaySize()) {
                options.override(config.getDisplaySize().getWidth(), config.getDisplaySize().getHeight());
            }

            //将操作设置到builder中
            builder.apply(options);

            //设置监听器
            if (null != listener) {
                setListener(builder, listener);
            }

            // 圆角处理
            if (config.isCropCircle()) {
                final ImageLoderConfig finalConfig = config;
                builder.into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(360);
                        circularBitmapDrawable.setAntiAlias(true);
                        if (finalConfig.getBorderSize() > 0) {
                            imageView.setImageBitmap(ImageUtil.toRound(circularBitmapDrawable.getBitmap(), finalConfig.getBorderSize(), finalConfig.getBorderColor()));
                        } else {
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });
            } else if (config.getCornersRadius() > 0) {
                final ImageLoderConfig finalConfig = config;
                builder.into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(finalConfig.getCornersRadius());
                        circularBitmapDrawable.setAntiAlias(true);
                        if (finalConfig.getBorderSize() > 0) {
                            imageView.setImageBitmap(ImageUtil.toRound(circularBitmapDrawable.getBitmap(), finalConfig.getBorderSize(), finalConfig.getBorderColor()));
                        } else {
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });

            } else if (config.getBorderSize() > 0) {//边框处理
                final ImageLoderConfig finalConfig = config;
                builder.into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        imageView.setImageBitmap(ImageUtil.toRound(resource, finalConfig.getBorderSize(), finalConfig.getBorderColor()));
                    }
                });
            } else {
                Log.d("glide", "成功");
                builder.into(imageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("glide", "异常" + e.getMessage());
            if (config != null && config.getErrorResId() != 0) {
                imageView.setImageResource(config.getErrorResId());
            }
        }
    }

    /**
     * @param imageView ImageView
     * @param imageUrl  Url
     */
    @Override
    public void loadImage(ImageView imageView, String imageUrl) {
        loadImage(imageView, imageUrl, defaultConfigBuilder, null);
    }

    @Override
    public void loadImage(ImageView imageView, String imageUrl, String thumbnailUrl) {

    }

    @Override
    public void loadRoundedImage(ImageView imageView, String imageUrl, float cornersRadius) {
        ImageLoderConfig roundConfigBuilder = new ImageLoderConfig.Builder()
                .cropType(ImageLoderConfig.CENTER_CROP)
                .asBitmap(true)
                .cornersRadius(cornersRadius)
                .placeHolderResId(R.drawable.bg_image_placeholder)
                .errorResId(R.drawable.bg_image_placeholder)
                .diskCacheStrategy(ImageLoderConfig.MDiskCacheStrategy.DATA)
                .loadPriority(ImageLoderConfig.LoadPriority.HIGH).build();
        loadImage(imageView, imageUrl, roundConfigBuilder, null);
    }

    @Override
    public void loadRoundedImage(ImageView imageView, String imageUrl, float cornersRadius, LoaderListener listener) {
        ImageLoderConfig roundConfigBuilder = new ImageLoderConfig.Builder()
                .cropType(ImageLoderConfig.CENTER_CROP)
                .asBitmap(true)
                .cornersRadius(cornersRadius)
                .placeHolderResId(R.drawable.bg_image_placeholder)
                .errorResId(R.drawable.bg_image_placeholder)
                .diskCacheStrategy(ImageLoderConfig.MDiskCacheStrategy.DATA)
                .loadPriority(ImageLoderConfig.LoadPriority.HIGH).build();
        loadImage(imageView, imageUrl, roundConfigBuilder, listener);
    }

    @Override
    public void loadCircleImage(ImageView imageView, String imageUrl) {
        ImageLoderConfig roundConfigBuilder = new ImageLoderConfig.Builder()
                .cropType(ImageLoderConfig.CENTER_CROP)
                .asBitmap(true)
                .cropCircle(true)
                .placeHolderResId(R.drawable.bg_image_placeholder)
                .errorResId(R.drawable.bg_image_placeholder)
                .diskCacheStrategy(ImageLoderConfig.MDiskCacheStrategy.DATA)
                .loadPriority(ImageLoderConfig.LoadPriority.HIGH).build();
        loadImage(imageView, imageUrl, roundConfigBuilder, null);
    }

    @Override
    public void loadGif(ImageView imageView, String imageUrl) {
    }

    @SuppressLint("CheckResult")
    @Override
    public void downloadImage(final Context context, final Handler handler, final String uri, final String savePath, final String name, final boolean isInsertMedia) {
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(uri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (handler != null) {
                            handler.sendEmptyMessage(ImageUtil.SAVE_IMAGE_FAIL);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap bitmap = resource;
                        ImageUtil.saveImageAndRefresh(context, handler, bitmap, uri, savePath, name, isInsertMedia);
                        return false;
                    }
                }).submit();
    }

    @SuppressLint("CheckResult")
    @Override
    public void downloadImage(final Context context, final Handler handler, final String uri,
                              final String savePath, final String name, final boolean isInsertMedia,
                              final LoaderListener loaderListener) {
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(uri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        loaderListener.onError();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap bitmap = resource;
                        ImageUtil.saveImageAndRefresh(context, handler, bitmap, uri, savePath, name, isInsertMedia);
                        loaderListener.onSuccess(bitmap);
                        return false;
                    }
                }).submit();
    }
}
