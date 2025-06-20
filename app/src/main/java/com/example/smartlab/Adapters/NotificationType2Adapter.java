package com.example.smartlab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Models.Notification;
import com.example.smartlab.R;

import java.util.List;

public class NotificationType2Adapter extends RecyclerView.Adapter<NotificationType2Adapter.ViewHolder> {
    private Context context;
    private List<Notification> notificationList;

    public NotificationType2Adapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationType2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_type_purchase, parent, false);
        return new NotificationType2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationType2Adapter.ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.textView.setText(notification.getText());
        holder.sumTextView.setText((int) notification.getSum());
        holder.timeView.setText(notification.getPeriod_of_relevance());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, sumTextView, timeView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            sumTextView = itemView.findViewById(R.id.sumTextView);
            timeView = itemView.findViewById(R.id.timeView);
        }
    }
}
