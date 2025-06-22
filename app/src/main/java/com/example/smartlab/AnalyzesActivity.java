package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Adapters.AnalyzesCategoriesAdapter;
import com.example.smartlab.Adapters.CategoriesAdapter;
import com.example.smartlab.Models.Basket;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.ProfileUpdate;
import com.example.smartlab.Models.UpdateBasket;
import com.example.smartlab.filter.AnalysisFilterDialog;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AnalyzesActivity extends AppCompatActivity implements AnalyzesAdapter.OnItemClickListener{

    private RecyclerView recyclerViewAnalyzes;
    private TextView priceTextView;
    private ImageButton textFiltering;
    private Set<Integer> cartItems = new HashSet<>();
    private AnalyzesAdapter analyzesAdapter;
    private int totalPrice = 0;
    private RelativeLayout relative;
    private Button basketButton;
    private int id_additions = 0;
    private Button searchEditText;
    private List<Analyzes> analyzesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analyzes);
    try {
        basketButton = findViewById(R.id.basketButton);
        relative = findViewById(R.id.relative);
        priceTextView = findViewById(R.id.priceTextView);
        recyclerViewAnalyzes = findViewById(R.id.recyclerViewAnalyzes);
        textFiltering = findViewById(R.id.textFiltering);
        searchEditText = findViewById(R.id.EditTextSearch);
        getCategoriesAnalyzes();
        getAllAnalyzes();
        textFiltering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoriesAllAnalyzes();
            }
        });
        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AnalyzesActivity.this, BasketActivity.class));
            }
        });

        ImageButMenu();
        ImageButClickMenu();

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AnalyzesActivity.this,
                        getString(R.string.text_not_feature),
                        Toast.LENGTH_SHORT).show();
            }
        });
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }
    }
    @Override
    public void onItemClick(Analyzes analyzes) {
        AnalyzeDetailFragment fragment = new AnalyzeDetailFragment();
        fragment.setAnalyzeData(analyzes);
        totalPrice += analyzes.getPrice();
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
    private void getBasketDelete(int id_additions){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.deleteBasket(String.valueOf(id_additions), new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getBasketDelete:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getBasketDelete:onResponse", responseBody);
                });
            }
        });
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
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        id_additions = json.getInt("id_additions");
                    } catch (JSONException e) {
                        Log.e("Ошибка обработки ответа", "0");
                    }
                });
            }
        });

    }
    private void getCategoriesAnalyzes(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCategoriesAllAnalyzes(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getCategoriesAnalyzes:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getCategoriesAnalyzes:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<CategoriesAnalyzes>>(){}.getType();
                    List<CategoriesAnalyzes> categoriesAnalyzesList = gson.fromJson(responseBody, type);
                    setupCategoriesRecycler(categoriesAnalyzesList);
                });
            }
        });
    }
    private void setupCategoriesRecycler(List<CategoriesAnalyzes> categoriesList) {
        List<Category> categories = new ArrayList<>();
        for (CategoriesAnalyzes category : categoriesList) {
            categories.add(new Category(
                    category.getId_categories_of_analyses(),
                    category.getTitle()
            ));
        }
        RecyclerView recyclerView = findViewById(R.id.categoriesRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        CategoriesAdapter adapter = new CategoriesAdapter(categories);
        recyclerView.setAdapter(adapter);
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


        if (priceFrom != null) {
            query.append("price=gte.").append(priceFrom).append("&");
        }
        if (priceTo != null) {
            query.append("price=lte.").append(priceTo).append("&");
        }

        if (maxDays > 0) {
            query.append("period_of_execution=lte.").append(maxDays).append("&");
        }

        if (!selectedCategories.isEmpty()) {
            query.append("id_categories_of_analyses=in.(")
                    .append(String.join(",", selectedCategories))
                    .append(")&");
        }

        if (query.length() > 0 && query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        loadAnalysesFilter(query.toString());
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
                    AnalyzesAdapter analyzesAdapter = new AnalyzesAdapter(getApplicationContext(), analyzesList, AnalyzesActivity.this);
                    recyclerViewAnalyzes.setAdapter(analyzesAdapter);
                    recyclerViewAnalyzes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
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
                    analyzesList = gson.fromJson(responseBody, type);
                    AnalyzesAdapter analyzesAdapter = new AnalyzesAdapter(getApplicationContext(), analyzesList, AnalyzesActivity.this);
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
