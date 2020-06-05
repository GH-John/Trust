package com.application.arenda.entities.serverApi.chat

import com.application.arenda.BuildConfig
import com.application.arenda.entities.models.ModelChat
import com.application.arenda.entities.models.ModelMessage
import com.application.arenda.entities.serverApi.client.ServerResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal interface IApiChat {
    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_CHATS_USER)
    fun loadChats(
            @Field("token") token: String?,
            @Field("idChat") lastIdChat: Long = 0,
            @Field("limitItemsInPage") limitItemsInPage: Int = 10
    ): Call<ServerResponse<List<ModelChat>>>

    @FormUrlEncoded
    @POST(BuildConfig.URL_LOADING_CHAT_USER)
    fun loadChat(
            @Field("token") token: String?,
            @Field("idUser_To") idUser_To: Long,
            @Field("idMessageAfter") loadAfterIdMessage: Long = 0,
            @Field("idMessageBefore") loadBeforeIdMessage: Long = 0,
            @Field("limitItemsInPage") limitItemsInPage: Int = 10
    ): Call<ServerResponse<List<ModelMessage>>>
}