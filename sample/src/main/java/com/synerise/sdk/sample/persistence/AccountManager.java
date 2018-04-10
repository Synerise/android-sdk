package com.synerise.sdk.sample.persistence;

import android.content.Context;
import android.support.annotation.NonNull;

import com.synerise.sdk.sample.persistence.model.StoragePOJO;

public class AccountManager {

    @NonNull private final IPrefsStorage prefsStorage;
    @NonNull private final StoragePOJO storagePOJO;

    // ****************************************************************************************************************************************

    public AccountManager(Context context, @NonNull IPrefsStorage prefsStorage) {
        this.prefsStorage = prefsStorage;
        this.storagePOJO = prefsStorage.readStoragePOJO() == null ? new StoragePOJO() : prefsStorage.readStoragePOJO();
    }

    // ****************************************************************************************************************************************

    public void setSignedIn(boolean isSignedIn) {
        storagePOJO.setSignedIn(isSignedIn);
        prefsStorage.saveStoragePOJO(storagePOJO);
        // sign out = clear data
        if (!isSignedIn) {
            setUserName(null);
            setUserLastName(null);
            setUserEmail(null);
        }
    }

    public void setUserName(String name) {
        storagePOJO.setName(name);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public void setUserLastName(String lastName) {
        storagePOJO.setLastName(lastName);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public void setUserEmail(String email) {
        storagePOJO.setEmail(email);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public boolean isSignedIn() {
        return storagePOJO.isSignedIn();
    }

    public String getFirstName() {
        return storagePOJO.getName();
    }

    public String getLastName() {
        return storagePOJO.getLastName();
    }

    public String getEmail() {
        return storagePOJO.getEmail();
    }
}