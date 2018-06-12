package com.synerise.sdk.sample.util;

import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

public class ViewUtils {

    public static void loadImage(String url, SimpleDraweeView imageView) {
        if (url != null && imageView != null)
            imageView.setImageURI(url);
    }

    public static void loadImage(String url, SimpleDraweeView imageView, ProgressBar progressBar) {
        if (url != null && imageView != null) {
            // setController must be before setUri to get callback on fast phones
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                                                    @Override
                                                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo,
                                                                                @Nullable Animatable animatable) {
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                })
                                                .setUri(url)
                                                .build();
            imageView.setController(controller);
        }
    }
}
