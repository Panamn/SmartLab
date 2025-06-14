package com.example.smartlab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        String firstName = "";
        String middleName = "";
        String[] nameParts = splitFIO(doctors.getProfile().getFull_name());
        firstName = nameParts[1];
        middleName = nameParts[2];
        holder.fullNameTextView.setText(firstName + " " + middleName);
        holder.priceTextView.setText(String.valueOf(doctors.getPrice_of_admission()) + " ₽");
        String specialization = context.getString(R.string.not_specified);
        if (doctors.getSpecializationCategories() != null && doctors.getSpecializationCategories().getTitle() != null) {
            specialization = doctors.getSpecializationCategories().getTitle();
        }
        holder.specializationTextView.setText(specialization);
        String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/avatars/";
        Glide.with(context)
                .load(url + doctors.getProfile().getAvatar_url())
                .placeholder(R.drawable.brock)
                .error(R.drawable.icon_error)
                .into(holder.imageDoctorsView);
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDoctorsView;
        TextView fullNameTextView, specializationTextView, priceTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDoctorsView = itemView.findViewById(R.id.imageDoctorsView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            specializationTextView = itemView.findViewById(R.id.specializationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
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
}