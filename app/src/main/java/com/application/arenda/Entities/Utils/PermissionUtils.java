package com.application.arenda.Entities.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    //Request Permisson
    public static void Request_STORAGE(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, code);
    }

    public static void Request_CALL_PHONE(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.CALL_PHONE}, code);
    }

    public static void Request_CAMERA(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.CAMERA}, code);
    }

    public static void Request_FINE_LOCATION(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.ACCESS_FINE_LOCATION}, code);
    }

    public static void Request_COARSE_LOCATION(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, code);
    }

    public static void Request_Location_Permissions(Activity activity, int code) {
        ActivityCompat.requestPermissions(activity, new
                String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, code);
    }

    public static void Request_READ_SMS(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_SMS}, code);
    }

    public static void Request_READ_CONTACTS(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_CONTACTS}, code);
    }

    public static void Request_READ_CALENDAR(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.READ_CALENDAR}, code);
    }

    public static void Request_RECORD_AUDIO(Activity act, int code) {
        ActivityCompat.requestPermissions(act, new
                String[]{Manifest.permission.RECORD_AUDIO}, code);
    }

    //Check Permisson
    public static boolean Check_STORAGE(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_CALL_PHONE(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_Location_Permissions(Activity act) {
        return Check_FINE_LOCATION(act) && Check_COARSE_LOCATION(act);
    }

    public static boolean Check_FINE_LOCATION(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_COARSE_LOCATION(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_READ_SMS(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_READ_CONTACTS(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_READ_CALENDAR(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_CALENDAR);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean Check_RECORD_AUDIO(Activity act) {
        int result = ContextCompat.checkSelfPermission(act, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}