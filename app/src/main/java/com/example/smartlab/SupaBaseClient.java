package com.example.smartlab;

import androidx.annotation.NonNull;

import com.example.smartlab.Models.LoginRequest;
import com.example.smartlab.Models.Profile;
import com.example.smartlab.Models.ProfileUpdate;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupaBaseClient {
    public interface SBC_Callback{
        public void onFailure(IOException e);
        public void onResponse(String responseBody);
    }
    public static String DOMAIN_NAME = "https://ubotxdvkhvusymbhrgvy.supabase.co/";
    public static String REST_PATH = "rest/v1/";
    public static String AUTH_PATH = "auth/v1/";
    public static String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVib3R4ZHZraHZ1c3ltYmhyZ3Z5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDg5MTc2MDIsImV4cCI6MjA2NDQ5MzYwMn0.mbxaS4NKcoSrNfFbF89g7MXpD1zvVjCXxiOGpOS_BeE";

    OkHttpClient client = new OkHttpClient();

    public void updateProfile(ProfileUpdate profile, final SBC_Callback callback){
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?id=eq." + DataBinding.getUuidUser())
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization",DataBinding.getBearerToken())
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
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
    public void register(LoginRequest loginRequest, final SBC_Callback callback){
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "signup")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
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

    public void login(LoginRequest loginRequest, final SBC_Callback callback){
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "token?grant_type=password")
                .method("POST", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
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

    public void fetchCurrentUser(final SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?select=*&id=eq." + DataBinding.getUuidUser())
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
    public void fetchAllUserAnalyzes(final SBC_Callback callback){
        Request request = new Request.Builder()
                .url( DOMAIN_NAME + REST_PATH + "analyzes?select=*&id_profile=eq." + DataBinding.getUuidUser())
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
