package com.application.arenda.entities.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import com.github.piasy.biv.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import timber.log.Timber;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {

            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                ParcelFileDescriptor descriptor = context.getContentResolver().openFileDescriptor(uri, "r", null);

                InputStream inputStream = new FileInputStream(descriptor.getFileDescriptor());
                file = new File(context.getCacheDir(), getFileName(context, uri));
                OutputStream outputStream = new FileOutputStream(file);
                IOUtils.copy(inputStream, outputStream);
            } else{
                file = new File(URI.create(String.valueOf(uri)).toURL().getFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static String getFileName(Context context, Uri uri) {
        String name = "";
        Cursor cursor = null;
        try {

            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    name = cursor.getString(index);

                    cursor.close();
                }
            } else {
                if (uri == null) return null;

                String path = uri.getPath();

                int cut = path.lastIndexOf('/');
                if (cut != -1) {
                    name = path.substring(cut + 1);
                }
            }

        } catch (Throwable e) {
            Timber.e(e);
        }

        return name;
    }
}