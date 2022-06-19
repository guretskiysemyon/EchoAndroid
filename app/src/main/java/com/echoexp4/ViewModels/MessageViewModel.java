package com.echoexp4.ViewModels;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Repositories.AppRepository;
import com.echoexp4.Repositories.ConnectionRepository;
import com.echoexp4.api.UserAPI;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<Message>> messages;
    private Contact contact;

    public MessageViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public void setViewModel(Application application) {
       // UserAPI userAPI = new UserAPI();

        //appRepository.getContact(contact);
        //messages = appRepository.getAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return messages;
    }

    public void setMessages(Contact contact){
        appRepository.setMessagesData(contact);
        this.contact = contact;
        this.messages = appRepository.getAllMessages(contact);

    }
    public void addMessage(Message message) {
       appRepository.addMessageToRoom(message);
    }

}