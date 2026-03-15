package com.example.Varsani.Staff.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.R;
import com.example.Varsani.Staff.Finance.PaySupplier;
import com.example.Varsani.Staff.Models.SupplyPaymentsModel;

import java.util.List;

public class AdapterSupplyPayments extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SupplyPaymentsModel> items;


    private Context ctx;
    ProgressDialog progressDialog;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //


    public static final String TAG = " adapter";

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }
//
//    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
//        this.onMoreButtonClickListener = onMoreButtonClickListener;
//    }

    public AdapterSupplyPayments(Context context, List<SupplyPaymentsModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_requestID,txv_supplierName,txv_amount,
                txv_requestStatus;


        public OriginalViewHolder(View v) {
            super(v);

            txv_amount =v.findViewById(R.id.txv_amount);
            txv_supplierName =v.findViewById(R.id.txv_supplierName);
            txv_requestID = v.findViewById(R.id.txv_requestID);
            txv_requestStatus = v.findViewById(R.id.txv_requestStatus);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_payment_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final SupplyPaymentsModel o= items.get(position);

            view.txv_requestID.setText("Request ID: "+o.getRequestID());
            view.txv_supplierName.setText("Supplier: "+o.getSupplierName());
            view.txv_amount.setText("Amount Ksh: "+o.getTotalPrice());
            view.txv_requestStatus.setText("Status: "+o.getBidStatus());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent in=new Intent(ctx, PaySupplier.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("requestID",o.getRequestID());
                    in.putExtra("supplierName",o.getSupplierName());
                    in.putExtra("unitPrice",o.getUnitPrice());
                    in.putExtra("totalPrice",o.getTotalPrice());
                    in.putExtra("bitStatus",o.getBidStatus());
                    in.putExtra("quantity",o.getQuantity());
                    ctx.startActivity(in);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, ProductModal obj, int pos);
//    }
//
//    public interface OnMoreButtonClickListener {
//        void onItemClick(View view, ProductModal obj, MenuItem item);
//    }



}