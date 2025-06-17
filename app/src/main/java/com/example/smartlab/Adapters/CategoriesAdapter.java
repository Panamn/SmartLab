package com.example.smartlab.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.AnalysesListActivity;
import com.example.smartlab.CategoriesAnalyzesActivity;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.Models.Category;
import com.example.smartlab.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private List<Category> categoryList;
    private int sectionPosition = -1;


    public CategoriesAdapter(List<Category> categoryList) {
        this.categoryList = categoryList.subList(0, Math.min(categoryList.size(), 3));
        if (categoryList.size() > 3) {
            this.categoryList.add(new Category(0, "Другие"));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.chip.setText(category.getTitle());
        holder.chip.setSelected(position == sectionPosition);

        holder.chip.setOnClickListener(v -> {
            int previousSelected = sectionPosition;
            sectionPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(sectionPosition);

            if (category.getId_categories_of_analyses() == 0) {
                Intent intent = new Intent(v.getContext(), CategoriesAnalyzesActivity.class);
                v.getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(v.getContext(), AnalysesListActivity.class);
                intent.putExtra("category_id", category.getId_categories_of_analyses());
                intent.putExtra("category_title", category.getTitle());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.categoryChip);
        }
    }
}
