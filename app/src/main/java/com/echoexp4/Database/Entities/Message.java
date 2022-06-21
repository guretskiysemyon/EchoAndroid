package com.echoexp4.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String created;
    private boolean sent;

    private String contactID;


    public Message(String content, String created, boolean sent, String contactID) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.sent = sent;
        this.contactID = contactID;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    public boolean isSent() {
        return sent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
