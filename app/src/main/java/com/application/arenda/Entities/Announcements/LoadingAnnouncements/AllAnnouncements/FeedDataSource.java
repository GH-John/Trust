package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.AppController;
import com.application.arenda.Entities.Utils.Network.NetworkState;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedDataSource extends PageKeyedDataSource<Long, ModelAllAnnouncement> {

    private static final String TAG = FeedDataSource.class.getSimpleName();

    private Context context;

    private AppController appController;

    private MutableLiveData networkState;
    private MutableLiveData initialLoading;

    public FeedDataSource(Context context) {
        this.context = context;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, ModelAllAnnouncement> callback) {
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        appController.getRestApi().loadAllAnnouncements(UserCookie.getToken(context), 0)
                .enqueue(new Callback<List<ModelAllAnnouncement>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ModelAllAnnouncement>> call, @NonNull Response<List<ModelAllAnnouncement>> response) {
                        if (response.isSuccessful()) {
                            callback.onResult(response.body(), null, 2L);
                            initialLoading.postValue(NetworkState.LOADED);
                            networkState.postValue(NetworkState.LOADED);

                        } else {
                            initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ModelAllAnnouncement>> call, @NonNull Throwable t) {
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, t.getMessage()));
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ModelAllAnnouncement> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ModelAllAnnouncement> callback) {

    }
}