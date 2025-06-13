package com.example.smartlab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.Profile;
import com.example.smartlab.Models.ProfileUpdateCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditCardActivity extends AppCompatActivity {
    private EditText firstNameEditText, middleNameEditText, lastNameEditText, birthDateEditText;
    private Spinner genderSpinner;
    private Calendar birthDateCalendar;
    private Button saveButton, buttonSkip;
    private boolean firstNameValid = false;
    private boolean lastNameValid = false;
    private boolean birthDateValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        firstNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                firstNameValid = validateFirstName();
            }
        });
        middleNameEditText = findViewById(R.id.middleNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        lastNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                lastNameValid = validateLastName();
            }
        });
        birthDateEditText = findViewById(R.id.birthDateEditText);
        birthDateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                birthDateValid = validateBirthDate();
            }
        });
        birthDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                birthDateValid = validateBirthDate();
                updateButtonState();
            }
        });
        genderSpinner = findViewById(R.id.genderSpinner);
        saveButton = findViewById(R.id.saveButton);
        buttonSkip = findViewById(R.id.buttonSkip);
        birthDateCalendar = Calendar.getInstance();
        setupGenderSpinner();



        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerDialog();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameEditText.getText().toString().trim();
                String middleName = middleNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String fullName = lastName + " " + firstName + " " + middleName;

                String birthDate = birthDateEditText.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                String normalizedGender = gender.toLowerCase(Locale.getDefault()).trim();


                if (normalizedGender.equals("мужской") ||
                        normalizedGender.equals("man")) {
                    gender = "man";
                }
                else if (normalizedGender.equals("женский") ||
                        normalizedGender.equals("woman")) {
                    gender = "woman";
                }
                savePatientData(fullName, birthDate, gender);
            }
        });
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences shared= getSharedPreferences("new_user", MODE_PRIVATE);
                SharedPreferences.Editor edit = shared.edit();
                edit.putString("entrance", "old_user");
                edit.apply();
                startActivity(new Intent(EditCardActivity.this, HomeActivity.class));
                finish();
            }
        });

    }
    private void updateButtonState() {
        if (firstNameValid && lastNameValid && birthDateValid) {
            saveButton.setEnabled(true);
            saveButton.setBackgroundResource(R.drawable.blue_button_background);
        }
    }
    private boolean validateFirstName(){
        String firstName = firstNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError(getString(R.string.text_validate10));
            return false;
        }
        firstNameEditText.setError(null);
        return true;
    }
    private boolean validateLastName(){
        String lastName = lastNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError(getString(R.string.text_validate11));
            return false;
        }
        lastNameEditText.setError(null);
        return true;
    }
    private boolean validateBirthDate(){
        String birthDate = birthDateEditText.getText().toString().trim();
        if (TextUtils.isEmpty(birthDate)) {
            birthDateEditText.setError(getString(R.string.text_validate12));
            return false;
        }
        birthDateEditText.setError(null);
        return true;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    birthDateCalendar.set(Calendar.YEAR, year);
                    birthDateCalendar.set(Calendar.MONTH, month);
                    birthDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateBirthDateText();
                },
                birthDateCalendar.get(Calendar.YEAR),
                birthDateCalendar.get(Calendar.MONTH),
                birthDateCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    private void updateBirthDateText() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("ru"));
        birthDateEditText.setText(sdf.format(birthDateCalendar.getTime()));
    }
    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);


    }

    private void savePatientData(String fullName,  String birthDate, String gender) {

        SupaBaseClient supaBaseClient = new SupaBaseClient();
        ProfileUpdateCard profileUpdateCard = new ProfileUpdateCard(fullName, birthDate, gender);
        supaBaseClient.updateProfileCard(profileUpdateCard, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("savePatientData:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("savePatientData:onResponse", responseBody);
                    SharedPreferences shared= getSharedPreferences("new_user", MODE_PRIVATE);
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putString("entrance", "old_user");
                    edit.apply();
                    startActivity(new Intent(EditCardActivity.this, HomeActivity.class));
                    finish();
                });
            }
        });
    }
}
