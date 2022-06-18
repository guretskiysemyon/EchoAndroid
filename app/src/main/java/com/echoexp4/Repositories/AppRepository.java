package com.echoexp4.Repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.api.UserAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppRepository {
    private AllDao dao;
    private UserAPI api;
    private MutableLiveData<List<Contact>> contacts;

    public AppRepository(Application application) {
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = api;
        contacts = new MutableLiveData<>();
        contacts.setValue(dao.allContacts());
        //contacts.setValue(dao.allContacts());
    }


    class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            setValue(new ArrayList<Contact>());
        }

        @Override
        protected void onActive() {
            super.onActive();
           // UserAPI userAPI = new UserAPI();
            //userAPI.getContacts();
/*
            new Thread(() -> {
                contactListData.postValue(dao.get);
            }).start();
 */
        }
    }

    public LiveData<List<Contact>> getAllContacts(){
        return contacts;
    }

    public void insertContact(Contact contact){
        new InsertContactAsyncTask(dao).execute(contact);
    }


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



}
