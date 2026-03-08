package com.example.Varsani.Girls.Models;

public class NoticeModel {

    private String fullName;
    private String appStatus;
    private String title;
    private String seminarDate;
    private String seminarTime;

    public NoticeModel(String fullName, String appStatus, String title, String seminarDate, String seminarTime) {
        this.fullName = fullName;
        this.appStatus = appStatus;
        this.title = title;
        this.seminarDate = seminarDate;
        this.seminarTime = seminarTime;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getSeminarDate() {
        return seminarDate;
    }

    public String getSeminarTime() {
        return seminarTime;
    }

}