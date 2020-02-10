package com.application.arenda.Entities.Utils.RestApi;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    @POST("/AndroidConnectWithServer/php/load/LoadingAnnouncements.php")
    Call<List<ModelAllAnnouncement>> loadAllAnnouncements(@Query("token") String token, @Query("idAnnouncement") long lastID);
}