package com.rmuttproject.bios.computer_science_assistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class Setting extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String Usernamepref = "edit_text_preference_1";
    public static final String listpref_text_size = "list_preference_text_size";
    public static final String listpref_sound = "list_sound";
    public static final String listBG = "listpref_bg";

    public Preference Usernamepreference;
    public Preference tspreference;
    public Preference soundpreference;
    public Preference BGpreference;

    public  SharedPreferences sp;
    public String Username;
    public String sizetext;
    public String sound;
    public String background;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Username = sp.getString(Usernamepref,"User");
        sizetext = sp.getString(listpref_text_size,"กลาง");
        sound = sp.getString(listpref_sound,"เสียง1");
        background = sp.getString(listBG,"Normal");

        Usernamepreference = findPreference(Usernamepref);
        tspreference = findPreference(listpref_text_size);
        soundpreference = findPreference(listpref_sound);
        BGpreference = findPreference(listBG);

        Usernamepreference.setSummary(Username);
        tspreference.setSummary(sizetext.toString());
        soundpreference.setSummary(sound.toString());
        BGpreference.setSummary(background.toString());


    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

        @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    //****************************  Menu Toolbar **********************************
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_for_setting, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case R.id.action_main:{
//                finish();
//                break;
//            }
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }



    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        finish();
        overridePendingTransition(0,0);
        startActivity(getIntent());
        overridePendingTransition(0,0);
    }
}
