package com.application.arenda.entities.serverApi.user;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.models.ModelUserProfileToView;
import com.application.arenda.entities.serverApi.client.ApiHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IApiUser {
    @Multipart
    @POST(BuildConfig.URL_REGISTRATION)
    Call<ServerResponse<ModelUser>> registration(@Part MultipartBody.Part logo,
                                                 @Part("name") RequestBody name,
                                                 @Part("lastName") RequestBody lastName,
                                                 @Part("login") RequestBody login,
                                                 @Part("email") RequestBody email,
                                                 @Part("password") RequestBody password,
                                                 @Part("phone") RequestBody phone,
                                                 @Part("address_1") RequestBody address,
                                                 @Part("accountType") RequestBody accountType);

    @FormUrlEncoded
    @POST(BuildConfig.URL_AUTHORIZATION)
    Call<ServerResponse<ModelUser>> authorization(
            @Field("email_login") String email_login,
            @Field("password") String password);

    @FormUrlEncoded
    @POST(BuildConfig.URL_USER_FOLLOWING)
    Call<ApiHandler> followToUser(
            @Field("token") String token,
            @Field("idUser") long idUser,
            @Field("isFollow") Boolean isFollow);

    @FormUrlEncoded
    @POST(BuildConfig.URL_USER_LOAD_OWN_PROFILE)
    Call<ServerResponse<ModelUser>> loadOwnProfile(@Field("token") String token);

    @FormUrlEncoded
    @POST(BuildConfig.URL_USER_LOAD_PROFILE_TO_VIEW)
    Call<ServerResponse<ModelUserProfileToView>> loadProfileToView(@Field("token") String token,
                                                                   @Field("idUser") long idUser);

    @FormUrlEncoded
    @POST(BuildConfig.URL_USER_UPDATE_PROFILE)
    Call<ApiHandler> updateProfile(@Field("token") String token,
                                   @Field("address_1") String address_1,
                                   @Field("address_2") String address_2,
                                   @Field("address_3") String address_3,

                                   @Field("phone_1") String phone_1,
                                   @Field("phone_2") String phone_2,
                                   @Field("phone_3") String phone_3);
}