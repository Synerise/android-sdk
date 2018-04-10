package com.synerise.sdk.sample.util;

import android.support.annotation.Nullable;

import io.reactivex.disposables.Disposable;

public class DisposableHelper {

    public static void dispose(@Nullable Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}