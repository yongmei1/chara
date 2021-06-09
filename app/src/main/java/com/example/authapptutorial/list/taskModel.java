package com.example.authapptutorial.list;

public class taskModel {
    public String taskDetails;
    public String taskName;
    public String userid;

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

    public void setTaskDetails(String taskDet){
        this.taskDetails = taskDet;
    }

    public void setTaskTitle(String taskTitle){
        this.taskDetails = taskTitle;
    }
    public void setuserid(String userid){
        this.userid = userid;
    }

    public String toString(){
        return "taskModel string output: "+taskName+" "+taskDetails+" "+userid;
    }
}
