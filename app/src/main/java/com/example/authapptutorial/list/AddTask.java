package com.example.authapptutorial.list;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapptutorial.Calender;
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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
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

    private Button datePickerBtn;
    private DatePickerDialog datePickerDialog;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        initDatePicker();

        title = findViewById(R.id.inputtitle);
        details = findViewById(R.id.inputdetails);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        work = findViewById(R.id.workbutton);
        personal = findViewById(R.id.personalbutton);
        other = findViewById(R.id.otherbutton);
        datePickerBtn = findViewById(R.id.datePicker);
        datePickerBtn.setText(getTodaysDate());

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
            String date = datePickerBtn.getText().toString();

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

            task = new taskModel(taskDetails,taskTitle, userID, date);
            ArrayList<Object> list = new ArrayList<>();
            list.add(task);

          //  HashMap<String, Object> user = new HashMap<>();
          //  user.put("title", taskTitle);
          //  user.put("details",taskDetails);

            HashMap<String, String> user2 = new HashMap<>();
            //user2.put("list", list);
            user2.put("title", taskTitle);
            user2.put("details", taskDetails);
            user2.put("date",date);
            documentReference.set(task).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: task added successfully for user: " + userID)).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));


        });
    }

    private String getTodaysDate() {
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        month = month+1;
        int day = calender.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = makeDateString(dayOfMonth, month, year);
                datePickerBtn.setText(date);

            }
        };
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this,dateSetListener,year,month,day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {

        if(month ==1 ) return "JAN";
        if(month ==2) return "FEB";
        if(month ==3) return "MAR";
        if(month ==4) return "APR";
        if(month ==5) return "MAY";
        if(month ==6) return "JUN";
        if(month ==7) return "JUL";
        if(month ==8) return "AUG";
        if(month ==9) return "SEP";
        if(month ==10) return "OCT";
        if(month ==11) return "NOV";
        if(month ==12) return "DEC";
        //default jan should never happen
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }


}
