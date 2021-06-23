package com.example.authapptutorial.list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapptutorial.EditAccount;
import com.example.authapptutorial.Login;
import com.example.authapptutorial.R;
import com.example.authapptutorial.main_navigation.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Objects;


public class ViewTaskDetails extends AppCompatActivity {

    ImageView cancelBtn;
    Button deleteBtn, editBtn;
    TextView taskTitle, taskDetails, taskType;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    public String taskID;

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
        taskType = findViewById(R.id.inputtype);

        taskTitle.setText(ListTasks.itemValue);

        cancelBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), ListTasks.class);
            startActivity(i);
        });

        //important for accessing taskid from delete function in viewtaskdetails class
        fStore.collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private final Object TAG = null;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d((String) TAG, "fffffffffff "+document.getId() + " => " + document.getData());
                                taskID = document.getId();
                            }
                        } else {
                            Log.d((String) TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
       deleteBtn.setOnClickListener(v ->{
            //remove item on firebase, then remove item in list
            final EditText deleteItem = new EditText(v.getContext());
            final AlertDialog.Builder deleteItemDialog = new AlertDialog.Builder(v.getContext());
            deleteItemDialog.setTitle("Are you sure you want to delete this task?");
            deleteItemDialog.setView(deleteItem);


           //find another way to get itemvalue cause its clearly static which wont work in our favour
           String P = ListTasks.itemValStore;
           System.out.println("ITEMVALUEEEEEEEEEE "+P);
           CollectionReference docRef = fStore.collection("tasks");
           fStore.collection("tasks")
                   .whereEqualTo("taskName", P)
                   .get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       private final Object TAG = null;

                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                   Log.d((String) TAG, "gggggggggg "+document.getId() + " => " + document.getData());
                                   taskID = document.getId();

                               }
                           } else {
                               Log.d((String) TAG, "Error getting documents: ", task.getException());
                           }
                       }
                   });


            deleteItemDialog.setPositiveButton("Yes", (dialog, which) -> {
                fStore.collection("tasks").document(taskID)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ViewTaskDetails.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ViewTaskDetails.this, "Task failed to delete", Toast.LENGTH_SHORT).show();
                        });
                startActivity(new Intent(getApplicationContext(), ListTasks.class));
            });

            deleteItemDialog.setNegativeButton("No", (dialog, which) -> {
            });
            deleteItemDialog.create().show();
        });

        editBtn.setOnClickListener(v ->{
            //edit, update in firebase, then list maybe
            Intent i = new Intent(v.getContext(), EditTask.class);
            startActivity(i);


        });

        String currentUser = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                //.whereEqualTo("date", Calender.s)
                .whereEqualTo("taskName", taskTitle.getText())
               // .whereEqualTo("taskType", taskType.getText())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            System.out.println("deets " + document.get("taskDetails"));
                            String details = Objects.requireNonNull(document.get("taskDetails")).toString();
                            String type = Objects.requireNonNull(document.get("taskType")).toString();
                            taskDetails.setText(details);
                            taskType.setText(type);
                        }

                        FirebaseAuth fAuth;
                        fAuth = FirebaseAuth.getInstance();
                        if (fAuth.getCurrentUser() == null) {
                            startActivity(new Intent(getApplication(), Login.class));
                            finish();
                        }
                    }});}}

