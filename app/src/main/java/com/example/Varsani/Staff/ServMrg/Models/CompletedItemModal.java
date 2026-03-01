package com.example.Varsani.Staff.ServMrg.Models;

public class CompletedItemModal {
    private String orderID;
    private String servName;
    private String totalFee;
    private String orderDate;
    private String orderStatus;
    private String clientName;
    private String rating;
    private  String orderRemark;


    public CompletedItemModal(String orderID, String servName, String totalFee,
                              String orderDate, String orderStatus, String clientName, String rating, String orderRemark){
        this.orderID=orderID;
        this.servName=servName;
        this.totalFee=totalFee;
        this.orderDate=orderDate;
        this.orderStatus=orderStatus;
        this.clientName=clientName;
        this.rating=rating;
        this.orderRemark=orderRemark;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getServName() {
        return servName;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public String getClientName() {
        return clientName;
    }
    public String getRating() {
        return rating;
    }
    public String getOrderRemark() {
        return orderRemark;
    }
}
