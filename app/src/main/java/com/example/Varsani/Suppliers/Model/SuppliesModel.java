package com.example.Varsani.Suppliers.Model;

public class SuppliesModel {
    private String requestID;
    private String items;
    private String requestDate;
    private String requestStatus;
    private String quantity;

    public SuppliesModel(String requestID, String items, String requestDate, String requestStatus, String quantity) {
        this.requestID = requestID;
        this.items = items;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
        this.quantity = quantity;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getItems() {
        return items;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getQuantity() {
        return quantity;
    }
}
