package com.example.Varsani.Girls;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.Varsani.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoticeDetails extends AppCompatActivity {

    private TextView tvGreeting, tvMessage, tvSeminarTitle, tvSeminarDate,
            tvSeminarTime, tvInstructions, tvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification Details");

        // Initialize views
        tvGreeting = findViewById(R.id.tvGreeting);
        tvMessage = findViewById(R.id.tvMessage);
        tvSeminarTitle = findViewById(R.id.tvSeminarTitle);
        tvSeminarDate = findViewById(R.id.tvSeminarDate);
        tvSeminarTime = findViewById(R.id.tvSeminarTime);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvContact = findViewById(R.id.tvContact);

        // Get data from intent
        Intent intent = getIntent();
        String fullName = intent.getStringExtra("fullName");
        String title = intent.getStringExtra("title");
        String seminarDate = intent.getStringExtra("seminarDate");
        String seminarTime = intent.getStringExtra("seminarTime");
        String appStatus = intent.getStringExtra("appStatus");

        // Display data
        displayNotificationDetails(fullName, title, seminarDate, seminarTime, appStatus);
    }

    private void displayNotificationDetails(String fullName, String title,
                                            String seminarDate, String seminarTime, String appStatus) {

        // Greeting
        tvGreeting.setText("Dear " + fullName + ",");

        // Main message
        String message = "Congratulations! We are pleased to inform you that your registration for the seminar has been approved.\n\n" +
                "We look forward to seeing you at this empowering event designed to support and uplift girls in our community.";
        tvMessage.setText(message);

        // Seminar details
        tvSeminarTitle.setText("📋 " + title);
        tvSeminarDate.setText("📅 Date: " + formatDate(seminarDate));
        tvSeminarTime.setText("🕐 Time: " + formatTime(seminarTime));

        // Instructions
        String instructions = "What to Bring:\n" +
                "• A valid ID for registration\n" +
                "• A notebook and pen for notes\n" +
                "• An open mind and positive attitude\n\n" +
                "Important Notes:\n" +
                "• Please arrive 15 minutes early for registration\n" +
                "• Refreshments will be provided\n" +
                "• Certificate of attendance will be issued";
        tvInstructions.setText(instructions);

        // Contact information
        String contact = "For any questions or concerns, please contact:\n\n" +
                "📞 Phone: 1195 (FGM Hotline)\n" +
                "📧 Email: info@everygirlsdream.org\n" +
                "🌐 Website: www.everygirlsdream.org";
        tvContact.setText(contact);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}