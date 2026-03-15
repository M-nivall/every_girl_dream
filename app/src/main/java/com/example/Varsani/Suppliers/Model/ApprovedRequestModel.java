package com.example.Varsani.Suppliers.Model;

public class ApprovedRequestModel {
    private String requestID;
    private String quantityNeeded;
    private String urgency;
    private String status;
    private String createdAt;
    private String bidCount;
    private String unitPrice;
    private String totalPrice;

    public ApprovedRequestModel(String requestID, String quantityNeeded, String urgency,
                             String status, String createdAt, String bidCount, String unitPrice, String totalPrice) {
        this.requestID = requestID;
        this.quantityNeeded = quantityNeeded;
        this.urgency = urgency;
        this.status = status;
        this.createdAt = createdAt;
        this.bidCount = bidCount;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public String getRequestID() { return requestID; }
    public String getQuantityNeeded() { return quantityNeeded; }
    public String getUrgency() { return urgency; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getBidCount() { return bidCount; }
    public String getUnitPrice() { return unitPrice; }
    public String getTotalPrice() { return totalPrice; }
}
