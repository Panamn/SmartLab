package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlab.Models.AuthResponse;
import com.example.smartlab.Models.InputEmail;
import com.google.gson.Gson;

import java.io.IOException;

public class EmailInputActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button sendCodeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_input);

        emailEditText = findViewById(R.id.emailEditText);
        sendCodeButton = findViewById(R.id.sendCodeButton);

        sendCodeButton.setOnClickListener(v -> sendRecoveryRequest());
    }

    private void sendRecoveryRequest(){
        String email = emailEditText.getText().toString().trim();
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        InputEmail inputEmail = new InputEmail(email, "recovery");
        supaBaseClient.sendRecoveryRequest(String.valueOf(inputEmail), new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("sendRecoveryRequest:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("sendRecoveryRequest:onResponse", responseBody);
                    Intent intent = new Intent(EmailInputActivity.this, CodeInputActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                });
            }
        });
    }
}
