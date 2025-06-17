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
    private Set<Integer> itemsInCart = new HashSet<>();
    SupaBaseClient supaBaseClient;

    public interface OnItemClickListener {
        void onItemClick(Analyzes analyzes);
        void onAddToCartClick(Analyzes analyzes, boolean addToCart);
    }

    public AnalyzesAdapter(Context context, List<Analyzes> analyzesList, OnItemClickListener listener) {
        this.context = context;
        this.analyzesList = analyzesList;
        this.listener = listener;
        this.supaBaseClient = new SupaBaseClient();
        loadCartItems();
    }
    private void loadCartItems() {

        supaBaseClient.fetchAllBasket(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                    Log.e("getAllBasket:onFailure", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(String responseBody) {
                    Log.e("getAllBasket:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Basket>>(){}.getType();
                    List<Basket> basketList = gson.fromJson(responseBody, type);
                    for (Basket item : basketList) {
                        itemsInCart.add(item.getId_analyzes());
                    }
            }
        });
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
        boolean isInCart = itemsInCart.contains(analyzes.getId_analyzes());

        holder.TextTitleAnalyzes.setText(analyzes.getTitle());


        holder.TextExecuteAnalyzes.setText(formatDays(analyzes.getPeriod_of_execution()));

        holder.TextPriceAnalyzes.setText(String.valueOf(analyzes.getPrice()) + " ₽");


        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(analyzes);
        });

        holder.analyzesButton.setOnClickListener(v -> {
            boolean newState = !isInCart;
            if (newState) {
                itemsInCart.add(analyzes.getId_analyzes());
            } else {
                itemsInCart.remove(analyzes.getId_analyzes());
            }
            updateButtonAppearance(holder.analyzesButton, newState);
            listener.onAddToCartClick(analyzes, newState);
        });

    }
    private void updateButtonAppearance(Button button, boolean isInCart) {
        if (isInCart) {
            button.setText("Убрать");
            button.setBackgroundResource(R.drawable.blue_button_background_radius);
            button.setTextColor(ContextCompat.getColor(context, R.color.blue_button));
        } else {
            button.setText("Добавить");
            button.setBackgroundResource(R.drawable.blue_button_background);
            button.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
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
    public void updateCartItems(Set<Integer> cartItems) {
        this.itemsInCart.clear();
        this.itemsInCart.addAll(cartItems);
    }
}
