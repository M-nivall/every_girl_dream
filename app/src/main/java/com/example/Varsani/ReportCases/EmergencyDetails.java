package com.example.Varsani.ReportCases;

import static com.example.Varsani.utils.Urls.URL_ASSIGN_RECUE_TEAM;
import static com.example.Varsani.utils.Urls.URL_GET_RESCUE_TEAM;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.cardview.widget.CardView;
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
import com.example.Varsani.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmergencyDetails extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView txv_reportID,txv_county,txv_town,
            txv_address,txv_ageGroup,txv_noGirls,
            txv_urgency,txv_status,txv_description;
    private Button btn_assign_rescue;
    private CardView card_assign_rescue;
    private ArrayList<String> rescueTeams;

    private EditText edt_rescueLead;

    private String reportID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progressBar);
        txv_ageGroup=findViewById(R.id.txv_ageGroup);
        txv_noGirls=findViewById(R.id.txv_noGirls);
        txv_urgency=findViewById(R.id.txv_urgency);
        txv_county=findViewById(R.id.txv_county);
        txv_status=findViewById(R.id.txv_status);
        txv_description=findViewById(R.id.txv_description);
        txv_town=findViewById(R.id.txv_town);
        txv_reportID=findViewById(R.id.txv_reportID);
        txv_address=findViewById(R.id.txv_address);
        btn_assign_rescue=findViewById(R.id.btn_assign_rescue);
        edt_rescueLead=findViewById(R.id.edt_rescueLead);
        card_assign_rescue=findViewById(R.id.card_assign_rescue);

        edt_rescueLead.setFocusable(false);

        rescueTeams = new ArrayList<>();

        Intent intent=getIntent();

        reportID=intent.getStringExtra("reportID");
        String anonymous=intent.getStringExtra("anonymous");
        String urgency=intent.getStringExtra("urgency");
        String county=intent.getStringExtra("county");
        String village=intent.getStringExtra("village");
        String address=intent.getStringExtra("address");
        String ageGroup=intent.getStringExtra("ageGroup");
        String girls=intent.getStringExtra("girls");
        String desc=intent.getStringExtra("desc");
        String reportStatus=intent.getStringExtra("reportStatus");

        txv_reportID.setText("Report ID: " + reportID);
        txv_county.setText("County: " + county );
        txv_town.setText("Town Village: " + village );
        txv_address.setText("Address: " + address );
        txv_ageGroup.setText("Age Group: " + ageGroup );
        txv_noGirls.setText("No Girls: " + girls );
        txv_urgency.setText("Urgency: " + urgency );
        txv_status.setText("Status: " + reportStatus );
        txv_description.setText(desc);

        edt_rescueLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertTeams(v);
            }
        });


        btn_assign_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertAssign(v);
            }
        });

        getRescueTeams();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void assign(){

        final String selectedTeam=edt_rescueLead.getText().toString().trim();

        if(TextUtils.isEmpty(selectedTeam)){
            Toast toast= Toast.makeText(getApplicationContext(), "Please select Rescue Team", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,250);
            toast.show();
            return;
        }
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_ASSIGN_RECUE_TEAM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("reportID",reportID);
                params.put("selectedTeam",selectedTeam);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getRescueTeams() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_RESCUE_TEAM,
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
                                    String team_name = jsn.getString("team_name");
                                    rescueTeams.add(team_name);
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getAlertTeams(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Rescue Team");

        // Create a string array of full names for the dialog
        String[] teamsArray = rescueTeams.toArray(new String[0]);

        builder.setItems(teamsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When an instructor is selected, set the username in the EditText
                edt_rescueLead.setText(rescueTeams.get(which)); // Get the corresponding username
            }
        });

        builder.show();
    }

    public void getAlertAssign(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Assign Rescue Team");
        final String[] array = rescueTeams.toArray(new String[rescueTeams.size()]);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                assign();

                return;
            }
        });
        builder.show();
    }
}