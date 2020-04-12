package com.application.arenda.Entities.User;

import android.content.Context;

import com.application.arenda.Entities.Utils.ObjectStreamHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class UserCookie {
    private static final String getProfilesDir = "map_profiles.ar";
    private static final String getUseProfileDir = "uses_profile.ar";
    private static Map<String, UserProfile> mapSaveProfiles = new HashMap<>();

    public static void removeProfile(Context context, String token) {
        if (getToken(context).contentEquals(token))
            removeToken(context);

        mapSaveProfiles.remove(token);
    }

    public static String getToken(Context context) {
        FileInputStream fileInputStream = null;
        String token = "";
        try {
            fileInputStream = context.openFileInput(getUseProfileDir);

            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);

            token = new String(bytes);
        } catch (IOException ex) {
            Timber.tag("UserCookie").e(ex);
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException ex) {
                Timber.tag("UserCookie").e(ex);
            }
        }
        return token;
    }

    public static void putProfile(Context context, String token, UserProfile userProfile) {
        mapSaveProfiles.put(token, userProfile);
        writeToken(context, token);
    }

    private static void removeToken(Context context) {
        context.deleteFile(getUseProfileDir);
    }

    private static void writeToken(final Context context, final String token) {

        new Thread(() -> {
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = context.openFileOutput(getUseProfileDir, Context.MODE_PRIVATE);
                fileOutputStream.write(token.getBytes());
            } catch (IOException ex) {
                Timber.tag("UserCookie").e(ex);
            } finally {
                try {
                    if (fileOutputStream != null)
                        fileOutputStream.close();
                } catch (IOException ex) {
                    Timber.tag("UserCookie").e(ex);
                }
            }
        }).start();
    }

    private static Map readProfiles(Context context) {
        try {
            return ObjectStreamHelper.ObjectInputStream(context.openFileInput(getProfilesDir));
        } catch (FileNotFoundException e) {
            Timber.tag("UserCookie").e(e);
        }
        return null;
    }

    private static void writeProfiles(Context context) {
        try {
            ObjectStreamHelper.ObjectOutputStream(mapSaveProfiles, context.openFileOutput(getProfilesDir, Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            Timber.tag("UserCookie").e(e);
        }
    }
}