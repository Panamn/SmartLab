package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlab.Models.InputEmail;
import com.example.smartlab.Models.LoginRequest;
import com.example.smartlab.Models.UpdateRequest;

import java.io.IOException;

public class NewPasswordActivity extends AppCompatActivity {
    private EditText passwordEditText;
    private Button changePasswordButton;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        token = getIntent().getStringExtra("access_token");

        passwordEditText = findViewById(R.id.passwordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(v -> changePassword());
    }
    private void changePassword(){
        String newPassword = passwordEditText.getText().toString().trim();
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        UpdateRequest updateRequest = new UpdateRequest(newPassword);
        supaBaseClient.updatePassword(String.valueOf(updateRequest), new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("verifyCode:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("verifyCode:onResponse", responseBody);
                    Toast.makeText(
                            NewPasswordActivity.this,
                            "Пароль успешно изменён!",
                            Toast.LENGTH_SHORT
                    ).show();
                    // Перейти на экран входа
                    startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
                    finishAffinity();



                });
            }
        });
    }
}
