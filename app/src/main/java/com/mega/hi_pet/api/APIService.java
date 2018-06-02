package com.mega.hi_pet.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("/update")
    Call<ResponseBody> updateFeed(@Query("api_key") String api_key,
                                  @Query("field1") String field1);
}
