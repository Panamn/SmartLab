package com.example.smartlab.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class ASession {
    private static final String PREFS_NAME = "AppSmartLab";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static SharedPreferences sharedPref;

    public ASession(Context context) {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY_IS_LOGGED_IN);
        editor.apply();
    }
}