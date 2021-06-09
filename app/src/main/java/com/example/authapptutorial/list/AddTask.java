package com.example.authapptutorial.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapptutorial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.View;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class AddTask extends AppCompatActivity {

    taskModel task;
    EditText title, details;
    Button work, personal,other,cancel,save;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    DocumentReference documentReference;


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

        AlertDialog dialog;


        save = findViewById(R.id.savebutton);
        save.setOnClickListener(v-> {
            String taskTitle = title.getText().toString().trim();
            String taskDetails = details.getText().toString().trim();

            Intent i = new Intent(v.getContext(), List.class);
            startActivity(i);
            Toast.makeText(this,"Task successfully added", Toast.LENGTH_LONG).show();
            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
            documentReference = fStore.collection("tasks").document(taskTitle);

            //DocumentReference documentReference = fStore.collection("tasks").document(userID);
            //Map<String,Object> user = new HashMap<>();/
            //user.put("title", taskTitle);
            //user.put("detail", taskDetails);
            //documentReference.set(user).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: task added successfully for user: "+ userID)).addOnFailureListener(e -> Log.d(TAG,"onFailure: "+e.toString()));

            task = new taskModel(taskDetails,taskTitle, userID);
            ArrayList<Object> list = new ArrayList<>();
            list.add(task);

          //  HashMap<String, Object> user = new HashMap<>();
          //  user.put("title", taskTitle);
          //  user.put("details",taskDetails);

            HashMap<String, String> user2 = new HashMap<>();
            //user2.put("list", list);
            user2.put("title", taskTitle);
            user2.put("details", taskDetails);
            documentReference.set(task).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: task added successfully for user: " + userID)).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));


        });
    }
}
