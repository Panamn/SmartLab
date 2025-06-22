package com.example.smartlab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Adapters.DoctorsAdapter;
import com.example.smartlab.Adapters.NewsAdapter;
import com.example.smartlab.Adapters.RecordsAdapter;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.Doctors;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.Models.Records;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class RecordsDoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records_with_doctors);

            try {
        recyclerRecords = findViewById(R.id.recyclerRecords);

        getAllRecords();
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }

    }
    private void getAllRecords(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllRecords(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllDoctors:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllDoctors:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Records>>(){}.getType();
                    List<Records> records = gson.fromJson(responseBody, type);
                    RecordsAdapter recordsAdapter = new RecordsAdapter(getApplicationContext(), records);
                    recyclerRecords.setAdapter(recordsAdapter);
                    recyclerRecords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });
    }
}