package com.benyi.sdk.imageLayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.benyi.sdk.imageLayout.listener.ImageLayoutGestureActionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MuFe on 2018/5/28.
 * @author MuFe
 */
public class ImageLayoutGestureListenerView extends ViewGroup {
    public enum Orientation {
        NONE,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
    private boolean inFinshAnimation;
    private boolean isInMove;
    private Orientation orientation;
    private float startX;
    private float startY;
    private long startTime;
    private List<ImageLayoutGestureActionListener> imageViewActionListeners;

    public ImageLayoutGestureListenerView(Context context) {
        this(context, null, 0);
    }

    public ImageLayoutGestureListenerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayoutGestureListenerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.inFinshAnimation = true;
        this.isInMove = false;
        this.orientation = Orientation.NONE;
        this.imageViewActionListeners = new ArrayList<>();
    }


    private void changeMoveState(Orientation e) {
        for(int i = 0; i<this.imageViewActionListeners.size(); i++){
            ImageLayoutGestureActionListener listener = this.imageViewActionListeners.get(i);
            this.isInMove = true;
            listener.startMove(e);
        }
    }

    private void move(Orientation e, float x, float y) {
        float width = (float) getWidth();
        float height = (float) getHeight();
        float xPproportion = x / width;
        float yPproportion = y / height;
        for(int i = 0; i<this.imageViewActionListeners.size(); i++){
            ImageLayoutGestureActionListener listener = this.imageViewActionListeners.get(i);
            if (e == Orientation.LEFT) {
                listener.moveLeft(x, xPproportion);
            } else if (e == Orientation.RIGHT) {
                listener.moveRight(x, -xPproportion);
            } else if (e == Orientation.UP) {
                listener.moveTop(x, y, yPproportion);
            } else if (e == Orientation.DOWN) {
                listener.moveBottom(x, y, -yPproportion);
            }
        }
    }

    private void endMove(Orientation e, float x, float y) {
        float temp1 = 160.0F;
        float temp2 = 120.0F;
        this.isInMove = false;
        for(int i = 0; i<this.imageViewActionListeners.size(); i++){
            ImageLayoutGestureActionListener listener = this.imageViewActionListeners.get(i);
            listener.endMove();
            if (e == Orientation.LEFT) {
                listener.leftEnd(x > temp2);
            } else if (e == Orientation.RIGHT) {
                listener.ringtEnd(-x > temp2);
            } else if (e == Orientation.UP) {
                listener.topEnd(y > temp1);
            } else if (e == Orientation.DOWN) {
                listener.downEnd(-y > temp1);
            }
        }
    }

    private void singeClick() {
        this.isInMove = false;
        for(int i = 0; i<this.imageViewActionListeners.size(); i++){
            ImageLayoutGestureActionListener listener = this.imageViewActionListeners.get(i);
            listener.singeClick();
        }
    }

    public boolean isInMove() {
        return this.isInMove;
    }

    public void endAnimation() {
        this.inFinshAnimation = true;
    }

    public void startAnimation() {
        this.inFinshAnimation = false;
    }

    boolean isAnimation() {
        return this.inFinshAnimation;
    }

    public void addListener(ImageLayoutGestureActionListener listener) {
        this.imageViewActionListeners.add(listener);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isAnimation()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = Calendar.getInstance().getTimeInMillis();
                startX = event.getX();
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float X1 = startX - event.getX();
                float Y1 = startY - event.getY();
                if (this.orientation == Orientation.NONE) {
                    if (X1 == 0) {
                        if (Y1 == 0) {
                            return true;
                        }
                    }
                    boolean isSu = (double) (X1 * X1) < ((double) (Y1 * Y1)) / 1.5;
                    if (isSu) {
                        if (Y1 > 0) {
                            this.orientation = Orientation.UP;
                        } else {
                            this.orientation = Orientation.DOWN;
                        }
                    } else {
                        if (X1 > 0) {
                            this.orientation = Orientation.LEFT;
                        } else {
                            this.orientation = Orientation.RIGHT;
                        }
                    }
                    changeMoveState(this.orientation);
                }
                move(this.orientation, X1, Y1);
                break;
            case MotionEvent.ACTION_UP:
                float X2 = startX - event.getX();
                float Y2 = startY - event.getY();
                long time = Calendar.getInstance().getTimeInMillis();
                time = time - startTime;
                float X3;
                float Y3;
                if (X2 > 0) {
                    X3 = X2;
                } else {
                    X3 = -X2;
                }
                if (Y2 > 0) {
                    Y3 = Y2;
                } else {
                    Y3 = -Y2;
                }
                if (time < 200 && X3 < 20 && Y3 < 20) {
                    singeClick();
                    this.orientation = Orientation.NONE;
                } else {
                    endMove(this.orientation , X2, Y2);
                    this.orientation = Orientation.NONE;
                }
                return true;
            default:
                this.orientation = Orientation.NONE;
        }
        return true;
    }



}
