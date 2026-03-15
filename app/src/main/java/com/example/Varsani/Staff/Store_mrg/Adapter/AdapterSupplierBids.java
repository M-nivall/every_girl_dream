package com.example.Varsani.Staff.Store_mrg.Adapter;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Varsani.utils.Urls.URL_APPROVE_SUPPLIER;
import static com.example.Varsani.utils.Urls.URL_CONFIRM_STOCK_SUPPLY;

public class AdapterSupplierBids extends RecyclerView.Adapter<AdapterSupplierBids.MyViewHolder> {

    Context context;
    List<BidModel> list;
    String requestID, quantity, bid_status;

    public AdapterSupplierBids(Context context, List<BidModel> list, String requestID, String quantity,String bid_status) {
        this.context = context;
        this.list = list;
        this.requestID = requestID;
        this.quantity = quantity;
        this.bid_status = bid_status;
    }

    @NonNull
    @Override
    public AdapterSupplierBids.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_supplier_bid, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSupplierBids.MyViewHolder holder, int position) {

        BidModel model = list.get(position);

        holder.txt_supplier.setText("Supplier: " + model.getSupplierName());
        holder.txt_unit_price.setText("Unit Price: KES " + model.getUnitPrice());
        holder.txt_total_price.setText("Total Price: KES " + model.getTotalPrice());
        holder.txt_status.setText("Status: " + model.getBid_status());

        String status = model.getBid_status();

        if ("Supplied".equals(status)) {
            holder.btn_approve.setVisibility(View.GONE);
            holder.btn_confirm_supply.setVisibility(View.VISIBLE);

        } else if ("Approved".equals(status)) {
            holder.btn_approve.setVisibility(View.GONE);
            holder.btn_confirm_supply.setVisibility(View.GONE);

        } else {
            holder.btn_approve.setVisibility(View.VISIBLE);
            holder.btn_confirm_supply.setVisibility(View.GONE);
        }

        holder.btn_approve.setOnClickListener(v -> approveSupplier(model.getSupplierID()));
        holder.btn_confirm_supply.setOnClickListener(v -> confirmSupply(model.getSupplierID()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_supplier, txt_unit_price, txt_total_price, txt_status;
        Button btn_approve, btn_confirm_supply;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_supplier = itemView.findViewById(R.id.txt_supplier);
            txt_unit_price = itemView.findViewById(R.id.txt_unit_price);
            txt_total_price = itemView.findViewById(R.id.txt_total_price);
            txt_status = itemView.findViewById(R.id.txt_status);
            btn_approve = itemView.findViewById(R.id.btn_approve);
            btn_confirm_supply = itemView.findViewById(R.id.btn_confirm_supply);
            cardView = itemView.findViewById(R.id.cardRequest);
        }
    }

    private void approveSupplier(String supplierID){

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Approving Supplier...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_APPROVE_SUPPLIER,

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

                params.put("requestID",requestID);
                params.put("supplierID",supplierID);

                return params;
            }

        };

        Volley.newRequestQueue(context).add(request);

    }

    private void confirmSupply(String supplierID){

        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Confirming Supply...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_CONFIRM_STOCK_SUPPLY,

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

                params.put("requestID",requestID);
                params.put("supplierID",supplierID);
                params.put("quantity",quantity);

                return params;
            }

        };

        Volley.newRequestQueue(context).add(request);

    }
}