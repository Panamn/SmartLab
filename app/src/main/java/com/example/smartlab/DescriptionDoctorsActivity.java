package com.example.smartlab;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.Adapters.TimeAdapter;
import com.example.smartlab.Models.AddNotificationBuyAnalyzes;
import com.example.smartlab.Models.AddNotificationRecords;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.Records;
import com.example.smartlab.Models.UpdateBasket;
import com.example.smartlab.Models.UpdateRecords;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DescriptionDoctorsActivity extends AppCompatActivity {

    private TextView fullNameTextView, specializationTextView, experienceTextView, ratingTextView, aboutDoctorsTextView;
    private ImageView imageDoctorsView;
    private Button buttonSingUp, dateButton;
    private ImageButton backButton;
    private RecyclerView recyclerTime;
    private TimeAdapter timeAdapter;
    private List<String> time = Arrays.asList("10:00", "12:00", "14:00", "16:00", "18:00", "20:00");
    private String selectedDate;
    private String selectedDateFormat;
    private String selectedTime;
    private  String reception_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctors_description);

            try {
        fullNameTextView = findViewById(R.id.fullNameTextView);
        specializationTextView = findViewById(R.id.specializationTextView);
        experienceTextView = findViewById(R.id.experienceTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        aboutDoctorsTextView = findViewById(R.id.aboutDoctorsTextView);
        imageDoctorsView = findViewById(R.id.imageDoctorsView);
        dateButton = findViewById(R.id.dateButton);
        buttonSingUp = findViewById(R.id.buttonSingUp);
        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUpAppointment();
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerTime = findViewById(R.id.recyclerTime);
        recyclerTime.setLayoutManager(new GridLayoutManager(this, 3));
        timeAdapter = new TimeAdapter(time, this::onTimeSelected);
        recyclerTime.setAdapter(timeAdapter);

        dateButton.setOnClickListener(v -> showDatePickerDialog());
        settingDate();
        loadingData();
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }

    }
        private void settingDate(){
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getString(R.string.text_ru_en)));
            selectedDate = getString(R.string.text_today) + ", " + sdf.format(new Date());
            dateButton.setText(selectedDate);
            SimpleDateFormat sdfDb = new SimpleDateFormat("yyyy-MM-dd", new Locale(getString(R.string.text_ru_en)));
            selectedDateFormat = sdfDb.format(new Date());
        }
        private void loadingData(){
            Intent intent = getIntent();
            String doctorName = intent.getStringExtra("doctor_name");
            String specialization = intent.getStringExtra("doctor_specialization");
            int price = intent.getIntExtra("doctor_price", 0);
            String photoUrl = intent.getStringExtra("doctor_photo_url");
            int doctor_experience = intent.getIntExtra("doctor_experience", 0);
            float rating = intent.getFloatExtra("doctor_rating", 0);
            String about_doctor = intent.getStringExtra("doctor_about_doctor");

            fullNameTextView.setText(doctorName);
            specializationTextView.setText(specialization);
            buttonSingUp.setText(getResources().getText(R.string.text_item_button_sign_up) + " " + String.valueOf(price) + " ");
            String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/avatars/";
            Glide.with(this)
                    .load(url + photoUrl)
                    .placeholder(R.drawable.brock)
                    .error(R.drawable.icon_error)
                    .into(imageDoctorsView);
            experienceTextView.setText(String.valueOf(doctor_experience) + "+");
            ratingTextView.setText(String.valueOf(rating));
            aboutDoctorsTextView.setText(about_doctor);
        }
    private void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", new Locale(getString(R.string.text_ru_en)));
                    String dateText = sdf.format(selectedCalendar.getTime());

                    SimpleDateFormat sdfDb = new SimpleDateFormat("yyyy-MM-dd", new Locale(getString(R.string.text_ru_en)));
                    selectedDateFormat = sdfDb.format(selectedCalendar.getTime());

                    Calendar today = Calendar.getInstance();
                    if (selectedCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            selectedCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                        dateText = getString(R.string.text_today) + ", " + dateText;
                    }
                    selectedDateFormat = dateText;
                    selectedDate = dateText;
                    dateButton.setText(dateText);

                },
                year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void onTimeSelected(String time) {
        selectedTime = time;
        timeAdapter.setSelectedTime(time);
    }
    private void singUpAppointment() {
        if (selectedTime == null) {
            Toast.makeText(this, getString(R.string.text_description_time_1), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = getIntent();
        String id_doctors = intent.getStringExtra("doctor_id");
        String id_user = DataBinding.getUuidUser();
        reception_time = selectedDateFormat + " " + selectedTime + ":00";

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.text_description_time_2))
                .setMessage(getString(R.string.text_description_time_3) + " " + selectedDate + " " + getString(R.string.text_in) +" " + selectedTime + "?")
                .setPositiveButton(getString(R.string.text_yes), (dialog, which) -> {
                    addRecords(reception_time, getString(R.string.text_description_time_6), id_doctors, id_user);
                    addNotification();
                    finish();
                })
                .setNegativeButton(getString(R.string.text_no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
    public void addRecords(String reception_time, String status, String id_doctor, String id_client){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        UpdateRecords updaterecords = new UpdateRecords(reception_time, status, id_doctor, id_client);
        supaBaseClient.addRecords(updaterecords, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("addRecords:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("addRecords:onResponse", responseBody);
                });
            }
        });

    }
    private void addNotification(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        Intent intent = getIntent();
        String doctorName = intent.getStringExtra("doctor_name");
        String specialization = intent.getStringExtra("doctor_specialization");
        AddNotificationRecords addNotificationRecords = new AddNotificationRecords(
                getString(R.string.text_notification_1),
                specialization,
                reception_time,
                DataBinding.getUuidUser(),
                doctorName,
                "make_an_appointment"
        );
        supaBaseClient.addNotificationRecords(addNotificationRecords, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("createOrderItem:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("createOrderItem:onResponse", responseBody);
                });
            }
        });
    }
}
