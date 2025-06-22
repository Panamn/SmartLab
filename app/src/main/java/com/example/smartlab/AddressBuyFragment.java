package com.example.smartlab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddressBuyFragment extends BottomSheetDialogFragment {


    public interface OnAddressSavedListener {
        void onAddressSaved(String fullAddress, boolean saveToProfile);
    }
    private OnAddressSavedListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnAddressSavedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnAddressSavedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address_buy, container, false);
    }


    private EditText cityEditText, streetEditText, apartmentEditText, entranceEditText, floorEditText;
    private Button confirmButton;
    private Switch saveSwitch;

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

//        String fullAddress = city + ", " + street + ", "
//                + apartment + "(" + entrance + ", " + floor + ")";

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEditText.getText().toString().trim();
                String street = streetEditText.getText().toString().trim();
                String apartment = apartmentEditText.getText().toString().trim();
                String entrance = entranceEditText.getText().toString().trim();
                String floor = floorEditText.getText().toString().trim();
                String fullAddress = city + ", " + street + ", "
                + apartment + "(" + entrance + ", " + floor + ")";
                listener.onAddressSaved(fullAddress, saveSwitch.isChecked());
                dismiss();
            }
        });
    }

    public static AddressBuyFragment newInstance() {
        return new AddressBuyFragment();
    }
}

//    private void getProfile(){
//        SupaBaseClient supaBaseClient = new SupaBaseClient();
//        supaBaseClient.fetchCurrentUser(new SupaBaseClient.SBC_Callback() {
//            @Override
//            public void onFailure(IOException e) {
//                requireActivity().runOnUiThread(() -> {
//                    Log.e("getProfile:onFailure", e.getLocalizedMessage());
//                });
//
//            }
//
//            @Override
//            public void onResponse(String responseBody) {
//                requireActivity().runOnUiThread(() -> {
//                    Log.e("getProfile:onResponse", responseBody);
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<List<Profile>>(){}.getType();
//                    List<Profile> profileList = gson.fromJson(responseBody, type);
//                    Profile profile = profileList.get(0);
//                    String[] addressParts =  splitAddress(profile.getAddress());
//                    if(addressParts != null) {
//                        cityEditText.setText(addressParts[0]);
//                        streetEditText.setText(addressParts[1]);
//                        apartmentEditText.setText(addressParts[2]);
//                        entranceEditText.setText(addressParts[3]);
//                        floorEditText.setText(addressParts[4]);
//                    }else{
//                        cityEditText.setText(getString(R.string.text_address_1_edit));
//                        streetEditText.setText(getString(R.string.text_address_2_edit));
//                        apartmentEditText.setText(getString(R.string.text_address_3_edit));
//                        entranceEditText.setText(getString(R.string.text_address_3_edit));
//                        floorEditText.setText(getString(R.string.text_address_3_edit));
//                    }
//                });
//            }
//        });
//
//    }
//    public static String[] splitAddress(String fullAddress) {
//        if (fullAddress == null || fullAddress.trim().isEmpty()) {
//            return new String[]{"", "", "", "", ""};
//        }
//
//        String[] parts = fullAddress.trim().split("\\s+", 5);
//        String city = parts.length > 0 ? parts[0] : ", ";
//        String street = parts.length > 1 ? parts[1] : ", ";
//        String apartment = parts.length > 2 ? parts[2] : "(";
//        String entrance = parts.length > 2 ? parts[3] : ", ";
//        String floor = parts.length > 2 ? parts[4] : ")";
//
//
//        return new String[]{city, street, apartment, entrance, floor};
//    }
