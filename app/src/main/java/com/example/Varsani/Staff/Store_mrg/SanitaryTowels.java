package com.example.Varsani.Staff.Store_mrg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterStockRequest;
import com.example.Varsani.Staff.Store_mrg.Model.StockRequestModel;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SanitaryTowels extends AppCompatActivity {

    private TextView tvCurrentStock, tvMinimumStock, tvStockStatus, tvLastUpdated, tvNoRequests;
    private Button btnUpdateStock, btnRequestStock;
    private RecyclerView recyclerViewRequests;
    private ProgressBar progressBar;

    private SessionHandler session;
    private UserModel user;

    private List<StockRequestModel> requestList;
    private AdapterStockRequest adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanitary_towels);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanitary Towel Stock");

        // Initialize views
        tvCurrentStock = findViewById(R.id.tvCurrentStock);
        tvMinimumStock = findViewById(R.id.tvMinimumStock);
        tvStockStatus = findViewById(R.id.tvStockStatus);
        tvLastUpdated = findViewById(R.id.tvLastUpdated);
        tvNoRequests = findViewById(R.id.tvNoRequests);
        btnUpdateStock = findViewById(R.id.btnUpdateStock);
        btnRequestStock = findViewById(R.id.btnRequestStock);
        recyclerViewRequests = findViewById(R.id.recyclerViewRequests);
        progressBar = findViewById(R.id.progressBar);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        requestList = new ArrayList<>();
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this));

        // Load stock data
        getStockLevel();
        getMyRequests();

        // Update stock button
        btnUpdateStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateStockDialog();
            }
        });

        // Request stock button
        btnRequestStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestStockDialog();
            }
        });
    }

    /**
     * Get current stock level
     */
    public void getStockLevel() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_STOCK_LEVEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("STOCK_RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                JSONObject stock = jsonObject.getJSONObject("stock");

                                int quantity = stock.getInt("quantity");
                                int minLevel = stock.getInt("minimum_stock_level");
                                String lastUpdated = stock.getString("last_updated");

                                tvCurrentStock.setText(String.valueOf(quantity));
                                tvMinimumStock.setText(String.valueOf(minLevel));
                                tvLastUpdated.setText("Last updated: " + formatDateTime(lastUpdated));

                                // Update status indicator
                                updateStockStatus(quantity, minLevel);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("ERROR", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("ERROR", error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Update stock status indicator
     */
    private void updateStockStatus(int quantity, int minLevel) {
        if (quantity <= minLevel) {
            tvStockStatus.setText("⚠️ Stock Level Critical - Order Immediately!");
            tvStockStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            tvStockStatus.setBackgroundResource(R.drawable.bg_status_critical);
        } else if (quantity <= minLevel * 1.5) {
            tvStockStatus.setText("⚡ Stock Level Low - Consider Ordering");
            tvStockStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            tvStockStatus.setBackgroundResource(R.drawable.bg_status_low);
        } else {
            tvStockStatus.setText("✓ Stock Level Adequate");
            tvStockStatus.setTextColor(getResources().getColor(R.color.green_700));
            tvStockStatus.setBackgroundResource(R.drawable.bg_status_good);
        }
    }

    /**
     * Show dialog to update stock
     */
    private void showUpdateStockDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_stock, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        RadioGroup rgAction = dialogView.findViewById(R.id.rgAction);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        final AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityStr = etQuantity.getText().toString().trim();

                if (quantityStr.isEmpty()) {
                    etQuantity.setError("Enter quantity");
                    return;
                }

                int selectedAction = rgAction.getCheckedRadioButtonId();
                if (selectedAction == -1) {
                    Toast.makeText(SanitaryTowels.this, "Select an action", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton rbAction = dialogView.findViewById(selectedAction);
                String action = rbAction.getText().toString();

                updateStock(quantityStr, action, dialog);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Update stock in database
     */
    private void updateStock(final String quantity, final String action, final AlertDialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_UPDATE_STOCK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("UPDATE_RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                Toast toast = Toast.makeText(SanitaryTowels.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                                dialog.dismiss();
                                getStockLevel(); // Refresh stock
                            } else {
                                Toast toast = Toast.makeText(SanitaryTowels.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SanitaryTowels.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SanitaryTowels.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("quantity", quantity);
                //params.put("action", action);
                //params.put("user_id", user.getClientID());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Show dialog to request stock from suppliers
     */
    private void showRequestStockDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_request_stock, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText etQuantityNeeded = dialogView.findViewById(R.id.etQuantityNeeded);
        RadioGroup rgUrgency = dialogView.findViewById(R.id.rgUrgency);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        Button btnSubmitRequest = dialogView.findViewById(R.id.btnSubmitRequest);
        Button btnCancelRequest = dialogView.findViewById(R.id.btnCancelRequest);

        final AlertDialog dialog = builder.create();

        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityNeeded = etQuantityNeeded.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                if (quantityNeeded.isEmpty()) {
                    etQuantityNeeded.setError("Enter quantity needed");
                    return;
                }

                int selectedUrgency = rgUrgency.getCheckedRadioButtonId();
                if (selectedUrgency == -1) {
                    Toast.makeText(SanitaryTowels.this, "Select urgency level", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton rbUrgency = dialogView.findViewById(selectedUrgency);
                String urgency = rbUrgency.getText().toString();

                createStockRequest(quantityNeeded, urgency, description, dialog);
            }
        });

        btnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Create stock request for suppliers to bid on
     */
    private void createStockRequest(final String quantityNeeded, final String urgency,
                                    final String description, final AlertDialog dialog) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_CREATE_STOCK_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("REQUEST_RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                Toast toast = Toast.makeText(SanitaryTowels.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                                dialog.dismiss();
                                getMyRequests(); // Refresh requests
                            } else {
                                Toast toast = Toast.makeText(SanitaryTowels.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SanitaryTowels.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SanitaryTowels.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("quantity_needed", quantityNeeded);
                params.put("urgency", urgency);
                params.put("description", description);
                //params.put("requested_by", user.getClientID());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /**
     * Get my stock requests
     */
    public void getMyRequests() {
        progressBar.setVisibility(View.VISIBLE);
        tvNoRequests.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_MY_REQUESTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("REQUESTS_RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            progressBar.setVisibility(View.GONE);

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                requestList.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);

                                    String requestID = jsn.getString("request_id");
                                    String quantityNeeded = jsn.getString("quantity_needed");
                                    String urgency = jsn.getString("urgency");
                                    String requestStatus = jsn.getString("request_status");
                                    String createdAt = jsn.getString("created_at");
                                    String bidCount = jsn.optString("quantity_needed", "0");

                                    StockRequestModel model = new StockRequestModel(
                                            requestID, quantityNeeded, urgency,
                                            requestStatus, createdAt, bidCount
                                    );
                                    requestList.add(model);
                                }

                                adapter = new AdapterStockRequest(SanitaryTowels.this, requestList);
                                recyclerViewRequests.setAdapter(adapter);

                            } else {
                                tvNoRequests.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Log.e("ERROR", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Log.e("ERROR", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user.getClientID());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private String formatDateTime(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateTime;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getStockLevel();
        getMyRequests();
    }

}