package com.example.menuwhiz;

import android.content.Context;
import android.content.SharedPreferences;

//activity that deals with session management- both customer and restaurant
public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SHARED_PREF_NAME = "session";
    String SESSION_KEY_CUSTOMER = "session user";
    String SESSION_KEY_RESTAURANT = "session restaurant";

    public SessionManagement(Context context) {
        sharedPreferences= context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSessionRestaurant ( Restaurant R) {

        String id = R.getRestaurant_id();

        editor.putString(SESSION_KEY_RESTAURANT, id).commit();
    }

    public void saveSessionCustomer ( Customer C) {

        String id = C.getCustomer_id();

        editor.putString(SESSION_KEY_CUSTOMER, id).commit();
    }
    public String getSessionRestaurant() {
        //return user whose session is saved
        return sharedPreferences.getString(SESSION_KEY_RESTAURANT, null);
    }

    public String getSessionCustomer() {
        //return user whose session is saved
        return sharedPreferences.getString(SESSION_KEY_CUSTOMER, null);
    }


    public void removeSessionRestaurant() {
        editor.putString(SESSION_KEY_RESTAURANT, null).commit();
    }

    public void removeSessionCustomer() {
        editor.putString(SESSION_KEY_CUSTOMER, null).commit();
    }
}

