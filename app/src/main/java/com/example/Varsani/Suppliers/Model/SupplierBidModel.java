package com.example.Varsani.Suppliers.Models;

public class SupplierBidModel {

    private String bidID;
    private String requestID;
    private String quantityOffered;
    private String unitPrice;
    private String totalPrice;
    private String deliveryTimeframe;
    private String notes;
    private String status;
    private String submittedAt;

    public SupplierBidModel(String bidID, String requestID, String quantityOffered,
                            String unitPrice, String totalPrice, String deliveryTimeframe,
                            String notes, String status, String submittedAt) {
        this.bidID = bidID;
        this.requestID = requestID;
        this.quantityOffered = quantityOffered;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.deliveryTimeframe = deliveryTimeframe;
        this.notes = notes;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    public String getBidID() { return bidID; }
    public String getRequestID() { return requestID; }
    public String getQuantityOffered() { return quantityOffered; }
    public String getUnitPrice() { return unitPrice; }
    public String getTotalPrice() { return totalPrice; }
    public String getDeliveryTimeframe() { return deliveryTimeframe; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public String getSubmittedAt() { return submittedAt; }
}