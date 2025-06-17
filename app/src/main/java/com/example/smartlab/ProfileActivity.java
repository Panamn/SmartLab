package com.example.smartlab;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.ASession;
import com.example.smartlab.Models.Profile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String PIN = "Pin";
    private static final String KEY_PIN = "saved_pin";

    private ImageView imageUserView;
    private ASession aSession;
    private Button exitButton, cardButton, recordsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        aSession = new ASession(this);
        exitButton = findViewById(R.id.buttonExitSession);
        recordsButton = findViewById(R.id.recordsDoctorsButton);
        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, RecordsDoctorsActivity.class));
            }
        });
        imageUserView = findViewById(R.id.imageUser);

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
        cardButton = findViewById(R.id.cardButton);
        cardButton.setOnClickListener(view -> { startActivity(new Intent(ProfileActivity.this, ProfileCardActivity.class));});
        getProfileCard();
        ImageButMenu();
        ImageButClickMenu();
    }
    private void getProfileCard(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCurrentUser(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getProfileCard:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getProfileCard:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Profile>>(){}.getType();
                    List<Profile> profileList = gson.fromJson(responseBody, type);
                    if(profileList != null && !profileList.isEmpty()) {
                        Profile profile = profileList.get(0);
                        String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/avatars/";
                        Glide.with(getApplicationContext())
                                .load(url + profile.getAvatar_url())
                                .placeholder(R.drawable.brock)
                                .error(R.drawable.icon_error)
                                .into(imageUserView);
                    }
                });
            }
        });

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
