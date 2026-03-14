package com.example.Varsani.Staff.Store_mrg.Model;

public class StockRequestModel {

    private String requestID;
    private String quantityNeeded;
    private String urgency;
    private String status;
    private String createdAt;
    private String bidCount;

    public StockRequestModel(String requestID, String quantityNeeded, String urgency,
                             String status, String createdAt, String bidCount) {
        this.requestID = requestID;
        this.quantityNeeded = quantityNeeded;
        this.urgency = urgency;
        this.status = status;
        this.createdAt = createdAt;
        this.bidCount = bidCount;
    }

    public String getRequestID() { return requestID; }
    public String getQuantityNeeded() { return quantityNeeded; }
    public String getUrgency() { return urgency; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getBidCount() { return bidCount; }
}