package com.example.authapptutorial.main_navigation;

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

import com.example.authapptutorial.Login;
import com.example.authapptutorial.R;
import com.example.authapptutorial.calendar.Calendar;
import com.example.authapptutorial.list.AddTask;
import com.example.authapptutorial.list.ViewTaskDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class List extends AppCompatActivity {
    ImageView calendarBtn, addBtn, taskDiagnostics;
    TextView todays_date, numTasks;
    public static String size;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ListView listView;
    public static String taskName;
    public static String itemValue;
    public static String itemValStore;
    public static int itemPosition;


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
        String mm=" ";
        String date = new SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(new Date());
        String[] arr = date.split(" ", 3);
        String day = arr[0];
        String year = arr[2];

        if(arr[1].equals("01")){mm= "January";}
        if(arr[1].equals("02")){mm= "February";}
        if(arr[1].equals("03")){mm= "March";}
        if(arr[1].equals("04")){mm= "April";}
        if(arr[1].equals("05")){mm= "May";}
        if(arr[1].equals("06")){mm= "June";}
        if(arr[1].equals("07")){mm= "July";}
        if(arr[1].equals("08")){mm= "August";}
        if(arr[1].equals("09")){mm= "September";}
        if(arr[1].equals("10")){mm= "October";}
        if(arr[1].equals("11")){mm= "November";}
        if(arr[1].equals("12")){mm= "December";}


        todays_date = findViewById(R.id.todays_date);
        String setDate = day + " "+ mm + " " + year;
        todays_date.setText(setDate);

       // numTasks = findViewById(R.id.numTasks);
   //     numTasks.setText("You have "+size+ " tasks due today");


        String currentUser = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "current user: "+currentUser);
        fStore.collection("tasks")
                .whereEqualTo("userid", currentUser)
                .whereEqualTo("date", setDate)
                .get()
                .addOnCompleteListener(task -> {
                    System.out.println("dae : "+ Calendar.s);
                    ArrayList<String> temp = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            taskName = document.get("taskName").toString();
                            storeTasks.add(taskName);
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

        addBtn = findViewById(R.id.addTaskBtn);
        addBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), AddTask.class);
            startActivity(i);
        });
        calendarBtn = findViewById(R.id.calendarBtn);
        calendarBtn.setOnClickListener(v ->{
            Intent i = new Intent(v.getContext(), Calendar.class);
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