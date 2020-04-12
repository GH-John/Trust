package com.application.arenda.UI.ContainerImg.Galery;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.application.arenda.Entities.Utils.PermissionUtils;

public class ChooseImages {
    private static ChooseImages instance;

    private Fragment fragment;
    private AppCompatActivity activity;
    private int SELECTED_IMG_CODE;

    private ChooseImages(AdapterGalery galery) {
        if (galery instanceof Fragment) {
            this.fragment = (Fragment) galery;
            this.SELECTED_IMG_CODE = galery.getRequestCode();
        } else if (galery instanceof AppCompatActivity) {
            this.activity = (AppCompatActivity) galery;
            this.SELECTED_IMG_CODE = galery.getRequestCode();
        }
    }

    public static ChooseImages getInstance(AdapterGalery galery) {
        if (instance == null)
            instance = new ChooseImages(galery);

        return instance;
    }

    public void open() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if (fragment != null) {
            if (PermissionUtils.Check_STORAGE(fragment.getActivity()))
                fragment.startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECTED_IMG_CODE);
            else
                PermissionUtils.Request_STORAGE(fragment.getActivity(), SELECTED_IMG_CODE);
        } else if (activity != null) {
            if (PermissionUtils.Check_STORAGE((Activity) fragment.getContext()))
                activity.startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECTED_IMG_CODE);
            else
                PermissionUtils.Request_STORAGE(activity, SELECTED_IMG_CODE);
        }
    }
}