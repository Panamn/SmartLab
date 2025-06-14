package com.example.smartlab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.AnalysesListActivity;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.CategoriesAnalyzes;
import com.example.smartlab.R;

import java.util.List;

public class AnalyzesCategoriesAdapter extends RecyclerView.Adapter<AnalyzesCategoriesAdapter.ViewHolder> {
    private Context context;
    private List<CategoriesAnalyzes> categoriesAnalyzesList;

    public AnalyzesCategoriesAdapter(Context context, List<CategoriesAnalyzes> categoriesAnalyzesList) {
        this.context = context;
        this.categoriesAnalyzesList = categoriesAnalyzesList;
    }

    @NonNull
    @Override
    public AnalyzesCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories_analyzes, parent, false);
        return new AnalyzesCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyzesCategoriesAdapter.ViewHolder holder, int position) {
        CategoriesAnalyzes categoriesAnalyzes = categoriesAnalyzesList.get(position);
        String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/categoriesanalyzes/";
        Glide.with(context)
                .load(url + categoriesAnalyzes.getIcons())
                .placeholder(R.drawable.brock)
                .error(R.drawable.icon_error)
                .into(holder.imageCategories);
        holder.titleCategories.setText(categoriesAnalyzes.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnalysesListActivity.class);
                intent.putExtra("category_id", categoriesAnalyzes.getId_categories_of_analyses());
                intent.putExtra("category_title", categoriesAnalyzes.getTitle());
                if (context instanceof Activity) {
                    context.startActivity(intent);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesAnalyzesList.size();
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

