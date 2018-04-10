package com.synerise.sdk.sample.ui.dashboard;

import android.util.Log;

public interface IDashboardContext {

    IDashboardContext NULL = new IDashboardContext() {
        @Override
        public void handleSignUpSuccess() {
            Log.e(this.getClass().getSimpleName(), "Context is missing!");
        }

        @Override
        public void handleSignInSuccess() {
            Log.e(this.getClass().getSimpleName(), "Context is missing!");
        }
    };

    void handleSignUpSuccess();

    void handleSignInSuccess();
}