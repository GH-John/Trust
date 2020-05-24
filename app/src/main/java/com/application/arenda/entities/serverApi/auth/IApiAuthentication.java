package com.application.arenda.entities.serverApi.auth;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.utils.retrofit.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IApiAuthentication {
    @Multipart
    @POST(BuildConfig.URL_REGISTRATION)
    Call<ServerResponse<ModelUser>> registration(@Part MultipartBody.Part logo,
                                                 @Part("name") RequestBody name,
                                                 @Part("lastName") RequestBody lastName,
                                                 @Part("login") RequestBody login,
                                                 @Part("email") RequestBody email,
                                                 @Part("password") RequestBody password,
                                                 @Part("phone") RequestBody phone,
                                                 @Part("accountType") RequestBody accountType);

    @FormUrlEncoded
    @POST(BuildConfig.URL_AUTHORIZATION)
    Call<ServerResponse<ModelUser>> authorization(
            @Field("email_login") String email_login,
            @Field("password") String password);
}