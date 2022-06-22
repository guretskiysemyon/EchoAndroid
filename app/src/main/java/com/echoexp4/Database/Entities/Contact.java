package com.echoexp4.Database.Entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Contacts")
public class Contact implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String last;
    private String lastdate;
    private String image;
    private String server;


    public Contact(@NonNull String id, String name, String image, String server) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.server = server;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLast() {
        return last;
    }

    public String getLastdate() {
        return lastdate;
    }

    public String getImage() {
        return image;
    }

    public String getServer() {
        return server;
    }
}
