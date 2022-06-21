package com.echoexp4.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;

import com.echoexp4.Repositories.ConnectionRepository;
import com.echoexp4.Requests.LogInRequest;
import com.echoexp4.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


public class LogInActivity extends ConnectionActivity {
    private ActivityLogInBinding binding;
    private ConnectionRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new ConnectionRepository(getApplicationContext(), this);
        setListeners();

        /*

        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            binding.buttonLogIn.setText(
                    "Disable Dark Mode");
        }
        else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            binding.buttonLogIn
                    .setText(
                            "Enable Dark Mode");
        }

        binding.buttonLogIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // When user taps the enable/disable
                        // dark mode button
                        if (isDarkModeOn) {

                            // if dark mode is on it
                            // will turn it off
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_NO);
                            // it will set isDarkModeOn
                            // boolean to false
                            editor.putBoolean(
                                    "isDarkModeOn", false);
                            editor.apply();

                            // change text of Button
                            binding.buttonLogIn.setText(
                                    "Enable Dark Mode");
                        }
                        else {

                            // if dark mode is off
                            // it will turn it on
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_YES);

                            // it will set isDarkModeOn
                            // boolean to true
                            editor.putBoolean(
                                    "isDarkModeOn", true);
                            editor.apply();

                            // change text of Button
                            binding.buttonLogIn.setText(
                                    "Disable Dark Mode");
                        }
                    }
                }

        );*/

    }

    private void setListeners(){
        binding.SigUp.setOnClickListener(e ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.buttonLogIn.setOnClickListener(e->{
            if (isValidData())
                logIn();
        });
    }

    private void logIn(){
        loading(true);
        LogInRequest logInRequest = new LogInRequest(binding.InputUsername.getText().toString(),
                                        binding.InputPassword.getText().toString());
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LogInActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                logInRequest.tokenFCM = instanceIdResult.getToken();
                repository.LogIn(logInRequest);
            }
        });

    }



    private void loading(Boolean isLoading){
        if (isLoading){
            binding.SigUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            return;
        }
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.SigUp.setVisibility(View.VISIBLE);
    }



    private Boolean isValidData(){
        if (binding.InputUsername.getText().toString().trim().isEmpty()){
            showToast("Fill the username");
           return false;
        } else if (binding.InputPassword.getText().toString().trim().isEmpty()){
            showToast("Fill the password field, please");
            return false;
        }
        return true;
    }

}