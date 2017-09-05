package com.m_eldeeb.maintenance.interfaces;


import com.m_eldeeb.maintenance.models.ChangeModelResult;
import com.m_eldeeb.maintenance.models.MachineModelResult;
import com.m_eldeeb.maintenance.models.PartModelResult;
import com.m_eldeeb.maintenance.models.categoryModelResult;
import com.m_eldeeb.maintenance.models.modelResult;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by melde on 6/12/2017.
 */

public interface GetData {

    @FormUrlEncoded
    @POST("rejster")
    Call<ResponseBody> register(
            @Field("userName") String userName,
            @Field("userPhone") String userPhone,
            @Field("userEmail") String userEmail,
            @Field("userPassword") String userPassword,

            @Field("notificationDate") String notificationDate,
            @Field("time") String time,
    @Field("UserToken") String UserToken,
            @Field("TimeZone") String TimeZone

    )
            ;


    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(
            @Field("userEmail") String userEmail,
            @Field("userPassword") String userPassword
    );
    @GET("category")
    Call<categoryModelResult> category(
    );
    @FormUrlEncoded
    @POST("model")
    Call<modelResult> model(
            @Field("catId") String catId
    );
    @FormUrlEncoded
    @POST("addUserMacine")
    Call<ResponseBody> addUserMachine(
            @Field("userId") String userId,
            @Field("catID") String catID,
            @Field("modelId") String modelId,
            @Field("groupId") String groupId,

            @Field("state") String state,
            @Field("workTime") String workTime,
            @Field("notfiy_numDays") String notfiy_numDays,
            @Field("dateOfModel") String dateOfModel

    );
    @FormUrlEncoded
    @POST("getUserMacine")
    Call<MachineModelResult> getUserMacine(
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("userSpearparts")
    Call<PartModelResult> userSpearParts(
            @Field("userId") String userId,
            @Field("catId") String catID,
            @Field("modelId") String modelId);


    @FormUrlEncoded
    @POST("changeSpearpart")
    Call<ResponseBody> changeSpearpart(
            @Field("userId") String userId,
            @Field("userMacineId") String userMacineId,
            @Field("workTime") String workTime);
    @FormUrlEncoded
    @POST("updateSpearparLivetime")
    Call<ResponseBody> updateSpearpartLivetime(
            @Field("userId") String userId,
            @Field("userMacineId") String userMacineId,
            @Field("mainLivetime") String mainLivetime);
    @FormUrlEncoded
    @POST("updatefullworkTime")
    Call<ResponseBody> updatefullworkTime(
            @Field("userId") String userId,
            @Field("MacineRowId") String MacineRowId,
            @Field("fullworkTime") String fullworkTime);
    @FormUrlEncoded
    @POST("userNotfications")
    Call<ChangeModelResult> userNotfications(
            @Field("userId") String userId);


    @FormUrlEncoded
    @POST("updateNotficationDate")
    Call<ResponseBody> updateNotficationDate(
            @Field("userId") String userId,
            @Field("notficationDate") String notficationDate
);
    @FormUrlEncoded
    @POST("updateTime")
    Call<ResponseBody> updateTime(
            @Field("userId") String userId,

            @Field("time") String time
    );

    @FormUrlEncoded
    @POST("updatetoken")
    Call<ResponseBody> updateToken(
            @Field("userId") String userId,

            @Field("UserToken") String token
    );
}
