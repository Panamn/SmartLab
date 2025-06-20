package com.example.smartlab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartlab.Models.AuthResponse;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.InputEmail;
import com.google.gson.Gson;

import java.io.IOException;

public class EmailInputActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button sendCodeButton;
    private ImageButton imageButtonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_input);

        emailEditText = findViewById(R.id.emailEditText);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(v -> finish());

        updateButton(false);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateButton(validateEmail());}
        });

        sendCodeButton.setOnClickListener(v -> {
            if (validateEmail()) {
                sendRecoveryRequest();
            }
        });
    }
    private boolean validateEmail() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.text_validate_1));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.text_validate_2));
            return false;
        }

        emailEditText.setError(null);
        updateButton(true);
        return true;
    }
    private void updateButton(boolean isActive) {
        if(isActive == true){
            sendCodeButton.setBackgroundResource(R.drawable.blue_button_background);
            sendCodeButton.setEnabled(true);
        }else {
            sendCodeButton.setBackgroundResource(R.drawable.blue_button_background_off);
            sendCodeButton.setEnabled(false);
        }
    }
    private void sendRecoveryRequest(){
        String email = emailEditText.getText().toString().trim();
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        InputEmail inputEmail = new InputEmail("email", email);
        supaBaseClient.sendRecoveryRequest(inputEmail, new SupaBaseClient.SBC_Callback() {
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
                    Intent intent = new Intent(EmailInputActivity.this, EmailCodeActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                });
            }
        });
    }
}
