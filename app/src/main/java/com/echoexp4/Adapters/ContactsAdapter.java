package com.echoexp4.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.echoexp4.Database.Entities.Contact;
import com.echoexp4.UserListener;
import com.echoexp4.databinding.ContactItemBinding;

import java.util.Base64;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.UserViewHolder> {


    private final List<Contact> contacts;
    private final UserListener userListener;

    public ContactsAdapter(List<Contact> contacts, UserListener ul) {
        this.contacts = contacts;
        this.userListener = ul;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactItemBinding itemBinding = ContactItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        ContactItemBinding binding;

        UserViewHolder (ContactItemBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void setUserData(Contact contact){
            binding.textName.setText(contact.getName());
            binding.textName.setTextColor(Color.parseColor("#ffffff"));
            //TODO:last message
            binding.getRoot().setOnClickListener( e-> userListener.onUserClicked(contact));
            binding.imageProfile.setImageBitmap(getUserImage(contact.getImage()));

        }
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

}
