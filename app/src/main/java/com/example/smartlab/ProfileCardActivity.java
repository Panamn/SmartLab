package com.example.smartlab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.smartlab.Models.ProfileUpdatePhone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileCardActivity extends AppCompatActivity {
    private ImageView imageUserView;
    private EditText firstNameEditText, middleNameEditText, lastNameEditText, birthDateEditText, phoneNameEditText, emailNameEditText;
    private Spinner genderSpinner;
    private Calendar birthDateCalendar;
    private Button saveButton, updateButton, recoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_card);

        imageUserView = findViewById(R.id.imageUser);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        middleNameEditText = findViewById(R.id.middleNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        phoneNameEditText = findViewById(R.id.phoneNameEditText);
        emailNameEditText = findViewById(R.id.emailNameEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        saveButton = findViewById(R.id.saveButton);
        updateButton = findViewById(R.id.updateButton);
        recoverButton = findViewById(R.id.recoverButton);
        birthDateCalendar = Calendar.getInstance();
        setupGenderSpinner();
        getProfileCard();

        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerDialog();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneNameEditText.getText().toString().trim();
                String email = emailNameEditText.getText().toString().trim();
                updateProfilePhone(phone);
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
                updateProfile(fullName, birthDate, gender);
            }
        });
        ImageButMenu();
        ImageButClickMenu();
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
    private void updateProfilePhone(String phone) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        ProfileUpdatePhone profileUpdatePhone = new ProfileUpdatePhone(phone);
        supaBaseClient.updateProfilePhone(profileUpdatePhone, new SupaBaseClient.SBC_Callback() {
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
                    startActivity(new Intent(ProfileCardActivity.this, ProfileActivity.class));
                    finish();
                });
            }
        });
    }
    private void updateProfile(String fullName,  String birthDate, String gender) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        ProfileUpdateCard profileUpdateCard = new ProfileUpdateCard(fullName, birthDate, gender);
        supaBaseClient.updateProfileCard(profileUpdateCard, new SupaBaseClient.SBC_Callback() {
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
                    startActivity(new Intent(ProfileCardActivity.this, ProfileActivity.class));
                    finish();
                });
            }
        });
    }
    private void getProfileCard(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCurrentUser(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getProfileCard:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getProfileCard:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Profile>>(){}.getType();
                    List<Profile> profileList = gson.fromJson(responseBody, type);
                    if(profileList != null && !profileList.isEmpty()) {
                        Profile profile = profileList.get(0);
                        String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/avatars/";
                        Glide.with(getApplicationContext())
                                .load(url + profile.getAvatar_url())
                                .placeholder(R.drawable.brock)
                                .error(R.drawable.icon_error)
                                .into(imageUserView);
                            String[] nameParts = splitFIO(profile.getFull_name());
                            lastNameEditText.setText(nameParts[0]);
                            firstNameEditText.setText(nameParts[1]);
                            middleNameEditText.setText(nameParts[2]);
                        birthDateEditText.setText(profile.getSate_of_birth());
                        phoneNameEditText.setText(profile.getPhone());
                        setGenderInSpinner(profile.getGender());
                    }
                });
            }
        });

    }
    public static String[] splitFIO(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", "", ""};
        }

        String[] parts = fullName.trim().split("\\s+", 3);
        String lastName = parts.length > 0 ? parts[0] : "";
        String firstName = parts.length > 1 ? parts[1] : "";
        String middleName = parts.length > 2 ? parts[2] : "";

        return new String[]{lastName, firstName, middleName};
    }
    private void setGenderInSpinner(String gender) {
        if (gender == null) return;

        String genderLower = gender.toLowerCase(Locale.getDefault());

        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) genderSpinner.getAdapter();

        for (int i = 0; i < adapter.getCount(); i++) {
            String item = adapter.getItem(i).toString().toLowerCase(Locale.getDefault());
            if (item.contains(genderLower)) {
                genderSpinner.setSelection(i);
                return;
            }
        }
        genderSpinner.setSelection(0);

    }
    private void ImageButMenu(){
        ImageButton ProfileButtonMenu = findViewById(R.id.ProfileButtonMenu);
        TextView TextProfileButtonMenu = findViewById(R.id.TextProfileButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        ProfileButtonMenu.setImageResource(R.drawable.icon_menu_profile_on);
        TextProfileButtonMenu.setTextColor(getResources().getColor(R.color.blue_text));
        HomeButtonMenu.setImageResource(R.drawable.icon_menu_home_off);
    }
    private void ImageButClickMenu(){
        ImageButton AnalyzesButtonMenu = findViewById(R.id.AnalyzesButtonMenu);
        ImageButton RecordsButtonMenu = findViewById(R.id.RecordsButtonMenu);
        ImageButton HomeButtonMenu = findViewById(R.id.HomeButtonMenu);
        ImageButton BasketButtonMenu = findViewById(R.id.BasketButtonMenu);
        ImageButton ProfileButtonMenu = findViewById(R.id.ProfileButtonMenu);

        AnalyzesButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, AnalyzesActivity.class)));
        RecordsButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, DoctorsActivity.class)));
        HomeButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        BasketButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, BasketActivity.class)));
        ProfileButtonMenu.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }
}

