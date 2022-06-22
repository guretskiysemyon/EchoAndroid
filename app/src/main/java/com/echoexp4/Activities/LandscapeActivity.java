package com.echoexp4.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.echoexp4.Adapters.ChatAdapter;
import com.echoexp4.Adapters.ContactsAdapter;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.Database.Entities.Message;
import com.echoexp4.Database.Entities.User;
import com.echoexp4.UserListener;
import com.echoexp4.ViewModels.LandscapeChatViewModel;
import com.echoexp4.ViewModels.MessageViewModel;
import com.echoexp4.databinding.ActivityChatBinding;
import com.echoexp4.databinding.LandActivityLayoutBinding;
import com.echoexp4.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class LandscapeActivity extends AppCompatActivity implements UserListener {
    private LandActivityLayoutBinding binding;
    private ContactsAdapter contactAdapter;
    private ChatAdapter chatAdapter;
    private LandscapeChatViewModel viewModel;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LandActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.contactAdapter = new ContactsAdapter(this);
        this.chatAdapter = new ChatAdapter();

        loadReceiverData();
        viewModel =  new ViewModelProvider(this).get(LandscapeChatViewModel.class);
        viewModel.setMessages(this.contact);

        viewModel.getAllContacts().observe( this , contacts -> contactAdapter.setContacts(contacts));
        viewModel.getAllMessages().observe(this, messages -> chatAdapter.setChatMessages(messages));

        getContacts();
        setListeners();
        init();
    }

    private void loadReceiverData(){
        contact = (Contact) getIntent().getSerializableExtra(Constants.KEY_CONTACT);
        binding.textName.setText(contact.getName());

    }


    private void init(){
        List<Message> messages = viewModel.getAllMessages().getValue();
        if (messages != null) {
            chatAdapter.setChatMessages(messages);
            binding.chatRecyclerView.setAdapter(chatAdapter);
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBarChat.setVisibility(View.INVISIBLE);
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


    private void setListeners(){
        //binding.ImageBack.setOnClickListener(e -> onBackPressed());
        binding.ImageClose.setOnClickListener(e-> {
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra(Constants.KEY_CONTACT, contact);
            startActivity(i);
            finish();
        });
        binding.layoutSend.setOnClickListener(e-> {
            try {
                sendMessage();
            } catch (InterruptedException ex) {
                showToast("Error");
            }
        });

        binding.addContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandscapeActivity.this, AddContactActivity.class);
            startActivityForResult(intent, 1);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String username = data.getStringExtra("Username");
            String name = data.getStringExtra("Name");
            String server = data.getStringExtra("Server");

            Contact contact = new Contact(username,name,null, server);
            viewModel.insertContact(contact);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra(Constants.KEY_CONTACT, contact);
            startActivity(i);
            finish();
        }
    }

    private void getContacts(){
        loading(true);
        AppDB db = AppDB.getDbInstance(getApplicationContext());
        List<Contact> contacts = db.allDao().allContacts().getValue();
        loading(false);
        if(contacts != null){
            binding.ContactRecyclerView.setAdapter(this.contactAdapter);
            binding.ContactRecyclerView.setVisibility(View.VISIBLE);
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "You have no contacts yet"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.progressBarContact.setVisibility(View.VISIBLE);
        }else{
            binding.progressBarContact.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserClicked(Contact contact) {
        this.contact = contact;
        viewModel.setMessages(contact);
        chatAdapter.setChatMessages(viewModel.getAllMessages().getValue());
    }
}