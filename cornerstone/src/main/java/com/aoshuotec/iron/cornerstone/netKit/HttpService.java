package com.aoshuotec.iron.cornerstone.netKit;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface HttpService {

    @GET
    Observable<Result<Map<String,String>>> get(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> queryMap);

    @GET
    Observable<Result<Map<String,Object>>> get2(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> queryMap);

    @FormUrlEncoded
    @POST
    Observable<Result<Map<String,String>>> post(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> queryMap);

    @FormUrlEncoded
    @POST
    Observable<Result<Map<String,Object>>> post2(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> queryMap);


    //上传文件
    @Multipart
    @POST
    Observable<ResponseBody> upLoadFiles(@Url String url, @PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> parts);
}
