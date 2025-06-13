package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartlab.Models.InputEmail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CodeInputActivity extends AppCompatActivity {
    private EditText codeEditText;
    private Button verifyButton;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_input);

        email = getIntent().getStringExtra("email");

        codeEditText = findViewById(R.id.codeEditText);
        verifyButton = findViewById(R.id.verifyButton);

        verifyButton.setOnClickListener(v -> verifyCode());

    }
    private void verifyCode() {
        String code = codeEditText.getText().toString().trim();
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        InputEmail inputEmail = new InputEmail(email, "recovery");
        supaBaseClient.verifyOtp(String.valueOf(inputEmail),code, new SupaBaseClient.SBC_Callback() {
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
                    String token = extractAccessToken(responseBody);
                    Intent intent = new Intent(CodeInputActivity.this, NewPasswordActivity.class);
                    intent.putExtra("access_token", token);
                    startActivity(intent);


                });
            }
        });

    }
    private String extractAccessToken(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.getString("access_token");
        } catch (JSONException e) {
            return "";
        }
    }
}
