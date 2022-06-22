package com.echoexp4.api;

import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Repositories.MessagesRepository;
import com.echoexp4.Requests.TransferRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MessageAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    MessagesRepository messagesRepository;
    private String token;

    public MessageAPI(MessagesRepository messagesRepository, String token) {
        this.token = token;
        this.messagesRepository = messagesRepository;
        //token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJCYXoiLCJqdGkiOiIyOWY4NTgzZi02ODA1LTQ5YTAtYjRiNC02YTMyY2JkNmE0ZGIiLCJpYXQiOiIxOC4wNi4yMDIyIDE2OjMyOjE2IiwiVXNlcklkIjoiS2FyaW5hIiwiZXhwIjoxNjU1NjQxOTM2LCJpc3MiOiJFY2hvIiwiYXVkIjoiRWNobyJ9.Y9yp8a7dRwoKRqueUjHWx0_6YXe_Nv2ZNAF-NJ-FD1I";
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7099/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void sendTransfer(Message message, TransferRequest request, String server) {
        Retrofit retrofit = retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7099/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        WebAnothersServiceApi service = retrofit.create(WebAnothersServiceApi.class);
        Call<Void> call = service.sendTransfer(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    addMessage(message);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String str;
            }
        });
    }

    public void getMessages(String contact)  {
        String tkn = "Bearer " + token;
        Call<List<Message>> call = webServiceAPI.getMessages(contact, tkn);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()){
                    List<Message> data = addContactId(response.body(), contact);
                    messagesRepository.insertMessages(data);
                    //messages.setValue(data);
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
            }
        });
    }

    private List<Message> addContactId(List<Message> messages, String contactId){
        List<Message> response = new ArrayList<>(messages);
        for(Message m: response){
            m.setContactID(contactId);
        }
        return response;

    }

    public void addMessage( Message msg) {
        String tkn = "Bearer " + token;
        Call<Void> call = webServiceAPI.addMessage(msg.getContactID(), tkn, msg);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                messagesRepository.addMessageToRoom(msg);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

}
