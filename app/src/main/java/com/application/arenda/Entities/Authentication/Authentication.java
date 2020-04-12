package com.application.arenda.Entities.Authentication;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Models.User;
import com.application.arenda.Entities.User.AccountType;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public final class Authentication {
    @SuppressLint("StaticFieldLeak")
    private static Authentication instance;

    private Authentication() {
    }

    public static Authentication getInstance() {
        if (instance == null)
            instance = new Authentication();

        return instance;
    }

    public void registration(@NonNull String name,
                             @NonNull String lastName,
                             @NonNull String login,
                             @NonNull String email,
                             @NonNull String password,
                             @NonNull String phone,
                             @NonNull AccountType accountType,
                             @NonNull AsyncCallback<BackendlessUser> callback) {

        BackendlessUser user = new BackendlessUser();

        user.setEmail(email);
        user.setPassword(password);

        user.setProperty("name", name);
        user.setProperty("lastName", lastName);

        user.setProperty("login", login);

        user.setProperty("phone_1", phone);

        user.setProperty("accountType", accountType.getType());

        Backendless.UserService.register(user, callback);
    }

    public void authorization(@NonNull String email,
                              @NonNull String password,
                              @NonNull AsyncCallback<BackendlessUser> callback) {

        Backendless.UserService.login(email, password, callback, true);
    }

    public void restorePassword(@NonNull String email,
                                @NonNull AsyncCallback<Void> callback) {
        Backendless.UserService.restorePassword(email, callback);
    }

    public void getUserData(@NonNull AsyncCallback<User> callback) {
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if (response) {
                    Backendless.Data.of(BackendlessUser.class).findById(getUserID(), new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            User user = new User();

                            user.setObjectId(response.getObjectId());
                            user.setName(String.valueOf(response.getProperty("name")));
                            user.setLastName(String.valueOf(response.getProperty("lastName")));

                            user.setLogin(String.valueOf(response.getProperty("login")));
                            user.setEmail(String.valueOf(response.getProperty("email")));
                            user.setUserPhotoUri(String.valueOf(response.getProperty("userPhotoUri")));

                            user.setAddress_1(String.valueOf(response.getProperty("address_1")));
                            user.setAddress_2(String.valueOf(response.getProperty("address_2")));
                            user.setAddress_3(String.valueOf(response.getProperty("address_3")));

                            user.setPhone_1(String.valueOf(response.getProperty("phone_1")));
                            user.setPhone_2(String.valueOf(response.getProperty("phone_2")));
                            user.setPhone_3(String.valueOf(response.getProperty("phone_3")));

                            user.setAccountType(AccountType.valueOf(String.valueOf(response.getProperty("accountType"))));
                            user.setBalance(Double.parseDouble(String.valueOf(response.getProperty("balance"))));
                            user.setRating(Double.parseDouble(String.valueOf(response.getProperty("rating"))));

                            callback.handleResponse(user);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            callback.handleFault(fault);
                        }
                    });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                callback.handleFault(fault);
            }
        });
    }

    private String getUserID() {
        return UserIdStorageFactory.instance().getStorage().get();
    }
}