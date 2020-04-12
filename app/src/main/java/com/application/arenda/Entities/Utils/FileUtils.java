package com.application.arenda.Entities.Utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import com.github.piasy.biv.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

public class FileUtils {
    @NonNull
    public static RequestBody createPartFromString(String s) {
        return RequestBody.create(MultipartBody.FORM, s);
    }

    @NonNull
    public static MultipartBody.Part createFilePart(Context context, String partName, Uri uri) {
        try {
            File file = getFileFromUri(context, uri);

            RequestBody requestBody = RequestBody.create(MediaType.parse(context.getContentResolver().getType(uri)), file);

            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);

        } catch (Throwable e) {
            Timber.e(e);
        }
        return null;
    }

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

    private static String getFileName(Context context, Uri uri) {
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