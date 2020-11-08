package com.ea2soa.skyphototips.dto;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ea2soa.skyphototips.R;

public class TokenManager {

    SharedPreferences sharedPref;
    Context context;

    /*public void saveToken(String token) {
        //SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_last_weather), weather);
        editor.apply();
    }

    public String readToken() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.saved_last_weather), "Sin Datos");
    }

    public void saveTokenRefresh(String tokenRefresh) {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_last_weather), weather);
        editor.apply();
    }

    public String readTokenRefresh() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.saved_last_weather), "Sin Datos");
    }*/
}
