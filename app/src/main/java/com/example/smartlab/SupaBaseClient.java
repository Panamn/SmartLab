package com.example.smartlab;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SupaBaseClient {
    public interface SBC_Callback{
        public void onFailure(IOException e);
        public void onResponse(String responseBody);
    }
    public static String DOMAIN_NAME = "https://ubotxdvkhvusymbhrgvy.supabase.co/";
    public static String REST_PATH = "rest/v1/";
    public static String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVib3R4ZHZraHZ1c3ltYmhyZ3Z5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDg5MTc2MDIsImV4cCI6MjA2NDQ5MzYwMn0.mbxaS4NKcoSrNfFbF89g7MXpD1zvVjCXxiOGpOS_BeE";
    //public static String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVib3R4ZHZraHZ1c3ltYmhyZ3Z5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDg5MTc2MDIsImV4cCI6MjA2NDQ5MzYwMn0.mbxaS4NKcoSrNfFbF89g7MXpD1zvVjCXxiOGpOS_BeE";

    OkHttpClient client = new OkHttpClient();
    public void fetchAllAnalyzes(final SBC_Callback callback){
        Request request = new Request.Builder()
                .url( DOMAIN_NAME + REST_PATH + "analyzes?select=*")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", DataBinding.getBearerToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseBody = response.body().string();
                    callback.onResponse(responseBody);
                }else {
                    callback.onFailure(new IOException("Ошибка сервера: " + response));
                }
            }
        });
    }
}
