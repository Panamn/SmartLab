package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.BasketAdapter;
import com.example.smartlab.Adapters.NewsAdapter;
import com.example.smartlab.Models.Basket;
import com.example.smartlab.Models.PromotionsNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class BasketActivity extends AppCompatActivity implements BasketAdapter.OnItemClickListener{

    private RecyclerView recyclerBasket;
    private ImageButton imageDeleteAll;
    private TextView sumPriceTextView;
    private Button buyButton;
    private List<Basket> basketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basket);

        recyclerBasket = findViewById(R.id.recyclerBasket);
        imageDeleteAll = findViewById(R.id.imageDeleteAll);
        sumPriceTextView = findViewById(R.id.sumPriceTextView);
        buyButton = findViewById(R.id.buyButton);

        buyButton.setOnClickListener(v -> startActivity(new Intent(BasketActivity.this, BuyActivity.class)));

        imageDeleteAllAnalyzes();
        getAllBasket();
        ImageButMenu();
        ImageButClickMenu();
    }
    private void imageDeleteAllAnalyzes(){
        imageDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(BasketActivity.this)
                        .setTitle(getString(R.string.text_delete_1))
                        .setMessage(getString(R.string.text_delete_2))
                        .setPositiveButton(getString(R.string.text_yes), (dialog, which) -> {
                            getAllBasketDelete();
                        })
                        .setNegativeButton(getString(R.string.text_no), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setCancelable(true)
                        .show();
            }
        });
    }
    private void getAllBasket(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllBasket(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllBasket:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllBasket:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Basket>>(){}.getType();
                    basketList = gson.fromJson(responseBody, type);
                    BasketAdapter basketAdapter = new BasketAdapter(getApplicationContext(), basketList, BasketActivity.this);
                    recyclerBasket.setAdapter(basketAdapter);
                    recyclerBasket.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    updateTotal();
                });
            }
        });
    }
    private void getAllBasketDelete(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.deleteAllBasket(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllBasketDelete:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllBasketDelete:onResponse", responseBody);
                    getAllBasket();
                });
            }
        });
    }
    private void ImageButMenu(){
        ImageButton BasketButtonMenu = findViewById(R.id.BasketButtonMenu);
        TextView TextBasketButtonMenu = findViewById(R.id.TextBasketButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        BasketButtonMenu.setImageResource(R.drawable.icon_menu_basket_on);
        TextBasketButtonMenu.setTextColor(getResources().getColor(R.color.blue_text));
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
    private void updateTotal() {
        int total = calculateTotal();
        sumPriceTextView.setText(String.format(Locale.getDefault(), "%d ₽", total));
    }

    private int calculateTotal() {
        int sum = 0;
        for (Basket item : basketList) {
            sum += item.getAnalyzes().getPrice();
        }
        return sum;
    }

    @Override
    public void onDeleteToCartClick(Basket basket) {
        String id_additions = String.valueOf(basket.getId_additions());
        new AlertDialog.Builder(BasketActivity.this)
                .setTitle(getString(R.string.text_delete_1))
                .setMessage(getString(R.string.text_delete_3))
                .setPositiveButton(getString(R.string.text_yes), (dialog, which) -> {
                    getBasketDelete(id_additions);
                })
                .setNegativeButton(getString(R.string.text_no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();

    }
    private void getBasketDelete(String id_additions){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.deleteBasket(id_additions, new SupaBaseClient.SBC_Callback() {
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
                    getAllBasket();
                });
            }
        });
    }
}
