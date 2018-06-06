package com.benyi.sdk.imageLayout.listener;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by MuFe on 2018/5/29.
 * @author MuFe
 */

public abstract class ChangeUpdateListener {
    WeakReference<View> viewWeakReference;
    float change(float x, float y, float z) {
        float temp = y - x;
        float temp1 = 1.0f - z;
        temp1 = temp1 * temp;
        return y - temp1;
    }

    boolean hasView() {
        return viewWeakReference.get()!=null;
    }

    class UpdateIntModel {
        public  int x;
        public  int y;
    }

    class UpdateFloatModel{
        public float x;
        public float y;
    }
}
