package com.echoexp4.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Repositories.ContactRepository;
import com.echoexp4.Repositories.MessagesRepository;

import java.util.List;

public class LandscapeChatViewModel extends AndroidViewModel {
    private ContactRepository contactRepository;
    private MessagesRepository messagesRepository;
    private LiveData<List<Contact>> contacts;
    private LiveData<List<Message>> currentMessages;

    public LandscapeChatViewModel(@NonNull Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        contacts = contactRepository.getAllContacts();

    }

    public LiveData<List<Contact>> getAllContacts() {
        return contacts;
    }


    public void insertContact(Contact contact){
        contactRepository.insertContact(contact);
    }

    public LiveData<List<Message>> getAllMessages() {
        return currentMessages;
    }


    public void setMessages(Contact contact){
        this.messagesRepository = new MessagesRepository(getApplication(), contact);
        currentMessages = messagesRepository.getAllMessages();

    }
    public void addMessage(Message message) {
        messagesRepository.addMessage(message);
    }
}
