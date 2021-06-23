package com.example.authapptutorial.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapptutorial.Login;
import com.example.authapptutorial.R;
import com.example.authapptutorial.calendar.Calendar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListTasks extends AppCompatActivity {

    public ImageView deleteBtn, addBtn, taskDiagnostics, cancelBtn;
    public ListView listView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    public TextView todays_date, numTasks;
    public static String size;
    public static String itemValue;
    public static String itemValStore;

    public static String details, title;
    public static int itemPosition;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtasks);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        listView = (ListView) findViewById(R.id.listview);
        addBtn = findViewById(R.id.addTaskBtn);
        taskDiagnostics = findViewById(R.id.taskDiagnostics);
        deleteBtn = findViewById(R.id.deleteTaskBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        ArrayList<String> storeTasks = new ArrayList<>();
        ArrayList<String> storeDetails = new ArrayList<>();

        Calendar c = new Calendar();
        c.getDate(Calendar.s);
        todays_date = findViewById(R.id.todays_date);
        todays_date.setText(Calendar.s);
        // numTasks = findViewById(R.id.numTasks);
        // numTasks.setText("You have "+size+ " tasks due today");

        String currentUser = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "current user: " + currentUser);
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                .whereEqualTo("date", Calendar.s)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<String> temp = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                             details = document.get("taskDetails").toString();
                             System.out.println("details :"+details);
                             storeDetails.add(document.get("taskDetails").toString());
                             title = document.get("taskName").toString();
                             System.out.println("title :"+title);
                             storeTasks.add(title);
                             temp.add(title);
                             System.out.println("id :"+document.getId());
                        }
                        int t = temp.size();
                        size = Integer.toString(t);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, storeTasks);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener((parent, v, position, id) -> {
                        itemPosition = position;
                        itemValue = (String) listView.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(),
                                "Position :" + itemPosition + " ListItem : " + itemValue, Toast.LENGTH_LONG)
                                .show();
                        itemValStore = itemValue;

                        Intent i = new Intent(getApplicationContext(), ViewTaskDetails.class);
                        startActivity(i);
                                });
                    });

                    cancelBtn.setOnClickListener(v -> {
                        Intent i = new Intent(v.getContext(), Calendar.class);
                        startActivity(i);
                    });

                    addBtn.setOnClickListener(v -> {
                        Intent i = new Intent(v.getContext(), AddTask.class);
                        startActivity(i);
                    });

                    FirebaseAuth fAuth;
                    fAuth = FirebaseAuth.getInstance();
                    if (fAuth.getCurrentUser() == null) {
                        startActivity(new Intent(getApplication(), Login.class));
                        finish();
                    }

}}
