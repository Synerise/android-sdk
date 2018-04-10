package com.synerise.sdk.sample.dagger;

import android.content.Context;

import com.synerise.sdk.sample.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigModule {

    @Singleton
    @Provides
    IServiceConfig provideServiceConfig(final Context context) {
        return new IServiceConfig() {

            @Override
            public String getBaseUrl() { return context.getString(R.string.api_base_url); }

            @Override
            public String getApiDateFormat() { return context.getString(R.string.api_date_format); }

            @Override
            public int getReadTimeout() { return context.getResources().getInteger(R.integer.api_read_timeout_sek); }

            @Override
            public int getConnectTimeout() {
                return context.getResources()
                              .getInteger(R.integer.api_connect_timeout_sek);
            }

            @Override
            public long getWriteTimeout() { return context.getResources().getInteger(R.integer.api_write_timeout_sek); }
        };
    }
}
