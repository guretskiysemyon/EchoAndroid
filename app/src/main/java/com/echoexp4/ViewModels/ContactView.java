package com.echoexp4.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Repositories.AppRepository;

import java.util.List;

public class ContactView  extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<Contact>> contacts;


    public ContactView(Application application){
        super(application);
        repository = new AppRepository(application);
        contacts = repository.getAllContacts();
        //allNotes = repository.getAllNotes();
    }


    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }


    public void insertContact(Contact contact){
        repository.insertContact(contact);
    }



}
