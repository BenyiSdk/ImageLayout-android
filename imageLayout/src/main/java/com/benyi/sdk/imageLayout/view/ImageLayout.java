package com.benyi.sdk.imageLayout.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.benyi.sdk.imageLayout.listener.ImageLayoutActionListener;
import com.benyi.sdk.imageLayout.listener.ImageLayoutGestureActionListener;
import com.benyi.sdk.imageLayout.util.ViewPropertyObjectAnimator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MuFe on 2018/5/28.
 *
 * @author MuFe
 */

public class ImageLayout<T extends Object> extends RelativeLayout implements ImageLayoutGestureActionListener {
    private Context context;
    /**
     * 响应监听
     */
    private ImageLayoutActionListener actionListener;
    /**
     * 宽
     */
    private int cardWidth;
    private int cardHeight;
    private boolean initLayout;
    private boolean isMoveIng;
    private int firstViewLeft;
    private int firstViewTop;
    private int gestureViewLeft;
    private int gestureViewTop;
    private int bottomViewLeft;
    private int bottomViewTop;
    private AnimatorSet animatorSet;
    private ImageLayoutCardView leftInboxCardView;
    private ImageLayoutCardView showInboxCardView;
    private ImageLayoutCardView bottomInboxCardView;
    private ImageLayoutCardView inboxCardView3;
    private View inboxFullscreenZoomView;
    private ImageLayoutGestureListenerView inboxGestureListenerView;
    public ImageLayout(Context context) {
        this(context, null, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initLayout = false;
        this.isMoveIng = false;
        this.animatorSet = null;
        this.context = context;
    }

    /**
     * 初始化化位置数据
     */
    public void init() {
        this.cardWidth = inboxGestureListenerView.getMeasuredWidth();
        this.cardHeight = inboxGestureListenerView.getMeasuredHeight();
        this.gestureViewLeft = inboxGestureListenerView.getLeft();
        this.gestureViewTop = inboxGestureListenerView.getTop();
        this.firstViewLeft = (this.gestureViewLeft - this.cardWidth);
        this.firstViewTop = this.gestureViewTop;
        this.bottomViewLeft = this.gestureViewLeft;
        this.bottomViewTop = this.gestureViewTop;
    }

    /**
     * 初始化动画执行器
     */
    private void initAnimatorSet() {
        this.animatorSet = new AnimatorSet();
    }

    /**
     * 判断大图是否打开
     *
     * @return 返回状态
     */
    public boolean isFullscreenZoomViewShow() {
        return inboxFullscreenZoomView != null && (inboxFullscreenZoomView.getVisibility() == View.VISIBLE);
    }

    /**
     * 打开大图
     *
     * @param data 资源文件
     */
    public void fullScreenPhoto(T data) {
        if (inboxFullscreenZoomView == null) return;
        actionListener.initfullScreenPhoto(data,inboxFullscreenZoomView);
        inboxFullscreenZoomView.setOnClickListener(new OnClickListener() {
            public void onClick(View paramView) {
                ImageLayout.this.closeFullScreenPhoto();
            }
        });
        detachViewFromParent(inboxFullscreenZoomView);
        attachViewToParent(inboxFullscreenZoomView, -1, inboxFullscreenZoomView.getLayoutParams());
        inboxFullscreenZoomView.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭大图
     */
    public void closeFullScreenPhoto() {
        if (inboxFullscreenZoomView == null) return;
        inboxFullscreenZoomView.setVisibility(View.INVISIBLE);
    }

    /**
     * 判断动画是否被初始化
     *
     * @return 状态
     */
    public boolean isAnimatorSetInit() {
        return this.animatorSet != null;
    }

    /**
     * 在cardView2的位置上插入cardView
     *
     * @param cardView1 插入的CardView
     * @param cardView2 对比的CardView
     */
    private void addCardViewToLayout(ImageLayoutCardView cardView1, ImageLayoutCardView cardView2) {
        if (cardView1 == null || cardView2 == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == cardView2) {
                addView(cardView1, i, cardView1.getLayoutParams());
                return;
            }
        }
    }

    /**
     * 在cardView2的位置的后面上插入cardView
     *
     * @param cardView1 插入的CardView
     * @param cardView2 对比的CardView
     */
    private void addCardViewAfterToLayout(ImageLayoutCardView cardView1, ImageLayoutCardView cardView2) {
        if (cardView1 == null || cardView2 == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == cardView2) {
                addView(cardView1, i + 1, cardView1.getLayoutParams());
                return;
            }
        }
    }


