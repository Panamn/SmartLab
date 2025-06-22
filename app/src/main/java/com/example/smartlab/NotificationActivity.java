package com.example.smartlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.NotificationAdapter;
import com.example.smartlab.Adapters.NotificationType2Adapter;
import com.example.smartlab.Models.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnItemClickListener, NotificationType2Adapter.OnItemClickListener2{

    private RecyclerView recyclerNotifications;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);

            try {
        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        getAllNotifications();
            } catch (Exception e) {
                ErrorHandler.handleError(this, e);
            }

    }

    private void getAllNotifications(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllNotification(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllNotifications:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllNotifications:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Notification>>(){}.getType();

                    List<Notification> notifications = gson.fromJson(responseBody, type);


                        List<Notification> type1Notifications = new ArrayList<>();
                        List<Notification> type2Notifications = new ArrayList<>();
                        List<Notification> type3Notifications = new ArrayList<>();
                    for (Notification notification : notifications) {
                        String notificationType = notification.getNotification_type();
                        if ("make_an_appointment".equals(notificationType)) {
                            type1Notifications.add(notification);
                            NotificationAdapter adapter1 = new NotificationAdapter(getApplicationContext(), type1Notifications, NotificationActivity.this);
                            recyclerNotifications.setAdapter(adapter1);
                        } else if ("purchase".equals(notificationType)) {
                            type2Notifications.add(notification);
                            NotificationType2Adapter adapter2 = new NotificationType2Adapter(getApplicationContext(), type2Notifications, NotificationActivity.this);
                            recyclerNotifications.setAdapter(adapter2);
                        } else {
                            recyclerNotifications.setAdapter(null);
                            Toast.makeText(NotificationActivity.this,
                                    getString(R.string.text_not_notification),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    recyclerNotifications.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                });
            }
        });
    }
    private void getNotificationDelete(String id_notifications){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.deleteNotification(id_notifications, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getNotificationDelete:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getNotificationDelete:onResponse", responseBody);
                    getAllNotifications();
                });
            }
        });
    }
    @Override
    public void onDeleteToCartClick1(Notification notification) {
        getNotificationDelete(String.valueOf(notification.getId_notifications()));
    }

    @Override
    public void onDeleteToCartClick2(Notification notification) {
        getNotificationDelete(String.valueOf(notification.getId_notifications()));
    }
}
