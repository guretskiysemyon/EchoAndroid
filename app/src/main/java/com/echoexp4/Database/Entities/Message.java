package com.echoexp4.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "Messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String crated;
    private boolean sent;

    private String contactId;

    public Message( String content, String crated, boolean sent, String contactId) {
        this.content = content;
        this.crated = crated;
        this.sent = sent;
        this.contactId = contactId;
    }


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCrated() {
        return crated;
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

    public void setCrated(String crated) {
        this.crated = crated;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
