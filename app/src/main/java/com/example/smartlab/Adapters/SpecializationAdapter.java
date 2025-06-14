package com.example.smartlab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.AnalysesListActivity;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.Models.SpecializationCategories;
import com.example.smartlab.R;

import java.util.List;

public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.ViewHolder> {
    private Context context;
    private List<SpecializationCategories> specializationCategoriesList;

    public SpecializationAdapter(Context context, List<SpecializationCategories> specializationCategoriesList) {
        this.context = context;
        this.specializationCategoriesList = specializationCategoriesList;
    }

    @NonNull
    @Override
    public SpecializationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_specialization_doctors, parent, false);
        return new SpecializationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecializationAdapter.ViewHolder holder, int position) {
        SpecializationCategories specializationCategories = specializationCategoriesList.get(position);
        String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/specialtiesdoctors/";
        Glide.with(context)
                .load(url + specializationCategories.getIcons())
                .placeholder(R.drawable.brock)
                .error(R.drawable.icon_error)
                .into(holder.iconsImageView);
        holder.specializationTextView.setText(specializationCategories.getTitle());
        holder.view.setBackgroundColor(Color.parseColor(specializationCategories.getBackground()));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AnalysesListActivity.class);
//                intent.putExtra("category_id", categoriesAnalyzes.getId_categories_of_analyses());
//                intent.putExtra("category_title", categoriesAnalyzes.getTitle());
//                if (context instanceof Activity) {
//                    context.startActivity(intent);
//                } else {
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return specializationCategoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconsImageView;
        TextView specializationTextView;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconsImageView = itemView.findViewById(R.id.iconsImageView);
            specializationTextView = itemView.findViewById(R.id.specializationTextView);
            view = itemView.findViewById(R.id.line1);
        }
    }
}

