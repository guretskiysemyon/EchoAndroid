package com.echoexp4.api;

import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.Repositories.AppRepository;
import com.echoexp4.Repositories.ConnectionRepository;
import com.echoexp4.LogInRequest;
import com.echoexp4.SignUpRequest;

import java.io.IOException;
import java.util.List;

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
    ConnectionRepository ConnectionRepository;
    AppRepository appRepository;
    private String token;

    public UserAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7130/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void setConnectionRepository(ConnectionRepository connectionrepository) {
        this.ConnectionRepository = connectionrepository;
    }

    public void setAppRepository(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    //TODO: Delete
    public void getAll() {
        Call<List<User>> call = webServiceAPI.getUsers();
        call.enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                List<User> users = response.body();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
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
                    ConnectionRepository.setUser(user, token);
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
                    ConnectionRepository.setUser(user, token);
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

    public void getContacts(String token)  {
        if (token == null) {
            return;
        }
        Call<List<Contact>> call = webServiceAPI.getContacts(token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> contacts = response.body();
            }
            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
            }
        });
    }


}