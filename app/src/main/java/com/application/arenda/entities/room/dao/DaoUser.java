package com.application.arenda.entities.room.dao;

import android.annotation.SuppressLint;

import androidx.room.Dao;
import androidx.room.Query;

import com.application.arenda.entities.models.ModelUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.ResourceCompletableObserver;
import io.reactivex.schedulers.Schedulers;

@Dao
public abstract class DaoUser implements BaseDao<ModelUser> {
    @Query("select * from users where idUser = :idUser")
    public abstract Maybe<ModelUser> getUser(long idUser);

    @Query("select * from users where isCurrent = 1")
    protected abstract Maybe<ModelUser> getCurrentUser();

    @Query("select * from users where isCurrent = 1")
    public abstract Flowable<List<ModelUser>> getActiveUser();

    @Query("delete from users where idUser = :idUser")
    public abstract Completable removeById(long idUser);

    @SuppressLint("CheckResult")
    public synchronized Completable logoutCurrentUser() {
        return Completable.create(emitter -> getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<ModelUser>() {
                    @Override
                    public void onSuccess(ModelUser user) {
                        if (user != null) {
                            user.setCurrent(false);

                            updateInRoom(user)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ResourceCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            emitter.onComplete();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            emitter.onError(e);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        emitter.onComplete();
                    }
                }));
    }

    @SuppressLint("CheckResult")
    public synchronized Completable changeCurrentUser(ModelUser newUser) {
        return Completable.create(emitter -> getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<ModelUser>() {
                    @Override
                    public void onSuccess(ModelUser user) {
                        if (user != null) {
                            user.setCurrent(false);

                            updateInRoom(user)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ResourceCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            insertUser(newUser, emitter);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            emitter.onError(e);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        insertUser(newUser, emitter);
                    }
                }));
    }

    private synchronized void insertUser(ModelUser newUser, CompletableEmitter emitter) {
        if (newUser == null) {
            emitter.onError(new NullPointerException("ModelUser is null"));
            return;
        }

        newUser.setCurrent(true);

        insertToRoom(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceCompletableObserver() {
                    @Override
                    public void onComplete() {
                        emitter.onComplete();

                    }

                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }
                });
    }
}