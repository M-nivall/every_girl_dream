package com.example.Varsani.Employees.RescueLead.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Employees.RescueLead.Models.TeamMemberModel;
import com.example.Varsani.R;

import java.util.List;

public class AdapterTeamMember extends RecyclerView.Adapter<AdapterTeamMember.MyViewHolder> {

    Context context;
    List<TeamMemberModel> list;

    public AdapterTeamMember(Context context, List<TeamMemberModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_team_member, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TeamMemberModel model = list.get(position);

        holder.tvMemberID.setText(model.getUserID());
        holder.tvMemberName.setText(model.getFullName());
        holder.tvMemberPhone.setText(model.getPhoneNo());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemberID, tvMemberName, tvMemberPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMemberID = itemView.findViewById(R.id.tvMemberID);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            tvMemberPhone = itemView.findViewById(R.id.tvMemberPhone);
        }
    }

}