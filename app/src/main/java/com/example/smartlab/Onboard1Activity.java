package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Onboard1Activity extends AppCompatActivity {

    Button buttonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboard_1);

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
                startActivity(new Intent(Onboard1Activity.this, LoginActivity.class));
            }
        });



    }
    private void navigateToHome() {
        startActivity(new Intent(Onboard1Activity.this, Onboard2Activity.class));
    }
}