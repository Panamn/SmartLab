package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.DoctorsAdapter;
import com.example.smartlab.Models.Doctors;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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

        categoryTitleText.setText(getString(R.string.categories) + ":\n" + categoryTitle);

        ImageButMenu();
        ImageButClickMenu();
        getAllDoctorsSpecialization(categoryId);
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
