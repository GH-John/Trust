package com.application.arenda.Entities.Utils.Retrofit;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Utils.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

public class RetrofitUtils {
    @NonNull
    public static RequestBody createPartFromString(String s) {
        return RequestBody.create(MultipartBody.FORM, s);
    }

    @NonNull
    public static MultipartBody.Part createFilePart(Context context, String partName, Uri uri) {
        try {
            File file = FileUtils.getFileFromUri(context, uri);

            RequestBody requestBody = RequestBody.create(MediaType.parse(context.getContentResolver().getType(uri)), file);

            return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);

        } catch (Throwable e) {
            Timber.e(e);
        }
        return null;
    }
}
