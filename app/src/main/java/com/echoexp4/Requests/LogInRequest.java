package com.echoexp4.Requests;

public class LogInRequest {
    public String username;
    public String password;
    public String tokenFCM;

    public LogInRequest(String username, String password) {
        this.username = username;
        this.password = password;


    }
}

