package com.example.Varsani.Seminars.Models;

public class SeminarModel {

    String seminarID;
    String title;
    String location;
    String seminarDate;
    String seminarTime;
    String description;
    String seminarStatus;

    public SeminarModel(String seminarID, String title, String location,
                        String seminarDate, String seminarTime,
                        String description, String seminarStatus) {

        this.seminarID = seminarID;
        this.title = title;
        this.location = location;
        this.seminarDate = seminarDate;
        this.seminarTime = seminarTime;
        this.description = description;
        this.seminarStatus = seminarStatus;
    }

    public String getSeminarID() {
        return seminarID;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getSeminarDate() {
        return seminarDate;
    }

    public String getSeminarTime() {
        return seminarTime;
    }

    public String getDescription() {
        return description;
    }

    public String getSeminarStatus() {
        return seminarStatus;
    }
}