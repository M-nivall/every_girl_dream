package com.example.Varsani.Staff.Models;

public class SupplyPaymentsModel {

    private String requestID;
    private String supplierName;
    private String quantity;
    private String unitPrice;
    private String totalPrice;
    private  String bidStatus;

    public String getRequestID() {
        return requestID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public SupplyPaymentsModel(String requestID, String supplierName, String quantity, String unitPrice, String totalPrice, String bidStatus) {
        this.supplierName =supplierName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.requestID = requestID;
        this.bidStatus = bidStatus;
    }
}
