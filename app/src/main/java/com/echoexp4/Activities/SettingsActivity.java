package com.echoexp4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.echoexp4.R;
import com.echoexp4.SettingsFragment;
import com.echoexp4.databinding.ActivityChatBinding;


public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // below line is used to check if
        // frame layout is empty or not.
        if (findViewById(R.id.container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // below line is to inflate our fragment.
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
        }
    }
}


