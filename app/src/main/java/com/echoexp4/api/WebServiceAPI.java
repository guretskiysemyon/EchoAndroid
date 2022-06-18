package com.echoexp4.api;


import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.LogInRequest;
import com.echoexp4.SignUpRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WebServiceAPI {

    @GET("User")
    Call<List<User>> getUsers();

    @POST("User/signup")
    Call<Void> createUser(@Query("userId") String userId, @Query("userName") String userName, @Query("password") String password, @Query("img") String img);

    @POST("User")
    Call<ResponseBody> login(@Body LogInRequest user);

    @POST("User/signup")
    Call<ResponseBody> signup(@Body SignUpRequest user);

    @GET("contacts")
    Call<List<Contact>> getContacts(@Header("Authorization") String authHeader);


}
