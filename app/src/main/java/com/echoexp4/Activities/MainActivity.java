package com.echoexp4.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.echoexp4.Adapters.ContactsAdapter;
import com.echoexp4.Database.AppDB;
import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.UserListener;
import com.echoexp4.ViewModels.ContactView;
import com.echoexp4.databinding.ActivityMainBinding;
import com.echoexp4.utilities.Constants;

import java.util.Base64;
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
        binding.imageProfile.setImageBitmap(getUserImage(viewModel.getUser().getImage()));
        viewModel.getAllContacts().observe( this , contacts -> {
            adapter.setContacts(contacts);
        });
        getContacts();
        setListeners();

    }

    private void setListeners(){
        //binding.ImageBack.setOnClickListener(e -> onBackPressed());
        binding.imageSettings.setOnClickListener(e-> {
            Intent i  = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            finish();

        });
        binding.addContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivityForResult(intent, 1);

        });


    }

    private Bitmap getUserImage(String encodedImage){
        if (encodedImage == null){
            return null;
        }
        byte[] bytes;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            bytes = Base64.getDecoder().decode(encodedImage);
        } else {
            bytes = android.util.Base64.
                    decode(encodedImage, android.util.Base64.DEFAULT);
        }
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
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

            Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Contact not saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void getContacts(){
        loading(true);
        AppDB db = AppDB.getDbInstance(getApplicationContext());
        List<Contact> contacts = db.allDao().allContacts().getValue();
        loading(false);

            binding.ContactRecyclerView.setAdapter(this.adapter);
            binding.ContactRecyclerView.setVisibility(View.VISIBLE);

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
        int orientation = getResources().getConfiguration().orientation;
        Intent intent;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            intent = new Intent(getApplicationContext(), LandscapeActivity.class);
            intent.putExtra(Constants.KEY_CONTACT, contact);
        }else {
            intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_CONTACT, contact);
        }
        startActivity(intent);
        finish();
    }

}