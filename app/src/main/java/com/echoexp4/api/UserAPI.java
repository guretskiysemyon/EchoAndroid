package com.echoexp4.api;

import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.LogInRequest;
import com.echoexp4.Repositories.AppRepository;
import com.echoexp4.Repositories.ConnectionRepository;
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
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJCYXoiLCJqdGkiOiIyOWY4NTgzZi02ODA1LTQ5YTAtYjRiNC02YTMyY2JkNmE0ZGIiLCJpYXQiOiIxOC4wNi4yMDIyIDE2OjMyOjE2IiwiVXNlcklkIjoiS2FyaW5hIiwiZXhwIjoxNjU1NjQxOTM2LCJpc3MiOiJFY2hvIiwiYXVkIjoiRWNobyJ9.Y9yp8a7dRwoKRqueUjHWx0_6YXe_Nv2ZNAF-NJ-FD1I";
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7099/api/")
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

    public void getContacts(MutableLiveData<List<Contact>> contacts)  {
        String tkn = "Bearer " + token;
        Call<List<Contact>> call = webServiceAPI.getContacts(tkn);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                contacts.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
            }
        });
    }

    public void addContact(Contact contact) {
        String tkn = "Bearer " + token;
        Call<Void> call = webServiceAPI.addContact(tkn, contact);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    appRepository.insertContactToRoom(contact);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void deleteContact(Contact contact) {
        String tkn = "Bearer " + token;
        Call<Void> call = webServiceAPI.deleteContact(contact.getId(),tkn);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                appRepository.deleteInRoom(contact);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }


    public void changeContact(Contact contact) {
        String tkn = "Bearer " + token;
        Call<Void> call = webServiceAPI.changeContact(contact.getId(),tkn, contact);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                appRepository.changeContactInRoom(contact);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void getMessages(String contact, MutableLiveData<List<Message>> messages)  {
        String tkn = "Bearer " + token;
        Call<List<Message>> call = webServiceAPI.getMessages(contact, tkn);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
            }
        });
    }

    public void addMessage( Message msg) {
        String tkn = "Bearer " + token;
        Call<Void> call = webServiceAPI.addMessage(msg.getContactId(), tkn, msg);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Integer s = response.code();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }


}