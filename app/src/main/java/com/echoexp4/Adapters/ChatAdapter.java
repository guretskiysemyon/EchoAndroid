package com.echoexp4.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.echoexp4.Database.Entities.Message;
import com.echoexp4.databinding.ItemContainerReceivedMessagesBinding;
import com.echoexp4.databinding.ItemContainerSentMessageBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> chatMessages;
    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new ReceivedMessageViewHolder(
                ItemContainerReceivedMessagesBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
        return new SentMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (chatMessages.get(position).isSent()){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).isSent())
            return VIEW_TYPE_SENT;
        return VIEW_TYPE_RECEIVED;
    }


    public void setChatMessages(List<Message> messages){
        this.chatMessages = messages;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        if (chatMessages != null)
            return chatMessages.size();
        return 0;
    }


    public static String setDate(String dateStr) {
        String inputString = dateStr;
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-M-dd'T'hh:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy/M/dd hh:mm");
        String reformattedStr = inputString;
        try {
            reformattedStr = myFormat.format(fromUser.parse(inputString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    static class  SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding icsm){
            super(icsm.getRoot());
            binding = icsm;
        }

        void setData(Message chatMessage){
            String message = chatMessage.getContent()+ "\n" + setDate(chatMessage.getCreated());
            binding.textMessage.setText(message);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedMessagesBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessagesBinding icrm){
            super(icrm.getRoot());
            binding = icrm;
        }

        void setData(Message chatMessage){
            String message = chatMessage.getContent()+ "\n" + setDate(chatMessage.getCreated());
            binding.textMessage.setText(message);
        }
    }
}
