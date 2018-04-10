package com.synerise.sdk.sample.dagger;

public interface IServiceConfig {

    String getBaseUrl();

    String getApiDateFormat();

    int getReadTimeout();

    int getConnectTimeout();

    long getWriteTimeout();
}
