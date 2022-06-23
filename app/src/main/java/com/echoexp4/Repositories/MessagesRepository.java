package com.echoexp4.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Requests.TransferRequest;
import com.echoexp4.api.MessageAPI;

import java.util.List;

public class MessagesRepository {
    private LiveData<List<Message>> messages;
    private AllDao dao;
    private MessageAPI api;
    private Contact contact;


    public MessagesRepository(Application application, Contact contact){
        AppDB db = AppDB.getDbInstance(application.getApplicationContext());
        this.dao = db.allDao();
        this.api = new MessageAPI(this, dao.connectedUser().getToken());
        this.contact = contact;
        messages = dao.allMessages(this.contact.getId());
        api.getMessages(this.contact.getId());

    }


    public LiveData<List<Message>> getAllMessages() {
        return messages;
    }



    public void addMessage(Message message) {
        TransferRequest request = new TransferRequest(
                dao.connectedUser().getUsername(),
                contact.getId(),
                message.getContent());
        api.sendTransfer(message,request, contact.getServer());
    }



    public void addMessageToRoom(Message message) {
        dao.addMessage(message);
        dao.updateContact(message.getContent(), message.getCreated(), message.getContactID());

    }

    public void insertMessages(List<Message> messages){
        dao.insertMessages(messages);

    }


}
