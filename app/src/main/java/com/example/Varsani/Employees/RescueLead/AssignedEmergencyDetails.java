package com.example.Varsani.Employees.RescueLead;

import static com.example.Varsani.utils.Urls.URL_START_OPERATION;
import static com.example.Varsani.utils.Urls.URL_COMPLETE_OPERATION;
import static com.example.Varsani.utils.Urls.URL_GET_TEAM_MEMBERS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.example.Varsani.Employees.RescueLead.Adapters.AdapterTeamMember;
import com.example.Varsani.Employees.RescueLead.Models.TeamMemberModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignedEmergencyDetails extends AppCompatActivity {

    private ProgressBar progressBar;

    private TextView txv_reportID, txv_county, txv_town,
            txv_address, txv_ageGroup, txv_noGirls,
            txv_assignedTeam, txv_urgency, txv_status, txv_description;

    private CardView card_start_rescue, card_complete_rescue;

    private Button btn_start_rescue, btn_complete_rescue, btn_view_team_members;

    private String reportID, assignedTeam;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_emergency_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);

        txv_ageGroup = findViewById(R.id.txv_ageGroup);
        txv_noGirls = findViewById(R.id.txv_noGirls);
        txv_urgency = findViewById(R.id.txv_urgency);
        txv_county = findViewById(R.id.txv_county);
        txv_status = findViewById(R.id.txv_status);
        txv_description = findViewById(R.id.txv_description);
        txv_town = findViewById(R.id.txv_town);
        txv_reportID = findViewById(R.id.txv_reportID);
        txv_address = findViewById(R.id.txv_address);

        txv_assignedTeam = findViewById(R.id.txv_assignedTeam);

        card_start_rescue = findViewById(R.id.card_start_rescue);
        card_complete_rescue = findViewById(R.id.card_complete_rescue);

        btn_start_rescue = findViewById(R.id.btn_start_rescue);
        btn_complete_rescue = findViewById(R.id.btn_complete_rescue);
        btn_view_team_members = findViewById(R.id.btn_view_team_members);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        Intent intent = getIntent();

        reportID = intent.getStringExtra("reportID");
        String urgency = intent.getStringExtra("urgency");
        String county = intent.getStringExtra("county");
        String village = intent.getStringExtra("village");
        String address = intent.getStringExtra("address");
        String ageGroup = intent.getStringExtra("ageGroup");
        String girls = intent.getStringExtra("girls");
        String desc = intent.getStringExtra("desc");
        String reportStatus = intent.getStringExtra("reportStatus");
        assignedTeam = intent.getStringExtra("assignedTeam");

        txv_reportID.setText("Report ID: " + reportID);
        txv_county.setText("County: " + county);
        txv_town.setText("Town Village: " + village);
        txv_address.setText("Address: " + address);
        txv_ageGroup.setText("Age Group: " + ageGroup);
        txv_noGirls.setText("No Girls: " + girls);
        txv_urgency.setText("Urgency: " + urgency);
        txv_status.setText("Status: " + reportStatus);
        txv_description.setText(desc);
        txv_assignedTeam.setText("Team Assigned: " + assignedTeam);

        card_start_rescue.setVisibility(View.GONE);
        card_complete_rescue.setVisibility(View.GONE);

        if (reportStatus.equals("Pending") || reportStatus.equals("Assigned") ) {
            card_start_rescue.setVisibility(View.VISIBLE);
        }

        if (reportStatus.equals("In Progress")) {
            card_complete_rescue.setVisibility(View.VISIBLE);
        }

        if(user.getUser_type().equals("Rescue Worker")) {
            card_start_rescue.setVisibility(View.GONE);
            card_complete_rescue.setVisibility(View.GONE);
        }

        btn_start_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertStartRescue();
            }
        });

        btn_complete_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCompleteRescue();
            }
        });

        // View Team Members Button Click
        btn_view_team_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamMembersDialog();
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


    /* ================= VIEW TEAM MEMBERS ================= */

    public void showTeamMembersDialog() {

        // Inflate custom dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_team_members, null);

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        // Initialize dialog views
        TextView tvTeamName = dialogView.findViewById(R.id.tvTeamName);
        RecyclerView recyclerViewTeamMembers = dialogView.findViewById(R.id.recyclerViewTeamMembers);
        ProgressBar progressBarTeam = dialogView.findViewById(R.id.progressBarTeam);
        TextView tvNoMembers = dialogView.findViewById(R.id.tvNoMembers);
        Button btnCloseDialog = dialogView.findViewById(R.id.btnCloseDialog);

        // Set team name
        tvTeamName.setText(assignedTeam);

        // Setup RecyclerView
        recyclerViewTeamMembers.setLayoutManager(new LinearLayoutManager(this));

        // Show progress
        progressBarTeam.setVisibility(View.VISIBLE);
        recyclerViewTeamMembers.setVisibility(View.GONE);
        tvNoMembers.setVisibility(View.GONE);

        // Fetch team members
        fetchTeamMembers(assignedTeam, recyclerViewTeamMembers, progressBarTeam, tvNoMembers);

        // Close button
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void fetchTeamMembers(String teamName, final RecyclerView recyclerView,
                                 final ProgressBar progressBar, final TextView tvNoMembers) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_TEAM_MEMBERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("TEAM_RESPONSE", response);

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            progressBar.setVisibility(View.GONE);

                            if (status.equals("1")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("members");
                                List<TeamMemberModel> teamList = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);

                                    String userID = jsn.getString("userID");
                                    String fullName = jsn.getString("fullName");
                                    String phoneNo = jsn.getString("phoneNo");

                                    TeamMemberModel model = new TeamMemberModel(userID, fullName, phoneNo);
                                    teamList.add(model);
                                }

                                // Set adapter
                                AdapterTeamMember adapter = new AdapterTeamMember(AssignedEmergencyDetails.this, teamList);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);

                            } else {
                                // No members found
                                tvNoMembers.setVisibility(View.VISIBLE);
                                tvNoMembers.setText(msg);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            tvNoMembers.setVisibility(View.VISIBLE);
                            tvNoMembers.setText("Error loading team members");

                            Log.e("ERROR", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        tvNoMembers.setVisibility(View.VISIBLE);
                        tvNoMembers.setText("Network error");

                        Log.e("ERROR", error.toString());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("team_name", teamName);

                Log.e("PARAMS", "" + params);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    /* ================= START RESCUE ================= */

    public void startRescue() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_START_OPERATION,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("RESPONSE", response);

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(AssignedEmergencyDetails.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                                finish();

                            } else {

                                Toast toast = Toast.makeText(AssignedEmergencyDetails.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            Toast toast = Toast.makeText(AssignedEmergencyDetails.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Toast toast = Toast.makeText(AssignedEmergencyDetails.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("reportID", reportID);

                Log.e("PARAMS", "" + params);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    public void alertStartRescue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Start Rescue Operation");
        builder.setMessage("Confirm that the rescue team has started the operation.");

        builder.setCancelable(false);

        builder.setPositiveButton("Start Operation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startRescue();

            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }



    /* ================= COMPLETE RESCUE ================= */

    public void completeRescue() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COMPLETE_OPERATION,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.e("RESPONSE", response);

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(AssignedEmergencyDetails.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                                finish();

                            } else {

                                Toast toast = Toast.makeText(AssignedEmergencyDetails.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            Toast toast = Toast.makeText(AssignedEmergencyDetails.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Toast toast = Toast.makeText(AssignedEmergencyDetails.this, error.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("reportID", reportID);

                Log.e("PARAMS", "" + params);

                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    public void alertCompleteRescue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Complete Rescue Operation");
        builder.setMessage("Are you sure the rescue operation has been successfully completed?");

        builder.setCancelable(false);

        builder.setPositiveButton("Mark Completed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                completeRescue();

            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

}