package com.example.authapptutorial.list;

import java.util.ArrayList;

public class taskModel {
    public String taskDetails;
    public String taskName;
    public String userid;
    ArrayList<Object> storeTasks;

    public taskModel(String taskDetails, String taskName, String userid){
        this.taskDetails = taskDetails;
        this.taskName = taskName;
        this.userid = userid;

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

    public ArrayList<Object> getTasksList(){
        return storeTasks;
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
    public void setTasksList(ArrayList<Object> storeTasks){this.storeTasks=storeTasks;}

    public String toString(){
        return "taskModel string output: "+taskName+" "+taskDetails+" "+userid;
    }
}
