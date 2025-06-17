package com.example.smartlab.filter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smartlab.Models.Category;
import com.example.smartlab.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalysisFilterDialog extends Dialog {

    public interface FilterListener {
        void onFilterApplied(Integer priceFrom, Integer priceTo, int maxDays, Set<String> selectedCategories);
    }

    private final List<Category> categories;
    private final FilterListener listener;
    private final Set<String> selectedCategories = new HashSet<>();

    public AnalysisFilterDialog(@NonNull Context context, List<Category> categories, FilterListener listener) {
        super(context);
        this.categories = categories;
        this.listener = listener;
    }

    private TextInputEditText fromEditText, beforeEditText;
    private Slider sliderDateTime;
    private TextView dayTextView;
    private ChipGroup categoriesChipGroup;
    private Button resetButton, applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_overlay);

        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.TOP);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        fromEditText = findViewById(R.id.fromEditText);
        beforeEditText = findViewById(R.id.beforeEditText);
        sliderDateTime = findViewById(R.id.sliderDateTime);
        dayTextView = findViewById(R.id.dayTextView);
        categoriesChipGroup = findViewById(R.id.categoriesChipGroup);
        resetButton = findViewById(R.id.resetButton);
        applyButton = findViewById(R.id.applyButton);

        sliderDateTime.addOnChangeListener((slider, value, fromUser) -> {
            int days = (int) value;
            dayTextView.setText(getDaysText(days));
        });

        for (Category category : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(category.getTitle());
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeColorResource(R.color.blue_button);
            chip.setTextColor(getContext().getResources().getColor(R.color.black));
            chip.setRippleColorResource(R.color.blue_button);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCategories.add(String.valueOf(category.getId_categories_of_analyses()));
                    chip.setChipBackgroundColorResource(R.color.blue_button);
                    chip.setChipStrokeColorResource(R.color.blue_button);
                    chip.setTextColor(getContext().getResources().getColor(R.color.white));
                } else {
                    selectedCategories.remove(String.valueOf(category.getId_categories_of_analyses()));
                    chip.setChipBackgroundColorResource(R.color.white);
                    chip.setChipStrokeColorResource(R.color.blue_button);
                    chip.setTextColor(getContext().getResources().getColor(R.color.black));
                }
            });
            categoriesChipGroup.addView(chip);
        }

        resetButton.setOnClickListener(v -> {
            fromEditText.setText("");
            beforeEditText.setText("");
            sliderDateTime.setValue(0);
            selectedCategories.clear();
            categoriesChipGroup.clearCheck();
        });

        applyButton.setOnClickListener(v -> {
            Integer priceFrom =fromEditText.getText().toString().isEmpty() ?
                    null : Integer.parseInt(fromEditText.getText().toString());

            Integer priceTo =  beforeEditText.getText().toString().isEmpty() ?
                    null : Integer.parseInt(beforeEditText.getText().toString());

            int maxDays = (int) sliderDateTime.getValue();

            listener.onFilterApplied(priceFrom, priceTo, maxDays, selectedCategories);
            dismiss();
        });
    }

    private String getDaysText(int days) {
        if (days == 1) return "1 день";
        if (days >= 2 && days <= 4) return days + " дня";
        return days + " дней";
    }

}
