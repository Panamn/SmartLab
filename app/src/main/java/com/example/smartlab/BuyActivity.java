package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlab.Adapters.AnalyzesAdapter;
import com.example.smartlab.Adapters.DoctorsAdapter;
import com.example.smartlab.Adapters.NewsAdapter;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.Doctors;
import com.example.smartlab.Models.PromotionsNews;
import com.example.smartlab.Models.UpdateBasket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class BuyActivity extends AppCompatActivity  implements AddressBuyFragment.AddressSelectedListener {

    private Button addressButton, dateButton, buyButton;
    private ImageButton backButton;
    private EditText phoneEditText, commentEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        addressButton = findViewById(R.id.addressButton);
        dateButton = findViewById(R.id.dateButton);
        buyButton = findViewById(R.id.buyButton);
        phoneEditText = findViewById(R.id.phoneEditText);
        commentEditText = findViewById(R.id.commentEditText);

        addressButton.setOnClickListener(v -> showAddress());

    }
    @Override
    public void onAddressSelected(String address) {
        addressButton.setText(address);
    }
    private void showAddress(){
        AddressBuyFragment fragment = AddressBuyFragment.newInstance();
        fragment.setListener(this);
        fragment.show(getSupportFragmentManager(), "address_input");
    }


}