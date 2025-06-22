package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.AddNotificationBuyAnalyzes;
import com.example.smartlab.Models.Basket;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.Order;
import com.example.smartlab.Models.OrderId;
import com.example.smartlab.Models.OrderItem;
import com.example.smartlab.Models.Profile;
import com.example.smartlab.Models.ProfileUpdateAddress;
import com.example.smartlab.Models.ProfileUpdatePhone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuyActivity extends AppCompatActivity implements AddressBuyFragment.OnAddressSavedListener, DateBuyFragment.OnDateTimeSelectedListener{

    private Button addressButton, dateButton, buyButton;
    private ImageButton backButton;
    private EditText phoneEditText, commentEditText;
    private String currentAddress;
    private String selectedDate = "", selectedTime = "";
    private TextView sumPriceTextView;
    private int id_analysis_orders;
    private List<Basket> basketItems = new ArrayList<>();
    private List<String> analysisNames = new ArrayList<>();
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy);
        try {

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        addressButton = findViewById(R.id.addressButton);
        dateButton = findViewById(R.id.dateButton);
        buyButton = findViewById(R.id.buyButton);
        phoneEditText = findViewById(R.id.phoneEditText);
        commentEditText = findViewById(R.id.commentEditText);
        sumPriceTextView = findViewById(R.id.sumPriceTextView);


        addressButton.setOnClickListener(v ->  showAddressBottomSheet());
        dateButton.setOnClickListener(v -> showDateTimeBottomSheet());
        buyButton.setOnClickListener(v -> placeOrder());
        loadBasketTotal();
        getProfileCard();
    } catch (Exception e) {
        ErrorHandler.handleError(this, e);
    }
    }
    private void loadBasketTotal() {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllBasket(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("loadBasketTotal:onFailure", e.getLocalizedMessage());
                });
            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                        Log.e("loadBasketTotal:onResponse", responseBody);
                        basketItems = new Gson().fromJson(responseBody, new TypeToken<List<Basket>>(){}.getType());
                        updateTotal();
                        if (basketItems != null) {
                            for (Basket item : basketItems) {
                                if (item != null && item.getAnalyzes() != null) {
                                    String name = item.getAnalyzes().getTitle();
                                    if (name != null && !name.isEmpty()) {
                                        analysisNames.add(name);
                                    }
                                }
                            }
                        }
                        formatAnalysesList(analysisNames);
                });
            }
        });
    }
    private void updateTotal() {
        total = 0;
        if (basketItems != null) {
            for (Basket item : basketItems) {
                if (item != null && item.getAnalyzes() != null) {
                    total += item.getAnalyzes().getPrice();
                }
            }
        }
        sumPriceTextView.setText(String.format(Locale.getDefault(), "%.2f ₽", total));
    }
    private void showAddressBottomSheet() {
        AddressBuyFragment bottomSheet = AddressBuyFragment.newInstance();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
    private void showDateTimeBottomSheet() {
        DateBuyFragment bottomSheet = DateBuyFragment.newInstance();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }
    @Override
    public void onDateTimeSelected(String date, String time) {
        selectedDate = date;
        selectedTime = time;
        dateButton.setText(String.format("%s, %s", date, time));
    }

    @Override
    public void onAddressSaved(String fullAddress, boolean saveToProfile) {
        currentAddress = fullAddress;
        addressButton.setText(fullAddress);

        if (saveToProfile) {
            updateProfileAddress(fullAddress);
        }
    }
    public void placeOrder() {
        String phone = phoneEditText.getText().toString().trim();
        String comment = commentEditText.getText().toString().trim();

        if (currentAddress == null) {
            Toast.makeText(this, getString(R.string.text_buy_validate_1), Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_buy_validate_2), Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() < 12) {
            phoneEditText.setError(getString(R.string.text_buy_validate_3));
            return;
        }



        Order newOrder = new Order(
                (float) total,
                currentAddress,
                DataBinding.getUuidUser(),
                phone,
                comment
        );
        saveOrder(newOrder);
    }

    private void saveOrder(Order order) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.addOrder(order, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("saveOrder:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("saveOrder:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<OrderId>>(){}.getType();
                    List<OrderId> orders = gson.fromJson(responseBody, type);
                    OrderId orderId = orders.get(0);
                    id_analysis_orders = orderId.getId();
                    getAllBasket();
                });
            }
        });

    }
    private void updateProfileAddress(String address) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        ProfileUpdateAddress profileUpdateAddress = new ProfileUpdateAddress(address);
        supaBaseClient.updateProfileAddress(profileUpdateAddress, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("updateProfile:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("updateProfile:onResponse", responseBody);
                });
            }
        });
    }
    private void getProfileCard(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCurrentUser(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getProfileCard:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getProfileCard:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Profile>>(){}.getType();
                    List<Profile> profileList = gson.fromJson(responseBody, type);
                    if(profileList != null && !profileList.isEmpty()) {
                        Profile profile = profileList.get(0);
                        if(profile.getAddress() != null){
                            addressButton.setText(profile.getAddress());
                        }else {
                            addressButton.setText(getString(R.string.text_buy_1_button));
                        }
                            phoneEditText.setText(profile.getPhone());

                    }
                });
            }
        });

    }
    private void getAllBasket(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchAllBasket(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllBasket:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllBasket:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Basket>>(){}.getType();

                    List<Basket> basketList = gson.fromJson(responseBody, type);

                    List<OrderItem> orderItems  = new ArrayList<>();

                    for (Basket basketItem : basketList) {
                        orderItems.add(new OrderItem(
                                id_analysis_orders,
                                basketItem.getId_analyzes()
                        ));
                    }
                    createOrderItem(orderItems);
                });
            }
        });
    }
    private void createOrderItem(List<OrderItem> orderItems){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.addOrderItem(orderItems, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("createOrderItem:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("createOrderItem:onResponse", responseBody);
                    getAllBasketDelete();
                    addNotification();
                    startActivity(new Intent(BuyActivity.this, PaymentActivity.class ));
                });
            }
        });
    }
    private String formatAnalysesList(List<String> analyses) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < analyses.size(); i++) {
            sb.append(i + 1).append(". ").append(analyses.get(i));
            if (i < analyses.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    private void addNotification(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        String analysesText = formatAnalysesList(analysisNames);
        AddNotificationBuyAnalyzes addNotificationBuyAnalyzes = new AddNotificationBuyAnalyzes(
                getString(R.string.text_notification_1_1),
                analysesText,
                currentAddress,
                DataBinding.getUuidUser(),
                "purchase",
                (float) total
        );
        supaBaseClient.addNotificationBuyAnalyzes(addNotificationBuyAnalyzes, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("createOrderItem:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("createOrderItem:onResponse", responseBody);
                });
            }
        });
    }
    private void getAllBasketDelete(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.deleteAllBasket(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                runOnUiThread(() -> {
                    Log.e("getAllBasketDelete:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                runOnUiThread(() -> {
                    Log.e("getAllBasketDelete:onResponse", responseBody);
                });
            }
        });
    }
}