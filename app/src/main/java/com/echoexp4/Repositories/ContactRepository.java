package com.echoexp4.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.NotificationListener;
import com.echoexp4.Requests.InvitationRequest;
import com.echoexp4.api.ContactAPI;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository implements NotificationListener {
    private AllDao dao;
    private ContactAPI api;
    private ContactListData contacts;

    public ContactRepository(Application application) {
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new ContactAPI(this, dao.connectedUser().getToken());
        contacts = new ContactListData();
       // contacts.setValue(dao.allContacts());

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
        contacts.setValue(dao.allContacts());
    }

    public User getCurrentUser(){
        return dao.getUser();
    }

    public void insertContacts(List<Contact> contacts){
        dao.insertContacts(contacts);
        this.contacts.setValue(dao.allContacts());
    }


    public LiveData<List<Contact>> getAllContacts(){
        return contacts;
    }

    @Override
    public void pullNotification() {
        api.getContacts(this.contacts);
    }


////////////////////////////////////////////////////////////////////

    class ContactListData extends MutableLiveData<List<Contact>> {

        public ContactListData() {
            super();
            setValue(new ArrayList<Contact>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                contacts.postValue(dao.allContacts());
            }).start();

            api.getContacts(this);


        }
    }

    ////////////////////////////////////////////////////////////





}
