package com.example.Varsani.Suppliers;

import static com.example.Varsani.utils.Urls.URL_SUBMIT_BID;
import static com.example.Varsani.utils.Urls.URL_SUPPLY_BID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SupplyBid extends AppCompatActivity {

    private TextView txt_request_id, txt_quantity, txt_urgency, txv_unit_price,
            txv_total_price, txt_bid_status;
    private Button btn_supply, btn_view_payment;
    private ProgressBar progressBar;

    private String requestID, urgency, status, unitPrice, totalPrice;
    private int quantity;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supply_bid);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_request_id = findViewById(R.id.txt_request_id);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_urgency = findViewById(R.id.txt_urgency);
        txv_unit_price = findViewById(R.id.txv_unit_price);
        txv_total_price = findViewById(R.id.txv_total_price);
        txt_bid_status = findViewById(R.id.txt_bid_status);

        btn_supply = findViewById(R.id.btn_supply);
        progressBar = findViewById(R.id.progressBar);

        btn_view_payment = findViewById(R.id.btn_view_payment);

        progressBar.setVisibility(View.GONE);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        // Receive Intent data
        requestID = getIntent().getStringExtra("requestID");
        urgency = getIntent().getStringExtra("urgency");
        status = getIntent().getStringExtra("status");

        unitPrice = getIntent().getStringExtra("unitPrice");
        totalPrice = getIntent().getStringExtra("totalPrice");

        try {
            quantity = Integer.parseInt(getIntent().getStringExtra("quantityNeeded"));
        } catch (Exception e) {
            quantity = 0;
        }

        txt_request_id.setText("Request ID: " + requestID);
        txt_quantity.setText("Quantity Needed: " + quantity + " Units");
        txt_urgency.setText("Urgency: " + urgency);
        txt_bid_status.setText("Status: " + status);

        txv_unit_price.setText("Unit Price: Ksh " + unitPrice);
        txv_total_price.setText("Total Price: Ksh" + totalPrice);

        // Disable bid if not open
        if (status.equals("Approved") || status.equals("Supplied") || status.equals("Received") || status.equals("Paid")) {
            btn_supply.setVisibility(View.GONE);
        }

        if (status.equals("Paid")) {
            btn_view_payment.setVisibility(View.VISIBLE);
        }

        btn_view_payment.setOnClickListener(v -> {
            Intent intent = new Intent(SupplyBid.this, SupplyReceipt.class);
            intent.putExtra("requestID", requestID);
            intent.putExtra("urgency", urgency);
            intent.putExtra("status", status);
            intent.putExtra("unitPrice", unitPrice);
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("quantityNeeded", String.valueOf(quantity));
            intent.putExtra("supplierName", user.getFirstname() + " " + user.getLastname());
            intent.putExtra("supplierPhone", user.getPhoneNo());
            startActivity(intent);
        });

        btn_supply.setOnClickListener(v -> alertSubmit());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void supplyBid(){

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUPPLY_BID,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);

                        try {

                            Log.e("RESPONSE", response);

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(SupplyBid.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();

                                finish();

                            } else {

                                Toast toast = Toast.makeText(SupplyBid.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            Toast toast = Toast.makeText(SupplyBid.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);

                error.printStackTrace();

                Toast toast = Toast.makeText(SupplyBid.this, error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }

        }){

            @Override
            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("requestID", requestID);
                params.put("supplierID",user.getClientID());

                Log.e("PARAMS", "" + params);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // ==============================
    // CONFIRMATION DIALOG
    // ==============================

    public void alertSubmit(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setMessage("Confirm Supply?");
        alertDialog.setCancelable(false);

        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.setButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                supplyBid();
            }
        });

        alertDialog.show();
    }
}