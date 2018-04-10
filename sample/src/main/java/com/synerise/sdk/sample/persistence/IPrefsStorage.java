package com.synerise.sdk.sample.persistence;

import com.synerise.sdk.sample.persistence.model.StoragePOJO;

public interface IPrefsStorage {

    void saveStoragePOJO(StoragePOJO storagePOJO);

    StoragePOJO readStoragePOJO();
}
