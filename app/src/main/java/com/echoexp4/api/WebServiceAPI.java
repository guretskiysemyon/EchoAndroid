package com.echoexp4.api;


import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.LogInRequest;
import com.echoexp4.SignUpRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    @POST("contacts")
    Call<Void> addContact(@Header("Authorization") String authHeader, @Body Contact contact);

    @GET("contacts/{id}")
    Call<Contact> getContact(@Path("id") String id, @Header("Authorization") String authHeader);

    @PUT("contacts/{id}")
    Call<Void> changeContact(@Path("id") String id, @Header("Authorization") String authHeader, @Body Contact contact);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") String id, @Header("Authorization") String authHeader);

    @GET("contacts/{contactid}/messages")
    Call<List<Message>> getMessages(@Path("contactid") String contacid, @Header("Authorization") String authHeader);

    @POST("contacts/{contactid}/messages")
    Call<Void> addMessage(@Path("contactid") String contacid, @Header("Authorization") String authHeader, @Body Message message);

    @GET("contacts/{contactid}/messages/{id}")
    Call<Message> getMessage(@Path("id") String contacid, @Path("id") String id, @Header("Authorization") String authHeader);

    @PUT("contacts/{contactid}/messages/{id}")
    Call<Void> changeMessage(@Path("id") String contacid, @Path("id") String id, @Header("Authorization") String authHeader, @Body Message message);

    @DELETE("contacts/{contactid}/messages/{id}")
    Call<Void> deleteMessage(@Path("id") String contacid, @Path("id") String id, @Header("Authorization") String authHeader);

}