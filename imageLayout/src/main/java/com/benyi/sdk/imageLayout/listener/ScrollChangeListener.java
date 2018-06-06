package com.benyi.sdk.imageLayout.listener;

import android.animation.ValueAnimator;

/**
 * Created by MuFe on 2018/5/29.
 */

public class ScrollChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener{
    private UpdateIntModel xModel;
    private UpdateIntModel yModel;

    private int getScrollX(){
        return hasView()?viewWeakReference.get().getScrollX():0;
    }
    private int getScrollY(){
        return hasView()?viewWeakReference.get().getScrollY():0;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(hasView()){
            float fraction=animation.getAnimatedFraction();
            viewWeakReference.get().scrollTo(
                    ((int)(xModel==null?getScrollX():change(xModel.x,xModel.y,fraction))),
                    ((int)(yModel==null?getScrollY():change(yModel.x,yModel.y,fraction))));
            viewWeakReference.get().requestLayout();
        }
    }
}
