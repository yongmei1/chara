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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.authapptutorial.R;
import com.example.authapptutorial.main_navigation.List;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class EditTask extends AppCompatActivity {

    taskModel task;
    EditText title, details;
    Button save;
    ImageView cancel;
    CheckBox work, personal,other;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public String taskType;
    public String taskID;

    private Button datePickerBtn;
    private DatePickerDialog datePickerDialog;

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittask);
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

        cancel = findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(v-> {
            Intent i = new Intent(v.getContext(), ListTasks.class);
            startActivity(i);
            finish();
        });


        work.setOnClickListener(v -> {
            if (work.isChecked()){
                taskType="work";
                if(work.isChecked()&&personal.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(work.isChecked()&&other.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&&personal.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
            }
        });
        personal.setOnClickListener(v -> {
            if (personal.isChecked()){
                taskType="personal";
                if(work.isChecked()&&personal.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(work.isChecked()&&other.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&&personal.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
            }
        });
        other.setOnClickListener(v -> {
            if (other.isChecked()){
                taskType="other";
                if(work.isChecked()&&personal.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(work.isChecked()&&other.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
                if(other.isChecked()&&personal.isChecked()){
                    Toast.makeText(EditTask.this, "Please choose one option only",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //find another way to get itemvalue cause its clearly static which wont work in our favour
        String P = ListTasks.itemValStore;
        System.out.println("ITEMVALUEEEEEEEEEE "+P);

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


        save = findViewById(R.id.save);
        save.setOnClickListener(v-> {
            String taskTitle = title.getText().toString().trim();
            String taskDetails = details.getText().toString().trim();
            String date = datePickerBtn.getText().toString();

            startActivity(new Intent(getApplicationContext(), List.class));
            Toast.makeText(this,"Task successfully added", Toast.LENGTH_LONG).show();
            userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

            task = new taskModel(taskDetails,taskTitle, userID, date, taskType);
            ArrayList<Object> list = new ArrayList<>();
            list.add(task);

            HashMap<String, String> user2 = new HashMap<>();
            user2.put("title", taskTitle);
            user2.put("details", taskDetails);
            user2.put("date",date);
            user2.put("taskType",taskType);

            Map<String, Object> editedTask = new HashMap<>();
            editedTask.put("taskName", title.getText().toString());
            editedTask.put("taskDetails", details.getText().toString());
            editedTask.put("date",date);
            editedTask.put("taskType",taskType);
            editedTask.put("userid",userID);
            System.out.println(userID);


            //get taskID from onclick before this in list task page - list page
            //THIS P value gets taskName for us
            //now all we have to do is filter it with firestore to find the id with this taskName
            //maybe get the collection to get more information instead of getting documents
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
                                  /*  docRef.set("taskName", title.getText().toString(),
                                            "taskDetails", details.getText().toString(),
                                            "date",date,
                                            "taskType",taskType,
                                            "userID", userID);
                                            */
                                    taskModel task1 = new taskModel(taskDetails,taskTitle, userID, date, taskType);
                                    fStore.collection("tasks").document(taskID)
                                            .set(task1)
                                            .addOnSuccessListener(aVoid -> Log.d((String) TAG, "DocumentSnapshot successfully written!"))
                                            .addOnFailureListener(e -> Log.w((String) TAG, "Error writing document", e));
                                }
                            } else {
                                Log.d((String) TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            /*
            fStore.collection("tasks").whereEqualTo("taskName", P).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> System.out.println("taskNameeehere "+ queryDocumentSnapshots.getQuery()))
                    .addOnFailureListener(e -> System.out.println("fail message"));

                          /*  docRef.update("taskName", title.getText().toString(),
                                    "taskDetails", details.getText().toString(),
                                    "date",date,
                                    "taskType",taskType*/
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

