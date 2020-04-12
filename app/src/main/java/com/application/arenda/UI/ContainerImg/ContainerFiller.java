package com.application.arenda.UI.ContainerImg;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.application.arenda.Entities.Utils.BitmapUtils;
import com.application.arenda.UI.ContainerImg.CustomViews.CustomViewImg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressLint("Registered")
public class ContainerFiller implements AdapterContainer {
    private Container container;

    private Bitmap firstBitmap;
    private HashMap<Uri, View> currentMap = new HashMap<>();
    private HashMap<Uri, Bitmap> mapBitmap = new HashMap<>();

    @Override
    public void setActivityResult(final Intent data, final Context context) {
        Uri imageUri;
        Bitmap bitmap;

        try {
            mapBitmap.clear();
            if (data.getData() != null) {
                imageUri = data.getData();

                if (!mapBitmap.containsKey(imageUri)) {
                    bitmap = BitmapUtils.getThumbnail(context, 1000, imageUri);
                    mapBitmap.put(imageUri, bitmap);
                }

            } else if (data.getClipData() != null) {

                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUri = clipData.getItemAt(i).getUri();

                    if (!mapBitmap.containsKey(imageUri)) {
                        bitmap = BitmapUtils.getThumbnail(context, 1000, imageUri);
                        mapBitmap.put(imageUri, bitmap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public void inflateContainer() {
        if (container != null) {
            final ViewGroup viewGroup = container.getContainer();

            viewGroup.post(() -> {
                Uri uri;
                Bitmap bitmap;
                View view;

                int imgCount = mapBitmap.size();
                for (HashMap.Entry<Uri, Bitmap> entry : mapBitmap.entrySet()) {

                    uri = entry.getKey();
                    bitmap = mapBitmap.get(uri);

                    if (!currentMap.containsKey(uri)) {
                        view = container.getInstanceFiller();

                        if (((CustomViewImg) view).setImageBitmap(bitmap)) {

                            final View finalCustomViewImg = view;
                            final Uri finalUri = uri;

                            ((CustomViewImg) view).itemDeleteOnClickListener(v -> {
                                container.removeFromContainer(finalCustomViewImg);
                                removeFromCurrentMap(finalUri);
                            });

                            if (container.addToContainer(view))
                                currentMap.put(uri, view);
                        }
                    }
                }
            });
        }
    }

    public Bitmap getFirstBitmap() {
        if (mapBitmap.size() > 0) {
            for (HashMap.Entry<Uri, Bitmap> entry : mapBitmap.entrySet()) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Uri getFirstUri() {
        if (mapBitmap.size() > 0) {
            for (HashMap.Entry<Uri, Bitmap> entry : mapBitmap.entrySet()) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Map<Uri, Bitmap> getMapBitmap() {
        return mapBitmap;
    }

    public Set<Uri> getUris() {
        return mapBitmap.keySet();
    }

    @Override
    public Map<Uri, View> getCurrentMap() {
        return currentMap;
    }

    @Override
    public void removeFromCurrentMap(final Object key) {
        this.currentMap.remove(key);
    }

    @Override
    public int getSize() {
        return mapBitmap.size();
    }
}