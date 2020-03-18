package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;

import io.reactivex.disposables.CompositeDisposable;

public class DataSourceFactory extends androidx.paging.DataSource.Factory<Long, ModelAllAnnouncement> {

    private Context context;

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Context> mutableContext = new MutableLiveData<>();
    private MutableLiveData<DataSource> mutableDataSource = new MutableLiveData<>();

    public DataSourceFactory(Context context, CompositeDisposable compositeDisposable) {
        this.context = context;
        this.compositeDisposable = compositeDisposable;
    }

    @NonNull
    @Override
    public androidx.paging.DataSource create() {
        DataSource dataSource = new DataSource(context, compositeDisposable);
        mutableDataSource.postValue(dataSource);
        return dataSource;
    }

    @NonNull
    public MutableLiveData<DataSource> getDataSourceLiveData() {
        return mutableDataSource;
    }

    @NonNull
    public MutableLiveData<Context> getMutableContext() {
        return mutableContext;
    }
}