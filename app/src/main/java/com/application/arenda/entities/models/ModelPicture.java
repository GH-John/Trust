package com.application.arenda.entities.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "pictures",
        foreignKeys = @ForeignKey(entity = ModelAnnouncement.class,
                parentColumns = "idAnnouncement",
                childColumns = "idAnnouncement",
                onDelete = CASCADE,
                onUpdate = CASCADE))
public class ModelPicture implements IModel {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idPicture")
    private long ID;

    @ColumnInfo(index = true)
    @SerializedName("idAnnouncement")
    private long idAnnouncement = 0;

    @SerializedName("picture")
    private Uri uri;

    @SerializedName("isMainPicture")
    private boolean isMainPicture = false;

    public static Single<List<ModelPicture>> sortByMainPicture(List<ModelPicture> collection) {
        return Single.create(emitter -> {
            try {
                List<ModelPicture> pictures = new ArrayList<>(collection);

                Comparator<ModelPicture> valueComparator = (e1, e2) -> {
                    boolean v1 = e1.isMainPicture();
                    boolean v2 = e2.isMainPicture();
                    return v1 == v2 ? 1 : 0;
                };

                Collections.sort(pictures, valueComparator);

                emitter.onSuccess(pictures);
            } catch (Throwable throwable) {
                emitter.onError(throwable);
            }
        });
    }

    public static Single<List<Uri>> convertToUris(List<ModelPicture> collection) {
        return Single.create(emitter -> sortByMainPicture(collection)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSingleObserver<List<ModelPicture>>() {
                    @Override
                    public void onSuccess(List<ModelPicture> pictures) {
                        List<Uri> uris = new ArrayList<>();

                        for (ModelPicture picture : pictures) {
                            uris.add(picture.getUri());
                        }

                        emitter.onSuccess(uris);
                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                        Timber.e(e);
                    }
                }));
    }

    public static Single<Uri> getMainPicture(List<ModelPicture> collection) {
        return Single.create(emitter -> {
            for (ModelPicture picture : collection) {
                if (picture.isMainPicture) {
                    emitter.onSuccess(picture.getUri());
                    break;
                }
            }
        });
    }

    public long getIdAnnouncement() {
        return idAnnouncement;
    }

    public void setIdAnnouncement(long idAnnouncement) {
        this.idAnnouncement = idAnnouncement;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isMainPicture() {
        return isMainPicture;
    }

    public void setMainPicture(boolean mainPicture) {
        isMainPicture = mainPicture;
    }

    @Override
    public long getID() {
        return ID;
    }

    @Override
    public void setID(long id) {
        ID = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "ModelPicture{" +
                "idAnnouncement=" + idAnnouncement +
                ", picture='" + uri + '\'' +
                ", isMainPicture=" + isMainPicture +
                '}';
    }
}