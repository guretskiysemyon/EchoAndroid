package com.echoexp4.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.databinding.ItemContainerReceivedMessagesBinding;
import com.echoexp4.databinding.ItemContainerSentMessageBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Message> chatMessages;


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


    public void setChatMessages(List<Message> messages){
        this.chatMessages = messages;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    static class  SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding icsm){
            super(icsm.getRoot());
            binding = icsm;
        }

        void setData(Message chatMessage){
            String message = chatMessage.getContent()+ "\n" + chatMessage.getCrated();
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
            String message = chatMessage.getContent()+ "\n" + chatMessage.getCrated();
            binding.textMessage.setText(message);
        }
    }
}
