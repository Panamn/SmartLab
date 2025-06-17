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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Adapters.CategoriesAdapter;
import com.example.smartlab.Adapters.CategoriesSpecializationAdapter;
import com.example.smartlab.Adapters.DoctorsAdapter;
import com.example.smartlab.Adapters.SpecializationAdapter;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.Models.Category;
import com.example.smartlab.Models.Doctors;
import com.example.smartlab.Models.Specialization;
import com.example.smartlab.Models.SpecializationCategories;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerDoctorsAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctors);

        recyclerDoctorsAll = findViewById(R.id.recyclerDoctorsAll);

        getAllDoctors();
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
                    setupCategoriesRecycler(specializationCategoriesList);
                });
            }
        });
    }
    private void setupCategoriesRecycler(List<SpecializationCategories> specializationCategoriesList) {
        List<Specialization> specializations = new ArrayList<>();
        for (SpecializationCategories specializationCategories : specializationCategoriesList) {
            specializations.add(new Specialization(
                    String.valueOf(specializationCategories.getId_specialization_categories()),
                    specializationCategories.getTitle()
            ));
        }
        RecyclerView recyclerView = findViewById(R.id.categoriesRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        CategoriesSpecializationAdapter adapter = new CategoriesSpecializationAdapter(specializations);
        recyclerView.setAdapter(adapter);
    }
    private void getAllDoctors(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllDoctors(new SupaBaseClient.SBC_Callback() {
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
                    Type type = new TypeToken<List<Doctors>>(){}.getType();
                    List<Doctors> doctorsList = gson.fromJson(responseBody, type);
                    DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getApplicationContext(), doctorsList);
                    recyclerDoctorsAll.setAdapter(doctorsAdapter);
                    recyclerDoctorsAll.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
