package com.example.smartlab.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.AnalysesListActivity;
import com.example.smartlab.CategoriesAnalyzesActivity;
import com.example.smartlab.Models.Category;
import com.example.smartlab.Models.Specialization;
import com.example.smartlab.R;
import com.example.smartlab.SpecializationActivity;
import com.example.smartlab.SpecializationListActivity;
import com.google.android.material.chip.Chip;

import java.util.List;

public class CategoriesSpecializationAdapter extends RecyclerView.Adapter<CategoriesSpecializationAdapter.ViewHolder> {
    private List<Specialization> specializationList;
    private int sectionPosition = -1;


    public CategoriesSpecializationAdapter(List<Specialization> specializationList) {
        this.specializationList = specializationList.subList(0, Math.min(specializationList.size(), 3));
        if (specializationList.size() > 3) {
            this.specializationList.add(new Specialization("0", "Другие"));
        }
    }

    @NonNull
    @Override
    public CategoriesSpecializationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoriesSpecializationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesSpecializationAdapter.ViewHolder holder, int position) {
        Specialization specialization = specializationList.get(position);
        holder.chip.setText(specialization.getTitle());
        holder.chip.setSelected(position == sectionPosition);

        holder.chip.setOnClickListener(v -> {
            int previousSelected = sectionPosition;
            sectionPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(sectionPosition);

            if ("0".equals(specialization.getId())) {
                Intent intent = new Intent(v.getContext(), SpecializationActivity.class);
                v.getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(v.getContext(), SpecializationListActivity.class);
                intent.putExtra("category_specialization_id", specialization.getId());
                intent.putExtra("category_specialization_title", specialization.getTitle());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return specializationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.categoryChip);
        }
    }
}

