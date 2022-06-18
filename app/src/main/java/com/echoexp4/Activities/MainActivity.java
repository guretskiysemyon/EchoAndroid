package com.echoexp4.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.echoexp4.Adapters.ContactsAdapter;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.UserListener;
import com.echoexp4.databinding.ActivityMainBinding;
import com.echoexp4.utilities.Constants;

import java.util.List;


public class MainActivity extends AppCompatActivity implements UserListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        getContacts();
    }

    private void setListeners(){
        binding.ImageBack.setOnClickListener(e -> onBackPressed());
    }


    private void getContacts(){
        loading(true);
        AppDB db = AppDB.getDbInstance(getApplicationContext());
        List<Contact> contacts = db.allDao().allContacts();
        loading(false);
        if(contacts.size() > 0){
            ContactsAdapter userAdapter = new ContactsAdapter(contacts, this);
            binding.userRecyclerView.setAdapter(userAdapter);
            binding.userRecyclerView.setVisibility(View.VISIBLE);
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
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(Contact contact) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_CONTACT, contact);
        startActivity(intent);
    }

}