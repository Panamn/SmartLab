package com.example.smartlab;

import androidx.annotation.NonNull;

import com.example.smartlab.Models.DataBinding;
import com.example.smartlab.Models.LoginRequest;
import com.example.smartlab.Models.ProfileUpdate;
import com.example.smartlab.Models.ProfileUpdateCard;
import com.google.gson.Gson;

import org.json.JSONObject;

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

    public void sendRecoveryRequest(String email, final SBC_Callback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("type", "recovery");
            json.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(), MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "verify")
                .post(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                callback.onFailure(e); }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);
                        String accessToken = json.getString("access_token");

                        DataBinding.saveBearerToken(accessToken);
                        callback.onResponse(accessToken);
                    } catch (Exception e) {
                        callback.onFailure(new IOException("Ошибка обработки ответа: " + e.getMessage()));
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Неизвестная ошибка";
                    try {
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorCode = errorJson.optString("error_code", "");
                        String errorMsg = errorJson.optString("msg", "Ошибка сервера");

                        if ("otp_expired".equals(errorCode)) {
                            callback.onFailure(new IOException("OTP ист2уцуйёк"));
                        } else if ("invalid_token".equals(errorCode)) {
                            callback.onFailure(new IOException("Неверныйцуй токен"));
                        } else {
                            callback.onFailure(new IOException("Ошибйцука: " + errorMsg));
                        }
                    } catch (Exception e) {
                        callback.onFailure(new IOException("Ошибка сервера: " + errorBody));
                    }
                }
            }
        });
    }

    public void verifyOtp(String email, String code, final SBC_Callback callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("type", "recovery");
            jsonObject.put("token", code);
        } catch (Exception e) {
            callback.onFailure(new IOException("Ошибка формирования данных: " + e.getMessage()));
            return;
        }

        RequestBody body = RequestBody.create(MediaType.get("application/json"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "verify")
                .post(body)
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
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);
                        String accessToken = json.getString("access_token");

                        DataBinding.saveBearerToken(accessToken);
                        callback.onResponse(accessToken);
                    } catch (Exception e) {
                        callback.onFailure(new IOException("Ошибка обработки ответа: " + e.getMessage()));
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Неизвестная ошибка";
                    try {
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorCode = errorJson.optString("error_code", "");
                        String errorMsg = errorJson.optString("msg", "Ошибка сервера");

                        if ("otp_expired".equals(errorCode)) {
                            callback.onFailure(new IOException("OTP истёк"));
                        } else if ("invalid_token".equals(errorCode)) {
                            callback.onFailure(new IOException("Неверный токен"));
                        } else {
                            callback.onFailure(new IOException("Ошибка: " + errorMsg));
                        }
                    } catch (Exception e) {
                        callback.onFailure(new IOException("Ошибка сервера: " + errorBody));
                    }
                }
            }
        });
    }

    public void updatePassword(String newPassword, final SBC_Callback callback) {
        String accessToken = DataBinding.getBearerToken();
        if (accessToken == null) {
            callback.onFailure(new IOException("Требуется авторизация"));
            return;
        }

        JSONObject payload = new JSONObject();
        try {
            payload.put("password", newPassword);
        } catch (Exception e) {
            callback.onFailure(new IOException("Ошибка формирования данных: " + e.getMessage()));
            return;
        }

        RequestBody body = RequestBody.create(MediaType.get("application/json"), payload.toString());
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + AUTH_PATH + "user")
                .put(body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onResponse("Пароль успешно изменён");
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Неизвестная ошибка";
                    callback.onFailure(new IOException("Ошибка при смене пароля: " + errorBody));
                }
            }
        });
    }

    public void updateProfileCard(ProfileUpdateCard profile, final SBC_Callback callback){
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "profiles?id=eq." + DataBinding.getUuidUser())
                .method("PATCH", body)
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", DataBinding.getBearerToken())
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

    public void fetchAllDoctors(final SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "doctors?select=*,profiles(*),specialization_categories(*)")
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", DataBinding.getBearerToken())
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
    public void fetchAllSpecialization(final SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "specialization_categories?select=*")
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
                .url(DOMAIN_NAME + REST_PATH + "analyzes?select=*")
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
    public void fetchCategoriesAllAnalyzes(final SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "categories_of_analyses?select=*")
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
    public void fetchCategoriesListAnalyzes(int id, final SBC_Callback callback){
        Request request = new Request.Builder()
                .url(DOMAIN_NAME + REST_PATH + "analyzes?select=*&id_categories_of_analyses=eq." + id)
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
