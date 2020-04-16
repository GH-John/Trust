package com.application.arenda.Entities.Utils;

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

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor descriptor = context.getContentResolver().openFileDescriptor(uri, "r", null);

            InputStream inputStream = new FileInputStream(descriptor.getFileDescriptor());
            File file = new File(context.getCacheDir(), getFileName(context, uri));
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileName(Context context, Uri uri) {
        String name = "";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            name = cursor.getString(index);

            cursor.close();
        }

        return name;
    }
}