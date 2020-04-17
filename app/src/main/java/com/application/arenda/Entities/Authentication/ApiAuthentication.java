package com.application.arenda.Entities.Authentication;

import com.application.arenda.BuildConfig;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiAuthentication {
    @Multipart
    @POST(BuildConfig.URL_REGISTRATION)
    Call<ResponseBody> registration(@Part MultipartBody.Part logo,
                                    @Part("name") RequestBody name,
                                    @Part("lastName") RequestBody lastName,
                                    @Part("login") RequestBody login,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part("phone") RequestBody phone,
                                    @Part("accountType") RequestBody accountType);

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