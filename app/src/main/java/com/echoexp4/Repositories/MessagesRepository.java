package com.echoexp4.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.api.UserAPI;

import java.util.ArrayList;
import java.util.List;

public class MessagesRepository {
    private MessageListData messages;
    private AllDao dao;
    private UserAPI api;
    private String contactId;


    public MessagesRepository(Application application, String contactId){
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new UserAPI();
        api.setMessagesRepository(this);
        this.contactId = contactId;
        messages = new MessageListData();
        messages.setValue(dao.allMessages(this.contactId));
    }


    public LiveData<List<Message>> getAllMessages() {
        return messages;
    }


    public void setMessagesData(Contact contact){
        this.contactId = contact.getId();
        this.messages.setValue(dao.allMessages(contact.getId()));
    }

    public void addMessage(Message message) {
        api.addMessage(message);
    }

    public void addMessageToRoom(Message message) throws InterruptedException {
      dao.addMessage(message);
      //new InsertMessageAsyncTask(dao).execute(message);
       messages.setValue(dao.allMessages(this.contactId));
    }

/*
    private static class InsertMessageAsyncTask extends AsyncTask<Message, Void, Void> {
        private AllDao dao;

        protected InsertMessageAsyncTask(AllDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Message... mess) {
            dao.addMessage(mess[0]);
            return null;
        }
    }

*/
    class MessageListData extends MutableLiveData<List<Message>> {

        public MessageListData() {
            super();
            setValue(new ArrayList<Message>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            //
            new Thread(() -> {
                List<Message> mes = dao.allMessages(contactId);
                messages.postValue(mes);
            }).start();

            api.getMessages(contactId, this);
        }
    }
}
