package com.echoexp4.Requests;

public class InvitationRequest {
    public String from;
    public String to;
    public String server;

    public InvitationRequest(String from, String to, String server) {
        this.from = from;
        this.to = to;
        this.server = server;
    }
}
