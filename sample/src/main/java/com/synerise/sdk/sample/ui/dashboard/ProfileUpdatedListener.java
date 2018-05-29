package com.synerise.sdk.sample.ui.dashboard;

public interface ProfileUpdatedListener {
    ProfileUpdatedListener NULL = () -> {};

    void profileUpdated();
}
