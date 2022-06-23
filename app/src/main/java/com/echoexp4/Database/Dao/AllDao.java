package com.echoexp4.Database.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

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

   // SELECT * from table ORDER BY entity ASC.
    @Query("SELECT * FROM Contacts")
    LiveData<List<Contact>> allContacts();

    @Transaction
    @Query("SELECT * FROM Messages WHERE contactId=:id")
    LiveData<List<Message>> allMessages(String id);

    @Transaction
    @Query("DELETE FROM CONTACTS")
    void deleteContacts();

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
    @Query("UPDATE contacts SET last=:last, lastdate=:lastdate WHERE id=:contactId")
    void updateContact(String last, String lastdate, String contactId);

    @Transaction
    @Insert
    void addMessage(Message message);

    @Transaction
    @Query("SELECT * FROM user")
    User getUser();




}
