package com.benyi.sdk.imageLayout.listener;

import android.animation.ValueAnimator;

/**
 * Created by MuFe on 2018/5/29.
 */

public class PaddingChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    private UpdateIntModel left;
    private UpdateIntModel top;
    private UpdateIntModel right;
    private UpdateIntModel bottom;

    private int getLeft() {
        return hasView() ? viewWeakReference.get().getPaddingLeft() : 0;
    }

    private int getTop() {
        return hasView() ? viewWeakReference.get().getPaddingTop() : 0;
    }

    private int getBottom() {
        return hasView() ? viewWeakReference.get().getPaddingBottom() : 0;
    }

    private int getRight() {
        return hasView() ? viewWeakReference.get().getPaddingRight() : 0;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (hasView()) {
            float fraction = animation.getAnimatedFraction();
            viewWeakReference.get().setPadding(
                    left!=null?(int)change(left.x,left.y,fraction):getLeft(),
                    top!=null?(int)change(top.x,top.y,fraction):getTop(),
                    right!=null?(int)change(right.x,right.y,fraction):getRight(),
                    bottom!=null?(int)change(bottom.x,bottom.y,fraction):getBottom());
            viewWeakReference.get().requestLayout();
        }
    }
}
