package com.example.authapptutorial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddTask extends AppCompatActivity {

    EditText title, details;
    Button work, personal,other,cancel,save;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    String userID;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);

        title = findViewById(R.id.inputtitle);
        details = findViewById(R.id.inputdetails);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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
            String taskTitle = title.getText().toString().trim();
            String taskDetails = details.getText().toString().trim();

            Intent i = new Intent(v.getContext(), List.class);
            startActivity(i);
            Toast.makeText(this,"Task successfully added", Toast.LENGTH_LONG).show();
            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
            DocumentReference documentReference = fStore.collection("tasks").document(userID);
            Map<String,Object> user = new HashMap<>();
            user.put("title", taskTitle);
            user.put("detail", taskDetails);
            documentReference.set(user).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: task added successfully for user: "+ userID)).addOnFailureListener(e -> Log.d(TAG,"onFailure: "+e.toString()));

        });

    }
}
