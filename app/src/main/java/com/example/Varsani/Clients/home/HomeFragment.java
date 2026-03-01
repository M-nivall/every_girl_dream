package com.example.Varsani.Clients.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.Varsani.Clients.About;
import com.example.Varsani.Clients.Login;
import com.example.Varsani.Clients.Register;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.Clients.Models.UserModel;

public class HomeFragment extends Fragment {

    private SessionHandler session;
    private UserModel user;

    // Buttons
    private Button btnReportEmergency;
    private Button btnEmergencyHotline;
    private Button btnLogin;
    private Button btnRegister;

    // Cards
    private CardView cardRescueCenters;
    private CardView cardCounselling;
    private CardView cardSeminars;
    private CardView cardLearnMore;
    private CardView cardLoginPrompt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize session
        session = new SessionHandler(getContext());

        // Initialize views
        initializeViews(root);

        // Check if user is logged in
        if(session.isLoggedIn()) {
            user = session.getUserDetails();
            hideLoginPrompt();
        }

        // Set click listeners
        setClickListeners();

        return root;
    }

    private void initializeViews(View root) {
        // Emergency buttons
        btnReportEmergency = root.findViewById(R.id.btnReportEmergency);
        btnEmergencyHotline = root.findViewById(R.id.btnEmergencyHotline);

        // Login/Register buttons
        btnLogin = root.findViewById(R.id.btnLogin);
        btnRegister = root.findViewById(R.id.btnRegister);

        // Quick access cards
        cardRescueCenters = root.findViewById(R.id.cardRescueCenters);
        cardCounselling = root.findViewById(R.id.cardCounselling);
        cardSeminars = root.findViewById(R.id.cardSeminars);
        cardLearnMore = root.findViewById(R.id.cardLearnMore);
        cardLoginPrompt = root.findViewById(R.id.cardLoginPrompt);
    }

    private void setClickListeners() {
        // Emergency Report Button (No login required)
        btnReportEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmergencyReport();
            }
        });

        // Emergency Hotline Button
        btnEmergencyHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall();
            }
        });

        // Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        // Register Button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Register.class);
                startActivity(intent);
            }
        });

        // Rescue Centers Card
        cardRescueCenters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRescueCenters();
            }
        });

        // Counselling Card
        cardCounselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCounselling();
            }
        });

        // Seminars Card
        cardSeminars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSeminars();
            }
        });

        // Learn More Card
        cardLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLearnMore();
            }
        });
    }

    /**
     * Opens emergency report page (no login required)
     */
    private void openEmergencyReport() {
        // TODO: Create EmergencyReportActivity and uncomment
        // Intent intent = new Intent(getContext(), EmergencyReportActivity.class);
        // startActivity(intent);

        // Temporary toast until EmergencyReportActivity is created
        Toast.makeText(getContext(), "Opening Emergency Report...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Makes emergency call to FGM hotline
     */
    private void makeEmergencyCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:1195")); // Kenya FGM hotline number

        try {
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Unable to make call", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Opens rescue centers page
     */
    private void openRescueCenters() {
        // TODO: Create RescueCentersActivity and uncomment
        // Intent intent = new Intent(getContext(), RescueCentersActivity.class);
        // startActivity(intent);

        Toast.makeText(getContext(), "Opening Rescue Centers...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Opens counselling page (may require login)
     */
    private void openCounselling() {
        if(session.isLoggedIn()) {
            // TODO: Create CounsellingActivity and uncomment
            // Intent intent = new Intent(getContext(), CounsellingActivity.class);
            // startActivity(intent);

            Toast.makeText(getContext(), "Opening Counselling...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please login to access counselling services", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    /**
     * Opens seminars page
     */
    private void openSeminars() {
        // TODO: Create SeminarsActivity and uncomment
        // Intent intent = new Intent(getContext(), SeminarsActivity.class);
        // startActivity(intent);

        Toast.makeText(getContext(), "Opening Seminars...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Opens learn more / about FGM page
     */
    private void openLearnMore() {
        Intent intent = new Intent(getContext(), About.class);
        startActivity(intent);
    }

    /**
     * Hides login prompt card when user is already logged in
     */
    private void hideLoginPrompt() {
        if(cardLoginPrompt != null) {
            cardLoginPrompt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh login state when fragment resumes
        if(session.isLoggedIn()) {
            user = session.getUserDetails();
            hideLoginPrompt();
        }
    }
}