package com.example.Varsani.ReportCases.Adapters;

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
import com.example.Varsani.R;
import com.example.Varsani.ReportCases.EmergencyDetails;
import com.example.Varsani.ReportCases.Models.EmergencyModel;
import com.example.Varsani.Staff.Adapters.AdapterQuot;
import com.example.Varsani.Staff.Models.OrderToShipModel;
import com.example.Varsani.Staff.ServMrg.QuotItems;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterEmergencyReport extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<EmergencyModel> items;

    private Context ctx;
    ProgressDialog progressDialog;

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "Orders adapter";

    public AdapterEmergencyReport(Context context, List<EmergencyModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_reportID, txv_county, txv_no_girls;
        public TextView txv_urgency, txv_status;
        public Button btn_view_details;

        public OriginalViewHolder(View v) {
            super(v);

            txv_county =v.findViewById(R.id.txv_county);
            txv_reportID =v.findViewById(R.id.txv_reportID);
            txv_urgency = v.findViewById(R.id.txv_urgency);
            txv_no_girls = v.findViewById(R.id.txv_no_girls);
            txv_status = v.findViewById(R.id.txv_status);
            btn_view_details = v.findViewById(R.id.btn_view_details);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_emeregency_card, parent, false);
        vh = new AdapterEmergencyReport.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterEmergencyReport.OriginalViewHolder) {
            final AdapterEmergencyReport.OriginalViewHolder view = (AdapterEmergencyReport.OriginalViewHolder) holder;

            final EmergencyModel o= items.get(position);

            view.txv_reportID.setText("Report ID " + o.getReportID());
            view.txv_urgency.setText("Urgency: " + o.getUrgency());
            view.txv_no_girls.setText("No Girls: " + o.getNumberOfGirls());
            view.txv_county.setText("County: " + o.getCounty());
            view.txv_status.setText("Status: " + o.getStatus());

            view.btn_view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in=new Intent(ctx, EmergencyDetails.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("reportID", o.getReportID());
                    in.putExtra("anonymous",o.getAnonymous());
                    in.putExtra("urgency",o.getUrgency());
                    in.putExtra("county",o.getCounty());
                    in.putExtra("village",o.getTownVillage());
                    in.putExtra("address",o.getSpecificAddress());
                    in.putExtra("ageGroup",o.getAgeGroup());
                    in.putExtra("girls",o.getNumberOfGirls());
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
