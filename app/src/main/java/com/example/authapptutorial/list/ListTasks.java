package com.example.authapptutorial.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.authapptutorial.AddTask;
import com.example.authapptutorial.Login;
import com.example.authapptutorial.R;
import com.example.authapptutorial.calendar.Calender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class ListTasks extends AppCompatActivity {

    public  ImageView deleteBtn, addBtn, taskDiagnostics, cancelBtn;
    public  ListView listView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    public  TextView todays_date, numTasks;
    public static String size;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtasks);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        listView= (ListView)findViewById(R.id.listview);
        addBtn = findViewById(R.id.addTaskBtn);
        taskDiagnostics = findViewById(R.id.taskDiagnostics);
        deleteBtn = findViewById(R.id.deleteTaskBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        ArrayList<String> storeTasks = new ArrayList<>();

        Calender c = new Calender();
        c.getDate(Calender.s);
     //   System.out.println("ssss "+ Calender.s);
        todays_date = findViewById(R.id.todays_date);
        todays_date.setText(Calender.s);
        numTasks = findViewById(R.id.numTasks);
        numTasks.setText("You have "+size+ " tasks due today");

        String currentUser = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "current user: "+currentUser);
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                .whereEqualTo("date", Calender.s)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<String> temp = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                             storeTasks.add(document.getId());
                             temp.add(document.getId());
                        }

                    int t=temp.size();
                    System.out.println("tttttttt "+t);
                    size = Integer.toString(t);

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, storeTasks);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(ListTasks.this,"clicked item:" + position+ " "+storeTasks.get(position),Toast.LENGTH_SHORT).show());
                });

        cancelBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), Calender.class);
            startActivity(i);
        });

        addBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), AddTask.class);
            startActivity(i);
        });

        FirebaseAuth fAuth;
        fAuth =  FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplication(), Login.class));
            finish();
        }
    }
}

