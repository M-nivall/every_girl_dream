package com.example.Varsani.Clients.Models;

public class RescueCenterModel {

    private String centerID;
    private String centerName;
    private String county;
    private String town;
    private String address;
    private String phone;
    private String email;
    private String operatingHours;

    public RescueCenterModel(String centerID, String centerName, String county,
                             String town, String address, String phone,
                             String email, String operatingHours) {
        this.centerID = centerID;
        this.centerName = centerName;
        this.county = county;
        this.town = town;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.operatingHours = operatingHours;
    }

    // Getters
    public String getCenterID() { return centerID; }
    public String getCenterName() { return centerName; }
    public String getCounty() { return county; }
    public String getTown() { return town; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getOperatingHours() { return operatingHours; }
}