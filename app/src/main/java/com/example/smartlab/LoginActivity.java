package com.example.smartlab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlab.Models.AuthResponse;
import com.example.smartlab.Models.LoginRequest;
import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ASession aSession;
    private TextView forgotPasswordText, registrationText;
    private boolean emailValid = false;
    private boolean passwordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        aSession = new ASession(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        registrationText = findViewById(R.id.registrationText);

        loginButton.setEnabled(false);

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                emailValid = validateEmail();
            }
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                passwordValid = validatePassword();
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                passwordValid = validatePassword();
                updateButtonState();
            }
        });

        loginButton.setOnClickListener(view -> {
            if (validateAllFields()) {

                loginUser(
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim()
                );
            }
        });
        forgotPasswordText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, EmailInputActivity.class));
        });
        registrationText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        });
    }

    private void updateButtonState() {
        if (emailValid && passwordValid) {
            loginButton.setEnabled(true);
            loginButton.setBackgroundResource(R.drawable.blue_button_background);
        }
    }

    private boolean validateAllFields() {
        emailValid = validateEmail();
        passwordValid = validatePassword();
        return emailValid && passwordValid;
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
        return true;
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
        return true;
    }

    private void loginUser(String email, String password) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        LoginRequest loginRequest = new LoginRequest(email, password);
        supaBaseClient.login(loginRequest, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("loginUser:onFailure", e.getLocalizedMessage());
                    Toast.makeText(LoginActivity.this, getString(R.string.text_validate_5), Toast.LENGTH_LONG).show();
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("loginUser:onResponse", responseBody);
                    Gson gson = new Gson();
                    AuthResponse auth = gson.fromJson(responseBody, AuthResponse.class);
                    DataBinding.saveBearerToken("Bearer " + auth.getAccess_token());
                    DataBinding.saveUuidUser(auth.getUser().getId());

                    SharedPreferences sharedPreferences = getSharedPreferences("Data_binding", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("bearer_token", auth.getAccess_token());
                    editor.putString("uuid_user", auth.getUser().getId());
                    editor.apply();

                    SharedPreferences shared= getSharedPreferences("new_user", MODE_PRIVATE);
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putString("entrance", "old_user");
                    edit.apply();

                    ASession.setLoggedIn(true);
                    startActivity(new Intent(LoginActivity.this, PinActivity.class));
                    Log.e("loginUser:onResponse", auth.getUser().getId());
                    finish();

                });
            }
        });
    }
}