package com.synerise.sdk.sample.dagger;

import android.app.Application;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synerise.sdk.sample.persistence.AccountManager;
import com.synerise.sdk.sample.persistence.IPrefsStorage;
import com.synerise.sdk.sample.persistence.SharedPrefsStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private final Application app;

    public MainModule(Application app) {
        this.app = app;
    }

    @Provides
    Context provideAppContext() {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    Gson provideGson(IServiceConfig serviceConfig) {
        return new GsonBuilder()
                .setDateFormat(serviceConfig.getApiDateFormat())
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setPrettyPrinting()
                .create();
    }

    @Singleton
    @Provides
    IPrefsStorage provideSharedPrefsStorage(Context context, Gson gson) {
        return new SharedPrefsStorage(context, gson);
    }

    @Singleton
    @Provides
    AccountManager provideAccountManager(IPrefsStorage storage) {
        return new AccountManager(storage);
    }
}
