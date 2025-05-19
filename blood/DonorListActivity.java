package com.example.blood;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DonorListActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText searchBlood, searchLocation;
    private Button btnSearch;
    private RecyclerView donorRecyclerView;
    private DonorAdapter donorAdapter;
    private ArrayList<Donor> donorList; // Changed from ArrayList<String> to ArrayList<Donor>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize UI components
        searchBlood = findViewById(R.id.searchBlood);
        searchLocation = findViewById(R.id.searchLocation);
        btnSearch = findViewById(R.id.btnSearch);
        donorRecyclerView = findViewById(R.id.donorRecyclerView);
        donorList = new ArrayList<>();

        // Setup RecyclerView
        donorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorAdapter = new DonorAdapter(donorList); // Pass only donorList
        donorRecyclerView.setAdapter(donorAdapter);

        // Search Button Click Listener
        if (btnSearch != null) {
            btnSearch.setOnClickListener(view -> searchDonors());
        }
    }

    private void searchDonors() {
        String bloodType = searchBlood.getText().toString().trim();
        String location = searchLocation.getText().toString().trim();

        if (bloodType.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please enter blood group and location", Toast.LENGTH_SHORT).show();
            return;
        }

        donorList.clear();

        // Use try-with-resources to close cursor automatically
        try (Cursor cursor = db.searchDonors(bloodType, location)) {
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(1);
                    String blood = cursor.getString(2);
                    String loc = cursor.getString(3);

                    // Add donor objects to the list instead of String
                    donorList.add(new Donor(name, blood, loc));
                }
                donorAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No donors found!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving donors!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
