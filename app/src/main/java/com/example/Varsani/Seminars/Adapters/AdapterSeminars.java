package com.example.Varsani.Seminars.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.ProductModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.ReportCases.Adapters.AdapterEmergencyReport;
import com.example.Varsani.ReportCases.EmergencyDetails;
import com.example.Varsani.ReportCases.Models.EmergencyModel;
import com.example.Varsani.Seminars.Models.SeminarModel;
import com.example.Varsani.Seminars.RegisterSeminar;
import com.example.Varsani.utils.SessionHandler;

import java.util.ArrayList;
import java.util.List;

public class AdapterSeminars extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<SeminarModel> items;

    private List<SeminarModel> searchView;

    private Context ctx;
    ProgressDialog progressDialog;

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "Orders adapter";

    public AdapterSeminars(Context context, List<SeminarModel> items) {
        this.items = items;
        this.searchView = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_seminar_title, txt_status, txt_seminar_date;
        public TextView txt_seminar_time, txt_seminar_location;
        public Button btn_apply;

        public OriginalViewHolder(View v) {
            super(v);

            txt_seminar_title =v.findViewById(R.id.txt_seminar_title);
            txt_status =v.findViewById(R.id.txt_status);
            txt_seminar_time = v.findViewById(R.id.txt_seminar_time);
            txt_seminar_date = v.findViewById(R.id.txt_seminar_date);
            txt_seminar_location = v.findViewById(R.id.txt_seminar_location);
            btn_apply = v.findViewById(R.id.btn_apply);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item_seminars, parent, false);
        vh = new AdapterSeminars.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterSeminars.OriginalViewHolder) {
            final AdapterSeminars.OriginalViewHolder view = (AdapterSeminars.OriginalViewHolder) holder;

            final SeminarModel o= items.get(position);

            view.txt_seminar_title.setText(o.getTitle());
            view.txt_seminar_time.setText("Time: " + o.getSeminarTime());
            view.txt_seminar_date.setText("Date: " + o.getSeminarDate());
            view.txt_status.setText(o.getSeminarStatus());
            view.txt_seminar_location.setText("Location: " + o.getLocation());

            if (o.getSeminarStatus().equals("Completed") || o.getSeminarStatus().equals("In Progress")) {
                view.btn_apply.setVisibility(View.GONE);
            }

            view.btn_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in=new Intent(ctx, RegisterSeminar.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("seminarID", o.getSeminarID());

                    ctx.startActivity(in);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    items = searchView;
                } else {

                    ArrayList<SeminarModel> filteredList = new ArrayList<>();

                    for (SeminarModel androidVersion : items) {

                        if (androidVersion.getTitle().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    items = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (ArrayList<SeminarModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
