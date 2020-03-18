package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.User.UserCookie;
import com.application.arenda.Entities.Utils.Network.NetworkState;
import com.application.arenda.Entities.Utils.RestApi.RestApi;
import com.application.arenda.Entities.Utils.RestApi.RestApiFactory;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DataSource extends ItemKeyedDataSource<Long, ModelAllAnnouncement> {

    private Context context;

    private RestApi restApiService;
    private Completable retryCompletable;
    private CompositeDisposable compositeDisposable;

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();


    DataSource(Context context, CompositeDisposable compositeDisposable) {
        this.context = context;
        this.restApiService = RestApiFactory.getRestApi(context);
        this.compositeDisposable = compositeDisposable;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, Timber::e));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<ModelAllAnnouncement> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoad.postValue(NetworkState.LOADING);

        //get the initial users from the api
        compositeDisposable.add(restApiService
                .loadAllAnnouncements(UserCookie.getToken(context), 0, params.requestedLoadSize)
                .subscribe(users -> {
                            // clear retry since last request succeeded
                            setRetry(null);
                            networkState.postValue(NetworkState.LOADED);
                            initialLoad.postValue(NetworkState.LOADED);
                            callback.onResult(users);
                        },
                        throwable -> {
                            // keep a Completable for future retry
                            setRetry(() -> loadInitial(params, callback));
                            NetworkState error = NetworkState.error(throwable.getMessage());
                            // publish the error
                            networkState.postValue(error);
                            initialLoad.postValue(error);
                        }));
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<ModelAllAnnouncement> callback) {
        // set network value to loading.
        networkState.postValue(NetworkState.LOADING);

        //get the users from the api after id
        compositeDisposable.add(restApiService
                .loadAllAnnouncements(UserCookie.getToken(context), params.key, params.requestedLoadSize)
                .subscribe(users -> {
                            // clear retry since last request succeeded
                            setRetry(null);
                            networkState.postValue(NetworkState.LOADED);
                            callback.onResult(users);
                        },
                        throwable -> {
                            // keep a Completable for future retry
                            setRetry(() -> loadAfter(params, callback));
                            // publish the error
                            networkState.postValue(NetworkState.error(throwable.getMessage()));
                        }));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<ModelAllAnnouncement> callback) {
        // ignored, since we only ever append to our initial load
    }

    /**
     * The id field is a unique identifier for users.
     */
    @NonNull
    @Override
    public Long getKey(@NonNull ModelAllAnnouncement item) {
        return item.getIdAnnouncement();
    }

    @NonNull
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @NonNull
    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoad;
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

}