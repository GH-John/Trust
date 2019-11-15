package com.application.trust.CustomComponents.ContainerImg;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.application.trust.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContainerSelectedImages extends LinearLayout implements IContainerFiller {
    private LinearLayout containerImg;
    private CustomBtnAddImg btnAddImg;
    private List<CustomViewImg> selectedImgs;

    private IGalery galery;
    private ContainerFiller containerFiller;
    private List<Uri> currentListUri;
    private int CURRENT_SIZE_CONTAINER = 0;
    private final int MAX_SIZE = 9;

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
        currentListUri = new ArrayList<>();
    }

    private void initializationStyles() {
    }

    private void initializationListeners() {
        btnAddImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Galery(galery).open();
            }
        });
    }

    public void setInstanceGalery(IGalery galery) {
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
                LinearLayout.LayoutParams layoutParams;
                HashMap<Uri, Bitmap> mapBitmap = containerFiller.getMapBitmap();
                int imgCount = mapBitmap.size();

                selectedImgs = new ArrayList<>();

                for (HashMap.Entry entry : mapBitmap.entrySet()) {
                    uri = (Uri) entry.getKey();
                    bitmap = mapBitmap.get(uri);

                    if (CURRENT_SIZE_CONTAINER < MAX_SIZE && !currentListUri.contains(uri)) {
                        currentListUri.add(uri);

                        customViewImg = new CustomViewImg(getContext());
                        customViewImg.setImageBitmap(bitmap);
                        customViewImg.itemDeleteOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeImg(v);
                            }
                        });

                        selectedImgs.add(customViewImg);
                        containerImg.addView(customViewImg);

                        layoutParams = new LinearLayout.LayoutParams(customViewImg.getLayoutParams());
                        layoutParams.leftMargin = 8;
                        customViewImg.setLayoutParams(layoutParams);
                        CURRENT_SIZE_CONTAINER = containerImg.getChildCount() - 1;

                        Toast.makeText(getContext(), String.valueOf(CURRENT_SIZE_CONTAINER), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void removeImg(final View view) {
        containerImg.post(new Runnable() {
            public void run() {
                containerImg.removeView(view);
            }
        });
    }

    public int getMAX_SIZE() {
        return MAX_SIZE;
    }
}