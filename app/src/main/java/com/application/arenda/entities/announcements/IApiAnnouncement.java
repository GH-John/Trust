package com.application.arenda.entities.announcements;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelPicture;
import com.application.arenda.entities.models.ModelPeriodRent;
import com.application.arenda.entities.utils.retrofit.ServerHandler;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IApiAnnouncement {
    @POST(BuildConfig.URL_LOAD_CATEGORY)
    Call<ResponseBody> getCategories();

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOAD_SUBCATEGORY)
    Call<ResponseBody> getSubcategories(@Field("idCategory") long idCategory);

    @FormUrlEncoded
    @POST(BuildConfig.URL_INSERT_ANNOUNCEMENT)
    Call<ResponseBody> insertAnnouncement(
            @Field("token") String token,
            @Field("idSubcategory") int idSubcategory,

            @Field("name") String name,
            @Field("description") String description,

            @Field("costToBYN") float costToBYN,
            @Field("costToUSD") float costToUSD,
            @Field("costToEUR") float costToEUR,

            @Field("address") String address,

            @Field("phone_1") String phone_1,

            @Field("phone_2") String phone_2,

            @Field("phone_3") String phone_3
    );

    @Multipart
    @POST(BuildConfig.URL_INSERT_PHOTO)
    Call<ResponseBody> insertPictures(
            @Part("idAnnouncement") RequestBody idAnnouncement,
            @Part("nameMainPicture") RequestBody nameMainPicture,
            @Part List<MultipartBody.Part> pictures,
            @Part("countPictures") int countPictures);

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_ALL_ANNOUNCEMENT)
    Call<ServerHandler<List<ModelAnnouncement>>> loadAnnouncements(
            @Field("token") String token,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_USER_ANNOUNCEMENT)
    Call<ServerHandler<List<ModelAnnouncement>>> loadUserAnnouncements(
            @Field("token") String token,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_LAND_LORD_ANNOUNCEMENT)
    Call<ServerHandler<List<ModelAnnouncement>>> loadLandLordAnnouncements(
            @Field("token") String token,
            @Field("idLandLord") long idLandLord,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_SIMILAR_ANNOUNCEMENT)
    Call<ServerHandler<List<ModelAnnouncement>>> loadSimilarAnnouncements(
            @Field("token") String userToken,
            @Field("idSubcategory") long idSubcategory,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_PERIOD_RENT_ANNOUNCEMENT)
    Call<ServerHandler<List<ModelPeriodRent>>> loadPeriodRentAnnouncement(
            @Field("idAnnouncement") long idAnnouncement
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_PICTURES)
    Call<List<ModelPicture>> loadPictures(@Field("idAnnouncement") long idAnnouncement);

    @FormUrlEncoded
    @POST(BuildConfig.URL_INSERT_TO_FAVORITE)
    Call<ResponseBody> insertToFavorite(@Field("token") String token, @Field("idAnnouncement") long idAnnouncement);

    @FormUrlEncoded
    @POST(BuildConfig.URL_INSERT_VIEWER)
    Call<ResponseBody> insertViewer(@Field("token") String token, @Field("idAnnouncement") long idAnnouncement);

    enum AnnouncementCodes {
        SUCCESS_CATEGORIES_LOADED,
        UNSUCCESS_CATEGORIES_LOADED,

        SUCCESS_SUBCATEGORIES_LOADED,
        UNSUCCESS_SUBCATEGORIES_LOADED,

        SUCCESS_ANNOUNCEMENT_ADDED,
        UNSUCCESS_ANNOUNCEMENT_ADDED,

        SUCCESS_ANNOUNCEMENTS_LOADED,
        UNSUCCESS_ANNOUNCEMENTS_LOADED,

        SUCCESS_ANNOUNCEMENT_LOADED,
        UNSUCCESS_ANNOUNCEMENT_LOADED,

        SUCCESS_PICTURES_ADDED,
        UNSUCCESS_PICTURES_ADDED,

        SUCCESS_PICTURES_LOADED,
        UNSUCCESS_PICTURES_LOADED,

        SUCCESS_ADD_TO_FAVORITE,
        UNSUCCESS_ADD_TO_FAVORITE,

        SUCCESS_REMOVE_FROM_FAVORITE,
        UNSUCCESS_REMOVE_FROM_FAVORITE,

        USER_NOT_FOUND,
        UNSUCCESS_LOAD_MAIN_PICTURE,

        PHP_INI_NOT_LOADED,

        NONE_REZULT,
        NETWORK_ERROR,
        NOT_CONNECT_TO_DB,
        UNKNOW_ERROR
    }
}