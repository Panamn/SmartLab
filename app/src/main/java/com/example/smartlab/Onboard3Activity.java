package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Onboard3Activity extends AppCompatActivity {

    Button buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboard_3);

            try {
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        buttonSkip = (Button) findViewById(R.id.ButtonSkip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Onboard3Activity.this, LoginActivity.class));
            }
        });
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }

    }
    private void navigateToHome() {
        startActivity(new Intent(Onboard3Activity.this, LoginActivity.class));
    }
}
