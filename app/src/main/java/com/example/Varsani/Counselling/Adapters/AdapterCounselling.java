package com.example.Varsani.Counselling.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.Counselling.CounsellingDetails;
import com.example.Varsani.Counselling.Models.CounsellingModel;
import com.example.Varsani.R;
import com.example.Varsani.ReportCases.Adapters.AdapterEmergencyReport;
import com.example.Varsani.ReportCases.EmergencyDetails;
import com.example.Varsani.ReportCases.Models.EmergencyModel;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterCounselling extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CounsellingModel> items;

    private Context ctx;
    ProgressDialog progressDialog;

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "Orders adapter";

    public AdapterCounselling(Context context, List<CounsellingModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_sessionID, txv_county, txv_user;
        public TextView txv_address, txv_status;
        public Button btn_view_details;

        public OriginalViewHolder(View v) {
            super(v);

            txv_county =v.findViewById(R.id.txv_county);
            txv_sessionID =v.findViewById(R.id.txv_sessionID);
            txv_address = v.findViewById(R.id.txv_address);
            txv_status = v.findViewById(R.id.txv_status);
            txv_user = v.findViewById(R.id.txv_user);
            btn_view_details = v.findViewById(R.id.btn_view_details);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_counselling_card, parent, false);
        vh = new AdapterCounselling.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterCounselling.OriginalViewHolder) {
            final AdapterCounselling.OriginalViewHolder view = (AdapterCounselling.OriginalViewHolder) holder;

            final CounsellingModel o= items.get(position);

            view.txv_sessionID.setText("Session ID " + o.getSessionID());
            view.txv_county.setText("County: " + o.getCounty());
            view.txv_status.setText("Status: " + o.getStatus());
            view.txv_address.setText("Address: " + o.getSpecificAddress());
            view.txv_user.setText("User: " + o.getUserName());

            view.btn_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO CREATE ACTIVITY COUNSELLING DETAILS IN PLACE OF EmergencyDetails
                    Intent in=new Intent(ctx, CounsellingDetails.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("sessionID", o.getSessionID());
                    in.putExtra("county",o.getCounty());
                    in.putExtra("village",o.getTownVillage());
                    in.putExtra("address",o.getSpecificAddress());
                    in.putExtra("desc",o.getDescription());
                    in.putExtra("reportStatus",o.getStatus());
                    ctx.startActivity(in);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
