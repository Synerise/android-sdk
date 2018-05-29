package com.synerise.sdk.sample.util;

import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;

public class ViewUtils {

    public static void loadImage(String url, SimpleDraweeView imageView) {
        if (url != null && imageView != null)
            imageView.setImageURI(url);
    }

    public static void loadImage(String url, SimpleDraweeView imageView, ProgressBar progressBar) {
        if (url != null && imageView != null) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                .setUri(url)
                                                .setControllerListener(new SimpleDraweeViewControllerListener() {
                                                    @Override
                                                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo,
                                                                                @Nullable Animatable animatable) {
                                                        imageView.setVisibility(View.VISIBLE);
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                })
                                                .build();
            imageView.setController(controller);
        }
    }
}
