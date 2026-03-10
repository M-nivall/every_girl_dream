package com.example.Varsani.Counselling;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.ReportCases.EmergencyReportActivity;
import com.example.Varsani.Seminars.ScheduleSeminar;
import com.example.Varsani.utils.Urls;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CounsellingActivity extends AppCompatActivity {

    private Spinner spinnerCounty;
    private EditText etTownVillage, etSpecificAddress, etDescription, edtDate;
    private TextView tvCharCount;
    private Button btnSubmitReport, btnCancel;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_counselling);

        // Initialize views
        initializeViews();

        // Setup spinners
        setupSpinners();

        // Setup listeners
        setupListeners();
    }
    private void initializeViews() {

        spinnerCounty = findViewById(R.id.spinnerCounty);

        etTownVillage = findViewById(R.id.etTownVillage);
        etSpecificAddress = findViewById(R.id.etSpecificAddress);
        etDescription = findViewById(R.id.etDescription);
        edtDate = findViewById(R.id.edtDate);

        tvCharCount = findViewById(R.id.tvCharCount);

        btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnCancel = findViewById(R.id.btnCancel);

        calendar = Calendar.getInstance();
    }

    private void setupSpinners() {
        // Kenya Counties
        String[] counties = {
                "Select County",
                "Nairobi", "Mombasa", "Kisumu", "Nakuru", "Eldoret",
                "Baringo", "Bomet", "Bungoma", "Busia", "Elgeyo Marakwet",
                "Embu", "Garissa", "Homa Bay", "Isiolo", "Kajiado",
                "Kakamega", "Kericho", "Kiambu", "Kilifi", "Kirinyaga",
                "Kisii", "Kitui", "Kwale", "Laikipia", "Lamu",
                "Machakos", "Makueni", "Mandera", "Marsabit", "Meru",
                "Migori", "Murang'a", "Nandi", "Narok", "Nyamira",
                "Nyandarua", "Nyeri", "Samburu", "Siaya", "Taita Taveta",
                "Tana River", "Tharaka Nithi", "Trans Nzoia", "Turkana",
                "Uasin Gishu", "Vihiga", "Wajir", "West Pokot"
        };
        ArrayAdapter<String> countyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, counties);
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCounty.setAdapter(countyAdapter);

    }

    private void setupListeners() {
        // Character counter for description
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tvCharCount.setText(length + " / 20 characters (minimum)");

                if (length >= 20) {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        /* DATE PICKER */
        edtDate.setOnClickListener(v -> {

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CounsellingActivity.this,
                    (view, y, m, d) -> edtDate.setText(d + "/" + (m + 1) + "/" + y),
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Submit button
        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void validateAndSubmit() {

        // Get location
        String county = spinnerCounty.getSelectedItem().toString();
        if (county.equals("Select County")) {
            Toast.makeText(this, "Please select a county", Toast.LENGTH_SHORT).show();
            return;
        }

        String townVillage = etTownVillage.getText().toString().trim();
        if (townVillage.isEmpty()) {
            etTownVillage.setError("Town/Village is required");
            etTownVillage.requestFocus();
            return;
        }

        String specificAddress = etSpecificAddress.getText().toString().trim();

        // Get description
        String description = etDescription.getText().toString().trim();
        if (description.length() < 20) {
            etDescription.setError("Description must be at least 20 characters");
            etDescription.requestFocus();
            Toast.makeText(this, "Please provide more details (minimum 20 characters)", Toast.LENGTH_LONG).show();
            return;
        }

        // Show confirmation dialog
        showConfirmationDialog(county, townVillage, specificAddress,
                 description);
    }
    private void showConfirmationDialog(final String county,
                                        final String townVillage, final String specificAddress,
                                        final String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("⚠️ Confirm Counselling");
        builder.setMessage("You are about to submit an anonymous emergency report.\n\n" +
                "Location: " + townVillage + ", " + county + "\n\n" +
                "Counsellor will reach out in a few.\n\n" +
                "Do you want to continue?");

        builder.setPositiveButton("Yes, Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitReport(county, townVillage, specificAddress,
                         description);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submitReport(String county, String townVillage,
                              String specificAddress,
                              String description) {

        // Show loading
        btnSubmitReport.setEnabled(false);
        btnSubmitReport.setText("Submitting...");

        final String date = edtDate.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Urls.URL_REQUEST_COUNSELLING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("1")) {
                                // Success
                                showSuccessDialog();
                            } else {
                                Toast.makeText(CounsellingActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                                btnSubmitReport.setEnabled(true);
                                btnSubmitReport.setText("🚨 SUBMIT COUNSELLING");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(CounsellingActivity.this,
                                    "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            btnSubmitReport.setEnabled(true);
                            btnSubmitReport.setText("🚨 SUBMIT COUNSELLING");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(CounsellingActivity.this,
                                "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        btnSubmitReport.setEnabled(true);
                        btnSubmitReport.setText("🚨 SUBMIT COUNSELLING");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                // Always anonymous
                params.put("county", county);
                params.put("town_village", townVillage);
                params.put("specific_address", specificAddress);
                params.put("description", description);

                params.put("date", date);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✅ Counselling Submitted Successfully");
        builder.setMessage("Your counselling request has been submitted.\n\n" +
                "Counsellor will be sent as soon as possible.\n\n" +
                "Thank you for helping protect girls in our community.");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Request?");
        builder.setMessage("Are you sure you want to cancel this request?");

        builder.setPositiveButton("Yes, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No, Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}