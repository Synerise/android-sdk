package com.synerise.sdk.sample.test;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;

import com.synerise.sdk.sample.BuildConfig;

public class EspressoTestingIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static CountingIdlingResource mCountingIdlingResource = new CountingIdlingResource(RESOURCE);

    // ****************************************************************************************************************************************

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