package com.example.authapptutorial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class List extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //initialise and assign variable
        BottomNavigationView bottomNav = findViewById(R.id.menu_navigation);
        //set chatbot main selected
        bottomNav.setSelectedItemId(R.id.list);

        //perform itemselectedlistner
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), AccountFragment.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.list:
                    return true;
                case R.id.chatbot:
                    startActivity(new Intent(getApplicationContext(), MainChatbot.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), Library.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
}