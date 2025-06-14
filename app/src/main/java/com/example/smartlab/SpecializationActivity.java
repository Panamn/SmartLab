package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesCategoriesAdapter;
import com.example.smartlab.Adapters.SpecializationAdapter;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.Models.SpecializationCategories;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SpecializationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSpecializationAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specialization_doctors);
        recyclerViewSpecializationAll = findViewById(R.id.recyclerViewSpecializationAll);
        ImageButMenu();
        ImageButClickMenu();
        getCategoriesAllSpecialization();

    }
    private void getCategoriesAllSpecialization(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllSpecialization(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getCategoriesAllAnalyzes:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getCategoriesAllAnalyzes:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SpecializationCategories>>(){}.getType();
                    List<SpecializationCategories> specializationCategoriesList = gson.fromJson(responseBody, type);
                    SpecializationAdapter specializationAdapter = new SpecializationAdapter(getApplicationContext(), specializationCategoriesList);
                    recyclerViewSpecializationAll.setAdapter(specializationAdapter);
                    recyclerViewSpecializationAll.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                });
            }
        });
    }

    private void ImageButMenu(){
        ImageButton RecordsButtonMenu = findViewById(R.id.RecordsButtonMenu);
        TextView TextRecordsButtonMenu = findViewById(R.id.TextRecordsButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        RecordsButtonMenu.setImageResource(R.drawable.icon_menu_records_on);
        TextRecordsButtonMenu.setTextColor(getResources().getColor(R.color.blue_text));
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

