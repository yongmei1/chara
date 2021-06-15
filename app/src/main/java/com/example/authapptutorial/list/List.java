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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapptutorial.Account;
import com.example.authapptutorial.AddTask;
import com.example.authapptutorial.Library;
import com.example.authapptutorial.Login;
import com.example.authapptutorial.MainChatbot;
import com.example.authapptutorial.R;
import com.example.authapptutorial.calendar.Calender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class List extends AppCompatActivity {
    ImageView calendarBtn, addBtn, taskDiagnostics;
    TextView todays_date, numTasks;
    public static String size;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ListView listView;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"NonConstantResourceId", "WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        listView= (ListView)findViewById(R.id.listview);
        ArrayList<String> storeTasks = new ArrayList<>();

        LocalDate date = LocalDate.now();
        System.out.println("ddddddd "+date);

        String[] parts = date.toString().split("-");
        String part1=parts[0];
        String part2=parts[1];
        String part3=parts[2];
      //  System.out.println("SSSSS "+part3);
        todays_date = findViewById(R.id.todays_date);
        todays_date.setText(part3+" "+Calender.monthYearFromDate(date));

        numTasks = findViewById(R.id.numTasks);
        numTasks.setText("You have "+size+ " tasks due today");


        String currentUser = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "current user: "+currentUser);
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                .whereEqualTo("date", Calender.s)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(List.this,"clicked item:" + position+ " "+storeTasks.get(position),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        addBtn = findViewById(R.id.addTaskBtn);
        addBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), AddTask.class);
            startActivity(i);
        });

        calendarBtn = findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), Calender.class);
            startActivity(i);
        });


        FirebaseAuth fAuth;
        fAuth =  FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplication(), Login.class));
            finish();
        }

        //initialise and assign variable
        BottomNavigationView bottomNav = findViewById(R.id.menu_navigation);
        //set chatbot main selected
        bottomNav.setSelectedItemId(R.id.list);

        //perform itemselectedlistner
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.account:
                    startActivity(new Intent(getApplicationContext(), Account.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.list:
                    return true;
                case R.id.chatbot:
                    startActivity(new Intent(getApplicationContext(), MainChatbot.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.library:
                    startActivity(new Intent(getApplicationContext(), Library.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
}