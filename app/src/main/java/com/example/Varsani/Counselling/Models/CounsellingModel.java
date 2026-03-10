package com.example.Varsani.Counselling.Models;

public class CounsellingModel {

    String sessionID;
    String county;
    String townVillage;
    String specificAddress;
    String description;
    String status;

    public CounsellingModel(String sessionID, String county, String townVillage, String specificAddress,
                          String description, String status) {

        this.sessionID = sessionID;
        this.county = county;
        this.townVillage = townVillage;
        this.specificAddress = specificAddress;
        this.description = description;
        this.status = status;
    }

    public String getSessionID() {
        return sessionID;
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

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
