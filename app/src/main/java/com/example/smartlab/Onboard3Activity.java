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
    ImageButton button_onboard1, button_onboard2, button_onboard3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboard_3);

        buttonSkip = (Button) findViewById(R.id.ButtonSkip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Onboard3Activity.this, LoginActivity.class));
            }
        });
        button_onboard1 = (ImageButton) findViewById(R.id.button_onboard1);
        button_onboard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Onboard3Activity.this, Onboard1Activity.class));
            }
        });
        button_onboard2 = (ImageButton) findViewById(R.id.button_onboard2);
        button_onboard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Onboard3Activity.this, Onboard2Activity.class));
            }
        });
        button_onboard3 = (ImageButton) findViewById(R.id.button_onboard3);
        button_onboard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Onboard3Activity.this, Onboard3Activity.class));
            }
        });
    }
}
