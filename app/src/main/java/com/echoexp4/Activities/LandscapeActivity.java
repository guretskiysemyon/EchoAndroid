package com.echoexp4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.echoexp4.databinding.ActivityChatBinding;
import com.echoexp4.databinding.LandActivityLayoutBinding;

public class LandscapeActivity extends AppCompatActivity {
    private LandActivityLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LandActivityLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }



}