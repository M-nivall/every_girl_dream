package com.example.Varsani.Employees.Donor;

import static com.example.Varsani.utils.Urls.URL_SUBMIT_BID;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.Varsani.Suppliers.Bid;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DonateSanitaryTowel extends AppCompatActivity {

    private EditText edt_unit_price;
    private Button btn_submit_bid;
    private ProgressBar progressBar;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_donate_sanitary_towel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        edt_unit_price = findViewById(R.id.edt_unit_price);
        btn_submit_bid = findViewById(R.id.btn_submit_bid);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        btn_submit_bid.setOnClickListener(v -> alertSubmit());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void submitBid(){

        progressBar.setVisibility(View.VISIBLE);

        String unit_price = edt_unit_price.getText().toString();
        String total_price = String.valueOf(totalPrice);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT_BID,

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

                                Toast toast = Toast.makeText(Bid.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();

                                finish();

                            } else {

                                Toast toast = Toast.makeText(Bid.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            Toast toast = Toast.makeText(Bid.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.GONE);

                error.printStackTrace();

                Toast toast = Toast.makeText(Bid.this, error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }

        }){

            @Override
            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("requestID", requestID);
                params.put("unit_price", edt_unit_price.getText().toString());
                params.put("total_price", String.valueOf(totalPrice));
                params.put("supplierID",user.getClientID());
                params.put("quantity", String.valueOf(quantity));

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

        alertDialog.setMessage("Submit this bid?");
        alertDialog.setCancelable(false);

        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        alertDialog.setButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                submitBid();
            }
        });

        alertDialog.show();
    }

}