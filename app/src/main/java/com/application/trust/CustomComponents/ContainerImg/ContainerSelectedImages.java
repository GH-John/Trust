package com.application.trust.CustomComponents.ContainerImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.application.trust.R;

import java.util.HashMap;

public class ContainerSelectedImages extends LinearLayout implements AdapterContainer {
    private LinearLayout containerImg;
    private CustomBtnAddImg btnAddImg;

    private AdapterGalery galery;
    private HashMap<Uri, View> currentMapView;
    private ContainerFiller containerFiller;

    public ContainerSelectedImages(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializationComponents();
        initializationStyles();
        initializationListeners();
    }

    private void initializationComponents() {
        inflate(getContext(), R.layout.container_selected_img, this);
        btnAddImg = findViewById(R.id.btnAddImg);
        containerImg = findViewById(R.id.containerImg);
        currentMapView = new HashMap<>();
    }

    private void initializationStyles() {
    }

    private void initializationListeners() {
        btnAddImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooseImages(galery).open();
            }
        });
    }

    public void setInstanceGalery(AdapterGalery galery) {
        this.galery = galery;
    }

    @Override
    public void setContainerFiller(ContainerFiller filler) {
        containerFiller = filler;
    }

    @Override
    public void inflateContainer(final ContainerFiller containerFiller) {
        containerImg.post(new Runnable() {
            @Override
            public void run() {
                Uri uri;
                Bitmap bitmap;
                CustomViewImg customViewImg;
                HashMap<Uri, Bitmap> mapBitmap = containerFiller.getMapBitmap();

                int imgCount = mapBitmap.size();
                for (HashMap.Entry entry : mapBitmap.entrySet()) {
                    uri = (Uri) entry.getKey();
                    bitmap = mapBitmap.get(uri);

                    if (CURRENT_SIZE_CONTAINER() < MAX_SIZE_CONTAINER() && !currentMapView.containsKey(uri)) {
                        currentMapView.put(uri, containerImg);

                        customViewImg = new CustomViewImg(getContext());
                        customViewImg.setImageBitmap(bitmap);

                        final CustomViewImg finalCustomViewImg = customViewImg;
                        final Uri finalUri = uri;

                        customViewImg.itemDeleteOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeFromContainer(finalCustomViewImg, finalUri);
                            }
                        });
                        addToContainer(customViewImg);
                    }
                }
            }
        });
    }

    public void addToContainer(View view) {
        containerImg.addView(view);
        view.setLayoutParams(new LinearLayout.LayoutParams(view.getLayoutParams()));
    }

    public void removeFromContainer(final View v, final Uri key) {
        containerImg.post(new Runnable() {
            public void run() {
                containerImg.removeView(v);
                currentMapView.remove(key);
            }
        });
    }

    public int CURRENT_SIZE_CONTAINER(){
        return containerImg.getChildCount() - 1;
    }

    public int MAX_SIZE_CONTAINER() {
        return 9;
    }
}