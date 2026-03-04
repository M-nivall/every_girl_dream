package com.example.Varsani.ReportCases.Models;

public class EmergencyModel {

    String reportID;
    String anonymous;
    String urgency;
    String county;
    String townVillage;
    String specificAddress;
    String ageGroup;
    String numberOfGirls;
    String description;
    String status;

    public EmergencyModel(String reportID, String anonymous, String urgency,
                          String county, String townVillage, String specificAddress,
                          String ageGroup, String numberOfGirls,
                          String description, String status) {

        this.reportID = reportID;
        this.anonymous = anonymous;
        this.urgency = urgency;
        this.county = county;
        this.townVillage = townVillage;
        this.specificAddress = specificAddress;
        this.ageGroup = ageGroup;
        this.numberOfGirls = numberOfGirls;
        this.description = description;
        this.status = status;
    }

    public String getReportID() {
        return reportID;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getCounty() {
        return county;
    }

    public String getTownVillage() {
        return townVillage;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getNumberOfGirls() {
        return numberOfGirls;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

}