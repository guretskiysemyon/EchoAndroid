package com.echoexp4.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.Requests.InvitationRequest;
import com.echoexp4.api.ContactAPI;

import java.util.List;

public class ContactRepository {
    private AllDao dao;
    private ContactAPI api;
    private LiveData<List<Contact>> contacts;

    public ContactRepository(Application application) {
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new ContactAPI(this, dao.connectedUser().getToken());
        contacts = dao.allContacts();
        api.getContacts();

    }


    public void insertContact(Contact contact){
        InvitationRequest request = new InvitationRequest(
                dao.connectedUser().getUsername(),
                contact.getId(),
                dao.connectedUser().getServer()
        );
        api.sendInvitations(contact,request, contact.getServer());
    }

    public void insertContactToRoom(Contact contact){
        dao.addContact(contact);

    }

    public User getCurrentUser(){
        return dao.getUser();
    }

    public void insertContacts(List<Contact> contacts){
        dao.insertContacts(contacts);

    }


    public LiveData<List<Contact>> getAllContacts(){
        return contacts;
    }





}
