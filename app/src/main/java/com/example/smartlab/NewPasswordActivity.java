package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlab.Models.InputEmail;
import com.example.smartlab.Models.LoginRequest;
import com.example.smartlab.Models.UpdateRequest;

import java.io.IOException;
import java.io.StringReader;

public class NewPasswordActivity extends AppCompatActivity {
    private EditText passwordEditText;
    private Button changePasswordButton;
    private String token, email;
    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        token = getIntent().getStringExtra("access_token");
        email = getIntent().getStringExtra("email");
        passwordEditText = findViewById(R.id.passwordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(v -> finish());

        updateButton(false);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updateButton(validatePassword());}
        });

        changePasswordButton.setOnClickListener(v -> {
            if (validatePassword()) {
                changePassword(token, email);
            }
        });

    }
    private void updateButton(boolean isActive) {
        if(isActive == true){
            changePasswordButton.setBackgroundResource(R.drawable.blue_button_background);
            changePasswordButton.setEnabled(true);
        }else {
            changePasswordButton.setBackgroundResource(R.drawable.blue_button_background_off);
            changePasswordButton.setEnabled(false);
        }
    }
    private void changePassword(String token, String email){
        String newPassword = passwordEditText.getText().toString().trim();
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.updatePassword(token,email,newPassword, new SupaBaseClient.SBC_Callback() {
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
                    startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
                    finishAffinity();



                });
            }
        });
    }
    private boolean validatePassword() {
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.text_validate_3));
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError(getString(R.string.text_validate_4));
            return false;
        }

        passwordEditText.setError(null);
        updateButton(true);
        return true;
    }
}
