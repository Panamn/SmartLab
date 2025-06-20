package com.example.smartlab;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.NotificationType1Adapter;
import com.example.smartlab.Adapters.NotificationType2Adapter;
import com.example.smartlab.Adapters.RecordsAdapter;
import com.example.smartlab.Models.Notification;
import com.example.smartlab.Models.Records;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerNotifications;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        getAllNotifications();
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
                        if (notificationType == null) continue;

                        switch (notificationType) {
                            case "make_an_appointment":
                                type1Notifications.add(notification);
                                break;
                            case "purchase":
                                type2Notifications.add(notification);
                                break;
                            case "other":
                                type3Notifications.add(notification);
                                break;
                        }
                    }

                        if (!type1Notifications.isEmpty()) {
                            NotificationType1Adapter adapterType1 = new NotificationType1Adapter(getApplicationContext(), type1Notifications);
                            recyclerNotifications.setAdapter(adapterType1);
                        } else if (!type2Notifications.isEmpty()) {
                            NotificationType2Adapter adapterType2 = new NotificationType2Adapter(getApplicationContext(), type2Notifications);
                            recyclerNotifications.setAdapter(adapterType2);
                        }
                    recyclerNotifications.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                });
            }
        });
    }
}
