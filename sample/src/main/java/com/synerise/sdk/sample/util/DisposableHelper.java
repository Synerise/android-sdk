package com.synerise.sdk.sample.util;

import androidx.annotation.Nullable;

import io.reactivex.rxjava3.disposables.Disposable;

public class DisposableHelper {

    public static void dispose(@Nullable Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}