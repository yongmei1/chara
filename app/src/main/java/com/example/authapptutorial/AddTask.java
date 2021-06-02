package com.example.authapptutorial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTask extends AppCompatActivity {

    TextView title, details;
    Button work, personal,other,cancel,save;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        work = findViewById(R.id.workbutton);
        personal = findViewById(R.id.personalbutton);
        other = findViewById(R.id.otherbutton);

        cancel = findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(v-> {
            Intent i = new Intent(v.getContext(), List.class);
            startActivity(i);
        });


        save = findViewById(R.id.savebutton);
        save.setOnClickListener(v-> {
            Intent i = new Intent(v.getContext(), List.class);
            startActivity(i);
            Toast.makeText(this,"Task successfully added", Toast.LENGTH_LONG).show();
        });

    }
}
