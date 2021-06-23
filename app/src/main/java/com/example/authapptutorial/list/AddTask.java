package com.example.authapptutorial.list;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authapptutorial.main_navigation.List;
import com.example.authapptutorial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class AddTask extends AppCompatActivity {

    taskModel task;
    EditText title, details;
    Button cancel,save;
    CheckBox work, personal,other;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public String taskType;
    CollectionReference documentReference;
    private Button datePickerBtn;
    private DatePickerDialog datePickerDialog;


    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        initDatePicker();

        title = findViewById(R.id.inputtitle);
        details = findViewById(R.id.inputdetails);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        work = findViewById(R.id.work);
        personal = findViewById(R.id.personal);
        other = findViewById(R.id.other);
        datePickerBtn = findViewById(R.id.datePicker);
        datePickerBtn.setText(getTodaysDate());

        cancel = findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(v-> {
            Intent i = new Intent(v.getContext(), List.class);
            startActivity(i);
        });



        work.setOnClickListener(v -> {
            if (work.isChecked()){
                taskType="work";
                if(work.isChecked()&&personal.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(work.isChecked()&&other.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&&personal.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
            }
        });
        personal.setOnClickListener(v -> {
            if (personal.isChecked()){
                taskType="personal";
                if(work.isChecked()&&personal.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(work.isChecked()&&other.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&&personal.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
            }
        });
        other.setOnClickListener(v -> {
            if (other.isChecked()){
                taskType="other";
                if(work.isChecked()&&personal.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(work.isChecked()&&other.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&&personal.isChecked()){
                    Toast.makeText(AddTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
            }
        });



        save = findViewById(R.id.savebutton);
        save.setOnClickListener(v-> {
            String taskTitle = title.getText().toString().trim();
            String taskDetails = details.getText().toString().trim();
            String date = datePickerBtn.getText().toString();

            startActivity(new Intent(getApplicationContext(), List.class));
            Toast.makeText(this,"Task successfully added", Toast.LENGTH_LONG).show();
            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
         //   documentReference = fStore.collection("tasks").document(taskTitle);
            documentReference = fStore.collection("tasks");
            task = new taskModel(taskDetails,taskTitle, userID, date, taskType);
            ArrayList<Object> list = new ArrayList<>();
            list.add(task);

            HashMap<String, String> user2 = new HashMap<>();
            user2.put("taskName", taskTitle);
            user2.put("taskDetails", taskDetails);
            user2.put("date",date);
            user2.put("taskType",taskType);
            user2.put("userid",userID);
            documentReference.add(task).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: task added successfully for user: " + userID)).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));

        });
    }

    public String getTodaysDate() {
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        month = month+1;
        int day = calender.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            month = month+1;
            String date = makeDateString(dayOfMonth, month, year);
            datePickerBtn.setText(date);

        };
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this,dateSetListener,year,month,day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return dayOfMonth+ " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month ==1 ) return "January";
        if(month ==2) return "February";
        if(month ==3) return "March";
        if(month ==4) return "April";
        if(month ==5) return "May";
        if(month ==6) return "June";
        if(month ==7) return "July";
        if(month ==8) return "August";
        if(month ==9) return "September";
        if(month ==10) return "October";
        if(month ==11) return "November";
        if(month ==12) return "December";
        //default jan should never happen
        return "January";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

}
