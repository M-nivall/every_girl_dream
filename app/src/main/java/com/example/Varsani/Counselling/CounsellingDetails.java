package com.example.Varsani.Counselling;

import static com.example.Varsani.utils.Urls.URL_ASSIGN_COUNSELLOR;
import static com.example.Varsani.utils.Urls.URL_ASSIGN_RECUE_TEAM;
import static com.example.Varsani.utils.Urls.URL_COMPLETE_COUNSELLING;
import static com.example.Varsani.utils.Urls.URL_GET_COUNSELLOR;
import static com.example.Varsani.utils.Urls.URL_GET_RESCUE_TEAM;
import static com.example.Varsani.utils.Urls.URL_START_COUNSELLING;

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
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CounsellingDetails extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView txv_sessionID,txv_county,txv_town,txv_phone,
            txv_address, txv_user,txv_status,txv_description;
    private Button btn_assign_counsellor, btn_start_counselling, btn_complete_counselling;
    private CardView card_assign_rescue, card_counsellor;
    private ArrayList<String> counsellor;

    private EditText edt_counsellor;

    private String sessionID;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counselling_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progressBar);
        txv_user=findViewById(R.id.txv_user);
        txv_phone=findViewById(R.id.txv_phone);
        txv_county=findViewById(R.id.txv_county);
        txv_status=findViewById(R.id.txv_status);
        txv_description=findViewById(R.id.txv_description);
        txv_town=findViewById(R.id.txv_town);
        txv_sessionID=findViewById(R.id.txv_sessionID);
        txv_address=findViewById(R.id.txv_address);
        btn_assign_counsellor=findViewById(R.id.btn_assign_counsellor);
        edt_counsellor=findViewById(R.id.edt_counsellor);
        card_assign_rescue=findViewById(R.id.card_assign_rescue);
        card_counsellor=findViewById(R.id.card_counsellor);

        btn_start_counselling = findViewById(R.id.btn_start_counselling);
        btn_complete_counselling = findViewById(R.id.btn_complete_counselling);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        edt_counsellor.setFocusable(false);

        counsellor = new ArrayList<>();

        Intent intent=getIntent();

        sessionID=intent.getStringExtra("sessionID");
        String county=intent.getStringExtra("county");
        String village=intent.getStringExtra("village");
        String address=intent.getStringExtra("address");
        String desc=intent.getStringExtra("desc");
        String reportStatus=intent.getStringExtra("reportStatus");
        String userName=intent.getStringExtra("userName");
        String phone=intent.getStringExtra("phone");

        if (user.getUser_type().equals("Counsellor")){
            card_assign_rescue.setVisibility(View.GONE);
            card_counsellor.setVisibility(View.VISIBLE);

            if (reportStatus.equals("Assigned")){
                btn_complete_counselling.setVisibility(View.GONE);
            }
        }

        if (reportStatus.equals("In Progress")) {
            btn_start_counselling.setVisibility(View.GONE);
            btn_complete_counselling.setVisibility(View.VISIBLE);
        }


        txv_sessionID.setText("Session ID: " + sessionID);
        txv_county.setText("County: " + county );
        txv_town.setText("Town Village: " + village );
        txv_address.setText("Address: " + address );
        txv_user.setText("User: " + userName );
        txv_status.setText("Status: " + reportStatus );
        txv_phone.setText("Phone: " + phone);
        txv_description.setText(desc);

        edt_counsellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertCounsellors(v);
            }
        });

        btn_assign_counsellor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertAssign(v);
            }
        });

        btn_start_counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertStart(v);
            }
        });

        btn_complete_counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertComplete(v);
            }
        });

        getCounsellor();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void assign(){

        final String counsellor=edt_counsellor.getText().toString().trim();

        if(TextUtils.isEmpty(counsellor)){
            Toast toast= Toast.makeText(getApplicationContext(), "Please select Counsellor", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,250);
            toast.show();
            return;
        }
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_ASSIGN_COUNSELLOR,
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
                params.put("sessionID",sessionID);
                params.put("counsellor",counsellor);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void startCounselling(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_START_COUNSELLING,
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
                params.put("sessionID",sessionID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void completeCounselling(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_COMPLETE_COUNSELLING,
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
                params.put("sessionID",sessionID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void getCounsellor() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_COUNSELLOR,
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
                                    String username = jsn.getString("username");
                                    counsellor.add(username);
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

    public void getAlertCounsellors(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Counsellor");

        // Create a string array of full names for the dialog
        String[] teamsArray = counsellor.toArray(new String[0]);

        builder.setItems(teamsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When an instructor is selected, set the username in the EditText
                edt_counsellor.setText(counsellor.get(which)); // Get the corresponding username
            }
        });

        builder.show();
    }

    public void getAlertAssign(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Assign Counsellor");
        final String[] array = counsellor.toArray(new String[counsellor.size()]);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                assign();

                return;
            }
        });
        builder.show();
    }

    public void getAlertStart(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Start of Counselling Session");
        final String[] array = counsellor.toArray(new String[counsellor.size()]);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                startCounselling();

                return;
            }
        });
        builder.show();
    }

    public void getAlertComplete(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Start of Counselling Session");
        final String[] array = counsellor.toArray(new String[counsellor.size()]);
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                completeCounselling();

                return;
            }
        });
        builder.show();
    }
}