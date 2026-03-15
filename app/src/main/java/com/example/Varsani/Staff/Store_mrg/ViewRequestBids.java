package com.example.Varsani.Staff.Store_mrg;

import static com.example.Varsani.utils.Urls.URL_GET_REQUEST_BIDS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterSupplierBids;
import com.example.Varsani.Staff.Store_mrg.Model.BidModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewRequestBids extends AppCompatActivity {

    TextView txt_requestID, txt_quantity, txt_urgency;
    RecyclerView recycler_bids;
    ProgressBar progressBar;

    String requestID, quantity, urgency, bid_status;

    ArrayList<BidModel> list = new ArrayList<>();
    AdapterSupplierBids adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_bids);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Supply Requests");

        txt_requestID = findViewById(R.id.txt_requestID);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_urgency = findViewById(R.id.txt_urgency);
        recycler_bids = findViewById(R.id.recycler_bids);
        progressBar = findViewById(R.id.progressBar);

        recycler_bids.setLayoutManager(new LinearLayoutManager(this));

        requestID = getIntent().getStringExtra("requestID");
        quantity = getIntent().getStringExtra("quantityNeeded");
        urgency = getIntent().getStringExtra("urgency");

        txt_requestID.setText("Request ID: " + requestID);
        txt_quantity.setText("Quantity Needed: " + quantity);
        txt_urgency.setText("Urgency: " + urgency);

        loadBids();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBids(){

        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, URL_GET_REQUEST_BIDS,

                response -> {

                    progressBar.setVisibility(View.GONE);

                    try{

                        Log.e("BIDS_RESPONSE",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("data");

                        list.clear();

                        for(int i=0;i<array.length();i++){

                            JSONObject ob = array.getJSONObject(i);

                            BidModel model = new BidModel();

                            model.setSupplierName(ob.getString("supplier_name"));
                            model.setUnitPrice(ob.getString("unit_price"));
                            model.setTotalPrice(ob.getString("total_price"));
                            model.setSupplierID(ob.getString("supplier_id"));
                            model.setQuantity(ob.getString("quantity_offered"));
                            model.setBid_status(ob.getString("bid_status"));

                            list.add(model);
                        }

                        adapter = new AdapterSupplierBids(ViewRequestBids.this,list,requestID,quantity, bid_status);
                        recycler_bids.setAdapter(adapter);

                    }catch(Exception e){

                        Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                    }

                }, error -> {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("requestID",requestID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}