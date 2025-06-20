package com.example.smartlab.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.Basket;
import com.example.smartlab.R;
import com.example.smartlab.SupaBaseClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyzesAdapter extends RecyclerView.Adapter<AnalyzesAdapter.ViewHolder> {
    private Context context;
    private List<Analyzes> analyzesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Analyzes analyzes);
        void onAddToCartClick(Analyzes analyzes);
    }

    public AnalyzesAdapter(Context context, List<Analyzes> analyzesList, OnItemClickListener listener) {
        this.context = context;
        this.analyzesList = analyzesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnalyzesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_analyzes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyzesAdapter.ViewHolder holder, int position) {
        Analyzes analyzes = analyzesList.get(position);


        holder.TextTitleAnalyzes.setText(analyzes.getTitle());


        holder.TextExecuteAnalyzes.setText(formatDays(analyzes.getPeriod_of_execution()));

        holder.TextPriceAnalyzes.setText(String.valueOf(analyzes.getPrice()) + " ₽");


        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(analyzes);
        });


        holder.analyzesButton.setOnClickListener(v -> {
            listener.onAddToCartClick(analyzes);
        });

    }


    private String formatDays(int days) {
        if (days == 1) return "1 день";
        if (days >= 2 && days <= 4) return days + " дня";
        return days + " дней";
    }
    @Override
    public int getItemCount() {
        return analyzesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextTitleAnalyzes, TextExecuteAnalyzes, TextPriceAnalyzes;
        Button analyzesButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextTitleAnalyzes = itemView.findViewById(R.id.TextTitleAnalyzes);
            TextExecuteAnalyzes = itemView.findViewById(R.id.TextExecuteAnalyzes);
            TextPriceAnalyzes = itemView.findViewById(R.id.TextPriceAnalyzes);
            analyzesButton = itemView.findViewById(R.id.analyzesButton);
        }
    }

}
