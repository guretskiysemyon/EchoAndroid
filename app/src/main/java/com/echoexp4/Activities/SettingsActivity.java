package com.echoexp4.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.echoexp4.R;
import com.echoexp4.SettingsFragment;
import com.echoexp4.databinding.SettingsLayoutBinding;


public class SettingsActivity extends AppCompatActivity {

    private SettingsLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // below line is used to check if
        // frame layout is empty or not.
        if (savedInstanceState != null) {
            return;
        }
        // below line is to inflate our fragment.
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
        setListeners();
    }

    private void setListeners(){
        binding.ImageBack.setOnClickListener(e-> {
            Intent i  = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

}


