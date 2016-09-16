package com.chhd.stock.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CWQ on 2016/7/9.
 */
public class SharedPreferencesUtil {

    public static SharedPreferences mSharedPreferences;

    public static boolean getGuidetInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CONFIG, Context.MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean(Consts.KEY_FIRST_START, true);
        return firstStart;
    }

    public static void cacheGuideInfo(Context context, boolean firstStart) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Consts.CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Consts.KEY_FIRST_START, firstStart);
        editor.commit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(Consts.CONFIG, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(Consts.CONFIG, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getBoolean(key, false);
    }

    public static void putString(Context context, String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(Consts.CONFIG, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(Consts.CONFIG, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key, "");
    }
}
