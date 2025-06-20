package com.example.smartlab.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.Notification;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.R;

import java.util.List;

public class NotificationType1Adapter extends RecyclerView.Adapter<NotificationType1Adapter.ViewHolder> {
    private Context context;
    private List<Notification> notificationList;

    public NotificationType1Adapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationType1Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_make_an_appointment, parent, false);
        return new NotificationType1Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationType1Adapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.textTextView.setText(notification.getText());
        holder.nameTextView.setText(notification.getName_doctors());
        holder.timeTextView.setText(notification.getPeriod_of_relevance());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTextView, nameTextView, timeTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTextView = itemView.findViewById(R.id.textTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}

