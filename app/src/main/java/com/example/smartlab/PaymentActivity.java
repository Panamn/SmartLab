package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        statusTextView = findViewById(R.id.statusTextView);

        new Handler().postDelayed(() -> {
            statusTextView.setText(getString(R.string.text_buy2));
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        }, 4000);
    }
}
