package com.passta.a2ndproj.network;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ServiceApi {

    @POST("request_msg_to_location")
    Call<ResponseBody> selectMsg(@Query("sido") String sido, @Query("gusi") String gusi, @Query("limit") int limit);

}