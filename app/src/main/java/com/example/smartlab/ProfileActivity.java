package com.example.smartlab;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final String PIN = "Pin";
    private static final String KEY_PIN = "saved_pin";

    private ASession aSession;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        aSession = new ASession(this);
        exitButton = findViewById(R.id.buttonExitSession);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aSession.clearSession();
                SharedPreferences.Editor editor = getSharedPreferences(PIN, MODE_PRIVATE).edit();
                editor.remove(KEY_PIN);
                editor.apply();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });

        ImageButMenu();
        ImageButClickMenu();
    }
    private void ImageButMenu(){
        ImageButton ProfileButtonMenu = findViewById(R.id.ProfileButtonMenu);
        TextView TextProfileButtonMenu = findViewById(R.id.TextProfileButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        ProfileButtonMenu.setImageResource(R.drawable.icon_menu_profile_on);
        TextProfileButtonMenu.setTextColor(getResources().getColor(R.color.blue_text));
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
