package com.echoexp4.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.Repositories.ContactRepository;

import java.util.List;

public class ContactView  extends AndroidViewModel {
    private ContactRepository repository;
    private LiveData<List<Contact>> contacts;
    private User current_user;


    public ContactView(Application application){
        super(application);
        repository = new ContactRepository(application);
        contacts = repository.getAllContacts();
        //allNotes = repository.getAllNotes();
        current_user = repository.getCurrentUser();
    }


    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }

    public User getUser(){
        return current_user;
    }

    public void insertContact(Contact contact){
        repository.insertContact(contact);
    }



}
