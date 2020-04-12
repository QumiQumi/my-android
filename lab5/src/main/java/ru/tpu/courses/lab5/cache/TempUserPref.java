package ru.tpu.courses.lab5.cache;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.tpu.courses.lab5.db.User;

public class TempUserPref {
    private static final String PREF_LOGIN = "login";
    private static final String PREF_PASSWORD = "password";
    private final SharedPreferences prefs;

    public TempUserPref(@NonNull Context context) {
        prefs = context.getSharedPreferences("temp_user", Context.MODE_PRIVATE);
    }

    @Nullable
    public String getLogin() {
        return prefs.getString(PREF_LOGIN, null);
    }

    @Nullable
    public String getPassword() {
        return prefs.getString(PREF_PASSWORD, null);
    }


    public void setPasswods(@Nullable String login, @Nullable String password ){
        prefs.edit()
                .putString(PREF_LOGIN, login)
                .putString(PREF_PASSWORD, password)
                .apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}