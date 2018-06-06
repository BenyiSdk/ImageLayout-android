package com.benyi.sdk.imageLayout.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.util.Property;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import com.benyi.sdk.imageLayout.listener.DimensionChangeListener;
import com.benyi.sdk.imageLayout.listener.MarginChangeListener;
import com.benyi.sdk.imageLayout.listener.PaddingChangeListener;
import com.benyi.sdk.imageLayout.listener.PercentChangeListener;
import com.benyi.sdk.imageLayout.listener.ScrollChangeListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MuFe on 2018/5/29.
 * 动画构造
 * @author MuFe
 */

public class ViewPropertyObjectAnimator {
    private WeakReference<View> viewWeakReference;
    private boolean d = false;
    private Interpolator interpolator;
    private List<Animation.AnimationListener> animationListeners = new ArrayList<>();
    private List<ValueAnimator.AnimatorUpdateListener> animatorUpdateListeners = new ArrayList<>();
    private List<Animator.AnimatorPauseListener> animatorPauseListeners = new ArrayList<>();
    private ArrayMap<Property<View, Float>, PropertyValuesHolder> propertyValuesHolderArrayMap = new ArrayMap<>();
    private long duration = -1;
    private long startDelay = -1;
    private MarginChangeListener marginChangeListener;
    private DimensionChangeListener dimensionChangeListener;
    private PaddingChangeListener paddingChangeListener;
    private PercentChangeListener percentChangeListener;
    private ScrollChangeListener scrollChangeListener;

    private ViewPropertyObjectAnimator(View view) {
        viewWeakReference = new WeakReference<>(view);
    }

    public static ViewPropertyObjectAnimator getInstance(View view) {
        return new ViewPropertyObjectAnimator(view);
    }

    public void addProperty(Property<View, Float> property, float x) {
        if (isInit()) {
            addProperty(property, (property.get(viewWeakReference.get())), x);
        }
    }

    private void addProperty(Property<View, Float> property, float x, float y) {
        propertyValuesHolderArrayMap.remove(property);
        float[] temp = new float[2];
        temp[0] = x;
        temp[1] = y;
        propertyValuesHolderArrayMap.put(property, PropertyValuesHolder.ofFloat(property, temp));
    }

    @SuppressLint("NewApi")
    public ObjectAnimator getViewAnimator() {
        if (isInit()) {
            Collection collections = propertyValuesHolderArrayMap.values();
            PropertyValuesHolder[] propertyValuesHolders = new PropertyValuesHolder[collections.size()];
            propertyValuesHolders = (PropertyValuesHolder[]) collections.toArray(propertyValuesHolders);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(viewWeakReference.get(), propertyValuesHolders);
            if (d) {
                objectAnimator.addListener(new ViewAnimatorListenerAdapter());
            }
            if (this.startDelay != -1) {
                objectAnimator.setStartDelay(this.startDelay);
            }
            if (this.duration != -1) {
                objectAnimator.setDuration(this.duration);
            }
            if (this.interpolator != null) {
                objectAnimator.setInterpolator(this.interpolator);
            }
            Iterator iterator = animationListeners.iterator();
            while (iterator.hasNext()) {
                Animator.AnimatorListener animatorListener = (Animator.AnimatorListener) iterator.next();
                objectAnimator.addListener(animatorListener);
            }
            if (this.marginChangeListener != null) {
                objectAnimator.addUpdateListener(this.marginChangeListener);
            }
            if (this.paddingChangeListener != null) {
                objectAnimator.addUpdateListener(this.paddingChangeListener);
            }
            if (this.dimensionChangeListener != null) {
                objectAnimator.addUpdateListener(this.dimensionChangeListener);
            }
            if (this.percentChangeListener != null) {
                objectAnimator.addUpdateListener(this.percentChangeListener);
            }
            if (this.scrollChangeListener != null) {
                objectAnimator.addUpdateListener(this.scrollChangeListener);
            }
            iterator = animatorUpdateListeners.iterator();
            while (iterator.hasNext()) {
                ValueAnimator.AnimatorUpdateListener animatorListener = (ValueAnimator.AnimatorUpdateListener) iterator.next();
                objectAnimator.addUpdateListener(animatorListener);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                iterator = animatorPauseListeners.iterator();
                while (iterator.hasNext()) {
                    ValueAnimator.AnimatorPauseListener animatorListener = (ValueAnimator.AnimatorPauseListener) iterator.next();
                    objectAnimator.addPauseListener(animatorListener);
                }
            }
            return objectAnimator;
        } else {
            return ObjectAnimator.ofFloat(null, View.ALPHA, new float[]{1.0f, 1.0f});
        }
    }

    public ViewPropertyObjectAnimator setScaleXProperty(float scaleX) {
        addProperty(View.SCALE_X, scaleX);
        return this;
    }

    public ViewPropertyObjectAnimator setScaleYProperty(float scaleY) {
        addProperty(View.SCALE_Y, scaleY);
        return this;
    }

    public ViewPropertyObjectAnimator setAlphaProperty(float alpha) {
        addProperty(View.ALPHA, alpha);
        return this;
    }

    public ViewPropertyObjectAnimator setRotationProperty(float rotation) {
        addProperty(View.ROTATION, rotation);
        return this;
    }

    public ViewPropertyObjectAnimator setXProperty(float x) {
        addProperty(View.X, x);
        return this;
    }

    public ViewPropertyObjectAnimator setYProperty(float y) {
        addProperty(View.Y, y);
        return this;
    }

    public ViewPropertyObjectAnimator setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("duration cannot be < 0");
        }
        this.duration = duration;
        return this;
    }


    private boolean isInit() {
        return viewWeakReference.get() != null;
    }

    class ViewAnimatorListenerAdapter extends AnimatorListenerAdapter {
        private int layerType = View.LAYER_TYPE_NONE;


        @Override
        public void onAnimationEnd(Animator animation) {
            if (ViewPropertyObjectAnimator.this.isInit()) {
                ViewPropertyObjectAnimator.this.viewWeakReference.get().setLayerType(this.layerType, null);
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (ViewPropertyObjectAnimator.this.isInit()) {
                View view = ViewPropertyObjectAnimator.this.viewWeakReference.get();
                this.layerType = view.getLayerType();
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (view.isAttachedToWindow()) {
                        view.buildLayer();
                    }
                }
            }
        }
    }

}
