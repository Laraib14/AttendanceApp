package com.example.data;

public class Student {

public Student(){

}
    private String userID;
    private String status;
    private String currentDate;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String fullName) {
        this.userID = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentDate(String currentDate){
        this.currentDate=currentDate;
    }

    public String getCurrentDate(){
        return currentDate;
    }
}

