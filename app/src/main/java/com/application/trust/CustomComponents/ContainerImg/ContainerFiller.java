package com.application.trust.CustomComponents.ContainerImg;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.IOException;
import java.util.HashMap;

@SuppressLint("Registered")
public class ContainerFiller {
    private IContainerFiller container;

    private Context context;
    private HashMap<Uri, Bitmap> mapBitmap;

    public ContainerFiller(Context context) {
        this.context = context;
        this.mapBitmap = new HashMap<>();
    }

    public void inflateContainer(final Intent data, final Context context) {
        Uri imageUri;
        Bitmap bitmap;

        try {
            if (data.getData() != null) {
                imageUri = data.getData();
                bitmap = ThumbnailCompression.getThumbnail(context, imageUri);

                mapBitmap.put(imageUri, bitmap);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        inflate();
                    }
                }).start();

            } else if (data.getClipData() != null) {

                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUri = clipData.getItemAt(i).getUri();
                    bitmap = ThumbnailCompression.getThumbnail(context, imageUri);

                    mapBitmap.put(imageUri, bitmap);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        inflate();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setContainer(IContainerFiller container) {
        this.container = container;
    }

    public void setMapBitmap(HashMap<Uri, Bitmap> mapBitmap) {
        this.mapBitmap = mapBitmap;
    }

    public HashMap<Uri,Bitmap> getMapBitmap() {
        return mapBitmap;
    }

    private void inflate() {
        if (container != null)
            container.inflateContainer(this);
    }
}