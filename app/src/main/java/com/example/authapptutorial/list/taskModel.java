package com.example.authapptutorial.list;


public class taskModel {
    public String taskDetails;
    public String taskName;
    public String userid;
    public String date;
    public String taskType;

    public taskModel(String taskDetails, String taskName, String userid, String date, String taskType){
        this.taskDetails = taskDetails;
        this.taskName = taskName;
        this.userid = userid;
        this.date = date;
        this.taskType = taskType;

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
    public String getTaskType(){
        return taskType;
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

    public void setTaskType(String taskType){
        this.taskType = taskType;
    }

    public String toString(){
        return "taskModel string output: "+taskName+" "+taskDetails+" "+userid+" "+date+" "+taskType;
    }
}
