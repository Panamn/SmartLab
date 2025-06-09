package com.example.smartlab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.R;

import java.util.List;

public class AnalyzesAdapter extends RecyclerView.Adapter<AnalyzesAdapter.ViewHolder> {
    private Context context;
    private List<Analyzes> analyzesList;

    public AnalyzesAdapter(Context context, List<Analyzes> analyzesList) {
        this.context = context;
        this.analyzesList = analyzesList;
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

        int period = analyzes.getPeriod_of_execution();
        String periodText;
        if (period % 10 == 1 && period % 100 != 11) {
            periodText = period + " день";
        } else if (period % 10 >= 2 && period % 10 <= 4 && (period % 100 < 12 || period % 100 > 14)) {
            periodText = period + " дня";
        } else {
            periodText = period + " дней";
        }
        holder.TextExecuteAnalyzes.setText(periodText);

        holder.TextPriceAnalyzes.setText(String.valueOf(analyzes.getPrice()) + " ₽");
        // TODO
    }

    @Override
    public int getItemCount() {
        return analyzesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextTitleAnalyzes, TextExecuteAnalyzes, TextPriceAnalyzes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextTitleAnalyzes = itemView.findViewById(R.id.TextTitleAnalyzes);
            TextExecuteAnalyzes = itemView.findViewById(R.id.TextExecuteAnalyzes);
            TextPriceAnalyzes = itemView.findViewById(R.id.TextPriceAnalyzes);
        }
    }
}
