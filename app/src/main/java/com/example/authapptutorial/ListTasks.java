package com.example.authapptutorial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.authapptutorial.list.AddTask;
import com.example.authapptutorial.list.taskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListTasks extends AppCompatActivity {

    ImageView deleteBtn, addBtn, taskDiagnostics, cancelBtn;
    ListView listView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

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

        String currentUser = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "current user: "+currentUser);
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                 storeTasks.add(document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                       /* for(int i=0;i<storeTasks.size();i++){
                            System.out.println("STORETASKS  ----------------"+storeTasks.get(i));
                        }*/

                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, storeTasks);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(ListTasks.this,"clicked item:" + position+ " "+storeTasks.get(position),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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

