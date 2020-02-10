package com.application.arenda.Entities.Utils.Glide;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.application.arenda.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

public class GlideUtils {
    private static RequestOptions defaultRequestOptions = new RequestOptions()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .placeholder(R.color.colorNotFoundPicture)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerInside()
            .override(800, 800)
            .timeout(3000);

    private static DrawableCrossFadeFactory defaultFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    public static void loadImage(Context context, Uri uri, ImageView imageView) {
        if (context != null && uri != null && imageView != null) {
            GlideApp.with(context)
                    .load(uri)
                    .apply(defaultOptions())
                    .transition(DrawableTransitionOptions.withCrossFade(defaultFactory()))
                    .into(imageView);
        } else {
            throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
        }
    }

    public static void loadImage(Context context, RequestOptions requestOptions, DrawableCrossFadeFactory factory, Uri uri, ImageView imageView) {
        if (context != null && requestOptions != null && factory != null && uri != null && imageView != null) {
            GlideApp.with(context)
                    .load(uri)
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(imageView);
        } else {
            throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
        }
    }

    public static void loadImage(Context context, RequestOptions requestOptions, DrawableCrossFadeFactory factory, RequestListener listener, Uri uri, ImageView imageView) {
        if (context != null && requestOptions != null && factory != null && listener != null && uri != null && imageView != null) {
            GlideApp.with(context)
                    .load(uri)
                    .apply(requestOptions)
                    .listener(listener)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(imageView);
        } else {
            throw new NullPointerException("GlideUtils.loadImage contains in parameters null");
        }
    }

    public static void clearCache(Context context) {
        new Thread(() -> Glide.get(context).clearDiskCache()).start();
    }

    public static RequestOptions defaultOptions() {
        return defaultRequestOptions;
    }

    public static DrawableCrossFadeFactory defaultFactory() {
        return defaultFactory;
    }
}