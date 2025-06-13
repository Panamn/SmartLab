package com.example.smartlab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PinActivity extends AppCompatActivity {
    private static final String PIN = "Pin";
    private static final String KEY_PIN = "saved_pin";
    private static final int PIN_LENGTH = 4;

    private TextView titleTextView;
    private ImageView[] dots;
    private StringBuilder enteredPin = new StringBuilder();
    private boolean isCreatingNewPin = false;
    private String firstPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pin);

        titleTextView = findViewById(R.id.titleTextView);
        dots = new ImageView[]{
                findViewById(R.id.dot1),
                findViewById(R.id.dot2),
                findViewById(R.id.dot3),
                findViewById(R.id.dot4)
        };

        SharedPreferences sharedPreferences = getSharedPreferences("Data_binding", MODE_PRIVATE);
        String bearertoken = sharedPreferences.getString("bearer_token", "default_value");
        DataBinding.saveBearerToken("Bearer " +  bearertoken);
        String uuidUser = sharedPreferences.getString("uuid_user", "default_value");
        DataBinding.saveUuidUser(uuidUser);

        SharedPreferences prefs = getSharedPreferences(PIN, MODE_PRIVATE);
        String savePin = prefs.getString(KEY_PIN, null);



        if (savePin == null) {
            isCreatingNewPin = true;
            titleTextView.setText(getString(R.string.create_pin));
        } else {
            titleTextView.setText(getString(R.string.input_pin));
        }

        setupNumberButtons();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupNumberButtons() {
        int[] buttonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        for (int id : buttonIds) {
            Button btn = findViewById(id);
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundResource(R.drawable.pin_button_background_on);
                            btn.setTextColor(getResources().getColor(R.color.white));
                            return true;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundResource(R.drawable.pin_button_background);
                            btn.setTextColor(getResources().getColor(R.color.black));
                            if (enteredPin.length() < PIN_LENGTH) {
                                enteredPin.append(((Button)v).getText());
                                updateDots();
                                if (enteredPin.length() == PIN_LENGTH) {
                                    checkPin();
                                }
                            }
                            return true;
                        case MotionEvent.ACTION_CANCEL:
                            v.setBackgroundResource(R.drawable.pin_button_background);
                            btn.setTextColor(getResources().getColor(R.color.black));
                            return true;
                    }
                    return false;
                }
            });
        }

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.pin_button_background_on);
                        btnDelete.setTextColor(getResources().getColor(R.color.white));
                        return true;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundResource(R.drawable.pin_button_background);
                        btnDelete.setTextColor(getResources().getColor(R.color.black));
                        if (enteredPin.length() > 0) {
                            enteredPin.deleteCharAt(enteredPin.length() - 1);
                            updateDots();
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        v.setBackgroundResource(R.drawable.pin_button_background);
                        btnDelete.setTextColor(getResources().getColor(R.color.black));
                        return true;
                }
                return false;
            }
        });
    }

    private void updateDots() {
        for (int i = 0; i < dots.length; i++) {
            if (i < enteredPin.length()) {
                dots[i].setImageResource(R.drawable.pin_indicator_filled);
            } else {
                dots[i].setImageResource(R.drawable.pin_indicator_empty);
            }
        }
    }

    private void checkPin() {
        if (isCreatingNewPin) {
            if (firstPin.isEmpty()) {
                firstPin = enteredPin.toString();
                enteredPin.setLength(0);
                titleTextView.setText(getString(R.string.text_pin_сonfirm));
                updateDots();
            } else {
                if (firstPin.equals(enteredPin.toString())) {
                    savePin(firstPin);
                    proceedToMain();
                } else {
                    Toast.makeText(this, getString(R.string.text_pin_dont), Toast.LENGTH_SHORT).show();
                    resetPin();
                }
            }
        } else {
            SharedPreferences prefs = getSharedPreferences(PIN, MODE_PRIVATE);
            String savePin = prefs.getString(KEY_PIN, "");
            if (savePin.equals(enteredPin.toString())) {
                proceedToMain();
            } else {
                Toast.makeText(this, getString(R.string.text_pin_wrong), Toast.LENGTH_SHORT).show();
                resetPin();
            }
        }
    }

    private void savePin(String pin) {
        SharedPreferences.Editor editor = getSharedPreferences(PIN, MODE_PRIVATE).edit();
        editor.putString(KEY_PIN, pin);
        editor.apply();
    }

    private void resetPin() {
        enteredPin.setLength(0);
        firstPin = "";
        updateDots();
        if (isCreatingNewPin) {
            titleTextView.setText(getString(R.string.create_pin));
        }
    }
    private void proceedToMain() {
        SharedPreferences shared= getSharedPreferences("new_user", MODE_PRIVATE);
        String user = shared.getString("entrance", "default_value");
        if(user.equals("new_user")){
            startActivity(new Intent(this, EditCardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
