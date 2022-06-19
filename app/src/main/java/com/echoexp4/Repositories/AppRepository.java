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

public class AppRepository {
    private AllDao dao;
    private UserAPI api;
    private ContactListData contacts;
    private MessageListData messages;

    public AppRepository(Application application) {
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new UserAPI();
        api.setAppRepository(this);
        contacts = new ContactListData();
        contacts.setValue(dao.allContacts());
        messages = new MessageListData();

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
        //contacts.setValue(dao.allContacts());
        return contacts;
    }

    public LiveData<List<Message>> getAllMessages(Contact contact) {
        return messages;
    }


    public void setMessagesData(Contact contact){
        this.messages.setContactId(contact.getId());
        this.messages.setValue(dao.allMessages(contact.getId()));
    }

    /////////////////////////////////////////////////////////

    public void addMessage(Message message) {
        api.addMessage(message);
    }

    public void addMessageToRoom(Message message){
        new InsertMessageAsyncTask(dao).execute(message);
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

    class MessageListData extends MutableLiveData<List<Message>> {
        private String contactId;

        public MessageListData() {
            super();
            setValue(new ArrayList<Message>());
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //
            new Thread(() -> {
                messages.postValue(dao.allMessages(contactId));
            }).start();

            api.getMessages(contactId, this);
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

    private static class InsertMessageAsyncTask extends AsyncTask<Message, Void, Void> {
        private AllDao dao;

        protected InsertMessageAsyncTask(AllDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            dao.addMessage(messages[0]);
            return null;
        }
    }


}
