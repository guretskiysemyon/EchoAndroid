package com.echoexp4;

public class SignUpRequest {
    public String username;
    public String password;
    public String name;
    public String image;

    public SignUpRequest(String username, String password, String name, String image) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.image = image;
    }
}
