package com.example.authapptutorial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authapptutorial.calendar.Calender;
import com.example.authapptutorial.list.List;
import com.example.authapptutorial.list.ListTasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class ViewTaskDetails extends AppCompatActivity {

    ImageView cancelBtn;
    Button deleteBtn, editBtn;
    TextView taskTitle, taskDetails;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtaskdetails);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        cancelBtn = findViewById(R.id.cancelBtn);
        editBtn = findViewById(R.id.edit);
        deleteBtn = findViewById(R.id.delete);
        taskTitle = findViewById(R.id.inputtitle);
        taskDetails = findViewById(R.id.inputdetails);

        taskTitle.setText(ListTasks.itemValue);

        cancelBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), ListTasks.class);
            startActivity(i);
        });

        editBtn.setOnClickListener(v ->{
            //edit task, update in list and update in firebase
        });

        deleteBtn.setOnClickListener(v ->{
            //delete item in list and remove item on firebase
        });


        String currentUser = fAuth.getCurrentUser().getUid();
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                //.whereEqualTo("date", Calender.s)
                .whereEqualTo("taskName", taskTitle.getText())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            System.out.println("deets " + document.get("taskDetails"));
                            String details = Objects.requireNonNull(document.get("taskDetails")).toString();
                            taskDetails.setText(details);
                        }

                        FirebaseAuth fAuth;
                        fAuth = FirebaseAuth.getInstance();
                        if (fAuth.getCurrentUser() == null) {
                            startActivity(new Intent(getApplication(), Login.class));
                            finish();
                        }
                    }});}}

