package com.echoexp4.Repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.api.UserAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactRepository {
    private AllDao dao;
    private UserAPI api;
    private ContactListData contacts;

    public ContactRepository(Application application) {
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new UserAPI();
        api.setContactRepository(this);
        contacts = new ContactListData();
        contacts.setValue(dao.allContacts());

    }


    public void insertContact(Contact contact){
        api.addContact(contact);
    }

    public void insertContactToRoom(Contact contact){
        new InsertContactAsyncTask(dao).execute(contact);
    }

    public void deleteContact(Contact contact) {
        api.deleteContact(contact);
    }
    public void deleteInRoom(Contact contact){
        new DeleteContactAsyncTask(dao).execute(contact);
    }

    public void changeContact(Contact contact) {
        api.changeContact( contact);
    }

    public void changeContactInRoom(Contact contact) {
       new UpdateContactAsyncTask(dao).execute(contact);
    }

    public LiveData<List<Contact>> getAllContacts(){
        return contacts;
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

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private AllDao dao;

        protected InsertContactAsyncTask(AllDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            dao.addContact(contacts[0]);
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private AllDao dao;

        protected DeleteContactAsyncTask(AllDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            dao.deleteContact(contacts[0]);
            return null;
        }
    }

    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        private AllDao dao;

        protected UpdateContactAsyncTask(AllDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            dao.changeContact(contacts[0]);
            return null;
        }
    }



}
