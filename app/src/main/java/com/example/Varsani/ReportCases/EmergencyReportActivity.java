package com.example.Varsani.ReportCases;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.utils.Urls;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmergencyReportActivity extends AppCompatActivity {

    // Views
    private RadioGroup rgUrgency;
    private Spinner spinnerCounty, spinnerAgeGroup, spinnerNumberOfGirls;
    private EditText etTownVillage, etSpecificAddress, etDescription;
    private TextView tvCharCount;
    private Button btnSubmitReport, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_report);

        // Initialize views
        initializeViews();

        // Setup spinners
        setupSpinners();

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        rgUrgency = findViewById(R.id.rgUrgency);

        spinnerCounty = findViewById(R.id.spinnerCounty);
        spinnerAgeGroup = findViewById(R.id.spinnerAgeGroup);
        spinnerNumberOfGirls = findViewById(R.id.spinnerNumberOfGirls);

        etTownVillage = findViewById(R.id.etTownVillage);
        etSpecificAddress = findViewById(R.id.etSpecificAddress);
        etDescription = findViewById(R.id.etDescription);

        tvCharCount = findViewById(R.id.tvCharCount);

        btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnCancel = findViewById(R.id.btnCancel);
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

        // Age Groups
        String[] ageGroups = {
                "Select Age Group",
                "0-5 years",
                "6-10 years",
                "11-15 years",
                "16-18 years",
                "18+ years",
                "Unknown"
        };
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ageGroups);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeGroup.setAdapter(ageAdapter);

        // Number of Girls
        String[] numberOfGirls = {
                "Select Number",
                "1 girl",
                "2-5 girls",
                "5+ girls",
                "Unknown"
        };
        ArrayAdapter<String> numberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, numberOfGirls);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumberOfGirls.setAdapter(numberAdapter);
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
        // Get urgency level
        int selectedUrgency = rgUrgency.getCheckedRadioButtonId();
        if (selectedUrgency == -1) {
            Toast.makeText(this, "Please select urgency level", Toast.LENGTH_SHORT).show();
            return;
        }

        String urgency = "";
        if (selectedUrgency == R.id.rbCritical) urgency = "Critical";
        else if (selectedUrgency == R.id.rbHigh) urgency = "High";
        else if (selectedUrgency == R.id.rbMedium) urgency = "Medium";
        else if (selectedUrgency == R.id.rbLow) urgency = "Low";

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

        // Get victim information
        String ageGroup = spinnerAgeGroup.getSelectedItem().toString();
        if (ageGroup.equals("Select Age Group")) {
            Toast.makeText(this, "Please select age group", Toast.LENGTH_SHORT).show();
            return;
        }

        String numberOfGirls = spinnerNumberOfGirls.getSelectedItem().toString();
        if (numberOfGirls.equals("Select Number")) {
            Toast.makeText(this, "Please select number of girls at risk", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get description
        String description = etDescription.getText().toString().trim();
        if (description.length() < 20) {
            etDescription.setError("Description must be at least 20 characters");
            etDescription.requestFocus();
            Toast.makeText(this, "Please provide more details (minimum 20 characters)", Toast.LENGTH_LONG).show();
            return;
        }

        // Show confirmation dialog
        showConfirmationDialog(urgency, county, townVillage, specificAddress,
                ageGroup, numberOfGirls, description);
    }

    private void showConfirmationDialog(final String urgency, final String county,
                                        final String townVillage, final String specificAddress,
                                        final String ageGroup, final String numberOfGirls,
                                        final String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("⚠️ Confirm Anonymous Report");
        builder.setMessage("You are about to submit an anonymous emergency report.\n\n" +
                "Urgency: " + urgency + "\n" +
                "Location: " + townVillage + ", " + county + "\n\n" +
                "Rescue workers will be notified immediately.\n\n" +
                "Do you want to continue?");

        builder.setPositiveButton("Yes, Submit Report", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitReport(urgency, county, townVillage, specificAddress,
                        ageGroup, numberOfGirls, description);
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

    private void submitReport(String urgency, String county, String townVillage,
                              String specificAddress, String ageGroup, String numberOfGirls,
                              String description) {

        // Show loading
        btnSubmitReport.setEnabled(false);
        btnSubmitReport.setText("Submitting...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Urls.URL_SUBMIT_EMERGENCY_REPORT,
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
                                Toast.makeText(EmergencyReportActivity.this,
                                        message, Toast.LENGTH_LONG).show();
                                btnSubmitReport.setEnabled(true);
                                btnSubmitReport.setText("🚨 SUBMIT EMERGENCY REPORT");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(EmergencyReportActivity.this,
                                    "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            btnSubmitReport.setEnabled(true);
                            btnSubmitReport.setText("🚨 SUBMIT EMERGENCY REPORT");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EmergencyReportActivity.this,
                                "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        btnSubmitReport.setEnabled(true);
                        btnSubmitReport.setText("🚨 SUBMIT EMERGENCY REPORT");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                // Always anonymous
                params.put("is_anonymous", "1");
                params.put("urgency", urgency);
                params.put("county", county);
                params.put("town_village", townVillage);
                params.put("specific_address", specificAddress);
                params.put("age_group", ageGroup);
                params.put("number_of_girls", numberOfGirls);
                params.put("description", description);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("✅ Report Submitted Successfully");
        builder.setMessage("Your anonymous emergency report has been submitted.\n\n" +
                "Rescue workers have been notified and will respond as soon as possible.\n\n" +
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
        builder.setTitle("Cancel Report?");
        builder.setMessage("Are you sure you want to cancel this emergency report?");

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