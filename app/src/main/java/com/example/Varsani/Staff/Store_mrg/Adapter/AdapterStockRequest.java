package com.example.Varsani.Staff.Store_mrg.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Model.StockRequestModel;
import com.example.Varsani.Staff.Store_mrg.ViewRequestBids;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterStockRequest extends RecyclerView.Adapter<AdapterStockRequest.MyViewHolder> {

    Context context;
    List<StockRequestModel> list;

    public AdapterStockRequest(Context context, List<StockRequestModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_stock_request, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StockRequestModel model = list.get(position);

        holder.tvRequestID.setText("Request #" + model.getRequestID());
        holder.tvQuantity.setText(model.getQuantityNeeded() + " units");
        holder.tvUrgency.setText(model.getUrgency());
        holder.tvStatus.setText(model.getStatus());
        holder.tvBidCount.setText(model.getBidCount() + " bids received");
        holder.tvDate.setText(formatDate(model.getCreatedAt()));

        // Color code urgency
        if (model.getUrgency().equals("Critical")) {
            holder.tvUrgency.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if (model.getUrgency().equals("High")) {
            holder.tvUrgency.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if (model.getUrgency().equals("Medium")) {
            holder.tvUrgency.setTextColor(context.getResources().getColor(R.color.orange));
        } else {
            holder.tvUrgency.setTextColor(context.getResources().getColor(R.color.green_700));
        }

        // Color code status
        if (model.getStatus().equals("Open for Bids")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.blue_900));
        } else if (model.getStatus().equals("Supplier Selected")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_700));
        }

        // Click to view bids
        holder.cardRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRequestBids.class);
                intent.putExtra("requestID", model.getRequestID());
                intent.putExtra("quantityNeeded", model.getQuantityNeeded());
                intent.putExtra("urgency", model.getUrgency());
                intent.putExtra("status", model.getStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String formatDate(String dateTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateTime;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardRequest;
        TextView tvRequestID, tvQuantity, tvUrgency, tvStatus, tvBidCount, tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardRequest = itemView.findViewById(R.id.cardRequest);
            tvRequestID = itemView.findViewById(R.id.tvRequestID);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvUrgency = itemView.findViewById(R.id.tvUrgency);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvBidCount = itemView.findViewById(R.id.tvBidCount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}