package com.echoexp4.Database.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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
    @Query("DELETE FROM CONTACTS")
    void deleteContacts();


    @Transaction
    @Delete
    void deleteContact(Contact contact);

    @Transaction
    @Update
    void changeContact(Contact contact);


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContacts(List<Contact> contacts);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<Message> messages);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Transaction
    @Insert
    void addContact(Contact contact);

    @Transaction
    @Insert
    void addMessage(Message message);





}
