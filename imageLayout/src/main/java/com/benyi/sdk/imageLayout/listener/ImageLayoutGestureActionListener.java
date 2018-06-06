package com.benyi.sdk.imageLayout.listener;

import com.benyi.sdk.imageLayout.view.ImageLayoutGestureListenerView;

/**
 * Created by MuFe on 2018/6/6.
 */

public interface ImageLayoutGestureActionListener {
    /**
     * 向左移动
     * @param x 移动距离
     * @param xPproportion 移动距离和整个view长度的比例
     */
    void moveLeft(float x, float xPproportion);

    void moveTop(float x, float y, float yPproportion);

    /**
     * 向左移动
     * @param x 移动距离
     * @param xPproportion 移动距离和整个view长度的比例
     */
    void moveRight(float x, float xPproportion);

    void moveBottom(float x, float y, float yPproportion);

    void leftEnd(boolean change);

    void ringtEnd(boolean change);

    void topEnd(boolean change);

    void downEnd(boolean change);

    void startMove(ImageLayoutGestureListenerView.Orientation e);

    void endMove();

    void singeClick();
}