    /**
     * 将CardView添加到Layout中
     *
     * @param cardView cardView
     */
    private void addCardViewToLayout(ImageLayoutCardView cardView) {
        if (cardView == null) {
            return;
        }
        addView(cardView, -1, cardView.getLayoutParams());
    }

    /**
     * 初始化数据
     *
     * @param position 从第几个开始
     */
    public void initFromPosition(int position) {
        init();
        initCardViews(position);
    }

    /**
     * 初始化CardView
     *
     * @param position 从第几个开始
     */
    public void initCardViews(int position) {
        clearCardViews();
        this.bottomInboxCardView = createCardView(position + 1);
        addCardViewToLayout(this.bottomInboxCardView);
        this.showInboxCardView = createCardView(position);
        addCardViewToLayout(this.showInboxCardView);
        this.leftInboxCardView = createCardView(position - 1);
        addCardViewToLayout(this.leftInboxCardView);
    }


    /**
     * 通过ResourceData创建CardView
     *
     * @param data 资源信息
     * @return 返回CardView
     */
    private ImageLayoutCardView createInitCardView(T data) {
        ImageLayoutCardView createCardView = new ImageLayoutCardView(context,data,actionListener);
        createCardView.setLayoutParams(new ViewGroup.LayoutParams(this.cardWidth, this.cardHeight));
        createCardView.init(this.cardWidth, this.cardHeight);
        createCardView.setVisibility(View.INVISIBLE);
        actionListener.addViewToCardView(createCardView,data);
        return createCardView;
    }

    /**
     * 通过position创建CardView
     *
     * @param position 第几个
     * @return 返回CardView
     */
    private ImageLayoutCardView createCardView(int position) {
        T data = getShowResoureData(position);
        if (data == null)
            return null;
        return createInitCardView(data);
    }

