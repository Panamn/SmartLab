package com.example.smartlab.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.Models.SpecializationCategories;
import com.example.smartlab.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<PromotionsNews> promotionsNewsList;

    public NewsAdapter(Context context, List<PromotionsNews> promotionsNewsList) {
        this.context = context;
        this.promotionsNewsList = promotionsNewsList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        PromotionsNews promotionsNews = promotionsNewsList.get(position);
        holder.titleTextView.setText(promotionsNews.getTitle());
        holder.descriptionTextView.setText(promotionsNews.getDescription());
        holder.priceTextView.setText(String.valueOf(promotionsNews.getDiscount()));
        String url = "https://ubotxdvkhvusymbhrgvy.supabase.co/storage/v1/object/public/news/";
        Glide.with(context)
                .load(url + promotionsNews.getPhoto())
                .placeholder(R.drawable.brock)
                .error(R.drawable.icon_error)
                .into(holder.imageNew);
        int color = Color.parseColor(promotionsNews.getBackground());
        holder.view.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return promotionsNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNew;
        TextView titleTextView, descriptionTextView, priceTextView;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNew = itemView.findViewById(R.id.imageNew);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            view = itemView.findViewById(R.id.line1);
        }
    }
}
