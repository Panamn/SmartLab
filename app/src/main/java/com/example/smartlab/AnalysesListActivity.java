package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.Models.Category;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.UpdateBasket;
import com.example.smartlab.filter.AnalysisFilterDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AnalysesListActivity extends AppCompatActivity implements AnalyzesAdapter.OnItemClickListener{

    private RecyclerView recyclerViewCategoriesListAnalyzes;
    private TextView categoryTitleText, priceTextView;
    private int categoryId;
    private String categoryTitle;
    private ImageButton textFiltering;
    private int totalPrice = 0;
    private RelativeLayout relative;
    private Button basketButton, searchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories_list_analyses);

            try {
        basketButton = findViewById(R.id.basketButton);
        relative = findViewById(R.id.relative);
        priceTextView = findViewById(R.id.priceTextView);
        recyclerViewCategoriesListAnalyzes = findViewById(R.id.recyclerViewCategoriesListAnalyzes);
        categoryTitleText = findViewById(R.id.categoryTitleText);
        categoryId = getIntent().getIntExtra("category_id", 0); //-1
        categoryTitle = getIntent().getStringExtra("category_title");


        categoryTitleText.setText(getString(R.string.categories) + ": " + categoryTitle);

        ImageButMenu();
        ImageButClickMenu();
        getCategoriesListAnalyzes(categoryId);
        textFiltering = findViewById(R.id.textFiltering);
        textFiltering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoriesAllAnalyzes();
            }
        });
        searchEditText = findViewById(R.id.EditTextSearch);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AnalysesListActivity.this,
                        getString(R.string.text_not_feature),
                        Toast.LENGTH_SHORT).show();
            }
        });
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }
    }
    private void getCategoriesAllAnalyzes(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCategoriesAllAnalyzes(new SupaBaseClient.SBC_Callback() {
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
                    Type type = new TypeToken<List<CategoriesAnalyzes>>(){}.getType();
                    List<CategoriesAnalyzes> categoriesAnalyzesList = gson.fromJson(responseBody, type);
                    showFilterDialog(categoriesAnalyzesList);
                });
            }
        });
    }
    private void showFilterDialog(List<CategoriesAnalyzes> categoriesList) {
        List<Category> categories = new ArrayList<>();
        for (CategoriesAnalyzes category : categoriesList) {
            categories.add(new Category(
                    category.getId_categories_of_analyses(),
                    category.getTitle()
            ));
        }


        AnalysisFilterDialog dialog = new AnalysisFilterDialog(
                this,
                categories,
                new AnalysisFilterDialog.FilterListener() {
                    @Override
                    public void onFilterApplied(Integer priceFrom, Integer priceTo, int maxDays, Set<String> selectedCategories) {
                        applyFilters(priceFrom, priceTo, maxDays, selectedCategories);
                    }
                }
        );

        dialog.show();
    }

    private void applyFilters(Integer priceFrom, Integer priceTo, int maxDays, Set<String> selectedCategories) {

        StringBuilder query = new StringBuilder();
        List<String> nameFilters = new ArrayList<>();

        if (priceFrom != null) {
            query.append("price=gte.").append(priceFrom).append("&");
            nameFilters.add(getString(R.string.text_filter_1) + " " + priceFrom + " " + getString(R.string.text_rub));
        }
        if (priceTo != null) {
            query.append("price=lte.").append(priceTo).append("&");
            nameFilters.add(getString(R.string.text_filter_2) + " " + priceTo + " " + getString(R.string.text_rub));
        }

        if (maxDays > 0) {
            query.append("period_of_execution=lte.").append(maxDays).append("&");
            nameFilters.add(getString(R.string.text_filter_3_3) + " " + maxDays);
        }

        if (!selectedCategories.isEmpty()) {
            query.append("id_categories_of_analyses=in.(")
                    .append(String.join(",", selectedCategories))
                    .append(")&");
            nameFilters.add(getString(R.string.text_filter_4_4));
        }

        if (query.length() > 0 && query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }
        if(!query.toString().isEmpty()){
            loadAnalysesFilter(query.toString());
            if (nameFilters.isEmpty()) {
                categoryTitleText.setText(getString(R.string.text_filter_5));
            } else {
                categoryTitleText.setText(getString(R.string.text_filter_6) + " " + String.join(",\n", nameFilters));
            }
        } else {
            loadAnalysesFilter(query.toString());
            categoryTitleText.setText(getString(R.string.categories) + ": " + categoryTitle);
        }

    }
    private void loadAnalysesFilter(String query) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllAnalyzesFilters(query, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("loadAnalysesFilter:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("loadAnalysesFilter:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Analyzes>>(){}.getType();
                    List<Analyzes> analyzesList = gson.fromJson(responseBody, type);
                    AnalyzesAdapter analyzesAdapter = new AnalyzesAdapter(getApplicationContext(), analyzesList, AnalysesListActivity.this);
                    recyclerViewCategoriesListAnalyzes.setAdapter(analyzesAdapter);
                    recyclerViewCategoriesListAnalyzes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });

    }
    private void getCategoriesListAnalyzes(int categoryId){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCategoriesListAnalyzes(categoryId, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getCategoriesListAnalyzes:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getCategoriesListAnalyzes:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Analyzes>>(){}.getType();
                    List<Analyzes> analyzesList = gson.fromJson(responseBody, type);
                    AnalyzesAdapter analyzesAdapter = new AnalyzesAdapter(getApplicationContext(), analyzesList, AnalysesListActivity.this);
                    recyclerViewCategoriesListAnalyzes.setAdapter(analyzesAdapter);
                    recyclerViewCategoriesListAnalyzes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

    @Override
    public void onItemClick(Analyzes analyzes) {
        AnalyzeDetailFragment fragment = new AnalyzeDetailFragment();
        fragment.setAnalyzeData(analyzes);
        fragment.show(getSupportFragmentManager(), "analyze_detail");
    }

    @Override
    public void onAddToCartClick(Analyzes analyzes) {
        String userId = DataBinding.getUuidUser();

        updateBasket(analyzes.getId_analyzes(), userId);
        totalPrice += analyzes.getPrice();
        relative.setVisibility(View.VISIBLE);
        updateTotalPrice();
    }
    private void updateTotalPrice() {
        if (totalPrice > 0) {
            priceTextView.setText(String.format(Locale.getDefault(), "%d ₽", totalPrice));
        } else {
            priceTextView.setText("0 ₽");
        }
    }

    public void updateBasket(int id_analyzes, String id_client){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        UpdateBasket updateBasket = new UpdateBasket(id_analyzes, id_client);
        supaBaseClient.updateBasket(updateBasket, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("updateBasket:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("updateBasket:onResponse", responseBody);
                });
            }
        });

    }
}