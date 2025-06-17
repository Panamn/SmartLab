package com.example.smartlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Adapters.DoctorsAdapter;
import com.example.smartlab.Adapters.NewsAdapter;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.Doctors;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.Models.UpdateBasket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AnalyzesAdapter.OnItemClickListener{

    private Button buttonAnalyzes, buttonDoctors;
    private RecyclerView recyclerAnalyzes, recyclerDoctors, recyclerViewNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        buttonAnalyzes = findViewById(R.id.buttonAnalyzes);
        buttonDoctors = findViewById(R.id.buttonDoctors);
        recyclerAnalyzes = findViewById(R.id.recyclerAnalyzes);
        recyclerDoctors = findViewById(R.id.recyclerDoctors);
        recyclerViewNews = findViewById(R.id.recyclerViewNews);

        buttonAnalyzes.setOnClickListener(view -> getAllAnalyzes());
        buttonDoctors.setOnClickListener(v -> getAllDoctors());
        getAllAnalyzes();
        getAllNews();
        ImageButClickMenu();
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

                    recyclerAnalyzes.setVisibility(View.VISIBLE);
                    recyclerDoctors.setVisibility(View.GONE);
                    buttonAnalyzes.setBackgroundResource(R.drawable.blue_button_background);
                    buttonAnalyzes.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.white));

                    buttonDoctors.setBackgroundResource(R.drawable.gray_button_background);
                    buttonDoctors.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.black));

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Analyzes>>(){}.getType();
                    List<Analyzes> analyzesList = gson.fromJson(responseBody, type);
                    AnalyzesAdapter analyzesAdapter = new AnalyzesAdapter(getApplicationContext(), analyzesList, HomeActivity.this);
                    recyclerAnalyzes.setAdapter(analyzesAdapter);
                    recyclerAnalyzes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });
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

                    recyclerDoctors.setVisibility(View.VISIBLE);
                    recyclerAnalyzes.setVisibility(View.GONE);
                    buttonDoctors.setBackgroundResource(R.drawable.blue_button_background);
                    buttonDoctors.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.white));

                    buttonAnalyzes.setBackgroundResource(R.drawable.gray_button_background);
                    buttonAnalyzes.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.black));

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Doctors>>(){}.getType();
                    List<Doctors> doctorsList = gson.fromJson(responseBody, type);
                    DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getApplicationContext(), doctorsList);
                    recyclerDoctors.setAdapter(doctorsAdapter);
                    recyclerDoctors.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });
    }
    private void getAllNews(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllNews(new SupaBaseClient.SBC_Callback() {
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
                    Type type = new TypeToken<List<PromotionsNews>>(){}.getType();
                    List<PromotionsNews> promotionsNewsList = gson.fromJson(responseBody, type);
                    NewsAdapter newsAdapter = new NewsAdapter(getApplicationContext(), promotionsNewsList);
                    recyclerViewNews.setAdapter(newsAdapter);
                    recyclerViewNews.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
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

    @Override
    public void onItemClick(Analyzes analyzes) {

    }

    @Override
    public void onAddToCartClick(Analyzes analyzes, boolean addToCart) {
        String userId = DataBinding.getUuidUser();

        if (addToCart) {
            updateBasket(analyzes.getId_analyzes(), userId);
        } else {
//            removeFromBasket(analyzes.getId_analyzes(), userId);
        }
    }
    public void updateBasket(int id_analyzes, String id_client){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        UpdateBasket updateBasket = new UpdateBasket(id_analyzes, id_client);
        supaBaseClient.updateBasket(updateBasket, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("updateProfile:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("updateProfile:onResponse", responseBody);
                });
            }
        });

    }
}