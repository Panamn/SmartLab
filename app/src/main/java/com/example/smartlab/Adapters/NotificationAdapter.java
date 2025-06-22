package com.example.smartlab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Models.Notification;
import com.example.smartlab.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Type1ViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteToCartClick1(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnItemClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationAdapter.Type1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.Type1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.Type1ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.textTextView.setText(notification.getText());
        holder.nameTextView.setText(notification.getName_doctors());
        holder.timeTextView.setText(notification.getPeriod_of_relevance());
        holder.buttonDelete.setOnClickListener(v -> {
            listener.onDeleteToCartClick1(notification);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class Type1ViewHolder extends RecyclerView.ViewHolder {
        TextView  titleTextView, textTextView, nameTextView, timeTextView;
        ImageButton buttonDelete;
        public Type1ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

