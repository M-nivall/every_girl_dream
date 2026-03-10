package com.example.Varsani.Clients.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.RescueCenterModel;
import com.example.Varsani.R;

import java.util.List;

public class AdapterRescueCenter extends RecyclerView.Adapter<AdapterRescueCenter.MyViewHolder> {

    Context context;
    List<RescueCenterModel> list;

    public AdapterRescueCenter(Context context, List<RescueCenterModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_rescue_center, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RescueCenterModel model = list.get(position);

        holder.tvCenterName.setText(model.getCenterName());
        holder.tvLocation.setText(model.getTown() + ", " + model.getCounty());
        holder.tvAddress.setText("📍 " + model.getAddress());
        holder.tvPhone.setText("📞 " + model.getPhone());
        holder.tvEmail.setText("📧 " + model.getEmail());
        holder.tvHours.setText("🕐 " + model.getOperatingHours());

        // Call button
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(model.getPhone());
            }
        });

        // Get Directions button
        holder.btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections(model.getAddress() + ", " + model.getTown());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void makeCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));

        try {
            context.startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to make call", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDirections(String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            context.startActivity(mapIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Google Maps not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCenterName, tvLocation, tvAddress, tvPhone, tvEmail, tvHours;
        Button btnCall, btnDirections;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCenterName = itemView.findViewById(R.id.tvCenterName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvHours = itemView.findViewById(R.id.tvHours);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnDirections = itemView.findViewById(R.id.btnDirections);
        }
    }
}