package com.example.Varsani.Staff.ServMrg.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.ServMrg.CompletedItems;
import com.example.Varsani.Staff.ServMrg.Models.CompletedItemModal;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterCompletedItem extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<CompletedItemModal> items;

    private Context ctx;
    ProgressDialog progressDialog;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "Orders adapter";

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }
//
//    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
//        this.onMoreButtonClickListener = onMoreButtonClickListener;
//    }

    public AdapterCompletedItem(Context context, List<CompletedItemModal> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_orderID, txv_serviceType,txv_clientName,txv_status;


        public OriginalViewHolder(View v) {
            super(v);

            txv_orderID =v.findViewById(R.id.txv_orderID);
            txv_serviceType = v.findViewById(R.id.txv_serviceType);
            txv_status = v.findViewById(R.id.txv_status);
            txv_clientName = v.findViewById(R.id.txv_clientName);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final CompletedItemModal o= items.get(position);

            view.txv_orderID.setText("Booking ID: "+o.getOrderID());
            view.txv_status.setText("Status: " + o.getOrderStatus());
            view.txv_serviceType.setText("Service: "+o.getServName());
            view.txv_clientName.setText("Client: "+o.getClientName());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, CompletedItems.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("orderID", o.getOrderID());
                    in.putExtra("serviceName",o.getServName());
                    in.putExtra("totalFee",o.getTotalFee());
                    in.putExtra("orderDate",o.getOrderDate());
                    in.putExtra("orderStatus",o.getOrderStatus());
                    in.putExtra("clientName",o.getClientName());
                    in.putExtra("rating",o.getRating());
                    in.putExtra("remark",o.getOrderRemark());
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
