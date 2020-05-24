package com.application.arenda.entities.utils.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Size;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.application.arenda.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import timber.log.Timber;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;

public class GlideUtils {
    private static RequestOptions defaultRequestOptions = new RequestOptions()
            .format(DecodeFormat.PREFER_ARGB_8888)
//            .placeholder(R.color.colorPlaceHolderPicture)
            .error(R.drawable.ic_none)
            .override(800, 800)
            .diskCacheStrategy(ALL)
            .timeout(50000);

    private static DrawableCrossFadeFactory defaultFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public static void loadVector(Context context, Uri uri, ImageView imageView) {
        if (context != null && uri != null && imageView != null) {

            Glide.with(context)
                    .load(uri)
                    .into(imageView);
        } else {
            throw new NullPointerException("GlideUtils.loadVector contains in parameters null");
        }
    }

    @SuppressLint("CheckResult")
    public static void loadAvatar(Context context, Uri uri, ImageView imageView) {
        try {
            if (context != null && imageView != null) {
                RequestOptions options = defaultOptions().clone();
                options.circleCrop();
                options.priority(Priority.NORMAL);
                options.override(150, 150);
                options.placeholder(R.drawable.ic_user_logo);
                options.error(R.drawable.ic_user_logo);

                Glide.with(context)
                        .load(uri)
                        .apply(options)
                        .thumbnail(0.5f)
                        .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public static void loadImage(Context context, @DrawableRes int drawable, ImageView imageView) {
        try {
            if (context != null && imageView != null) {
                Glide.with(context)
                        .load(drawable)
                        .apply(defaultOptions())
                        .thumbnail(0.35f)
                        .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public static void loadImage(Context context, Uri uri, ImageView imageView) {
        try {
            if (context != null && imageView != null) {
                Glide.with(context)
                        .load(uri)
                        .apply(defaultOptions())
                        .thumbnail(0.35f)
                        .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    @SuppressLint("CheckResult")
    public static void loadImage(Context context, Uri uri, ImageView imageView, Priority priority) {
        try {
            if (context != null && imageView != null) {
                RequestOptions options = defaultOptions().clone();
                options.priority(priority);

                Glide.with(context)
                        .load(uri)
                        .apply(options)
                        .thumbnail(0.35f)
                        .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    @SuppressLint("CheckResult")
    public static void loadImage(Context context, Uri uri, ImageView imageView, Size size) {
        try {
            if (context != null && imageView != null) {
                RequestOptions options = defaultOptions().clone();
                options.override(size.getWidth(), size.getHeight());

                Glide.with(context)
                        .load(uri)
                        .apply(options)
                        .thumbnail(0.35f)
                        .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    @SuppressLint("CheckResult")
    public static void loadImage(Context context, Uri uri, ImageView imageView, @DrawableRes int drawableError) {
        try {
            if (context != null && imageView != null) {
                RequestOptions options = defaultOptions().clone();
                options.error(drawableError);

                Glide.with(context)
                        .load(uri)
                        .apply(options)
                        .thumbnail(0.35f)
                        .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public static void loadImage(Context context, RequestOptions requestOptions, DrawableCrossFadeFactory factory, Uri uri, ImageView imageView) {
        try {
            if (context != null && requestOptions != null && factory != null && uri != null && imageView != null) {
                Glide.with(context)
                        .load(uri)
                        .apply(requestOptions)
                        .thumbnail(0.35f)
                        .transition(DrawableTransitionOptions.withCrossFade(factory))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public static void loadImage(Context context, RequestOptions requestOptions, DrawableCrossFadeFactory factory, RequestListener listener, Uri uri, ImageView imageView) {
        try {
            if (context != null && requestOptions != null && factory != null && listener != null && uri != null && imageView != null) {
                Glide.with(context)
                        .load(uri)
                        .apply(requestOptions)
                        .thumbnail(0.35f)
                        .listener(listener)
                        .transition(DrawableTransitionOptions.withCrossFade(factory))
                        .into(imageView);
            } else {
                throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public static RequestOptions defaultOptions() {
        return defaultRequestOptions;
    }

    public static DrawableCrossFadeFactory defaultFactory() {
        return defaultFactory;
    }
}