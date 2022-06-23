package com.echoexp4.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.echoexp4.Repositories.ConnectionRepository;
import com.echoexp4.Requests.LogInRequest;
import com.echoexp4.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


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