package com.example.smartlab;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.smartlab.Models.Analyzes;
import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.Profile;
import com.example.smartlab.Models.ProfileUpdateCard;
import com.example.smartlab.Models.UpdateBasket;
import com.example.smartlab.Models.UpdateProfileAddress;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AddressBuyFragment extends BottomSheetDialogFragment {

    private EditText cityEditText, streetEditText, apartmentEditText, entranceEditText, floorEditText;
    private Button confirmButton;
    private Switch saveSwitch;
    private AddressSelectedListener listener;

    public static AddressBuyFragment newInstance() {
        return new AddressBuyFragment();
    }
    public interface AddressSelectedListener {
        void onAddressSelected(String address);
    }
    public void setListener(AddressSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address_buy, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityEditText = view.findViewById(R.id.cityEditText);
        streetEditText = view.findViewById(R.id.streetEditText);
        apartmentEditText = view.findViewById(R.id.apartmentEditText);
        entranceEditText = view.findViewById(R.id.entranceEditText);
        floorEditText = view.findViewById(R.id.floorEditText);
        confirmButton = view.findViewById(R.id.confirmButton);
        saveSwitch = view.findViewById(R.id.saveSwitch);

        String city = cityEditText.getText().toString().trim();
        String street = cityEditText.getText().toString().trim();
        String apartment = cityEditText.getText().toString().trim();
        String entrance = cityEditText.getText().toString().trim();
        String floor = cityEditText.getText().toString().trim();

        String fullAddress = city + ", " + street + ", "
                + apartment + "(" + entrance + ", " + floor + ")";

        getProfile();

        boolean isChecked = ((Switch) view).isChecked();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fullAddress.isEmpty()) {
                    if (listener != null) {
                        listener.onAddressSelected(fullAddress);
                    }
                    dismiss();
                } else {
                    Toast.makeText(requireContext(), "Введите адрес", Toast.LENGTH_SHORT).show();
                }
                if (isChecked) {
                    updateProfile(fullAddress);
                }else {
                    dismiss();
                }
            }
        });
        }
    private void updateProfile(String fullAddress) {
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        UpdateProfileAddress updateProfileAddress = new UpdateProfileAddress(fullAddress);
        supaBaseClient.updateProfileAddress(updateProfileAddress, new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("updateProfile:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("updateProfile:onResponse", responseBody);
                    dismiss();
                });
            }
        });
    }
    private void getProfile(){
        SupaBaseClient supaBaseClient = new SupaBaseClient();
        supaBaseClient.fetchCurrentUser(new SupaBaseClient.SBC_Callback() {
            @Override
            public void onFailure(IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("getProfile:onFailure", e.getLocalizedMessage());
                });

            }

            @Override
            public void onResponse(String responseBody) {
                requireActivity().runOnUiThread(() -> {
                    Log.e("getProfile:onResponse", responseBody);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Profile>>(){}.getType();
                    List<Profile> profileList = gson.fromJson(responseBody, type);
                    Profile profile = profileList.get(0);
                    String[] addressParts =  splitAddress(profile.getAddress());
                    if(addressParts != null) {
                        cityEditText.setText(addressParts[0]);
                        streetEditText.setText(addressParts[1]);
                        apartmentEditText.setText(addressParts[2]);
                        entranceEditText.setText(addressParts[3]);
                        floorEditText.setText(addressParts[4]);
                    }else{
                        cityEditText.setText(getString(R.string.text_address_1_edit));
                        streetEditText.setText(getString(R.string.text_address_2_edit));
                        apartmentEditText.setText(getString(R.string.text_address_3_edit));
                        entranceEditText.setText(getString(R.string.text_address_3_edit));
                        floorEditText.setText(getString(R.string.text_address_3_edit));
                    }
                });
            }
        });

    }
    public static String[] splitAddress(String fullAddress) {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            return new String[]{"", "", "", "", ""};
        }

        String[] parts = fullAddress.trim().split("\\s+", 5);
        String city = parts.length > 0 ? parts[0] : ", ";
        String street = parts.length > 1 ? parts[1] : ", ";
        String apartment = parts.length > 2 ? parts[2] : "(";
        String entrance = parts.length > 2 ? parts[3] : ", ";
        String floor = parts.length > 2 ? parts[4] : ")";


        return new String[]{city, street, apartment, entrance, floor};
    }
}
