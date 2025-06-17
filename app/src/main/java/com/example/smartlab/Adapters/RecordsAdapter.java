package com.example.smartlab.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.Models.Records;
import com.example.smartlab.R;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private Context context;
    private List<Records> recordsList;

    public RecordsAdapter(Context context, List<Records> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
    }

    @NonNull
    @Override
    public RecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_records, parent, false);
        return new RecordsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        Records records = recordsList.get(position);
        holder.fioTextView.setText(records.getDoctors().getProfile().getFull_name());
        holder.specializationTextView.setText(records.getDoctors().getSpecializationCategories().getTitle());
        holder.dateTextView.setText(records.getReception_time());
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fioTextView, specializationTextView, dateTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fioTextView = itemView.findViewById(R.id.fioTextView);
            specializationTextView = itemView.findViewById(R.id.specializationTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}

