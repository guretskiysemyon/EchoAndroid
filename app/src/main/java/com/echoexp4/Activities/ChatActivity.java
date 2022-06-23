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
import androidx.lifecycle.ViewModelProvider;

import com.echoexp4.Adapters.ChatAdapter;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.ViewModels.MessageViewModel;
import com.echoexp4.databinding.ActivityChatBinding;
import com.echoexp4.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private Contact contact;
    private ChatAdapter adapter;
    private MessageViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverData();
        adapter = new ChatAdapter();
        viewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        viewModel.setMessages(this.contact);
        viewModel.getAllMessages().observe( this , messages -> adapter.setChatMessages(messages));

        init();

    }

    private void init(){
        List<Message> messages = viewModel.getAllMessages().getValue();

            adapter.setChatMessages(messages);
            binding.chatRecyclerView.setAdapter(adapter);
            binding.chatRecyclerView.setVisibility(View.VISIBLE);

        binding.progressBar.setVisibility(View.INVISIBLE);
    }



    private void sendMessage() throws InterruptedException {
        String contact = this.contact.getId();
        String content = binding.inputMessage.getText().toString();
        String pattern = "yyyy-M-dd'T'hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        Message message = new Message(content,date,true,contact);
        viewModel.addMessage(message);
        binding.inputMessage.setText(null);
    }




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