package com.echoexp4.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.echoexp4.Adapters.ChatAdapter;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.databinding.ActivityChatBinding;
import com.echoexp4.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private Contact contact;
    private List<Message> chatMessages;
    private ChatAdapter adapter;
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverData();
        init();
       // listenMessages();
    }

    private void init(){
        chatMessages = new ArrayList<>();
        adapter = new ChatAdapter(chatMessages);
        binding.chatRecyclerView.setAdapter(adapter);
    }


    private void sendMessage(){
        String contact = this.contact.getId();
        String content = binding.inputMessage.getText().toString();
        String date = new Date().toString();
        Message message = new Message(content,date,true,contact);
        db.allDao().addMessage(message);
        binding.inputMessage.setText(null);
    }




    /*
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null)
            return;
        if (value != null){
            int count = chatMessages.size();
            for (DocumentChange documentChange: value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    Message chatMessage = new Message();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDate(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObj = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObj.compareTo(obj2.dateObj));
            if (count == 0){
                adapter.notifyDataSetChanged();
            } else {
                adapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size()-1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };

    private void listenMessages(){
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, contact.id)
                .addSnapshotListener(eventListener);
        db.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, contact.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_SENDER_ID))
                .addSnapshotListener(eventListener);
    }
    */



    private Bitmap getBitmapFromEncodedString(String encodedImage){
        byte[] bytes;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            bytes = Base64.getDecoder().decode(encodedImage);
        } else {
            bytes = android.util.Base64.
                    decode(encodedImage, android.util.Base64.DEFAULT);
        }
       return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }



    private void
    loadReceiverData(){
        contact = (Contact) getIntent().getSerializableExtra(Constants.KEY_CONTACT);
        binding.textName.setText(contact.getName());

    }

    private void setListeners(){
        binding.ImageBack.setOnClickListener(e-> onBackPressed());
       // binding.layoutSend.setOnClickListener(e-> sendMessage());
    }

    private String getReadableDate(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}