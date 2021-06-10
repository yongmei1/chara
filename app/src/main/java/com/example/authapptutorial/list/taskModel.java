package com.example.authapptutorial.list;

import java.util.ArrayList;

public class taskModel {
    public String taskDetails;
    public String taskName;
    public String userid;
    public String date;

    public taskModel(String taskDetails, String taskName, String userid, String date){
        this.taskDetails = taskDetails;
        this.taskName = taskName;
        this.userid = userid;
        this.date = date;

    }
    public taskModel(){}

    public String getTaskName(){
        return taskName;
    }
    public String getTaskDetails(){
        return taskDetails;
    }
    public String getuserid(){
        return userid;
    }
    public String getDate(){
        return date;
    }

    public void setTaskDetails(String taskDet){
        this.taskDetails = taskDet;
    }
    public void setTaskTitle(String taskTitle){
        this.taskDetails = taskTitle;
    }
    public void setuserid(String userid){
        this.userid = userid;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String toString(){
        return "taskModel string output: "+taskName+" "+taskDetails+" "+userid+" "+date;
    }
}
