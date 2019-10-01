package com.uni.localvillage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.uni.localvillage.model.User;

import java.util.ArrayList;

public class Utils {
    public final static String NAME = "name", TYPE = "type", EMAIL = "email", PHONE = "phone";

    public static String getValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void setUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor se = sharedPreferences.edit();

        se.putString(NAME, user.getName());
        se.putString(EMAIL, user.getEmail());
        se.putString(PHONE, user.getPhone());
        se.putString(TYPE, user.isProvider() ? "provider" : "client");

        se.apply();
        se.commit();
    }

    public static void showSuccessMessage(Context context, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setMessage(msg);
        ab.setPositiveButton("Ok", null);
        ab.create().show();
    }


    public static ArrayList<String> getCategories() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Internet Provider");
        arrayList.add("Water Supplier");
        arrayList.add("Plumber");
        arrayList.add("Mechanics");
        arrayList.add("RO service");
        arrayList.add("Painter");
        return arrayList;
    }
}
