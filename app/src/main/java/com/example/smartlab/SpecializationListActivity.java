package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.DoctorsAdapter;
import com.example.smartlab.Models.Doctors;
import com.example.smartlab.Models.Specialization;
import com.example.smartlab.Models.SpecializationCategories;
import com.example.smartlab.filter.DoctorsFilterDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpecializationListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSpecializationList;
    private TextView categoryTitleText;
    private int categoryId;
    private String categoryTitle;
    private ImageButton textFiltering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_specialization_list);
        recyclerViewSpecializationList = findViewById(R.id.recyclerViewSpecializationList);
        categoryTitleText = findViewById(R.id.categoryTitleText);
        categoryId = getIntent().getIntExtra("category_specialization_id", 0);
        categoryTitle = getIntent().getStringExtra("category_specialization_title");
        textFiltering = findViewById(R.id.textFiltering);
        textFiltering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSpecializations();
            }
        });

        categoryTitleText.setText(getString(R.string.categories) + ": " + categoryTitle);

        ImageButMenu();
        ImageButClickMenu();
        getAllDoctorsSpecialization(categoryId);
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
        List<String> nameFilters = new ArrayList<>();

        if (priceFrom != null) {
            query.append("price_of_admission=gte.").append(priceFrom).append("&");
            nameFilters.add(getString(R.string.text_filter_1) + " " + priceFrom + " " + getString(R.string.text_rub));
        }
        if (priceTo != null) {
            query.append("price_of_admission=lte.").append(priceTo).append("&");
            nameFilters.add(getString(R.string.text_filter_2) + " " + priceTo + " " + getString(R.string.text_rub));
        }

        if (minRating > 0) {
            query.append("rating=gte.").append(minRating).append("&");
            nameFilters.add(getString(R.string.text_filter_3) + " " + minRating);
        }

        if (!selectedSpecializations.isEmpty()) {
            query.append("id_specialization=in.(")
                    .append(String.join(",", selectedSpecializations))
                    .append(")&");
            nameFilters.add(getString(R.string.text_filter_4));
        }

        if (query.length() > 0 && query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        if(!query.toString().isEmpty()){
            getAllDoctorsFilter(query.toString());
            if (nameFilters.isEmpty()) {
                categoryTitleText.setText(getString(R.string.text_filter_5));
            } else {
                categoryTitleText.setText(getString(R.string.text_filter_6) + " " + String.join(",\n", nameFilters));
            }
        } else {
            getAllDoctorsSpecialization(categoryId);
            categoryTitleText.setText(getString(R.string.categories) + ": " + categoryTitle);
        }

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
                    recyclerViewSpecializationList.setAdapter(doctorsAdapter);
                    recyclerViewSpecializationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

    private void getAllDoctorsSpecialization(int categoryId){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllDoctorsSpecialization(categoryId, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllDoctorsSpecialization:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllDoctorsSpecialization:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Doctors>>(){}.getType();
                    List<Doctors> doctorsList = gson.fromJson(responseBody, type);
                    DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getApplicationContext(), doctorsList);
                    recyclerViewSpecializationList.setAdapter(doctorsAdapter);
                    recyclerViewSpecializationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
