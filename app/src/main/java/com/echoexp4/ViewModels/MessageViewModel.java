package com.echoexp4.ViewModels;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Repositories.ContactRepository;
import com.echoexp4.Repositories.MessagesRepository;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private ContactRepository contactRepository;

    private MessagesRepository repo;
    private LiveData<List<Message>> messages;


    public MessageViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Message>> getAllMessages() {
        return messages;
    }

    public void setMessages(Contact contact){
        this.repo = new MessagesRepository(getApplication(), contact);
        messages = repo.getAllMessages();

    }
    public void addMessage(Message message) throws InterruptedException {
       repo.addMessage(message);
    }


}