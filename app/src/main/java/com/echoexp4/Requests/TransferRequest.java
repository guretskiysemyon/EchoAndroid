package com.echoexp4.Requests;

public class TransferRequest {
    public String from;
    public String to;
    public String content;

    public TransferRequest(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }
}
