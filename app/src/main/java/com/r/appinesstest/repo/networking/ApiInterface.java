package com.r.appinesstest.repo.networking;


import com.r.appinesstest.repo.model.ResponseModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api.json")
    Observable<Response<List<ResponseModel>>> getDataFromApi();
}
