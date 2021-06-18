package com.example.authapptutorial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authapptutorial.calendar.Calender;
import com.example.authapptutorial.list.ListTasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class ViewTaskDetails extends AppCompatActivity {

    ImageView cancelBtn;
    TextView taskTitle, taskDetails;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewtaskdetails);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        cancelBtn = findViewById(R.id.cancelBtn);
        taskTitle = findViewById(R.id.inputtitle);
        taskDetails = findViewById(R.id.inputdetails);

        cancelBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), ListTasks.class);
            startActivity(i);
        });

        taskTitle.setText(ListTasks.itemValue);
      //  taskDetails.setText(ListTasks.itemValue);
        String currentUser = fAuth.getCurrentUser().getUid();
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                .whereEqualTo("date", Calender.s)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<String> temp = new ArrayList<>();
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