    /**
     * 清理所有的CardView
     */
    public void clearCardViews() {
        if (this.showInboxCardView != null) {
            removeCardView(this.showInboxCardView);
        }
        if (this.leftInboxCardView != null) {
            removeCardView(this.leftInboxCardView);
        }
        if (this.bottomInboxCardView != null) {
            removeCardView(this.bottomInboxCardView);
        }
        if (this.inboxCardView3 != null) {
            removeCardView(this.inboxCardView3);
        }
        this.leftInboxCardView = null;
        this.showInboxCardView = null;
        this.bottomInboxCardView = null;
        this.inboxCardView3 = null;
        List<ImageLayoutCardView> list = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ImageLayoutCardView) {
                list.add((ImageLayoutCardView) getChildAt(i));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            removeCardView(list.get(i));
        }
    }

    /**
     * 从页面中移除CardView
     *
     * @param cardView 要移除的CardView
     */
    private void removeCardView(ImageLayoutCardView cardView) {
        if (cardView == null) {
            return;
        }
        cardView.setVisibility(View.INVISIBLE);
        if (cardView.getParent() != null) {
            ((ViewGroup) cardView.getParent()).removeView(cardView);
        }
    }

    /**
     * 开始动画
     */
    public void startAnimator() {
        this.actionListener.animationStateChange(true);
        this.animatorSet.start();
    }


    /**
     * 添加动画
     *
     * @param animator 动画
     */
    public void addAnimator(ObjectAnimator animator) {
        if (animator != null) {
            this.animatorSet.play(animator);
        }
    }

    /**
     * 添加动画结束监听
     *
     * @param listener 监听事件
     */
    public void addAnimatorListener(final AnimatorEndListener listener) {
        this.animatorSet.addListener(new InboxAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.animatorSet = null;
                ImageLayout.this.actionListener.animationStateChange(false);
                listener.isFinish(finish);
            }
        }));
    }

    /**
     * 初始化屏幕左方的CardView的信息
     *
     * @param cardView 初始化的View
     */
    private void initLeftCardView(ImageLayoutCardView cardView) {
        cardView.layout(this.firstViewLeft, this.firstViewTop, this.firstViewLeft + this.cardWidth, this.firstViewTop + this.cardHeight);
        cardView.setX(this.firstViewLeft);
        cardView.setY(this.firstViewTop);
        cardView.setAlpha(1.0F);
        cardView.setVisibility(View.VISIBLE);
        cardView.start(this.firstViewLeft, this.firstViewTop);
        cardView.init();
    }

    /**
     * 初始化屏幕中间CardView的信息
     *
     * @param cardView 初始化的View
     */
    private void initShowCardView(ImageLayoutCardView cardView) {
        cardView.layout(this.gestureViewLeft, this.gestureViewTop, this.gestureViewLeft + this.cardWidth, this.gestureViewTop + this.cardHeight);
        cardView.setX(this.gestureViewLeft);
        cardView.setY(this.gestureViewTop);
        cardView.setAlpha(1.0F);
        cardView.setVisibility(View.VISIBLE);
        cardView.start(this.gestureViewLeft, this.gestureViewTop);
        cardView.init();
    }

    /**
     * 初始化底部CardView的信息
     *
     * @param cardView 初始化的View
     */
    private void initBottomCardView(ImageLayoutCardView cardView) {
        cardView.layout(this.bottomViewLeft, this.bottomViewTop, this.bottomViewLeft + this.cardWidth, this.bottomViewTop + this.cardHeight);
        cardView.setX(this.bottomViewLeft);
        cardView.setY(this.bottomViewTop);
        cardView.setAlpha(0.0F);
        cardView.setScaleX(0.8F);
        cardView.setScaleY(0.8F);
        cardView.setVisibility(View.VISIBLE);
        cardView.start(this.bottomViewLeft, this.bottomViewTop);
        cardView.init();
    }

    /**
     * 初始化从顶部出现的CardView的信息
     *
     * @param cardView 出现的CardView
     * @param rect     出现的CardView的区域
     */
    private void initTopShowCardView(ImageLayoutCardView cardView, Rect rect) {
        float f1 = (float) rect.width() / (float) cardView.getLayoutParams().width;
        float f2 = (float) rect.height() / (float) cardView.getLayoutParams().height;
        cardView.setX(rect.left - cardView.getLayoutParams().width * (1.0F - f1) / 2.0F);
        cardView.setY(rect.top - cardView.getLayoutParams().height * (1.0F - f2) / 2.0F);
        cardView.setScaleX(f1);
        cardView.setScaleY(f2);
        cardView.setRotation(10.0F);
        cardView.setAlpha(0.0F);
        cardView.setVisibility(View.VISIBLE);
        cardView.init();
    }

    /**
     * 初始化从底部出现的CardView的信息
     *
     * @param cardView 出现的CardView
     * @param rect     出现的CardView的区域
     */
    private void initBottomShowCardView(ImageLayoutCardView cardView, Rect rect) {
        float f1 = (float) rect.width() / (float) cardView.getLayoutParams().width;
        float f2 = (float) rect.height() / (float) cardView.getLayoutParams().height;
        cardView.setX(rect.left - cardView.getLayoutParams().width * (1.0F - f1) / 2.0F);
        cardView.setY(rect.top - cardView.getLayoutParams().height * (1.0F - f2) / 2.0F);
        cardView.setScaleX(f1);
        cardView.setScaleY(f2);
        cardView.setAlpha(0.2F);
        cardView.setVisibility(View.VISIBLE);
        cardView.init();
    }


    /**
     * 创建从左边出现的动画
     *
     * @param cardView 动画执行的View
     * @return 返回动画
     */
    public ObjectAnimator createLeftShowAnimator(ImageLayoutCardView cardView) {
        if (cardView == null) {
            return null;
        }
        return ViewPropertyObjectAnimator.getInstance(cardView)
                .setDuration(100)
                .setXProperty(gestureViewLeft)
                .setYProperty(gestureViewTop)
                .setAlphaProperty(1)
                .setScaleXProperty(1)
                .setScaleYProperty(1)
                .setRotationProperty(0)
                .getViewAnimator();
    }

    /**
     * 创建从上面出现的动画
     *
     * @param cardView 动画执行的View
     * @return 返回动画
     */
    public ObjectAnimator createTopShowAnimator(ImageLayoutCardView cardView) {
        if (cardView == null) {
            return null;
        }
        return ViewPropertyObjectAnimator.getInstance(cardView)
                .setDuration(200)
                .setXProperty(gestureViewLeft)
                .setYProperty(gestureViewTop)
                .setAlphaProperty(1)
                .setScaleXProperty(1)
                .setScaleYProperty(1)
                .setRotationProperty(0)
                .getViewAnimator();
    }

    /**
     * 创建从左边消失的动画
     *
     * @param cardView 动画执行的View
     * @return 返回动画
     */
    public ObjectAnimator createLeftHideAnimator(ImageLayoutCardView cardView) {
        if (cardView == null)
            return null;
        return ViewPropertyObjectAnimator.getInstance(cardView)
                .setDuration(100)
                .setXProperty(firstViewLeft)
                .setYProperty(firstViewTop)
                .setAlphaProperty(1)
                .setScaleXProperty(1)
                .setScaleYProperty(1)
                .setRotationProperty(0)
                .getViewAnimator();
    }

    /**
     * 创建从上面消失的动画
     *
     * @param cardView 动画执行的View
     * @param rect     消失的区域
     * @return 返回动画
     */
    public ObjectAnimator createTopHideAnimator(ImageLayoutCardView cardView, Rect rect) {
        if (cardView == null) {
            return null;
        }
        float f3 = (float) rect.width() / (float) cardView.getWidth();
        float f4 = (float) rect.height() / (float) cardView.getHeight();
        return ViewPropertyObjectAnimator.getInstance(cardView)
                .setDuration(150L)
                .setXProperty(rect.left - cardView.getWidth() * (1.0F - f3) / 2.0F)
                .setYProperty(rect.top - cardView.getHeight() * (1.0F - f4) / 2.0F)
                .setScaleXProperty(f3)
                .setScaleYProperty(f4)
                .setAlphaProperty(1.0f)
                .setRotationProperty(10.0f)
                .getViewAnimator();
    }


    /**
     * 创建从底部消失的动画
     *
     * @param cardView 动画执行的View
     * @param rect     消失的区域
     * @return 返回动画
     */
    public ObjectAnimator createBottomHideAnimator(ImageLayoutCardView cardView, Rect rect) {
        if (cardView == null) {
            return null;
        }
        float f1 = (float) rect.width() / (float) cardView.getWidth();
        float f2 = (float) rect.height() / (float) cardView.getHeight();
        return ViewPropertyObjectAnimator.getInstance(cardView)
                .setDuration(200)
                .setXProperty(rect.left - cardView.getWidth() * (1.0F - f1) / 2.0F)
                .setYProperty(rect.top - cardView.getHeight() * (1.0F - f2) / 2.0F)
                .setScaleXProperty(f1)
                .setScaleYProperty(f2)
                .setAlphaProperty(0.2f)
                .setRotationProperty(0)
                .getViewAnimator();
    }


    /**
     * 创建向后缩小消失动画
     *
     * @param cardView 使用该动画的view
     * @return 返回动画
     */
    public ObjectAnimator createBackwardHideAnimator(ImageLayoutCardView cardView) {
        if (cardView == null) {
            return null;
        }
        return ViewPropertyObjectAnimator.getInstance(cardView)
                .setDuration(100)
                .setXProperty(bottomViewLeft)
                .setYProperty(bottomViewTop)
                .setAlphaProperty(0)
                .setScaleXProperty(0.8f)
                .setScaleYProperty(0.8f)
                .setRotationProperty(0)
                .getViewAnimator();
    }


    /**
     * 向下滑动
     *
     * @param name 文件夹
     */
    public void bottomPhotoToFloder(final String name) {
        this.inboxCardView3 = this.showInboxCardView;
        initAnimatorSet();
        addAnimator(createLeftShowAnimator(this.bottomInboxCardView));
        addAnimator(createBottomHideAnimator(this.showInboxCardView, actionListener.getBottomHideRect()));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.showInboxCardView = ImageLayout.this.bottomInboxCardView;
                ImageLayout.this.bottomInboxCardView = createCardView(actionListener.getShowIndex() + 1);
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.bottomAction(name);
            }
        });
        startAnimator();
    }

    /**
     * 恢复向下滑动
     *
     * @param index       资源文件index
     */
    public void undoBottom(int index) {
        final ImageLayoutCardView inboxCardView = createCardView(index);
        if (this.showInboxCardView != null) {
            addCardViewAfterToLayout(inboxCardView, this.showInboxCardView);
        } else if (this.leftInboxCardView != null) {
            addCardViewToLayout(inboxCardView, this.leftInboxCardView);
        } else {
            addCardViewToLayout(inboxCardView);
        }
        initBottomShowCardView(inboxCardView, actionListener.getBottomHideRect());
        removeCardView(this.bottomInboxCardView);
        this.bottomInboxCardView = null;
        initAnimatorSet();
        addAnimator(createBackwardHideAnimator(this.showInboxCardView));
        addAnimator(createLeftShowAnimator(inboxCardView));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.bottomInboxCardView = ImageLayout.this.showInboxCardView;
                ImageLayout.this.showInboxCardView = inboxCardView;
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.undoAction();
            }
        });
        startAnimator();
    }


    /**
     * 恢复向上滑动
     *
     * @param index 资源文件Index
     */
    public void undoTop(int index) {
        this.inboxCardView3 = this.bottomInboxCardView;
        this.bottomInboxCardView = null;
        final ImageLayoutCardView cardView = createCardView(index);
        if (this.showInboxCardView != null) {
            addCardViewAfterToLayout(cardView, this.showInboxCardView);
        } else if (this.leftInboxCardView != null) {
            addCardViewToLayout(cardView, this.leftInboxCardView);
        } else {
            addCardViewToLayout(cardView);
        }
        initTopShowCardView(cardView, actionListener.getTopHideRect());
        initAnimatorSet();
        addAnimator(createBackwardHideAnimator(this.showInboxCardView));
        addAnimator(createTopShowAnimator(cardView));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.bottomInboxCardView = ImageLayout.this.showInboxCardView;
                ImageLayout.this.showInboxCardView = cardView;
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.undoAction();
            }
        });
        startAnimator();
    }

    /**
     * 恢复跳转position
     *
     * @param position position
     */
    public void undoSelect(int position) {
        initCardViews(position);
        this.actionListener.undoSelectAction();
    }


    /**
     * 恢复右划
     */
    public void undoRight() {
        ImageLayoutCardView cardView1 = this.bottomInboxCardView;
        ImageLayoutCardView cardView2 = this.showInboxCardView;
        this.inboxCardView3 = this.leftInboxCardView;
        this.leftInboxCardView = cardView2;
        this.showInboxCardView = cardView1;
        this.bottomInboxCardView = null;
        initAnimatorSet();
        addAnimator(createLeftHideAnimator(cardView2));
        addAnimator(createLeftShowAnimator(cardView1));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.bottomInboxCardView = createCardView(actionListener.getShowIndex() + 1);
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.undoAction();
            }
        });
        startAnimator();
    }

    /**
     * 恢复左划
     */
    public void undoLeft() {
        ImageLayoutCardView cardView1 = this.leftInboxCardView;
        ImageLayoutCardView cardView2 = this.showInboxCardView;
        this.inboxCardView3 = this.bottomInboxCardView;
        this.showInboxCardView = cardView1;
        this.bottomInboxCardView = cardView2;
        this.leftInboxCardView = null;
        initAnimatorSet();
        addAnimator(createBackwardHideAnimator(cardView2));
        addAnimator(createLeftShowAnimator(cardView1));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.leftInboxCardView = createCardView(actionListener.getShowIndex() - 1);
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.undoAction();
            }
        });
        startAnimator();
    }

    /**
     * 还原移动
     */
    public void restore() {
        initAnimatorSet();
        addAnimator(createLeftHideAnimator(this.leftInboxCardView));
        addAnimator(createLeftShowAnimator(this.showInboxCardView));
        addAnimator(createBackwardHideAnimator(this.bottomInboxCardView));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.requestLayout();
            }
        });
        startAnimator();
    }

    /**
     * 向下滑动
     */
    public void bottomAction() {
        initAnimatorSet();
        addAnimator(createLeftHideAnimator(this.leftInboxCardView));
        addAnimator(createLeftShowAnimator(this.showInboxCardView));
        addAnimator(createBackwardHideAnimator(this.bottomInboxCardView));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.actionListener.bottomAction();
            }
        });
        startAnimator();
    }

    /**
     * 向上滑动
     */
    public void topAction() {
        ImageLayoutCardView cardView1 = this.showInboxCardView;
        final ImageLayoutCardView cardView2 = this.bottomInboxCardView;
        this.bottomInboxCardView = null;
        this.inboxCardView3 = cardView1;
        initAnimatorSet();
        addAnimator(createTopHideAnimator(cardView1, actionListener.getTopHideRect()));
        addAnimator(createLeftShowAnimator(cardView2));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.showInboxCardView = cardView2;
                ImageLayout.this.bottomInboxCardView = createCardView(2 + actionListener.getShowIndex());
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.topAction();
            }
        });
        startAnimator();
    }

    /**
     * 向右滑动
     */
    public void rightAction() {
        ImageLayoutCardView cardView1 = this.leftInboxCardView;
        ImageLayoutCardView cardView2 = this.showInboxCardView;
        this.inboxCardView3 = this.bottomInboxCardView;
        this.showInboxCardView = cardView1;
        this.bottomInboxCardView = cardView2;
        this.leftInboxCardView = null;
        initAnimatorSet();
        addAnimator(createBackwardHideAnimator(cardView2));
        addAnimator(createLeftShowAnimator(cardView1));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.leftInboxCardView = createCardView(actionListener.getShowIndex() - 2);
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.prevAction();
            }
        });
        startAnimator();
    }

    /**
     * 向左滑动
     */
    public void leftAction() {
        ImageLayoutCardView cardView1 = this.bottomInboxCardView;
        ImageLayoutCardView cardView2 = this.showInboxCardView;
        this.inboxCardView3 = this.leftInboxCardView;
        this.leftInboxCardView = cardView2;
        this.showInboxCardView = cardView1;
        this.bottomInboxCardView = null;
        initAnimatorSet();
        addAnimator(createLeftShowAnimator(cardView1));
        addAnimator(createLeftHideAnimator(cardView2));
        addAnimatorListener(new AnimatorEndListener() {
            @Override
            public void isFinish(boolean finish) {
                ImageLayout.this.bottomInboxCardView = createCardView(actionListener.getShowIndex() + 2);
                ImageLayout.this.requestLayout();
                ImageLayout.this.actionListener.nextAction();
            }
        });
        startAnimator();
    }


    @Override
    public void moveLeft(float x, float xPproportion) {
        if (this.showInboxCardView != null) {
            this.showInboxCardView.moveToLeft(x, xPproportion);
        }
        if (this.bottomInboxCardView != null) {
            this.bottomInboxCardView.showNext(x, xPproportion);
        }
    }

    @Override
    public void moveTop(float x, float y, float yPproportion) {
        if (this.showInboxCardView != null) {
            this.showInboxCardView.moveToTop(x, y, yPproportion);
        }
        if (this.bottomInboxCardView != null) {
            this.bottomInboxCardView.showNext(x, yPproportion);
        }
    }


    @Override
    public void moveRight(float x, float xPproportion) {
        if (this.leftInboxCardView == null) {
            if (this.showInboxCardView != null) {
                this.showInboxCardView.firstPhotoMoveToRight(x, xPproportion);
            }
        } else {
            this.leftInboxCardView.moveToRight(x, xPproportion);
            if (this.showInboxCardView != null) {
                this.showInboxCardView.hideTop(x, xPproportion);
            }
        }
    }

    @Override
    public void moveBottom(float x, float y, float fraction) {
        if (this.showInboxCardView != null) {
            this.showInboxCardView.moveToBottom(x, y, fraction);
        }
        if (this.bottomInboxCardView != null) {
            this.bottomInboxCardView.showNext(x, fraction);
        }
    }


    @Override
    public void leftEnd(boolean change) {
        if (!change) {
            restore();
            return;
        }
        if (this.showInboxCardView == null) {
            restore();
            return;
        }
        leftAction();
    }

    @Override
    public void ringtEnd(boolean change) {
        if (!change) {
            restore();
            return;
        }
        if (this.leftInboxCardView == null) {
            restore();
            return;
        }
        rightAction();
    }

    @Override
    public void topEnd(boolean change) {
        if (this.showInboxCardView == null) {
            return;
        }
        if (!change) {
            restore();
            return;
        }
        topAction();
    }

    @Override
    public void downEnd(boolean change) {
        if (this.showInboxCardView == null) {
            return;
        }
        bottomAction();
    }

    @Override
    public void startMove(ImageLayoutGestureListenerView.Orientation e) {
        this.isMoveIng = true;
    }

    @Override
    public void endMove() {
        this.isMoveIng = false;
    }

    @Override
    public void singeClick() {
        restore();
        if (this.showInboxCardView == null || this.showInboxCardView.getResourceData() == null) {
            return;
        }
        fullScreenPhoto((T)this.showInboxCardView.getResourceData());
    }

    interface AnimatorEndListener {
        void isFinish(boolean finish);
    }


    class InboxAnimatorListener implements Animator.AnimatorListener {
        private AnimatorEndListener listener;

        InboxAnimatorListener(AnimatorEndListener listener) {
            this.listener = listener;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (this.listener != null) {
                this.listener.isFinish(true);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (this.listener != null) {
                this.listener.isFinish(false);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    public void finishLayout() {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.initLayout = true;
        if (this.cardWidth == 0) {
            init();
        }
        if (this.bottomInboxCardView != null) {
            removeViewInLayout(this.bottomInboxCardView);
            addViewInLayout(this.bottomInboxCardView, -1, this.bottomInboxCardView.getLayoutParams());
            initBottomCardView(this.bottomInboxCardView);
        }
        if (this.showInboxCardView != null) {
            removeViewInLayout(this.showInboxCardView);
            addViewInLayout(this.showInboxCardView, -1, this.showInboxCardView.getLayoutParams());
            initShowCardView(this.showInboxCardView);
        }
        if (this.leftInboxCardView != null) {
            removeViewInLayout(this.leftInboxCardView);
            addViewInLayout(this.leftInboxCardView, -1, this.leftInboxCardView.getLayoutParams());
            initLeftCardView(this.leftInboxCardView);
        }
        if (this.inboxCardView3 != null) {
            removeViewInLayout(this.inboxCardView3);
            initBottomCardView(inboxCardView3);
        }
        if (inboxFullscreenZoomView != null) {
            removeViewInLayout(inboxFullscreenZoomView);
            addViewInLayout(inboxFullscreenZoomView, -1, inboxFullscreenZoomView.getLayoutParams());
        }
        finishLayout();
        this.initLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!this.isMoveIng && !isAnimatorSetInit() && !this.initLayout) {
            super.requestLayout();
        }
    }

    /**
     * 设置响应事件
     *
     * @param listener 监听
     */
    public void setInboxActionListener(ImageLayoutActionListener listener) {
        this.actionListener = listener;
    }

    /**
     * 设置手势view
     *
     * @param inboxGestureListenerView view
     */
    public void setInboxGestureListenerView(ImageLayoutGestureListenerView inboxGestureListenerView) {
        this.inboxGestureListenerView = inboxGestureListenerView;
    }

    /**
     * 设置查看大图的View
     *
     * @param inboxFullscreenZoomView view
     */
    public void setInboxFullscreenZoomView(View inboxFullscreenZoomView) {
        this.inboxFullscreenZoomView = inboxFullscreenZoomView;
    }

    /**
     * 获取当前显示的图片资源
     *
     * @param position 当前index
     * @return 返回图片资源
     */
    public T getShowResoureData(int position) {
        if (position < 0) {
            return null;
        }
        return (T)actionListener.getShowResourceData(position);
    }

    /**
     * 刷新当前View
     */
    public void refreshShowCardView(){
        if(showInboxCardView==null){
            return;
        }
        showInboxCardView.refresh();
        actionListener.addViewToCardView(showInboxCardView,showInboxCardView.getResourceData());
    }

}
