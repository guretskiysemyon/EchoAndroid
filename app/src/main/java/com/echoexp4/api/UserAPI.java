package com.echoexp4.api;

import com.echoexp4.Database.Entities.User;
import com.echoexp4.Repositories.ConnectionRepository;
import com.echoexp4.Requests.LogInRequest;
import com.echoexp4.Requests.SignUpRequest;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class UserAPI extends Thread {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    ConnectionRepository repository;
    private String token;

    public UserAPI(ConnectionRepository repository) {
        this.repository = repository;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7099/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    //TODO: get data about user
    public void logIn(LogInRequest request) {
        Call<ResponseBody> call = webServiceAPI.login(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    token = response.body().string();
                    User user  = new User(request.username, request.username, request.password,null, token);
                    user.setServer("localhost:7099");
                    repository.setUser(user, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String str;
            }
        });
    }




    public void SignUp(SignUpRequest request) {
        Call<ResponseBody> call = webServiceAPI.signup(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    token = response.body().string();
                    User user  = new User(request.username, request.name, request.password,request.image, token);
                    repository.setUser(user, token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String str;
            }
        });
    }








}