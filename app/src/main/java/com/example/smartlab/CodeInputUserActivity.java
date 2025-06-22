package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartlab.Models.InputEmail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CodeInputUserActivity extends AppCompatActivity {

    private EditText[] codeEditText = new EditText[6];
    private ImageButton imageButtonBack;
    private String email;
    private TextView resendText;
    private CountDownTimer countDownTimer;
    private final long TIMER_DURATION = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_input_user);

            try {
        codeEditText[0] = findViewById(R.id.code1);
        codeEditText[1] = findViewById(R.id.code2);
        codeEditText[2] = findViewById(R.id.code3);
        codeEditText[3] = findViewById(R.id.code4);
        codeEditText[4] = findViewById(R.id.code5);
        codeEditText[5] = findViewById(R.id.code6);
        resendText = findViewById(R.id.resendText);


        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(v -> finish());

        email = getIntent().getStringExtra("email_code");

        setupOtpEditTexts();
        startCountdownTimer();
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }

    }

    private void setupOtpEditTexts() {
        for (int i = 0; i < codeEditText.length; i++) {
            final int currentIndex = i;
            codeEditText[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        if (currentIndex < codeEditText.length - 1) {
                            codeEditText[currentIndex + 1].requestFocus();
                        } else {
                            verifyCode(email);
                        }
                    } else if (s.length() == 0 && currentIndex > 0) {
                        codeEditText[currentIndex - 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void verifyCode(String email) {

        StringBuilder codeBuilder = new StringBuilder();
        for (EditText editText : codeEditText) {
            codeBuilder.append(editText.getText().toString());
        }
        String code = codeBuilder.toString();

        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.verifyOtp(email, code, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("verifyCode:onFailure", e.getLocalizedMessage());
                    Toast.makeText(CodeInputUserActivity.this, getString(R.string.text_validate_error), Toast.LENGTH_LONG).show();
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("verifyCode:onResponse", responseBody);
                    String token = extractAccessToken(responseBody);
                    Intent intent = new Intent(CodeInputUserActivity.this, EmailNewActivity.class);
                    intent.putExtra("access_token", token);
                    intent.putExtra("email", email);
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

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                resendText.setText(getString(R.string.text_email_code_2) + " " + seconds + " " + getString(R.string.text_email_code_22));
            }

            @Override
            public void onFinish() {
                resendText.setText(getString(R.string.text_email_code_3));
                resendText.setTextColor(ContextCompat.getColor(CodeInputUserActivity.this, R.color.black));
                resendText.setOnClickListener(v -> {
                    sendRecoveryRequest(email);

                });
            }
        }.start();
    }

    private void sendRecoveryRequest(String email){
        resendText.setEnabled(false);
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
                    startCountdownTimer();
                });
            }
        });
    }
}
