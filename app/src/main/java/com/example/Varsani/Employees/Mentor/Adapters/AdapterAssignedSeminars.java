package com.example.Varsani.Employees.Mentor.Adapters;

import static com.example.Varsani.utils.Urls.URL_START_SEMINAR;
import static com.example.Varsani.utils.Urls.URL_END_SEMINAR;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Employees.Mentor.Models.AssignedSeminarModel;
import com.example.Varsani.R;
import com.example.Varsani.Seminars.ToAttend;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterAssignedSeminars extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AssignedSeminarModel> items;
    private Context ctx;
    ProgressDialog progressDialog;

    public AdapterAssignedSeminars(Context context, List<AssignedSeminarModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_seminar_title, txt_status, txt_seminar_date;
        public TextView txt_seminar_time, txt_seminar_location, txt_mentor;

        public Button btn_start, btn_attendance, btn_end_seminar;

        public OriginalViewHolder(View v) {
            super(v);

            txt_seminar_title = v.findViewById(R.id.txt_seminar_title);
            txt_status = v.findViewById(R.id.txt_status);
            txt_seminar_time = v.findViewById(R.id.txt_seminar_time);
            txt_seminar_date = v.findViewById(R.id.txt_seminar_date);
            txt_seminar_location = v.findViewById(R.id.txt_seminar_location);
            txt_mentor = v.findViewById(R.id.txt_mentor);

            btn_start = v.findViewById(R.id.btn_start);
            btn_attendance = v.findViewById(R.id.btn_attendance);
            btn_end_seminar = v.findViewById(R.id.btn_end_seminar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_assigned_seminars, parent, false);

        return new OriginalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof OriginalViewHolder) {

            OriginalViewHolder view = (OriginalViewHolder) holder;
            AssignedSeminarModel o = items.get(position);

            view.txt_seminar_title.setText(o.getTitle());
            view.txt_seminar_time.setText("Time: " + o.getSeminarTime());
            view.txt_seminar_date.setText("Date: " + o.getSeminarDate());
            view.txt_status.setText(o.getSeminarStatus());
            view.txt_seminar_location.setText("Location: " + o.getLocation());
            view.txt_mentor.setText("Mentor: " + o.getMentor());

            // Attendance
            view.btn_attendance.setOnClickListener(v -> {

                Intent in = new Intent(ctx, ToAttend.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("seminarID", o.getSeminarID());
                ctx.startActivity(in);

            });

            // Start Seminar
            view.btn_start.setOnClickListener(v -> {

                showStartDialog(o.getSeminarID());

            });

            // End Seminar
            view.btn_end_seminar.setOnClickListener(v -> {

                showEndSeminarDialog(o.getSeminarID());

            });

            view.btn_end_seminar.setVisibility(View.GONE);

            // Disable start if already started
            if(o.getSeminarStatus().equals("In Progress")){
                view.btn_start.setVisibility(View.GONE);
                view.btn_end_seminar.setVisibility(View.VISIBLE);
            }

            if(o.getSeminarStatus().equals("Completed")){
                view.btn_start.setVisibility(View.GONE);
                view.btn_end_seminar.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ===============================
    // START SEMINAR
    // ===============================
    private void showStartDialog(String seminarID){

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setTitle("Start Seminar");
        builder.setMessage("Confirm starting of this seminar");

        builder.setPositiveButton("START", (dialog, which) -> {

            updateSeminarStatus(seminarID);

        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void updateSeminarStatus(String seminarID){

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Starting Seminar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_START_SEMINAR,

                response -> {

                    progressDialog.dismiss();

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        String message = jsonObject.getString("message");

                        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

            progressDialog.dismiss();
            Toast.makeText(ctx,"Network Error",Toast.LENGTH_LONG).show();

        }){

            protected Map<String, String> getParams(){

                Map<String,String> params = new HashMap<>();
                params.put("seminarID", seminarID);

                return params;
            }
        };

        Volley.newRequestQueue(ctx).add(request);
    }

    // ===============================
    // END SEMINAR DIALOG
    // ===============================
    private void showEndSeminarDialog(String seminarID){

        View view = LayoutInflater.from(ctx).inflate(R.layout.dialog_end_seminar,null);

        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setView(view)
                .create();

        dialog.show();

        EditText et_report = view.findViewById(R.id.et_report);
        Button btn_confirm_end = view.findViewById(R.id.btn_confirm_end);

        btn_confirm_end.setOnClickListener(v -> {

            String report = et_report.getText().toString().trim();

            if(report.isEmpty()){
                et_report.setError("Enter seminar remarks");
                return;
            }

            dialog.dismiss();

            endSeminar(seminarID, report);

        });
    }

    // ===============================
    // SEND END SEMINAR TO BACKEND
    // ===============================
    private void endSeminar(String seminarID, String report){

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Ending Seminar...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_END_SEMINAR,

                response -> {

                    progressDialog.dismiss();

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");

                        Toast.makeText(ctx,message,Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

            progressDialog.dismiss();
            Toast.makeText(ctx,"Network Error",Toast.LENGTH_LONG).show();

        }){

            protected Map<String, String> getParams(){

                Map<String,String> params = new HashMap<>();

                params.put("seminarID", seminarID);
                params.put("report", report);

                return params;
            }
        };

        Volley.newRequestQueue(ctx).add(request);
    }

}