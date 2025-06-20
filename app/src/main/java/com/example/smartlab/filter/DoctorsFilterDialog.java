package com.example.smartlab.filter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.smartlab.Models.Specialization;
import com.example.smartlab.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoctorsFilterDialog extends Dialog {

    public interface FilterListener {
        void onFilterApplied(Integer priceFrom, Integer priceTo, float minRating, Set<String> selectedSpecializations);
    }

    private final List<Specialization> specializations;
    private final FilterListener listener;
    private final Set<String> selectedSpecializations = new HashSet<>();

    private TextInputEditText fromEditText, beforeEditText;
    private RatingBar ratingDoctors;
    private ChipGroup specializationsChipGroup;
    private Button resetButton, applyButton;

    public DoctorsFilterDialog(@NonNull Context context, List<Specialization> specializations, FilterListener listener) {
        super(context);
        this.specializations = specializations;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_doctors);

        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        fromEditText = findViewById(R.id.fromEditText);
        beforeEditText = findViewById(R.id.beforeEditText);
        ratingDoctors = findViewById(R.id.ratingDoctors);
        specializationsChipGroup = findViewById(R.id.categoriesChipGroup);
        resetButton = findViewById(R.id.resetButton);
        applyButton = findViewById(R.id.applyButton);

        for (Specialization specialization : specializations) {
            Chip chip = new Chip(getContext());
            chip.setText(specialization.getTitle());
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeColorResource(R.color.blue_button);
            chip.setTextColor(getContext().getResources().getColor(R.color.black));
            chip.setRippleColorResource(R.color.blue_button);

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSpecializations.add(specialization.getId());
                    chip.setChipBackgroundColorResource(R.color.blue_button);
                    chip.setTextColor(getContext().getResources().getColor(R.color.white));
                } else {
                    selectedSpecializations.remove(specialization.getId());
                    chip.setChipBackgroundColorResource(R.color.white);
                    chip.setTextColor(getContext().getResources().getColor(R.color.black));
                }
            });
            specializationsChipGroup.addView(chip);
        }

        resetButton.setOnClickListener(v -> resetFilters());
        applyButton.setOnClickListener(v -> applyFilters());
    }

    private void resetFilters() {
        fromEditText.setText("");
        beforeEditText.setText("");
        ratingDoctors.setRating(0);
        selectedSpecializations.clear();
        specializationsChipGroup.clearCheck();
    }

    private void applyFilters() {
        try {
            Integer priceFrom = fromEditText.getText().toString().isEmpty() ?
                    null : Integer.parseInt(fromEditText.getText().toString());

            Integer priceTo = beforeEditText.getText().toString().isEmpty() ?
                    null : Integer.parseInt(beforeEditText.getText().toString());

            float minRating = ratingDoctors.getRating();

            listener.onFilterApplied(priceFrom, priceTo, minRating, selectedSpecializations);
            dismiss();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Введите корректные значения цены", Toast.LENGTH_SHORT).show();
        }
    }
}
