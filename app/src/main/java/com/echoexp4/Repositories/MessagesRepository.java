package com.echoexp4.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.NotificationListener;
import com.echoexp4.Requests.TransferRequest;
import com.echoexp4.api.MessageAPI;
import com.echoexp4.api.UserAPI;
import com.echoexp4.firebase.MyService;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MessagesRepository implements NotificationListener {
    private MessageListData messages;
    private AllDao dao;
    private MessageAPI api;
    private Contact contact;


    public MessagesRepository(Application application, Contact contact){
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new MessageAPI(this, dao.connectedUser().getToken());
        //api.setMessagesRepository(this);
        this.contact = contact;
        messages = new MessageListData();
        messages.setValue(dao.allMessages(this.contact.getId()));


    }




    public LiveData<List<Message>> getAllMessages() {
        return messages;
    }

/*
    public void setMessagesData(Contact contact){
        this.contact = contact.getId();
        this.messages.setValue(dao.allMessages(contact.getId()));
    }

 */

    public void addMessage(Message message) {
        TransferRequest request = new TransferRequest(
                dao.connectedUser().getUsername(),
                contact.getId(),
                message.getContent());
        api.sendTransfer(message,request, contact.getServer());
    }


    public void addMessageToRoom(Message message) {
        dao.addMessage(message);
      //new InsertMessageAsyncTask(dao).execute(message);
       messages.setValue(dao.allMessages(this.contact.getId()));
    }

    public void insertMessages(List<Message> messages){
        dao.insertMessages(messages);
        this.messages.setValue(dao.allMessages(contact.getId()));
    }

    @Override
    public void pullNotification() {
        api.getMessages(contact.getId(), messages);
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
                List<Message> mes = dao.allMessages(contact.getId());
                messages.postValue(mes);
            }).start();

            api.getMessages(contact.getId(), this);
        }
    }
}
