package com.example.Varsani.Employees.RescueLead;

import static com.example.Varsani.utils.Urls.URL_EMERGENCY_REPORTS;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.ReportCases.Adapters.AdapterEmergencyReport;
import com.example.Varsani.ReportCases.Models.EmergencyModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmergencyDuties extends AppCompatActivity {

    private List<EmergencyModel> list;
    private AdapterEmergencyReport adapterEmergencyReport;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emergency_duties);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Emergency Reports");
        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);

        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        newOrders();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void newOrders(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_EMERGENCY_REPORTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");

                            if(status.equals("1")){

                                JSONArray jsonArray=jsonObject.getJSONArray("details");
                                list.clear(); // Clear old data

                                for(int i=0; i <jsonArray.length();i++){
                                    JSONObject jsn=jsonArray.getJSONObject(i);

                                    String reportID = jsn.getString("reportID");
                                    String anonymous = jsn.getString("anonymous");
                                    String urgency = jsn.getString("urgency");
                                    String county = jsn.getString("county");
                                    String townVillage = jsn.getString("townVillage");
                                    String specificAddress = jsn.getString("specificAddress");
                                    String ageGroup = jsn.getString("ageGroup");
                                    String numberOfGirls = jsn.getString("numberOfGirls");
                                    String description = jsn.getString("description");
                                    String reportStatus = jsn.getString("status");

                                    EmergencyModel emergencyModel = new EmergencyModel(
                                            reportID,
                                            anonymous,
                                            urgency,
                                            county,
                                            townVillage,
                                            specificAddress,
                                            ageGroup,
                                            numberOfGirls,
                                            description,
                                            reportStatus
                                    );

                                    list.add(emergencyModel);
                                }

                                adapterEmergencyReport = new AdapterEmergencyReport(getApplicationContext(), list);
                                recyclerView.setAdapter(adapterEmergencyReport);
                                progressBar.setVisibility(View.GONE);

                            }else{
                                Toast toast=Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast=Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                            Log.e("ERROR E ", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast=Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                Log.e("ERROR E ", error.toString());
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}