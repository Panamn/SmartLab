package com.example.smartlab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.R;

import java.util.List;

public class AnalyzesCategoriesAdapter extends RecyclerView.Adapter<AnalyzesCategoriesAdapter.ViewHolder> {
    private Context context;
    private List<Analyzes> analyzesList;

    public AnalyzesCategoriesAdapter(Context context, List<Analyzes> analyzesList) {
        this.context = context;
        this.analyzesList = analyzesList;
    }

    @NonNull
    @Override
    public AnalyzesCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_analyzes, parent, false);
        return new AnalyzesCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyzesCategoriesAdapter.ViewHolder holder, int position) {
        Analyzes analyzes = analyzesList.get(position);
    }

    @Override
    public int getItemCount() {
        return analyzesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCategories;
        TextView titleCategories;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategories = itemView.findViewById(R.id.imageCategories);
            titleCategories = itemView.findViewById(R.id.titleCategories);
        }
    }
}

