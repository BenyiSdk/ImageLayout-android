package com.benyi.sdk.imageLayout.listener;

import android.animation.ValueAnimator;
import android.view.ViewGroup;

/**
 * Created by MuFe on 2018/5/29.
 */

public class MarginChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    private final ViewGroup.MarginLayoutParams marginLayoutParams = null;
    private UpdateIntModel left;
    private UpdateIntModel top;
    private UpdateIntModel right;
    private UpdateIntModel bottom;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (hasView()) {
            float fraction = animation.getAnimatedFraction();
            if (left != null) {
                marginLayoutParams.leftMargin = (int) change(left.x, left.y, fraction);
            }
            if (top != null) {
                marginLayoutParams.topMargin = (int) change(top.x, top.y, fraction);
            }
            if (right != null) {
                marginLayoutParams.rightMargin = (int) change(right.x, right.y, fraction);
            }
            if(bottom !=null){
                marginLayoutParams.bottomMargin = (int) change(bottom.x, bottom.y, fraction);
            }
            viewWeakReference.get().requestLayout();
        }
    }
}
