package com.synerise.sdk.sample.util;

import com.facebook.drawee.view.SimpleDraweeView;

public class ViewUtils {

    public static void loadImage(String url, SimpleDraweeView imageView) {
        if (url != null && imageView != null)
            imageView.setImageURI(url);
    }
}
