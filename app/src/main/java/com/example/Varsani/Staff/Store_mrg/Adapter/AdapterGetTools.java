package com.example.Varsani.Staff.Store_mrg.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.AddStock;
import com.example.Varsani.Staff.Store_mrg.Model.GetToolModel;
import com.example.Varsani.utils.SessionHandler;

import java.util.ArrayList;
import java.util.List;

public class AdapterGetTools extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GetToolModel> items;
    private List<GetToolModel> searchList;

    private Context ctx;
    ProgressDialog progressDialog;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = " adapter";

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }
//
//    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
//        this.onMoreButtonClickListener = onMoreButtonClickListener;
//    }

    public AdapterGetTools(Context context, List<GetToolModel> items) {
        this.items = items;
        this.searchList = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_title,txv_stock, txv_price;


        public OriginalViewHolder(View v) {
            super(v);

            txv_title =v.findViewById(R.id.txv_title);
            txv_price = v.findViewById(R.id.txv_price);
            txv_stock = v.findViewById(R.id.txv_stock);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_tools, parent, false);
        vh = new com.example.Varsani.Staff.Store_mrg.Adapter.AdapterGetTools.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof com.example.Varsani.Staff.Store_mrg.Adapter.AdapterGetTools.OriginalViewHolder) {
            final com.example.Varsani.Staff.Store_mrg.Adapter.AdapterGetTools.OriginalViewHolder view = (com.example.Varsani.Staff.Store_mrg.Adapter.AdapterGetTools.OriginalViewHolder) holder;

            final GetToolModel o= items.get(position);

            view.txv_title.setText("Tool Name: " + o.getToolName());
            view.txv_price.setText("Tool ID: "+o.getToolID());
            view.txv_stock.setText("Quantity "+o.getQuantity());
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent in=new Intent(ctx, AddStock.class);
                    //in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //in.putExtra("toolID", o.getToolID());
                    //in.putExtra("toolName", o.getToolName());
                    //in.putExtra("quantity", o.getQuantity());
                    //ctx.startActivity(in);

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

                    items = searchList;
                } else {

                    ArrayList<GetToolModel> filteredList = new ArrayList<>();

                    for (GetToolModel androidVersion : items) {

                        if (androidVersion.getToolName().toLowerCase().contains(charString)) {

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
                items = (ArrayList<GetToolModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
