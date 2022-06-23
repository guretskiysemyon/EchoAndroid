package com.echoexp4.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.echoexp4.Repositories.ConnectionRepository;
import com.echoexp4.Requests.SignUpRequest;
import com.echoexp4.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

public class SignUpActivity extends ConnectionActivity {
    private ActivitySignUpBinding binding;
    private String encodedImage;
    private ConnectionRepository repository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        repository = new ConnectionRepository(getApplicationContext(),this);
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners(){
        binding.LogIn.setOnClickListener(e -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(e -> {
            if (isValidDataDetails())
                signUp();
        });
        binding.layoutImage.setOnClickListener(e ->{

            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            pickImage.launch(i);

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        });
    }

    private void signUp(){
        loading(true);
        SignUpRequest signUpRequest = new SignUpRequest(
                binding.InputUsername.getText().toString(),
                binding.InputPassword.getText().toString(),
                binding.InputName.getText().toString(),
                encodedImage);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignUpActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                signUpRequest.tokenFCM = instanceIdResult.getToken();
                repository.SignUp(signUpRequest);
            }
        });
        /*
        repository.SignUp(new SignUpRequest(
                binding.InputUsername.getText().toString(),
                binding.InputPassword.getText().toString(),
                binding.InputName.getText().toString(),
                encodedImage
        ));
        */
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_USERNAME,binding.InputUsername.getText().toString());
        user.put(Constants.KEY_NAME, binding.InputName.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.InputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        db.collection(Constants.KEY_COLLECTION_USER)
                .add(user)
                .addOnSuccessListener( documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_LOGED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.InputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                      loading(false);
                      showToast(exception.getMessage());
                });

         */

    }

    private String encodeImage(Bitmap bm){
        int previewWidth = 150;
        int previewHeight = bm.getHeight() * previewWidth / bm.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bm, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteOutStream);
        byte[] bytes = byteOutStream.toByteArray();
        String s = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            s = Base64.getEncoder().encodeToString(bytes);
        } else {
            s = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
        }
        return s;
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.profileImage.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    private  Boolean isValidDataDetails(){
       if (binding.InputName.getText().toString().trim().isEmpty()){
            showToast("Enter name");
            return false;
        } else if (binding.InputName.getText().toString().trim().isEmpty()){
            showToast("Enter password");
            return false;
        } else if (binding.InputUsername.getText().toString().trim().isEmpty()){
            showToast("Enter username");
            return false;
        }else if (binding.InputPasswordVal.getText().toString().trim().isEmpty()){
            showToast("Confirm password");
            return false;
        } else if (!binding.InputPassword.getText().toString().equals(binding.InputPasswordVal.getText().toString())){
            showToast("Passwords doesn't match");
            return false;
        }
        return true;
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }


}