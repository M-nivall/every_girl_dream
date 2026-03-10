package com.example.Varsani.Clients;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.Varsani.R;
import com.example.Varsani.ReportCases.EmergencyReportActivity;

public class Help_in extends AppCompatActivity {

    private CardView cardEmergencyHelp, cardCallHotline;
    private CardView cardFaq1, cardFaq2, cardFaq3, cardFaq4, cardFaq5;

    private TextView iconFaq1, iconFaq2, iconFaq3, iconFaq4, iconFaq5;
    private TextView answerFaq1, answerFaq2, answerFaq3, answerFaq4, answerFaq5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_in);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help & Support");

        // Initialize Quick Action Cards
        cardEmergencyHelp = findViewById(R.id.cardEmergencyHelp);
        cardCallHotline = findViewById(R.id.cardCallHotline);

        // Initialize FAQ Cards
        cardFaq1 = findViewById(R.id.cardFaq1);
        cardFaq2 = findViewById(R.id.cardFaq2);
        cardFaq3 = findViewById(R.id.cardFaq3);
        cardFaq4 = findViewById(R.id.cardFaq4);
        cardFaq5 = findViewById(R.id.cardFaq5);

        // Initialize FAQ Icons and Answers
        iconFaq1 = findViewById(R.id.iconFaq1);
        iconFaq2 = findViewById(R.id.iconFaq2);
        iconFaq3 = findViewById(R.id.iconFaq3);
        iconFaq4 = findViewById(R.id.iconFaq4);
        iconFaq5 = findViewById(R.id.iconFaq5);

        answerFaq1 = findViewById(R.id.answerFaq1);
        answerFaq2 = findViewById(R.id.answerFaq2);
        answerFaq3 = findViewById(R.id.answerFaq3);
        answerFaq4 = findViewById(R.id.answerFaq4);
        answerFaq5 = findViewById(R.id.answerFaq5);

        // Setup Click Listeners
        setupQuickActions();
        setupFAQs();
    }

    private void setupQuickActions() {
        // Emergency Report
        cardEmergencyHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Help_in.this, EmergencyReportActivity.class);
                startActivity(intent);
            }
        });

        // Call Hotline
        cardCallHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall();
            }
        });
    }

    private void setupFAQs() {
        // FAQ 1
        cardFaq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFAQ(answerFaq1, iconFaq1);
            }
        });

        // FAQ 2
        cardFaq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFAQ(answerFaq2, iconFaq2);
            }
        });

        // FAQ 3
        cardFaq3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFAQ(answerFaq3, iconFaq3);
            }
        });

        // FAQ 4
        cardFaq4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFAQ(answerFaq4, iconFaq4);
            }
        });

        // FAQ 5
        cardFaq5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFAQ(answerFaq5, iconFaq5);
            }
        });
    }

    /**
     * Toggle FAQ answer visibility
     */
    private void toggleFAQ(TextView answer, TextView icon) {
        if (answer.getVisibility() == View.GONE) {
            // Show answer
            answer.setVisibility(View.VISIBLE);
            icon.setText("▲");
        } else {
            // Hide answer
            answer.setVisibility(View.GONE);
            icon.setText("▼");
        }
    }

    /**
     * Make emergency call
     */
    private void makeEmergencyCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:1195"));

        try {
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to make call", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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