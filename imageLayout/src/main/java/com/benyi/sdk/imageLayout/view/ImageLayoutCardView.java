package com.benyi.sdk.imageLayout.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.benyi.sdk.imageLayout.listener.ImageLayoutActionListener;

/**
 * Created by MuFe on 2018/5/29.
 * @author MuFe
 */

public class ImageLayoutCardView<T> extends LinearLayout {
    private T data;
    private float left;
    private float top;
    private int width;
    private int height;
    private boolean init;
    private ImageLayoutActionListener layoutActionListener;
    public ImageLayoutCardView(Context context,T data,ImageLayoutActionListener layoutActionListener) {
        super(context);
        this.init = false;
        this.data = data;
        this.layoutActionListener=layoutActionListener;

    }

    public void init() {
        if (this.init) {
            return;
        }
        this.init = true;
        layoutActionListener.loadResourceToCardView(data,this);
    }



    public void start(float x, float y) {
        this.left = x;
        this.top = y;
    }

    public void init(int x, int y) {
        this.width = x;
        this.height = y;
    }

    public void moveToTop(float x, float y, float proportion) {
        float temp = 0.0006f;
        setX(this.left - x);
        setY(this.top - y);
        setRotation(y * 12.0F / 800);
        setScaleX(1.0f-Math.abs(temp*y));
        setScaleY(1.0f-Math.abs(temp*y));
    }

    public void showNext(float x, float xPproportion) {
        float f1 = 1.0F;
        float f2 = 0.0F + xPproportion;
        float f3 = 0.8F + 0.2F * xPproportion;
        if(f2<=f1){
            if (f2 < 0) {
                f1 = 0;
            } else {
                f1 = f2;
            }
        }
        setAlpha(f1);
        setScaleX(f3);
        setScaleY(f3);
    }

    public void hideTop(float x, float xPproportion) {
        float f1 = 1.0F;
        float f2 = 0.0F + (f1 - xPproportion);
        float f3 = 0.8F + 0.2F * (f1 - xPproportion);
        if (f2 <= f1) {
            f1 = f2;
        }
        setAlpha(f1);
        setScaleX(f3);
        setScaleY(f3);
    }


    public void firstPhotoMoveToRight(float x, float xPproportion) {
        setX(this.left - 0.15f * x);
        setAlpha(1.0f);
    }

    public void moveToBottom(float x, float y, float yPproportion) {
        setY(this.top - 0.5f * y);
    }

    public void moveToRight(float x, float xPproportion) {
        setX(this.left - x);
        setAlpha(1.0f);
    }

    public void moveToLeft(float x, float xPproportion) {
        setX(this.left - x);
        setAlpha(1.0f);
    }

    public T getResourceData(){
        return data;
    }

    public void refresh(){
        this.init=false;
    }
}
