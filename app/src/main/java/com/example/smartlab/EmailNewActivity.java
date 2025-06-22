package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class EmailNewActivity extends AppCompatActivity {
    private EditText emailEditText;
    private Button changeEmailButton;
    private String token, email;
    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email);

            try {
        token = getIntent().getStringExtra("access_token");
        email = getIntent().getStringExtra("email_code");
        emailEditText = findViewById(R.id.emailEditText);
        changeEmailButton = findViewById(R.id.changeEmailButton);
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

        changeEmailButton.setOnClickListener(v -> {
            if (validateEmail()) {
                changePassword(token, email);
            }
        });
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }

    }
    private void updateButton(boolean isActive) {
        if(isActive == true){
            changeEmailButton.setBackgroundResource(R.drawable.blue_button_background);
            changeEmailButton.setEnabled(true);
        }else {
            changeEmailButton.setBackgroundResource(R.drawable.blue_button_background_off);
            changeEmailButton.setEnabled(false);
        }
    }
    private void changePassword(String token, String email){
        String newEmail = emailEditText.getText().toString().trim();
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.updateEmail(token,email,newEmail, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("changePassword:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("changePassword:onResponse", responseBody);
                    startActivity(new Intent(EmailNewActivity.this, HomeActivity.class));
                    finishAffinity();
                });
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
}

