package com.application.arenda.CustomComponents.ContainerImg;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.application.arenda.CustomComponents.ContainerImg.CustomViews.CustomViewImg;
import com.application.arenda.CustomComponents.Thumbnail.ThumbnailCompression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("Registered")
public class ContainerFiller implements AdapterContainer {
    private Container container;

    private Context context;
    private HashMap<Uri, View> currentMap;
    private HashMap<Uri, Bitmap> mapBitmap;

    public ContainerFiller(Context context) {
        this.context = context;
        this.mapBitmap = new HashMap<>();
        this.currentMap = new HashMap<>();
    }

    @Override
    public void setActivityResult(final Intent data, final Context context) {
        Uri imageUri;
        Bitmap bitmap;

        try {
            mapBitmap.clear();
            if (data.getData() != null) {
                imageUri = data.getData();

                if (!mapBitmap.containsKey(imageUri)) {
                    bitmap = ThumbnailCompression.getThumbnail(context, imageUri);
                    mapBitmap.put(imageUri, bitmap);
                }

            } else if (data.getClipData() != null) {

                ClipData clipData = data.getClipData();

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUri = clipData.getItemAt(i).getUri();

                    if (!mapBitmap.containsKey(imageUri)) {
                        bitmap = ThumbnailCompression.getThumbnail(context, imageUri);
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
        final ViewGroup viewGroup = container.getContainer();

        viewGroup.post(new Runnable() {
            @Override
            public void run() {
                Uri uri;
                Bitmap bitmap;
                View view;

                int imgCount = mapBitmap.size();
                for (HashMap.Entry entry : mapBitmap.entrySet()) {

                    uri = (Uri) entry.getKey();
                    bitmap = mapBitmap.get(uri);

                    if (!currentMap.containsKey(uri)) {
                        view = container.getInstanceFiller();

                        if (((CustomViewImg) view).setImageBitmap(bitmap)) {

                            final View finalCustomViewImg = view;
                            final Uri finalUri = uri;

                            ((CustomViewImg) view).itemDeleteOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    container.removeFromContainer(finalCustomViewImg);
                                    removeFromCurrentMap(finalUri);
                                }
                            });

                            if(container.addToContainer(view))
                                currentMap.put(uri, view);

                        }
                    }
                }
            }
        });
    }

    public Map<Uri, Bitmap> getMapBitmap() {
        return mapBitmap;
    }

    @Override
    public Map<Uri, View> getCurrentMap() {
        return currentMap;
    }

    @Override
    public void removeFromCurrentMap(final Object key) {
        this.currentMap.remove(key);
    }
}