package com.application.arenda.Entities.Authentication;

import com.application.arenda.BuildConfig;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ApiAuthentication {
    @Multipart
    @FormUrlEncoded
    @POST(BuildConfig.URL_REGISTRATION)
    Call<ResponseBody> registration(@Field("name") String name,
                                    @Field("lastName") String lastName,
                                    @Field("login") String login,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("phone") String phone,
                                    @Field("accountType") String accountType);

    @FormUrlEncoded
    @POST(BuildConfig.URL_AUTHORIZATION)
    Call<ResponseBody> authorization(@Field("email") String email,
                                     @Field("password") String password);

    @FormUrlEncoded
    @POST(BuildConfig.URL_AUTHORIZATION_USE_TOKEN)
    Call<ResponseBody> authorizationUseToken(@Field("token") String token);

    enum AuthenticationCodes {
        USER_SUCCESS_REGISTERED,
        USER_UNSUCCESS_REGISTERED,
        USER_WITH_LOGIN_EXISTS,
        USER_EXISTS,

        USER_LOGGED,
        WRONG_PASSWORD,
        WRONG_EMAIL,
        WRONG_TOKEN,

        NETWORK_ERROR,
        NOT_CONNECT_TO_DB,
        UNKNOW_ERROR
    }
}