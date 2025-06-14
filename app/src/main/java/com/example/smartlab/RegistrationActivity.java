package com.example.smartlab;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.smartlab.Models.ASession;
import com.example.smartlab.Models.AuthResponse;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.LoginRequest;
import com.example.smartlab.Models.ProfileUpdate;
import com.google.gson.Gson;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, repeatPasswordEditText;
    private Button registrationButton;
    private ASession aSession;
    private TextView loginText;
    private boolean emailValid = false;
    private boolean passwordValid = false;
    private boolean passwordMatchValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        aSession = new ASession(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        registrationButton = findViewById(R.id.registrationButton);
        loginText = findViewById(R.id.loginText);

        registrationButton.setEnabled(false);

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                emailValid = validateEmail();
            }
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                passwordValid = validatePassword();
                updateButtonState();
                if (!TextUtils.isEmpty(repeatPasswordEditText.getText())) {
                    passwordMatchValid = validatePasswordConfirmation();
                }
            }
        });

        repeatPasswordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !TextUtils.isEmpty(repeatPasswordEditText.getText())) {
                passwordMatchValid = validatePasswordConfirmation();
            }
        });

        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(repeatPasswordEditText.getText())) {
                    passwordMatchValid = validatePasswordConfirmation();
                    updateButtonState();
                }
            }
        };

        passwordEditText.addTextChangedListener(passwordWatcher);
        repeatPasswordEditText.addTextChangedListener(passwordWatcher);

        registrationButton.setOnClickListener(view -> {
            if (validateAllFields()) {
                registerUser(
                        emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim()
                );
                SharedPreferences shared= getSharedPreferences("new_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("entrance", "new_user");
                editor.apply();
            }
        });
        loginText.setOnClickListener(view -> {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        });
    }

    private void updateButtonState() {
        if (emailValid && passwordValid && passwordMatchValid) {
            registrationButton.setEnabled(true);
            registrationButton.setBackgroundResource(R.drawable.blue_button_background);
        }
    }

    private boolean validateAllFields() {
        emailValid = validateEmail();
        passwordValid = validatePassword();
        passwordMatchValid = validatePasswordConfirmation();
        return emailValid && passwordValid && passwordMatchValid;
    }

    private boolean validateEmail() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.text_validate1));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.text_validate2));
            return false;
        }

        emailEditText.setError(null);
        return true;
    }

    private boolean validatePassword() {
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.text_validate3));
            return false;
        }

        if (password.length() < 8) {
            passwordEditText.setError(getString(R.string.text_validate4));
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            passwordEditText.setError(getString(R.string.text_validate5));
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            passwordEditText.setError(getString(R.string.text_validate6));
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            passwordEditText.setError(getString(R.string.text_validate7));
            return false;
        }

        passwordEditText.setError(null);
        return true;
    }

    private boolean validatePasswordConfirmation() {
        String password = passwordEditText.getText().toString().trim();
        String repeatPassword = repeatPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(repeatPassword)) {
            repeatPasswordEditText.setError(getString(R.string.text_validate8));
            return false;
        }

        if (!password.equals(repeatPassword)) {
            repeatPasswordEditText.setError(getString(R.string.text_validate9));
            return false;
        }

        repeatPasswordEditText.setError(null);
        return true;
    }
    public void updateProfile(String email){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        ProfileUpdate profileUpdate = new ProfileUpdate(email, 2);
        supaBaseClient.updateProfile(profileUpdate, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("updateProfile:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("updateProfile:onResponse", responseBody);
                    startActivity(new Intent(RegistrationActivity.this, PinActivity.class));
                    finish();
                });
            }
        });

    }
    private void registerUser(String email, String password) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        LoginRequest loginRequest = new LoginRequest(email, password);
        supaBaseClient.register(loginRequest, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("registerUser:onFailure", e.getLocalizedMessage());
                    Toast.makeText(RegistrationActivity.this, getString(R.string.text_validate_5), Toast.LENGTH_LONG).show();
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("registerUser:onResponse", responseBody);
                    Gson gson = new Gson();
                    AuthResponse auth = gson.fromJson(responseBody, AuthResponse.class);

                    DataBinding.saveBearerToken("Bearer " + auth.getAccess_token());
                    DataBinding.saveUuidUser(auth.getUser().getId());

                    SharedPreferences sharedPreferences = getSharedPreferences("Data_binding", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uuid_user", auth.getUser().getId());
                    editor.apply();

                    updateProfile(email);
                    ASession.setLoggedIn(true);

                    Log.e("registerUser:onResponse", auth.getUser().getId());

                });
            }
        });
    }
}
