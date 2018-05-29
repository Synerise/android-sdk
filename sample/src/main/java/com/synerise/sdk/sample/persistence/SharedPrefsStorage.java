package com.synerise.sdk.sample.persistence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.synerise.sdk.sample.persistence.model.StoragePOJO;

public class SharedPrefsStorage implements IPrefsStorage {

    private static final String PREFS_NAME = "prefs_synerise_sample";

    private interface Prefs {
        String STORAGE_POJO = "storage_pojo";
    }

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    // ******************************************************************************************************

    public SharedPrefsStorage(Context context, Gson gson) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = gson;
    }

    // ******************************************************************************************************

    @SuppressLint("ApplySharedPref")
    @Override
    public void saveStoragePOJO(StoragePOJO storagePOJO) {
        String serializedUser = gson.toJson(storagePOJO);
        sharedPreferences.edit().putString(Prefs.STORAGE_POJO, serializedUser).commit();
    }

    @Override
    @Nullable
    public StoragePOJO readStoragePOJO() {
        String serializedUser = sharedPreferences.getString(Prefs.STORAGE_POJO, null);
        return gson.fromJson(serializedUser, StoragePOJO.class);
    }
}