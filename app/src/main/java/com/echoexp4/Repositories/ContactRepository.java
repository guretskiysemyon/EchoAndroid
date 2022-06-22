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
  //  private ContactListData contacts;
    private LiveData<List<Contact>> contacts;

    public ContactRepository(Application application) {
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new ContactAPI(this, dao.connectedUser().getToken());
        contacts = dao.allContacts();
        api.getContacts();
        //contacts = new ContactListData();
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
      //  contacts.setValue(dao.allContacts().getValue());
    }

    public User getCurrentUser(){
        return dao.getUser();
    }

    public void insertContacts(List<Contact> contacts){
        Contact[] c = contacts.toArray(new Contact[0]);


        dao.insertContacts(contacts);
      //  this.contacts.setValue(dao.allContacts().getValue());
    }


    public LiveData<List<Contact>> getAllContacts(){
        return contacts;
    }



////////////////////////////////////////////////////////////////////
/*
    class ContactListData extends MutableLiveData<List<Contact>> {

        public ContactListData() {
            super();
            setValue(new ArrayList<Contact>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                contacts.postValue(dao.allContacts().getValue());
            }).start();

            api.getContacts(this);


        }
    }


 */
    ////////////////////////////////////////////////////////////





}
