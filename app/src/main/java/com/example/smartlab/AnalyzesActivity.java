package com.example.smartlab;

import android.content.Intent;
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
import com.example.smartlab.Adapters.AnalyzesCategoriesAdapter;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AnalyzesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAnalyzes;
    private Button buttonCategoriesAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analyzes);
        recyclerViewAnalyzes = findViewById(R.id.recyclerViewAnalyzes);
        buttonCategoriesAll = findViewById(R.id.buttonCategoriesAll);
        buttonCategoriesAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnalyzesActivity.this, CategoriesAnalyzes.class));
            }
        });
        ImageButMenu();
        ImageButClickMenu();
        getAllAnalyzes();

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
                    recyclerViewAnalyzes.setAdapter(analyzesAdapter);
                    recyclerViewAnalyzes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });
    }
    private void ImageButMenu(){
        ImageButton AnalyzesButtonMenu = findViewById(R.id.AnalyzesButtonMenu);
        TextView TextAnalyzesButtonMenu = findViewById(R.id.TextAnalyzesButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        AnalyzesButtonMenu.setImageResource(R.drawable.icon_menu_analyzes_on);
        TextAnalyzesButtonMenu.setTextColor(getResources().getColor(R.color.blue_text));
        HomeButtonMenu.setImageResource(R.drawable.icon_menu_home_off);
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
