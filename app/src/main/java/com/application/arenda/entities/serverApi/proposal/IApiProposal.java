package com.application.arenda.entities.serverApi.proposal;

import com.application.arenda.BuildConfig;
import com.application.arenda.entities.models.ModelPeriodRent;
import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.serverApi.client.ApiHandler;
import com.application.arenda.entities.serverApi.client.ServerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IApiProposal {

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_PERIOD_RENT_ANNOUNCEMENT)
    Call<ServerResponse<List<ModelPeriodRent>>> loadPeriodRentAnnouncement(@Field("idAnnouncement") long idAnnouncement);

    @FormUrlEncoded
    @POST(BuildConfig.URL_INSERT_PROPOSAL)
    Call<ApiHandler> insertProposal(@Field("token") String token,
                                    @Field("idAnnouncement") long idAnnouncement,
                                    @Field("rentalStart") String rentalStart,
                                    @Field("rentalEnd") String rentalEnd);

    @FormUrlEncoded
    @POST(BuildConfig.URL_ACCEPT_INCOMING_PROPOSAL)
    Call<ApiHandler> acceptProposal(@Field("token") String token,
                                    @Field("idRent") long idRent);

    @FormUrlEncoded
    @POST(BuildConfig.URL_REJECT_INCOMING_PROPOSAL)
    Call<ApiHandler> rejectIncomingProposal(@Field("token") String token,
                                    @Field("idRent") long idRent);

    @FormUrlEncoded
    @POST(BuildConfig.URL_REJECT_OUTGOING_PROPOSAL)
    Call<ApiHandler> rejectOutgoingProposal(@Field("token") String token,
                                            @Field("idRent") long idRent);

//    @FormUrlEncoded
//    @POST(BuildConfig.URL_EDIT_OUTGOING_PROPOSAL)
//    Call<ApiHandler> editOutgoingProposal();

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_INCOMING_PROPOSALS)
    Call<ServerResponse<List<ModelProposal>>> loadIncomingProposals(
            @Field("token") String userToken,
            @Field("idRent") long idRent,
            @Field("limitItemsInPage") int limitItemsInPage);

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_OUTGOING_PROPOSALS)
    Call<ServerResponse<List<ModelProposal>>> loadOutgoingProposals(
            @Field("token") String userToken,
            @Field("idRent") long idRent,
            @Field("limitItemsInPage") int limitItemsInPage);

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_ACTIVE_PROPOSALS)
    Call<ServerResponse<List<ModelProposal>>> loadActiveProposals(
            @Field("token") String userToken,
            @Field("idRent") long idRent,
            @Field("limitItemsInPage") int limitItemsInPage);

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_HISTORY_PROPOSALS)
    Call<ServerResponse<List<ModelProposal>>> loadHistoryProposals(
            @Field("token") String userToken,
            @Field("idRent") long idRent,
            @Field("limitItemsInPage") int limitItemsInPage);
}