package com.example.smartlab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Models.Doctors;
import com.example.smartlab.R;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
    private Context context;
    private List<Doctors> doctorsList;

    public DoctorsAdapter(Context context, List<Doctors> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public DoctorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctors, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsAdapter.ViewHolder holder, int position) {
        Doctors doctors = doctorsList.get(position);
        holder.fullNameTextView.setText(doctors.getAbout_doctor());

    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDoctorsTextView;
        TextView fullNameTextView, specializationTextView, priceTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDoctorsTextView = itemView.findViewById(R.id.imageDoctorsTextView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            specializationTextView = itemView.findViewById(R.id.specializationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}