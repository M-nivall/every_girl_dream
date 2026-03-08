package com.example.Varsani.Employees.RescueLead.Models;

public class TeamMemberModel {

    private String userID;
    private String fullName;
    private String phoneNo;

    public TeamMemberModel(String userID, String fullName, String phoneNo) {
        this.userID = userID;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}