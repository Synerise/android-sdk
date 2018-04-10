package com.synerise.sdk.sample.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.synerise.sdk.sample.R;

public class ViewUtils {

    public static void loadImage(String url, ImageView imageView) {
        if (url != null && imageView != null)
            Picasso.with(imageView.getContext()).load(url).placeholder(R.mipmap.ic_launcher).into(imageView); // todo
    }
}
