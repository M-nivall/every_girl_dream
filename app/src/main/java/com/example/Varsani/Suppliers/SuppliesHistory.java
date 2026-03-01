package com.example.Varsani.Suppliers;

import static com.example.Varsani.utils.Urls.URL_MY_REQUESTS;
import static com.example.Varsani.utils.Urls.URL_SUPPLY_HISTORY;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.example.Varsani.Suppliers.Adapter.AdapterSupplies;
import com.example.Varsani.Suppliers.Model.SuppliesModel;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuppliesHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterSupplies adapter;
    private List<SuppliesModel> suppliesList;
    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplies_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);

        suppliesList = new ArrayList<>();
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        requests();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void requests() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUPPLY_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String requestID = jsn.getString("requestID");
                                    String items = jsn.getString("items");
                                    String requestDate = jsn.getString("requestDate");
                                    String requestStatus = jsn.getString("requestStatus");
                                    String quantity = jsn.getString("quantity");

                                    SuppliesModel rq = new SuppliesModel(requestID, items, requestDate, requestStatus, quantity);
                                    suppliesList.add(rq);
                                }
                                adapter = new AdapterSupplies(SuppliesHistory.this, suppliesList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                showToast(msg);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showToast(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", user.getClientID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
