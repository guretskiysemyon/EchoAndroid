package com.echoexp4.api;

import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Repositories.ContactRepository;
import com.echoexp4.Requests.InvitationRequest;
import com.echoexp4.Requests.TransferRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ContactAPI {
    ContactRepository contactRepository;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private String token;


    public ContactAPI(ContactRepository repository, String token){
        this.token = token;
        this.contactRepository = repository;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7099/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }



    public void getContacts()  {
        String tkn = "Bearer " + token;
        Call<List<Contact>> call = webServiceAPI.getContacts(tkn);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.isSuccessful()){
                    contactRepository.insertContacts(response.body());
                    //contacts.setValue(response.body());
                }

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
                    contactRepository.insertContactToRoom(contact);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }
    public void sendInvitations(Contact contact, InvitationRequest request, String server) {
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7099/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        WebAnothersServiceApi service = retrofit2.create(WebAnothersServiceApi.class);
        Call<Void> call = service.sendInvitations(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    addContact(contact);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String str;
            }
        });
    }

}
