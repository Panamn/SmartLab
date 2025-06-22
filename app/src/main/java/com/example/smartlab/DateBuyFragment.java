package com.example.smartlab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.TimeAdapter;
import com.example.smartlab.Adapters.TimeBuyAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateBuyFragment extends BottomSheetDialogFragment {

    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(String date, String time);
    }
    private OnDateTimeSelectedListener listener;
    private String selectedDate = "";
    private String selectedTime = "";
    private List<String> time = Arrays.asList("10:00", "12:00", "14:00", "16:00", "18:00", "20:00");
    private TimeBuyAdapter timeAdapter;
    private RecyclerView recyclerTime;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnDateTimeSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDateTimeSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_buy, container, false);
    }
    private Button dateButton, buttonConfirm;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateButton = view.findViewById(R.id.dateButton);
        buttonConfirm = view.findViewById(R.id.buttonConfirm);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        selectedDate = sdf.format(new Date());
        dateButton.setText(selectedDate);

        dateButton.setOnClickListener(v -> showDatePickerDialog());

        recyclerTime = view.findViewById(R.id.recyclerTime);
        recyclerTime.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        timeAdapter = new TimeBuyAdapter(time, this::onTimeSelected);
        recyclerTime.setAdapter(timeAdapter);


//        String[] times = {"10:00", "13:00", "14:00", "15:00", "16:00", "18:00", "19:00"};
//        for (String time : times) {
//            Button timeBtn = new Button(getContext());
//            timeBtn.setText(time);
//            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
//            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
//            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
//            timeBtn.setLayoutParams(params);
//
//            timeBtn.setOnClickListener(v -> {
//                selectedTime = time;
//                updateTimeSelection(timeGrid, time);
//            });
//
//            timeGrid.addView(timeBtn);
//        }
        buttonConfirm.setOnClickListener(v -> {
            if (selectedTime.isEmpty()) {
                Toast.makeText(getContext(), "Выберите время", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onDateTimeSelected(selectedDate, selectedTime);
            dismiss();
        });
    }
    private void onTimeSelected(String time) {
        selectedTime = time;
        timeAdapter.setSelectedTime(time);
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
                    selectedDate = sdf.format(calendar.getTime());
                    Button btnSelectDate = getView().findViewById(R.id.dateButton);
                    if (btnSelectDate != null) {
                        btnSelectDate.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
    private void updateTimeSelection(GridLayout timeGrid, String selectedTime) {
        for (int i = 0; i < timeGrid.getChildCount(); i++) {
            View child = timeGrid.getChildAt(i);
            if (child instanceof Button) {
                child.setSelected(((Button) child).getText().equals(selectedTime));
            }
        }
    }
    public static DateBuyFragment newInstance() {
        return new DateBuyFragment();
    }
}
