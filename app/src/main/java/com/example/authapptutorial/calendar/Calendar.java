package com.example.authapptutorial.calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.authapptutorial.main_navigation.List;
import com.example.authapptutorial.Login;
import com.example.authapptutorial.R;
import com.example.authapptutorial.list.ListTasks;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Calendar extends AppCompatActivity implements CalendarAdaptor.OnItemListener {
    public static LocalDate selectedDate;
    public TextView monthYearText;
    public RecyclerView calendarRecyclerView;
    private ImageView backBtn, nextBtn, listBtn;

    public  static String message,s;


    @SuppressLint({"WrongViewCast", "NonConstantResourceId"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        backBtn = findViewById(R.id.previousMonthBtn);
        nextBtn = findViewById(R.id.nextMonthBtn);
        listBtn = findViewById(R.id.listBtn);

        backBtn.setOnClickListener(v-> previousMonthAction(calendarRecyclerView));
        nextBtn.setOnClickListener(v-> nextMonthAction(calendarRecyclerView));
        listBtn.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), List.class);
                startActivity(i);
    });


        FirebaseAuth fAuth;
        fAuth =  FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplication(), Login.class));
            finish();
        }}

    public void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.month);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdaptor calendarAdapter = new CalendarAdaptor(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 2; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            s = dayText +" "+ monthYearFromDate(selectedDate);

            // this works get rid of adfa shit ...
            System.out.println( "adfa "+ s);
            getDate(s);
            startActivity(new Intent(getApplicationContext(), ListTasks.class));
        }
    }

    public String date;
    public void getDate(String date){
        System.out.println("date   "+date);
    }
}
