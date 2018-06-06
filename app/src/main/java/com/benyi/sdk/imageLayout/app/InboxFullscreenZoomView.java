package com.benyi.sdk.imageLayout.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by MuFe on 2018/5/30.
 * @author MuFe
 */
public class InboxFullscreenZoomView extends SubsamplingScaleImageView {
    public InboxFullscreenZoomView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public InboxFullscreenZoomView(Context context) {
        this(context, null);
    }

    public void init(String resourceData) {
        Glide.with(getContext())
                .asBitmap()
                .load(resourceData)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setImage(ImageSource.cachedBitmap(resource));
                    }
                });
    }

}

