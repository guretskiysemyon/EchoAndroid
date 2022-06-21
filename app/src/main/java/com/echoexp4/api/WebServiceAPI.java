package com.echoexp4.api;


import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.Requests.InvitationRequest;
import com.echoexp4.Requests.LogInRequest;
import com.echoexp4.Requests.SignUpRequest;
import com.echoexp4.Requests.TransferRequest;

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
import retrofit2.http.Url;

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

    @PUT("contacts/{id}")
    Call<Void> changeContact(@Path("id") String id, @Header("Authorization") String authHeader, @Body Contact contact);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") String id, @Header("Authorization") String authHeader);

    @GET("contacts/{contactid}/messages")
    Call<List<Message>> getMessages(@Path("contactid") String contacid, @Header("Authorization") String authHeader);

    @POST("contacts/{contactid}/messages")
    Call<Void> addMessage(@Path("contactid") String contacid, @Header("Authorization") String authHeader, @Body Message message);


    

}