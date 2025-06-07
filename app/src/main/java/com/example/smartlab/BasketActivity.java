package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_basket);
        ImageButMenu();
        ImageButClickMenu();
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
}
