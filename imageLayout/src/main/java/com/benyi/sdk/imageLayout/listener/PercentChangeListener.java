package com.benyi.sdk.imageLayout.listener;

import android.animation.ValueAnimator;
import android.support.percent.PercentLayoutHelper;

/**
 * Created by MuFe on 2018/5/29.
 */

public class PercentChangeListener extends ChangeUpdateListener implements ValueAnimator.AnimatorUpdateListener{
    private PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo;
    private UpdateFloatModel widthPercentModel;
    private UpdateFloatModel heightPercentModel;
    private UpdateFloatModel leftPercentModel;
    private UpdateFloatModel rightPercentModel;
    private UpdateFloatModel topPercentModel;
    private UpdateFloatModel bottomPercentModel;
    private UpdateFloatModel aspectRatioModel;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(hasView()){
            float fraction=animation.getAnimatedFraction();
            if(widthPercentModel!=null){
                percentLayoutInfo.widthPercent=change(widthPercentModel.x,widthPercentModel.y,fraction);
            }
            if(heightPercentModel!=null){
                percentLayoutInfo.heightPercent=change(heightPercentModel.x,heightPercentModel.y,fraction);
            }
            if(leftPercentModel!=null){
                percentLayoutInfo.leftMarginPercent=change(leftPercentModel.x,leftPercentModel.y,fraction);
            }
            if(rightPercentModel!=null){
                percentLayoutInfo.rightMarginPercent=change(rightPercentModel.x,rightPercentModel.y,fraction);
            }
            if(topPercentModel!=null){
                percentLayoutInfo.topMarginPercent=change(topPercentModel.x,topPercentModel.y,fraction);
            }
            if(bottomPercentModel!=null){
                percentLayoutInfo.bottomMarginPercent=change(bottomPercentModel.x,bottomPercentModel.y,fraction);
            }
            if(aspectRatioModel!=null){
                percentLayoutInfo.aspectRatio=change(aspectRatioModel.x,aspectRatioModel.y,fraction);
            }
            viewWeakReference.get().requestLayout();
        }
    }
}
