package com.application.arenda.ui.widgets.containerImg;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.application.arenda.entities.utils.BitmapUtils;
import com.application.arenda.ui.widgets.containerImg.customViews.CustomViewImg;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressLint("Registered")
public class ContainerFiller implements AdapterContainer {
    private Container container;

    private View lastSelectedView;
    private Uri lastSelectedUri;

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

                            if (lastSelectedView == null) {
                                ((CustomViewImg) finalCustomViewImg).setItemChecked(true);

                                lastSelectedView = finalCustomViewImg;
                                lastSelectedUri = finalUri;
                            }

                            ((CustomViewImg) view).itemDeleteOnClickListener(v -> {
                                container.removeFromContainer(finalCustomViewImg);
                                removeFromCurrentMap(finalUri);

                                if (lastSelectedUri != null && lastSelectedView != null)
                                    if (lastSelectedUri.equals(finalUri) || (getUri(0).equals(finalUri)) && currentMap.size() > 1) {
                                        lastSelectedUri = getUri(0);
                                        lastSelectedView = getView(0);

                                        ((CustomViewImg) lastSelectedView).setItemChecked(true);
                                    }
                            });

                            ((CustomViewImg) view).setImgClickListener(v -> {
                                ((CustomViewImg) lastSelectedView).setItemChecked(false);
                                ((CustomViewImg) finalCustomViewImg).setItemChecked(true);

                                lastSelectedView = finalCustomViewImg;
                                lastSelectedUri = finalUri;
                            });

                            if (container.addToContainer(view))
                                currentMap.put(uri, view);
                        }
                    }
                }
            });
        }
    }

    public Uri getUri(View view) {
        for (Map.Entry<Uri, View> entry : currentMap.entrySet()) {
            if (entry.getValue().equals(view))
                return entry.getKey();
        }

        return null;
    }

    public Uri getUri(int index) {
        int i = 0;
        for (Uri uri : getUris()) {
            if (i == index)
                return uri;
            i++;
        }

        return null;
    }

    public View getView(int index) {
        int i = 0;
        for (View view : getViews()) {
            if (i == index)
                return view;
            i++;
        }
        return null;
    }

    public Uri getLastSelected() {
        return lastSelectedUri;
    }

    public Set<Uri> getUris() {
        return currentMap.keySet();
    }

    public Collection<View> getViews() {
        return currentMap.values();
    }

    @Override
    public Map<Uri, View> getCurrentMap() {
        return currentMap;
    }

    @Override
    public void removeFromCurrentMap(final Object key) {
        this.currentMap.remove(key);
        this.mapBitmap.remove(key);

        if (currentMap.size() == 0) {
            lastSelectedUri = null;
            lastSelectedView = null;
        }
    }

    @Override
    public int getSize() {
        return currentMap.size();
    }
}