package com.echoexp4.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.ViewModels.ContactView;
import com.echoexp4.databinding.ActivityAddContactBinding;
import com.echoexp4.databinding.ActivityMainBinding;

public class AddContactActivity extends AppCompatActivity {

    private ActivityAddContactBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.ImageBack.setOnClickListener(e -> onBackPressed());
        binding.buttonAddContact.setOnClickListener(e -> addContact());
    }


    private void addContact(){
        String username = binding.InputUsername.getText().toString();
        String name = binding.InputName.getText().toString();
        String server = binding.InputServer.getText().toString();

        if (username.trim().isEmpty() || name.trim().isEmpty()
                            || server.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra("Username", username);
        data.putExtra("Name", name);
        data.putExtra("Server", server);
        setResult(RESULT_OK, data);
        finish();
    }


}