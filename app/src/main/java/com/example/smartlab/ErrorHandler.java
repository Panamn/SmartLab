package com.example.smartlab;

import android.content.Context;
import android.net.http.HttpException;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ErrorHandler {

    public static void handleError(Context context, Throwable error) {
        String errorMessage = parseErrorMessage(error);

        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showErrorDialog(errorMessage);
        }

        logError(error);
    }

    private static String parseErrorMessage(Throwable error) {
        if (error == null) {
            return "Неизвестная ошибка";
        }

        if (error instanceof IOException) {
            return "Нет подключения к интернету. Проверьте сеть и попробуйте снова";
        }

        if (error instanceof HttpException) {
            int code = ((HttpException) error).hashCode();
            switch (code) {
                case 401:
                    return "Ошибка авторизации (401)";
                case 403:
                    return "Доступ запрещен (403)";
                case 404:
                    return "Данные не найдены (404)";
                case 500:
                    return "Ошибка сервера (500)";
                default:
                    return "Ошибка сервера: " + code;
            }
        }

        if (error instanceof SocketTimeoutException) {
            return "Таймаут соединения. Проверьте интернет";
        }

        if (error instanceof UnknownHostException) {
            return "Сервер недоступен. Проверьте подключение";
        }

        String userMessage = "Произошла ошибка";
        if (error.getMessage() != null && !error.getMessage().isEmpty()) {
            userMessage += ": " + error.getMessage();
        }

        return userMessage;
    }

    private static void logError(Throwable error) {
        Log.e("APP_ERROR", error.getMessage(), error);
    }
}
