package com.example.smartlab.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.Basket;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.R;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private Context context;
    private List<Basket> basketList;

    public BasketAdapter(Context context, List<Basket> basketList) {
        this.context = context;
        this.basketList = basketList;
    }

    @NonNull
    @Override
    public BasketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_basket, parent, false);
        return new BasketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.ViewHolder holder, int position) {
        Basket basket = basketList.get(position);
        holder.titleTextView.setText(basket.getAnalyzes().getTitle());
        holder.priceTextView.setText(String.valueOf(basket.getAnalyzes().getPrice()));
    }

    @Override
    public int getItemCount() {
        return basketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, priceTextView;
        ImageButton buttonDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

