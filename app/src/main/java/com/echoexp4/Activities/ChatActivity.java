package com.echoexp4.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.echoexp4.Adapters.ChatAdapter;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.ViewModels.ContactView;
import com.echoexp4.ViewModels.LandscapeChatViewModel;
import com.echoexp4.ViewModels.MessageViewModel;
import com.echoexp4.databinding.ActivityChatBinding;
import com.echoexp4.utilities.Constants;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private Contact contact;
    //private List<Message> chatMessages;
    private ChatAdapter adapter;
    private MessageViewModel viewModel;
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverData();
        adapter = new ChatAdapter();
        viewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        //viewModel = new  ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ContactView.class);
        viewModel.setMessages(this.contact);
        viewModel.getAllMessages().observe( this , messages -> adapter.setChatMessages(messages));

        init();
       // listenMessages();
    }

    private void init(){
        List<Message> messages = viewModel.getAllMessages().getValue();

            adapter.setChatMessages(messages);
            binding.chatRecyclerView.setAdapter(adapter);
            binding.chatRecyclerView.setVisibility(View.VISIBLE);

        binding.progressBar.setVisibility(View.INVISIBLE);
    }



    private void sendMessage() throws InterruptedException {

        //TODO: change contact name
        String contact = this.contact.getId();
        String content = binding.inputMessage.getText().toString();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date now = new Date();
        String date = now.toString();
        Message message = new Message(content,date,true,contact);
        viewModel.addMessage(message);
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


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Intent i = new Intent(this, LandscapeActivity.class);
            i.putExtra(Constants.KEY_CONTACT, contact);
            startActivity(i);
            finish();
        }
    }


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



    private void loadReceiverData(){
        contact = (Contact) getIntent().getSerializableExtra(Constants.KEY_CONTACT);
        binding.textName.setText(contact.getName());

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void setListeners(){
        binding.ImageBack.setOnClickListener(e-> {
            int orientation = getResources().getConfiguration().orientation;
            Intent intent;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                intent = new Intent(getApplicationContext(), LandscapeActivity.class);
                intent.putExtra(Constants.KEY_CONTACT, contact);
            }else {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(Constants.KEY_CONTACT, contact);
            }
            startActivity(intent);
        });
        binding.layoutSend.setOnClickListener(e-> {
            try {
                sendMessage();
            } catch (InterruptedException ex) {
                showToast("Error");
            }
        });

    }


}