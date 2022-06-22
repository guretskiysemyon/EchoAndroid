package com.echoexp4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.echoexp4.Activities.LogInActivity;
import com.echoexp4.Activities.SettingsActivity;
import com.echoexp4.Database.AppDB;
import com.echoexp4.databinding.SettingsLayoutBinding;

import java.util.Collections;

public class SettingsFragment extends PreferenceFragmentCompat {

    private AppDB db;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        setListeners();
        this.db = AppDB.getDbInstance(getContext());

        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences
                = this.getActivity().getSharedPreferences(
                "sharedPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        SwitchPreference switchPreference = findPreference("theme_mode");

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchPreference.setTitle("Disable Dark Mode");

        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchPreference.setTitle("Enable Dark Mode");

        }

        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
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
                    switchPreference.setTitle("Enable Dark Mode");

                }
                else {

                    // if dark mode is off
                    // it will turn it on
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    // it will set isDarkModeOn
                    // boolean to true
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();

                    // change text of Button
                    switchPreference.setTitle("Disable Dark Mode");

                }
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                startActivity(intent);
                getActivity().finish();
                return true;
            }
        });


    }

    private void setListeners(){
        Preference myPref = (Preference) findPreference("log_out");
        //assert myPref != null;
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                logOut();
                return true;
            }
        });



    }

    private void logOut(){
        db.clearAllTables();
        Intent intent = new Intent(getContext(), LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        startActivity(intent);
    }
}