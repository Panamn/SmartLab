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
import com.example.smartlab.filter.AnalysisFilterDialog;
import com.example.smartlab.filter.DoctorsFilterDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerDoctorsAll;
    private ImageButton textFiltering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctors);

        recyclerDoctorsAll = findViewById(R.id.recyclerDoctorsAll);
        textFiltering = findViewById(R.id.textFiltering);
        textFiltering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSpecializations();
            }
        });


        getAllDoctors();
        ImageButMenu();
        ImageButClickMenu();
        getCategoriesAllSpecialization();

    }

    private void showFilterDialog(List<SpecializationCategories> specializationList) {
        List<Specialization> specialization = new ArrayList<>();
        for (SpecializationCategories category : specializationList) {
            specialization.add(new Specialization(
                    String.valueOf(category.getId_specialization_categories()),
                    category.getTitle()
            ));
        }


        DoctorsFilterDialog dialog = new DoctorsFilterDialog(
                this,
                specialization,
                new DoctorsFilterDialog.FilterListener() {
                    @Override
                    public void onFilterApplied(Integer priceFrom, Integer priceTo, float minRating, Set<String> selectedSpecializations) {
                        applyFilters(priceFrom, priceTo, minRating, selectedSpecializations);
                    }
                }
        );

        dialog.show();
    }
    private void applyFilters(Integer priceFrom, Integer priceTo, float minRating, Set<String> selectedSpecializations) {

        StringBuilder query = new StringBuilder();

        if (priceFrom != null) {
            query.append("price_of_admission=gte.").append(priceFrom).append("&");
        }
        if (priceTo != null) {
            query.append("price_of_admission=lte.").append(priceTo).append("&");
        }

        if (minRating > 0) {
            query.append("rating=gte.").append(minRating).append("&");
        }

        if (!selectedSpecializations.isEmpty()) {
            query.append("id_specialization=in.(")
                    .append(String.join(",", selectedSpecializations))
                    .append(")&");
        }

        if (query.length() > 0 && query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }
        Log.e("query", query.toString());
        getAllDoctorsFilter(query.toString());
    }

    private void getAllDoctorsFilter(String query){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllDoctorsFilters(query, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllDoctorsFilter:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllDoctorsFilter:onResponse", responseBody);
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

    private void loadSpecializations() {
        SupaBaseClient client = new SupaBaseClient();
        client.fetchAllSpecialization(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                Log.e("DoctorsActivity", "Error loading specializations", e);
            }
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SpecializationCategories>>(){}.getType();
                    List<SpecializationCategories> specializationList = gson.fromJson(response, type);
                    showFilterDialog(specializationList);
                });
            }

        });
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
