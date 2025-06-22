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

public class NotificationType2Adapter extends RecyclerView.Adapter<NotificationType2Adapter.Type2ViewHolder> {
    private Context context;
    private List<Notification> notificationList;
    private OnItemClickListener2 listener;

    public interface OnItemClickListener2 {
        void onDeleteToCartClick2(Notification notification);
    }
    public NotificationType2Adapter(Context context, List<Notification> notificationList, OnItemClickListener2 listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationType2Adapter.Type2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_type_purchase, parent, false);
        return new NotificationType2Adapter.Type2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationType2Adapter.Type2ViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        holder.titleTextView.setText(notification.getTitle());
        holder.textView.setText(notification.getText());
        holder.sumTextView.setText(String.valueOf(notification.getSum()));
        holder.timeView.setText(notification.getPeriod_of_relevance());
        holder.buttonDelete.setOnClickListener(v -> {
            listener.onDeleteToCartClick2(notification);
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class Type2ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, textView, sumTextView, timeView;
        ImageButton buttonDelete;
        public Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            textView = itemView.findViewById(R.id.textView);
            sumTextView = itemView.findViewById(R.id.sumTextView);
            timeView = itemView.findViewById(R.id.timeView);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
