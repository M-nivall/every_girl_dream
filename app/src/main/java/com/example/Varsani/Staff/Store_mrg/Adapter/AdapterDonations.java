package com.example.Varsani.Staff.Store_mrg.Adapter;

import static com.example.Varsani.utils.Urls.URL_APPROVE_SUPPLIER;
import static com.example.Varsani.utils.Urls.URL_CONFIRM_STOCK_SUPPLY;
import static com.example.Varsani.utils.Urls.URL_RECEIVE_DONATIONS;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Model.BidModel;
import com.example.Varsani.Staff.Store_mrg.Model.DonationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterDonations extends RecyclerView.Adapter<AdapterDonations.MyViewHolder>{
    Context context;
    List<DonationModel> list;
    String donation_id, quantity, donation_status;

    public AdapterDonations(Context context, List<DonationModel> list, String donation_id, String quantity,String donation_status) {
        this.context = context;
        this.list = list;
        this.donation_id = donation_id;
        this.quantity = quantity;
        this.donation_status = donation_status;
    }

    @NonNull
    @Override
    public AdapterDonations.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_donations, parent, false);

        return new AdapterDonations.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDonations.MyViewHolder holder, int position) {

        DonationModel model = list.get(position);

        holder.txt_donor.setText("Donor: " + model.getDonner_name());
        holder.txt_quantity.setText("Unit Quantity: " + model.getQuantity());
        holder.txt_status.setText("Status: " + model.getDonation_status());


        holder.btn_receive_donations.setOnClickListener(v -> receiveDonations(model.getDonation_id(), model.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_donor, txt_quantity, txt_status;
        Button btn_receive_donations;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_donor = itemView.findViewById(R.id.txt_donor);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_status = itemView.findViewById(R.id.txt_status);
            btn_receive_donations = itemView.findViewById(R.id.btn_receive_donations);
            cardView = itemView.findViewById(R.id.cardRequest);
        }
    }

    private void receiveDonations(String donation_id, String quantity){

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Receive Donation...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_RECEIVE_DONATIONS,

                response -> {

                    dialog.dismiss();
                    Toast.makeText(context,response,Toast.LENGTH_LONG).show();

                },

                error -> {

                    dialog.dismiss();
                    Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();

                }){

            @Override
            protected Map<String, String> getParams(){

                Map<String,String> params = new HashMap<>();

                params.put("donation_id",donation_id);
                params.put("quantity",quantity);

                return params;
            }

        };

        Volley.newRequestQueue(context).add(request);

    }

}
