package com.echoexp4.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.echoexp4.Database.Dao.AllDao;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;


@Database(entities = {Contact.class, Message.class, User.class}, version  = 13)
public abstract class AppDB extends RoomDatabase {

    private static AppDB INSTANCE;

    public abstract AllDao allDao();

    public static AppDB getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDB.class, "EchoDB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return INSTANCE;
    }


}
