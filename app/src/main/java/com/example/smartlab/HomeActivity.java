package com.example.smartlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Models.Analyzes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button buttonAnalyzes, buttonDoctors;
    private RecyclerView recyclerAllLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        buttonAnalyzes = findViewById(R.id.buttonAnalyzes);
        buttonDoctors = findViewById(R.id.buttonDoctors);
        recyclerAllLab = findViewById(R.id.recyclerAllLab);
        buttonAllLab();
        ImageButClickMenu();
        View.OnFocusChangeListener focusListener = (v, hasFocus) -> {
            if (hasFocus) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        };
    }
    private void buttonAllLab(){
        buttonAnalyzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonAnalyzes.setTextColor(getResources().getColor(R.color.white));
                buttonDoctors.setTextColor(getResources().getColor(R.color.gray_text));
                buttonAnalyzes.setBackgroundResource(R.drawable.blue_button_background);
                buttonDoctors.setBackgroundResource(R.drawable.gray_button_background);
                getAllAnalyzes();
            }
        });
        buttonDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonDoctors.setTextColor(getResources().getColor(R.color.white));
                buttonAnalyzes.setTextColor(getResources().getColor(R.color.gray_text));
                buttonAnalyzes.setBackgroundResource(R.drawable.gray_button_background);
                buttonDoctors.setBackgroundResource(R.drawable.blue_button_background);
            }
        });

    }
    private void getAllAnalyzes(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllAnalyzes(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllAnalyzes:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllAnalyzes:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Analyzes>>(){}.getType();
                    List<Analyzes> analyzesList = gson.fromJson(responseBody, type);
                    AnalyzesAdapter analyzesAdapter = new AnalyzesAdapter(getApplicationContext(), analyzesList);
                    recyclerAllLab.setAdapter(analyzesAdapter);
                    recyclerAllLab.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });
    }
    private void ImageButClickMenu(){
        ImageButton AnalyzesButtonMenu = findViewById(R.id.AnalyzesButtonMenu);
        ImageButton RecordsButtonMenu = findViewById(R.id.RecordsButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        ImageButton BasketButtonMenu = findViewById(R.id.BasketButtonMenu);
        ImageButton ProfileButtonMenu = findViewById(R.id.ProfileButtonMenu);

        AnalyzesButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, AnalyzesActivity.class)));
        RecordsButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, DoctorsActivity.class)));
        HomeButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        BasketButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, BasketActivity.class)));
        ProfileButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

}