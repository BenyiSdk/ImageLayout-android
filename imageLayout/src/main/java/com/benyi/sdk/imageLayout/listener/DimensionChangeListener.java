package com.benyi.sdk.imageLayout.listener;

import android.animation.ValueAnimator;
import android.view.ViewGroup;

/**
 * Created by MuFe on 2018/5/29.
 * @author MuFe
 */
public class DimensionChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener{
    private final ViewGroup.LayoutParams layoutParams = null;
    private UpdateIntModel width;
    private UpdateIntModel height;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (hasView()) {
            float fraction = animation.getAnimatedFraction();
            if (width != null) {
                layoutParams.width = (int) change(width.x, width.y, fraction);
            }
            if (height != null) {
                layoutParams.height = (int) change(height.x, height.y, fraction);
            }
            viewWeakReference.get().requestLayout();
        }
    }
}
