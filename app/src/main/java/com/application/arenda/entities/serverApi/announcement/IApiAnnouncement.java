package com.application.arenda.entities.serverApi.announcement;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.models.ModelAnnouncement;
import com.application.arenda.entities.models.ModelCategory;
import com.application.arenda.entities.models.ModelFavoriteAnnouncement;
import com.application.arenda.entities.models.ModelSubcategory;
import com.application.arenda.entities.serverApi.client.ApiHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;

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
    Call<ServerResponse<List<ModelCategory>>> loadCategories();

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOAD_SUBCATEGORY)
    Call<ServerResponse<List<ModelSubcategory>>> loadSubcategories(@Field("idCategory") long idCategory);

    @Multipart
    @POST(BuildConfig.URL_INSERT_ANNOUNCEMENT)
    Call<ApiHandler> insertAnnouncement(
            @Part("token") RequestBody token,
            @Part("idSubcategory") RequestBody idSubcategory,

            @Part("name") RequestBody name,
            @Part("description") RequestBody description,

            @Part("hourlyCost") RequestBody hourlyCost,
            @Part("hourlyCurrency") RequestBody hourlyCurrency,

            @Part("dailyCost") RequestBody dailyCost,
            @Part("dailyCurrency") RequestBody dailyCurrency,

            @Part("address") RequestBody address,

            @Part("phone_1") RequestBody phone_1,

            @Part("phone_2") RequestBody phone_2,

            @Part("phone_3") RequestBody phone_3,

            @Part("minTime") RequestBody minTime,

            @Part("minDay") RequestBody minDay,

            @Part("maxRentalPeriod") RequestBody maxRentalPeriod,

            @Part("timeOfIssueWith") RequestBody timeOfIssueWith,
            @Part("timeOfIssueBy") RequestBody timeOfIssueBy,

            @Part("returnTimeWith") RequestBody returnTimeWith,
            @Part("returnTimeBy") RequestBody returnTimeBy,

            @Part("withSale") RequestBody withSale,

            @Part("nameMainPicture") RequestBody nameMainPicture,
            @Part List<MultipartBody.Part> pictures,
            @Part("countPictures") RequestBody countPictures
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_ALL_ANNOUNCEMENT)
    Call<ServerResponse<List<ModelAnnouncement>>> loadAnnouncements(
            @Field("token") String token,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_FAVORITE_ANNOUNCEMENTS)
    Call<ServerResponse<List<ModelFavoriteAnnouncement>>> loadFavoriteAnnouncements(
            @Field("token") String token,
            @Field("idFavorite") long lastID,
            @Field("limitItemsInPage") int limitItemsInPage);

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_USER_ANNOUNCEMENT)
    Call<ServerResponse<List<ModelAnnouncement>>> loadUserAnnouncements(
            @Field("token") String token,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_LAND_LORD_ANNOUNCEMENT)
    Call<ServerResponse<List<ModelAnnouncement>>> loadLandLordAnnouncements(
            @Field("token") String token,
            @Field("idLandLord") long idLandLord,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_SIMILAR_ANNOUNCEMENT)
    Call<ServerResponse<List<ModelAnnouncement>>> loadSimilarAnnouncements(
            @Field("token") String userToken,
            @Field("idSubcategory") long idSubcategory,
            @Field("idAnnouncement") long lastIdAnnouncement,
            @Field("limitItemsInPage") int limitItemsInPage,
            @Field("query") String query
    );

    @FormUrlEncoded
    @POST(BuildConfig.URL_INSERT_TO_FAVORITE)
    Call<ResponseBody> insertToFavorite(@Field("token") String token, @Field("idAnnouncement") long idAnnouncement);

    @FormUrlEncoded
    @POST(BuildConfig.URL_INSERT_VIEWER)
    Call<ApiHandler> insertViewer(@Field("token") String token, @Field("idAnnouncement") long idAnnouncement);
}