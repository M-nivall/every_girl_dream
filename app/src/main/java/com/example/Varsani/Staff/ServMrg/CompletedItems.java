package com.example.Varsani.Staff.ServMrg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

public class CompletedItems extends AppCompatActivity {
    private SessionHandler session;
    private UserModel user;

    private ProgressBar progressBar;
    private TextView tv_service_name,tv_order_id,tv_total_fee,tv_rating,
            tv_order_date,tv_order_status,tv_client_name,tv_remark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_items);
        getSupportActionBar().setTitle("Completed Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_service_name=findViewById(R.id.tv_service_name);
        tv_order_id=findViewById(R.id.tv_order_id);
        tv_total_fee=findViewById(R.id.tv_total_fee);
        tv_order_date=findViewById(R.id.tv_order_date);
        tv_order_status=findViewById(R.id.tv_order_status);
        tv_client_name=findViewById(R.id.tv_client_name);
        tv_remark=findViewById(R.id.tv_remark);
        tv_rating=findViewById(R.id.tv_rating);

        final Intent intent=getIntent();
        String orderID=intent.getStringExtra("orderID");
        String serviceName=intent.getStringExtra("serviceName");
        String totalFee=intent.getStringExtra("totalFee");
        String orderStatus=intent.getStringExtra("orderStatus");
        String orderDate=intent.getStringExtra("orderDate");
        String clientName=intent.getStringExtra("clientName");
        String rating=intent.getStringExtra("rating");
        String remark=intent.getStringExtra("remark");



        tv_order_id.setText("Booking ID: #" + orderID);
        tv_total_fee.setText("Total Fee: Ksh " + totalFee);
        tv_order_date.setText("Booking Date: " + orderDate);
        tv_service_name.setText("Service Name: " + serviceName);
        tv_order_status.setText("Status: " + orderStatus);
        tv_client_name.setText("Client: " + clientName);
        tv_remark.setText(remark);
        tv_rating.setText("Rating: " + rating);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}