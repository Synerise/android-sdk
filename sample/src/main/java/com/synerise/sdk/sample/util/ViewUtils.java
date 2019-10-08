package com.synerise.sdk.sample.util;

import android.animation.Animator;
import android.graphics.drawable.Animatable;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import static android.view.View.GONE;

public class ViewUtils {

    public static void loadImage(String url, SimpleDraweeView imageView) {
        if (url != null && imageView != null)
            imageView.setImageURI(url);
    }

    public static void loadImage(String url, SimpleDraweeView imageView, ProgressBar progressBar) {
        if (url != null && imageView != null) {
            // setController must be before setUri to get callback on fast phones
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                                                    @Override
                                                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo,
                                                                                @Nullable Animatable animatable) {
                                                        progressBar.setVisibility(GONE);
                                                    }
                                                    @Override
                                                    public void onFailure(String id, Throwable throwable) {
                                                        Log.i("DraweeUpdate", "Image failed to load: " + throwable.getMessage());
                                                    }
                                                })
                                                .setUri(url)
                                                .build();
            imageView.setController(controller);
        }
    }

    public static void pulse(View view) {
        view.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .translationZ(4f)
            .setDuration(200L)
            .setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationZ(0f)
                        .setDuration(200L)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                view.setEnabled(true);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
    }

    public static String formatStringToJsonLook(String text){

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}
