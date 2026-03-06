package com.example.Varsani.Seminars;

import static com.example.Varsani.utils.Urls.URL_CREATE_SEMINAR;
import static com.example.Varsani.utils.Urls.URL_SEND_REQUEST;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleSeminar extends AppCompatActivity {

    private EditText edtSeminarTitle, edtLocation, edtDate, edtTime, edtDescription;
    private Button btnCreateSeminar;
    private ProgressBar progressBar;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_seminar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtSeminarTitle = findViewById(R.id.edtSeminarTitle);
        edtLocation = findViewById(R.id.edtLocation);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtDescription = findViewById(R.id.edtDescription);
        btnCreateSeminar = findViewById(R.id.btnCreateSeminar);
        progressBar = findViewById(R.id.progress_bar);

        calendar = Calendar.getInstance();

        /* DATE PICKER */
        edtDate.setOnClickListener(v -> {

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ScheduleSeminar.this,
                    (view, y, m, d) -> edtDate.setText(d + "/" + (m + 1) + "/" + y),
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        /* TIME PICKER */
        edtTime.setOnClickListener(v -> {

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ScheduleSeminar.this,
                    (view, hourOfDay, minute1) ->
                            edtTime.setText(hourOfDay + ":" + minute1),
                    hour,
                    minute,
                    true
            );

            timePickerDialog.show();
        });

        btnCreateSeminar.setOnClickListener(view -> getAlert());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /* CREATE SEMINAR FUNCTION */

    public void createSeminar() {

        final String title = edtSeminarTitle.getText().toString().trim();
        final String location = edtLocation.getText().toString().trim();
        final String date = edtDate.getText().toString().trim();
        final String time = edtTime.getText().toString().trim();
        final String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            showToast("Please enter seminar title");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            showToast("Please enter seminar location");
            return;
        }

        if (TextUtils.isEmpty(date)) {
            showToast("Please select seminar date");
            return;
        }

        if (TextUtils.isEmpty(time)) {
            showToast("Please select seminar time");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            showToast("Please enter seminar description");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnCreateSeminar.setEnabled(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREATE_SEMINAR,
                response -> {

                    progressBar.setVisibility(View.GONE);
                    btnCreateSeminar.setEnabled(true);

                    try {

                        Log.e("RESPONSE", response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        showToast(msg);

                        if (status.equals("1")) {
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("Parsing error occurred");
                    }

                }, error -> {

            progressBar.setVisibility(View.GONE);
            btnCreateSeminar.setEnabled(true);

            error.printStackTrace();
            showToast("Network error. Please try again");

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("location", location);
                params.put("date", date);
                params.put("time", time);
                params.put("description", description);

                Log.e("PARAMS", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /* CONFIRMATION ALERT */

    public void getAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Seminar Creation");

        builder.setMessage(
                "You are about to create a new seminar.\n\n" +
                        "Please confirm that all seminar details are correct before submitting."
        );

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("Create Seminar", (dialog, which) -> createSeminar());

        builder.setCancelable(false);
        builder.show();
    }

    /* TOAST METHOD */

    private void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
    }
}