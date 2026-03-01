package com.example.Varsani.Suppliers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Varsani.R;
import com.example.Varsani.Suppliers.Model.SuppliesModel;
import com.example.Varsani.Suppliers.SupplyItems;

import java.util.List;

public class AdapterSupplies extends RecyclerView.Adapter<AdapterSupplies.ViewHolder> {

    private List<SuppliesModel> suppliesList;
    private Context context;

    public AdapterSupplies(Context context, List<SuppliesModel> suppliesList) {
        this.context = context;
        this.suppliesList = suppliesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_supply_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SuppliesModel supply = suppliesList.get(position);

        holder.tvRequestID.setText("Request ID: " + supply.getRequestID());
        holder.tvItems.setText("Items: " + supply.getItems());
        holder.tvRequestStatus.setText("Status: " + supply.getRequestStatus());
        holder.tvRequestDate.setText("Request Date: " + supply.getRequestDate());
        holder.tvQuantity.setText("Quantity: " + supply.getQuantity());

        // Set a click listener on the CardView to navigate to ViewHistory activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SupplyItems.class);
            intent.putExtra("requestID", supply.getRequestID());
            intent.putExtra("items", supply.getItems());
            intent.putExtra("requestStatus", supply.getRequestStatus());
            intent.putExtra("requestDate", supply.getRequestDate());
            intent.putExtra("quantity", supply.getQuantity());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return suppliesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestID, tvItems, tvRequestStatus, tvRequestDate, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestID = itemView.findViewById(R.id.tv_request_id);
            tvItems = itemView.findViewById(R.id.tv_items);
            tvRequestStatus = itemView.findViewById(R.id.tv_request_status);
            tvRequestDate = itemView.findViewById(R.id.tv_request_date);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
