package com.echoexp4.Database.Dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;

import java.util.List;

@Dao
public interface AllDao {

    @Transaction
    @Query("SELECT * FROM User")
    User connectedUser();

    @Transaction
    @Query("SELECT * FROM Contacts")
    List<Contact> allContacts();

    @Transaction
    @Query("SELECT * FROM Messages WHERE contactId=:id")
    List<Message> allMessages(String id);

    @Transaction
    @Insert
    void insertContacts(List<Contact> contacts);

    @Transaction
    @Insert
    void insertMessages(List<Message> messages);

    @Transaction
    @Insert
    void insertUser(User user);

    @Transaction
    @Update
    void addContact(Contact contact);

    @Transaction
    @Update
    void addMessage(Message message);





}
