package com.synerise.sdk.sample.util;

import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

public abstract class SimpleDraweeViewControllerListener implements ControllerListener<ImageInfo> {

    @Override
    public void onSubmit(String id, Object callerContext) {

    }

    @Override
    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable){

    }

    @Override
    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

    }

    @Override
    public void onIntermediateImageFailed(String id, Throwable throwable) {

    }

    @Override
    public void onFailure(String id, Throwable throwable) {

    }

    @Override
    public void onRelease(String id) {

    }
}