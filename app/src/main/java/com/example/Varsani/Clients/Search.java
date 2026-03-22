package com.example.Varsani.Clients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.MainActivity;
import com.example.Varsani.R;
import com.example.Varsani.Seminars.Adapters.AdapterSeminars;
import com.example.Varsani.Seminars.Models.SeminarModel;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private SessionHandler session;
    private UserModel user;
    private List<SeminarModel> list;
    private AdapterSeminars adapterSeminars;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");

        // ✅ Use only ONE layout manager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        session = new SessionHandler(this);
        user = session.getUserDetails();

        list = new ArrayList<>();

        getProducts(); // fixed name
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        setupSearch(searchView);
        return true;
    }

    private void setupSearch(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // ✅ Prevent crash
                if (adapterSeminars != null) {
                    adapterSeminars.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    public void getProducts() {

        progressBar.setVisibility(View.VISIBLE); // ✅ show loading

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_SEMINARS,
                response -> {
                    try {
                        Log.e("Response", response);

                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("details");

                            list.clear(); // ✅ avoid duplicates

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsn = jsonArray.getJSONObject(i);

                                SeminarModel model = new SeminarModel(
                                        jsn.getString("seminarID"),
                                        jsn.getString("title"),
                                        jsn.getString("location"),
                                        jsn.getString("seminarDate"),
                                        jsn.getString("seminarTime"),
                                        jsn.getString("description"),
                                        jsn.getString("seminarStatus")
                                );

                                list.add(model);
                            }

                            // ✅ Use Activity context (IMPORTANT)
                            adapterSeminars = new AdapterSeminars(Search.this, list);
                            recyclerView.setAdapter(adapterSeminars);

                        } else {
                            Toast.makeText(Search.this, "No seminars found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Search.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(Search.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}