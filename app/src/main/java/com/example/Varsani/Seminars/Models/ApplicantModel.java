package com.example.Varsani.Seminars.Models;

public class ApplicantModel {

    private String ID;
    private String fullName;
    private String phone;
    private String ageGroup;
    private String appStatus; // Add this field

    public ApplicantModel(String ID, String fullName, String phone, String ageGroup, String appStatus) {
        this.ID = ID;
        this.fullName = fullName;
        this.phone = phone;
        this.ageGroup = ageGroup;
        this.appStatus = appStatus;
    }

    // Getters
    public String getID() {
        return ID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getStatus() {
        return appStatus;
    }

    // Setters
    public void setStatus(String appStatus) {
        this.appStatus = appStatus;
    }
}