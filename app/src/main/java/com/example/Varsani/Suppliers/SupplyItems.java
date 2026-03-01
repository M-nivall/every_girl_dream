package com.example.Varsani.Suppliers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Varsani.R;

public class SupplyItems extends AppCompatActivity {

    private TextView tvRequestID, tvItems, tvRequestStatus, tvRequestDate, tvQuantity, tv_go_to_details;
    private String requestStatus, requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize TextViews
        tvRequestID = findViewById(R.id.tv_request_id);
        tvItems = findViewById(R.id.tv_items);
        tvRequestStatus = findViewById(R.id.tv_request_status);
        tvRequestDate = findViewById(R.id.tv_request_date);
        tvQuantity = findViewById(R.id.tv_quantity);
        tv_go_to_details = findViewById(R.id.tv_go_to_details);

        //Initially hide the "Go to Details" TextView
        //tv_go_to_details.setVisibility(View.GONE);

        // Get data from Intent
        Intent intent = getIntent();
        requestStatus = intent.getStringExtra("requestStatus");

        // Set TextViews with data from Intent
        requestID = intent.getStringExtra("requestID");
        tvRequestStatus.setText("Status: " + requestStatus);
        tvRequestID.setText("Request ID: " + requestID);
        tvItems.setText("Items: " + intent.getStringExtra("items"));
        tvRequestDate.setText("Request Date: " + intent.getStringExtra("requestDate"));
        tvQuantity.setText("Quantity: " + intent.getStringExtra("quantity"));

        // Show "Go to Details" link if requestStatus is "Paid"
        if ("Paid".equalsIgnoreCase(requestStatus)) {
            tv_go_to_details.setVisibility(View.VISIBLE);
        }

        // Set click listener for the "Go to Details" TextView
        tv_go_to_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to ReceiptPayment activity
                Intent i = new Intent(SupplyItems.this, ReceiptPayment.class);

                // Pass the requestID to ReceiptPayment activity
                i.putExtra("requestID", requestID);

                // Start the new activity
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
