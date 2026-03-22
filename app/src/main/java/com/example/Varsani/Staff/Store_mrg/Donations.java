package com.example.Varsani.Staff.Store_mrg;

import static com.example.Varsani.utils.Urls.URL_GET_DONATIONS;
import static com.example.Varsani.utils.Urls.URL_GET_REQUEST_BIDS;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterDonations;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterSupplierBids;
import com.example.Varsani.Staff.Store_mrg.Model.BidModel;
import com.example.Varsani.Staff.Store_mrg.Model.DonationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Donations extends AppCompatActivity {
    RecyclerView recycler_bids;
    ProgressBar progressBar;

    String donation_id, quantity, donation_status;

    ArrayList<DonationModel> list = new ArrayList<>();
    AdapterDonations adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_donations);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Supply Requests");

        recycler_bids = findViewById(R.id.recycler_bids);
        progressBar = findViewById(R.id.progressBar);

        recycler_bids.setLayoutManager(new LinearLayoutManager(this));

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

        StringRequest request = new StringRequest(Request.Method.POST, URL_GET_DONATIONS,

                response -> {

                    progressBar.setVisibility(View.GONE);

                    try{

                        Log.e("BIDS_RESPONSE",response);

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("data");

                        list.clear();

                        for(int i=0;i<array.length();i++){

                            JSONObject ob = array.getJSONObject(i);

                            DonationModel model = new DonationModel();

                            model.setDonner_name(ob.getString("donner_name"));
                            model.setQuantity(ob.getString("quantity"));
                            model.setDonation_status(ob.getString("donation_status"));
                            model.setDonation_id(ob.getString("donation_id"));

                            list.add(model);
                        }

                        adapter = new AdapterDonations(Donations.this,list,donation_id,quantity, donation_status);
                        recycler_bids.setAdapter(adapter);

                    }catch(Exception e){

                        Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                    }

                }, error -> {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show();
        });

        Volley.newRequestQueue(this).add(request);
    }

}