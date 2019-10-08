package com.synerise.sdk.sample.test;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.synerise.sdk.sample.BuildConfig;

public class EspressoTestingIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static CountingIdlingResource mCountingIdlingResource = new CountingIdlingResource(RESOURCE);

    public static void increment() {
        if (BuildConfig.DEBUG) mCountingIdlingResource.increment();
    }

    public static void decrement() {
        if (BuildConfig.DEBUG) mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}