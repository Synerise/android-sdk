package com.synerise.sdk.sample.ui.auth;

interface OnPhoneConfirmedListener {
    OnPhoneConfirmedListener NULL = () -> {

    };

    void onPhoneNumberConfirmed();
}
