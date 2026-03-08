package com.example.Varsani.Girls.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Girls.Models.NoticeModel;
import com.example.Varsani.Girls.NoticeDetails;
import com.example.Varsani.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterNotice extends RecyclerView.Adapter<AdapterNotice.MyViewHolder> {

    Context context;
    List<NoticeModel> list;

    public AdapterNotice(Context context, List<NoticeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_notice, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoticeModel model = list.get(position);

        // Set notification title
        holder.tvNotificationTitle.setText("🎉 Seminar Registration Approved!");

        // Set seminar title
        holder.tvSeminarTitle.setText(model.getTitle());

        // Format and display date and time
        String formattedDate = formatDate(model.getSeminarDate());
        String formattedTime = formatTime(model.getSeminarTime());

        holder.tvDateTime.setText("📅 " + formattedDate + " at " + formattedTime);

        // Set status badge
        if (model.getAppStatus().equals("Approved")) {
            holder.tvStatus.setText("✓ Approved");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_approved);
            holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        // Set preview message
        holder.tvPreviewMessage.setText("Congratulations! Your registration has been approved. Tap to view details.");

        // Click listener to open details
        holder.cardNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoticeDetails.class);
                intent.putExtra("fullName", model.getFullName());
                intent.putExtra("title", model.getTitle());
                intent.putExtra("seminarDate", model.getSeminarDate());
                intent.putExtra("seminarTime", model.getSeminarTime());
                intent.putExtra("appStatus", model.getAppStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Format date to readable format
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    /**
     * Format time to readable format
     */
    private String formatTime(String timeStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            Date time = inputFormat.parse(timeStr);
            return outputFormat.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardNotice;
        TextView tvNotificationTitle, tvSeminarTitle, tvDateTime, tvStatus, tvPreviewMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardNotice = itemView.findViewById(R.id.cardNotice);
            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvSeminarTitle = itemView.findViewById(R.id.tvSeminarTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPreviewMessage = itemView.findViewById(R.id.tvPreviewMessage);
        }
    }
}