package com.example.authapptutorial.main_navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.authapptutorial.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainChatbot extends AppCompatActivity {

    private Button button;
    private EditText usermessagebox;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";


    @SuppressLint({"NonConstantResourceId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chatbot);

        button = findViewById(R.id.button);
        //initialise and assign variable
        BottomNavigationView bottomNav = findViewById(R.id.menu_navigation);
        //set chatbot main selected
        bottomNav.setSelectedItemId(R.id.chatbot);

        //perform itemselectedlistner
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), Account.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.list:
                    startActivity(new Intent(getApplicationContext(), List.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.chatbot:
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), Library.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });


    button.setOnClickListener(v -> {
        startActivity(new Intent(this, MainChatbot2.class));
        finish();
    });
   // finish();

}
}

