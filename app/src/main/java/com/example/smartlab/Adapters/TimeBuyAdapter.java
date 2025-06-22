package com.example.smartlab.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.R;

import java.util.List;

public class TimeBuyAdapter extends RecyclerView.Adapter<TimeBuyAdapter.TimeSlotViewHolder> {

    private List<String> timeSlots;
    private TimeBuyAdapter.OnTimeSelectedListener listener;
    private String selectedTime;

    public interface OnTimeSelectedListener {
        void onTimeSelected(String time);
    }

    public TimeBuyAdapter(List<String> timeSlots, TimeBuyAdapter.OnTimeSelectedListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeBuyAdapter.TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_buy, parent, false);
        return new TimeBuyAdapter.TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeBuyAdapter.TimeSlotViewHolder holder, int position) {
        String time = timeSlots.get(position);
        holder.timeButton.setText(time);

        if (time.equals(selectedTime)) {
            holder.timeButton.setBackgroundColor(holder.itemView.getContext().getColor(R.color.blue_button));
            holder.timeButton.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        } else {
            holder.timeButton.setBackgroundColor(holder.itemView.getContext().getColor(R.color.blue_button_off));
            holder.timeButton.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }

        holder.timeButton.setOnClickListener(v -> {
            selectedTime = time;
            notifyDataSetChanged();
            if (listener != null) {
                listener.onTimeSelected(time);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void setSelectedTime(String time) {
        this.selectedTime = time;
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        Button timeButton;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeButton = itemView.findViewById(R.id.timeButton);
        }
    }
}

