package com.application.trust.CustomComponents.ContainerImg;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Galery {
    private Fragment fragment;
    private AppCompatActivity activity;

    private int SELECTED_IMG_CODE;

    public Galery(IGalery galery) {
        if(galery instanceof Fragment) {
            this.fragment = (Fragment) galery;
            this.SELECTED_IMG_CODE = galery.getRequestCode();
        }else if(galery instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) galery;
            this.SELECTED_IMG_CODE = galery.getRequestCode();
        }
    }

    public void open() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        if (fragment != null) {
            fragment.startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECTED_IMG_CODE);
        }else if (activity != null) {
            activity.startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECTED_IMG_CODE);
        }
    }
}