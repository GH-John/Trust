package com.application.arenda.Entities.Announcements;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.Announcement;
import com.application.arenda.Entities.Models.Category;
import com.application.arenda.Entities.Models.Subcategory;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ApiAnnouncement {
    @SuppressLint("StaticFieldLeak")
    private static ApiAnnouncement apiAnnouncement;

    private ApiAnnouncement() {
    }

    public static ApiAnnouncement getInstance() {
        if (apiAnnouncement == null)
            apiAnnouncement = new ApiAnnouncement();

        return apiAnnouncement;
    }

    public Observable<Boolean> insertAnnouncement(Announcement announcement) {
        return Observable.create(emitter -> Backendless.Data.of("Announcements").save(Announcement.convertToMap(announcement), new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map response) {
                Backendless.Data.of("Announcements")
                        .setRelation(response,
                                "subcategory:Subcategories:n",
                                announcement.getSubcategories(),
                                new AsyncCallback<Integer>() {
                                    @Override
                                    public void handleResponse(Integer response) {
                                        if (response > 0)
                                            emitter.onNext(true);
                                        else
                                            emitter.onNext(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Timber.e(fault.toString());
                                    }
                                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Timber.e(fault.toString());
            }
        }));
    }

    public Observable<List<Category>> getCategories(Context context) {
        return Observable.create(emitter -> Backendless.Data.of("Categories").find(new AsyncCallback<List<Map>>() {
            @SuppressLint("CheckResult")
            @Override
            public void handleResponse(List<Map> response) {
                Observable.fromIterable(response)
                        .subscribeOn(Schedulers.io())
                        .map(map -> Category.convertFromMap(context, map))
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<Category>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(List<Category> categories) {
                                emitter.onNext(categories);
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onError(e);
                            }
                        });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Timber.e(fault.toString());
            }
        }));
    }

    public Observable<List<Subcategory>> getSubcategories(@NonNull Context context, @NonNull String idCategory) {
        DataQueryBuilder builder = DataQueryBuilder.create();
        builder.setWhereClause("Categories[subcategory].objectId = '" + idCategory + "'");


        return Observable.create(emitter -> Backendless.Data.of("Subcategories").find(builder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {
                Observable.fromIterable(response)
                        .subscribeOn(Schedulers.io())
                        .map(map -> Subcategory.convertFromMap(context, map))
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<Subcategory>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(List<Subcategory> categories) {
                                emitter.onNext(categories);
                            }

                            @Override
                            public void onError(Throwable e) {
                                emitter.onError(e);
                            }
                        });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Timber.e(fault.toString());
            }
        }));
    }
}