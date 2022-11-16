package com.example.notify.Public_thing;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils{

    public static void saveSwitch(Boolean flag,Context context) {
        SharedPreferences aSwitch = context.getSharedPreferences("switch", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = aSwitch.edit();
        edit.putBoolean("state", flag);
        edit.commit();
    }
    public static void saveDate(String date,Context context) {
        SharedPreferences aDate = context.getSharedPreferences("date", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = aDate.edit();
        edit.putString("date_info", date);
        edit.commit();
    }
}
