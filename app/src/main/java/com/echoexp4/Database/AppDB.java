package com.echoexp4.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;

import java.util.ArrayList;
import java.util.List;


@Database(entities = {Contact.class, Message.class, User.class}, version  = 4)
public abstract class AppDB extends RoomDatabase {

    private static AppDB INSTANCE;

    public abstract AllDao allDao();

    public static AppDB getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDB.class, "EchoDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            if (INSTANCE.allDao().allContacts().size()==0){
                List<Contact> contacts = new ArrayList<>();
                contacts.add(new Contact("Borys","Borys", null, null,null,null));
                contacts.add(new Contact("Borys1","Borys", null, null,null,null));
                contacts.add(new Contact("Borys2","Borys", null, null,null,null));
                contacts.add(new Contact("Borys3","Borys", null, null,null,null));
                INSTANCE.allDao().insertContacts(contacts);
            }
        }
        return INSTANCE;
    }

}
