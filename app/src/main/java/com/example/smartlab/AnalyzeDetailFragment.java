package com.example.smartlab;

import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.BasketId;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.UpdateBasket;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AnalyzeDetailFragment extends BottomSheetDialogFragment {

    private Analyzes analyze;
    private TextView title, description, preparation, period, biomaterial;
    private Button addButton;
    private ImageButton backButton;
    private boolean isInCart = false;
    private int id_additions = 0;



    public void setAnalyzeData(Analyzes analyze) {
        this.analyze = analyze;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        preparation = view.findViewById(R.id.duration);
        period = view.findViewById(R.id.result);
        biomaterial = view.findViewById(R.id.biomaterial);
        addButton = view.findViewById(R.id.addButton);
        backButton= view.findViewById(R.id.backButton);

        if (analyze != null) {
            title.setText(analyze.getTitle());
            description.setText(analyze.getDescription());
            preparation.setText(analyze.getPreparation());
            period.setText(formatDays(analyze.getPeriod_of_execution()));
            biomaterial.setText(analyze.getBiomaterial());
            addButton.setText(getString(R.string.text_add) + " " + analyze.getPrice() + " ₽");

            addButton.setOnClickListener(v -> {
                if(isInCart == false){
                    String userId = DataBinding.getUuidUser();
                    isInCart = true;
                    updateBasket(analyze.getId_analyzes(), userId);

                    int color = ContextCompat.getColor(requireContext(), R.color.blue_button_off);
                    ColorStateList colorStateList = ColorStateList.valueOf(color);
                    addButton.setText(getString(R.string.text_item_button_remove));
                    addButton.setBackgroundTintList(colorStateList);
                    addButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text));
                }else if(isInCart == true){
                    getBasketDelete(id_additions);

                    int color = ContextCompat.getColor(requireContext(), R.color.blue_button);
                    ColorStateList colorStateList = ColorStateList.valueOf(color);
                    addButton.setText(getString(R.string.text_add) + " " + analyze.getPrice() + " ₽");
                    addButton.setBackgroundTintList(colorStateList);
                    addButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    isInCart = false;

                }


            });
            backButton.setOnClickListener(v ->{

                dismiss();
            });
        }
    }
    private void getBasketDelete(int id_additions){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.deleteBasket(String.valueOf(id_additions), new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("getBasketDelete:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("getBasketDelete:onResponse", responseBody);
                });
            }
        });
    }
    public void updateBasket(int id_analyzes, String id_client){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        UpdateBasket updateBasket = new UpdateBasket(id_analyzes, id_client);
        supaBaseClient.updateBasket(updateBasket, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("updateBasket:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("updateBasket:onResponse", responseBody);
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        id_additions = json.getInt("id_additions");
                    } catch (JSONException e) {
                        Log.e("Ошибка обработки ответа", "0");
                    }
                });
            }
        });
    }
    private String formatDays(int days) {
        if (days == 1) return "1 день";
        if (days >= 2 && days <= 4) return days + " дня";
        return days + " дней";
    }
}
