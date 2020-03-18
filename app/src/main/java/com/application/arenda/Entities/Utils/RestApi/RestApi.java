package com.application.arenda.Entities.Utils.RestApi;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    @POST("php/load/LoadingAnnouncements.php")
    Single<List<ModelAllAnnouncement>> loadAllAnnouncements(@Query("token") String token,
                                                            @Query("idAnnouncement") long lastID,
                                                            @Query("pageSize") int pageSize);
}