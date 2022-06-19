package com.echoexp4.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.echoexp4.Adapters.ContactsAdapter;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.UserListener;
import com.echoexp4.ViewModels.ContactView;
import com.echoexp4.databinding.ActivityMainBinding;
import com.echoexp4.utilities.Constants;

import java.util.List;


public class MainActivity extends AppCompatActivity implements UserListener {

    private ActivityMainBinding binding;
    private ContactView viewModel;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.adapter = new ContactsAdapter( this);

       viewModel = new ViewModelProvider(this).get(ContactView.class);
        //viewModel = new  ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ContactView.class);

        viewModel.getAllContacts().observe( this , new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        });
        getContacts();
        setListeners();

    }

    private void setListeners(){
        //binding.ImageBack.setOnClickListener(e -> onBackPressed());

        binding.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String username = data.getStringExtra("Username");
            String name = data.getStringExtra("Name");
            String server = data.getStringExtra("Server");

            Contact contact = new Contact(username,name,null,null,null, server);
            viewModel.insertContact(contact);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void getContacts(){
        loading(true);
        AppDB db = AppDB.getDbInstance(getApplicationContext());
        List<Contact> contacts = db.allDao().allContacts();
        loading(false);
        if(contacts.size() > 0){
            binding.ContactRecyclerView.setAdapter(this.adapter);
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