package com.example.Varsani.Seminars.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Seminars.Models.ApplicantModel;
import com.example.Varsani.utils.Urls;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterApplicant extends RecyclerView.Adapter<AdapterApplicant.MyViewHolder> {

    Context context;
    List<ApplicantModel> list;

    public AdapterApplicant(Context context, List<ApplicantModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_applicant, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ApplicantModel model = list.get(position);

        holder.tvID.setText(model.getID());
        holder.tvName.setText(model.getFullName());
        holder.tvPhone.setText(model.getPhone());
        holder.tvAge.setText(model.getAgeGroup());

        // Handle status display
        String status = model.getStatus();
        if (status != null && status.equals("Approved")) {
            // Show approved status
            holder.btnApprove.setVisibility(View.GONE);
            holder.tvStatusApproved.setVisibility(View.VISIBLE);
        } else {
            // Show approve button
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.tvStatusApproved.setVisibility(View.GONE);
        }

        // Approve button click listener
        holder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveApplicant(model, holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Approve applicant - send to backend
     */
    private void approveApplicant(final ApplicantModel model, final MyViewHolder holder, final int position) {
        // Disable button to prevent multiple clicks
        holder.btnApprove.setEnabled(false);
        holder.btnApprove.setText("...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Urls.URL_APPROVE_APPLICANT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("1")) {
                                // Success - update UI
                                model.setStatus("Approved");
                                holder.btnApprove.setVisibility(View.GONE);
                                holder.tvStatusApproved.setVisibility(View.VISIBLE);

                                Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                            } else {
                                // Failed
                                Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();

                                // Re-enable button
                                holder.btnApprove.setEnabled(true);
                                holder.btnApprove.setText("Approve");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();

                            // Re-enable button
                            holder.btnApprove.setEnabled(true);
                            holder.btnApprove.setText("Approve");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast toast = Toast.makeText(context, "Network error: " + error.toString(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();

                        // Re-enable button
                        holder.btnApprove.setEnabled(true);
                        holder.btnApprove.setText("Approve");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("applicant_id", model.getID());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvName, tvPhone, tvAge, tvStatusApproved;
        Button btnApprove;
        LinearLayout layoutStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAge = itemView.findViewById(R.id.tvAge);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            tvStatusApproved = itemView.findViewById(R.id.tvStatusApproved);
            layoutStatus = itemView.findViewById(R.id.layoutStatus);
        }
    }
}