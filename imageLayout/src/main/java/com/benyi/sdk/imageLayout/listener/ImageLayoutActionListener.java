package com.benyi.sdk.imageLayout.listener;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MuFe on 2018/5/30.
 * @author MuFe
 */
public interface ImageLayoutActionListener<T>{
    /**
     * 恢复响应事件
     */
    void undoAction();

    /**
     * 动画状态改变
     * @param isStart 开始改变
     */
    void animationStateChange(boolean isStart);

    /**
     * 下一个响应
     */
    void nextAction();

    /**
     * 上一个响应
     */
    void prevAction();

    /**
     * 恢复上一个选择
     */
    void undoSelectAction();

    /**
     * 向上滑动响应
     */
    void topAction();

    /**
     * 向下滑动响应
     */
    String getBottomActionName();

    /**
     * 向下滑动响应
     * @param name 文件夹名称
     */
    void bottomAction(String name);

    /**
     * CardView从外部加载资源
     * @param data  资源文件
     * @param view  加载的CardView
     */
    void loadResourceToCardView(T data,View view);

    /**
     * 初始化全屏View资源
     * @param data 源文件
     * @param view 全屏的View
     */
    void singleClick(T data,View view);

    /**
     * 向CardView中添加View
     * @param cardView CardView
     * @param data 资源文件
     */
    void addViewToCardView(ViewGroup cardView,T data);

    /**
     * 获取正在展示的Index
     * @return 返回index
     */
    int getShowIndex();

    /**
     * 获取正在展示的资源
     * @param position index
     * @return 返回资源文件
     */
    T getShowResourceData(int position);

    /**
     * 获取向上滑动CardView消失区域
     * @return 返回区域
     */
    Rect getTopHideRect();

    /**
     * 获取向下滑动CardView消失区域
     * @return 返回区域
     */
    Rect getBottomHideRect();
}
