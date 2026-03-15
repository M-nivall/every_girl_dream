package com.example.Varsani.Seminars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.example.Varsani.utils.Urls;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterSeminar extends AppCompatActivity {

    private EditText edt_fullname, edt_phone;
    private Spinner sp_age_group;
    private Button btn_register_seminar;
    private ProgressBar progress_bar;

    private SessionHandler session;
    private UserModel user;

    private String seminarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seminar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        edt_fullname = findViewById(R.id.edt_fullname);
        edt_phone = findViewById(R.id.edt_phone);
        sp_age_group = findViewById(R.id.sp_age_group);
        btn_register_seminar = findViewById(R.id.btn_register);
        progress_bar = findViewById(R.id.progress_bar);

        progress_bar.setVisibility(View.GONE);

        Intent intent = getIntent();
        seminarID = intent.getStringExtra("seminarID");

        /* AGE GROUP SPINNER */
        String[] ageGroups = {
                "Select Age Group",
                "10 - 13 years",
                "14 - 17 years",
                "18 - 21 years",
                "22+ years"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ageGroups
        );

        sp_age_group.setAdapter(adapter);

        btn_register_seminar.setOnClickListener(view -> confirmRegistration());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /* CONFIRMATION DIALOG BEFORE REGISTERING */
    private void confirmRegistration(){

        new AlertDialog.Builder(RegisterSeminar.this)
                .setTitle("Confirm Seminar Registration")
                .setMessage("Are you sure you want to register for this seminar?")
                .setCancelable(false)

                .setPositiveButton("Confirm", (dialog, which) -> {

                    dialog.dismiss();
                    registerSeminar();

                })

                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /* REGISTER SEMINAR */
    public void registerSeminar(){

        btn_register_seminar.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);

        final String fullname = edt_fullname.getText().toString().trim();
        final String phone = edt_phone.getText().toString().trim();
        final String ageGroup = sp_age_group.getSelectedItem().toString();

        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this,"Enter full name",Toast.LENGTH_SHORT).show();
            resetButton();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Enter phone number",Toast.LENGTH_SHORT).show();
            resetButton();
            return;
        }

        if(phone.length()!=10){
            Toast.makeText(this,"Phone number should contain 10 digits",Toast.LENGTH_SHORT).show();
            resetButton();
            return;
        }

        if(ageGroup.equals("Select Age Group")){
            Toast.makeText(this,"Please select age group",Toast.LENGTH_SHORT).show();
            resetButton();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_REGISTER_FOR_SEMINAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Response", response);

                        try{
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if(status.equals("1")){
                                showResultDialog("Registration Successful", msg, true);
                            }else{
                                showResultDialog("Registration Failed", msg, false);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            showResultDialog("Error","Unexpected server response.",false);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        showResultDialog("Network Error",
                                "Please check your internet connection and try again.",false);

                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("seminarID", seminarID);
                params.put("fullname", fullname);
                params.put("phone", phone);
                params.put("ageGroup", ageGroup);
                params.put("userID",user.getClientID());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void resetButton(){
        btn_register_seminar.setVisibility(View.VISIBLE);
        progress_bar.setVisibility(View.GONE);
    }

    /* RESULT DIALOG AFTER SUBMISSION */
    private void showResultDialog(String title, String message, boolean success){

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterSeminar.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        if(success){
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                finish();
            });
        }else{
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                resetButton();
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
