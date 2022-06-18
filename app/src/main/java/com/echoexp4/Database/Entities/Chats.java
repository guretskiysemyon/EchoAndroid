package com.echoexp4.Database.Entities;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class Chats {
    @Embedded
    public Contact contact;
    @Relation(
            parentColumn = "id",
            entityColumn = "contactId"
    )
    public List<Message> messages;

}
