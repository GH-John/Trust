package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.Utils.Network.NetworkState;

import io.reactivex.disposables.CompositeDisposable;

public class AllAnnouncementsViewModel extends AndroidViewModel {

    private static final int pageSize = 15;
    private LiveData<PagedList<ModelAllAnnouncement>> collection;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DataSourceFactory dataSourceFactory;

    public AllAnnouncementsViewModel(@NonNull Application application) {
        super(application);

        dataSourceFactory = new DataSourceFactory(getApplication(), compositeDisposable);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build();

        collection = new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    public LiveData<PagedList<ModelAllAnnouncement>> getCollection() {
        return collection;
    }

    public void retry() {
        dataSourceFactory.getDataSourceLiveData().getValue().retry();
    }

    public void refresh() {
        dataSourceFactory.getDataSourceLiveData().getValue().invalidate();
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(dataSourceFactory.getDataSourceLiveData(), DataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(dataSourceFactory.getDataSourceLiveData(), DataSource::getInitialLoad);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}