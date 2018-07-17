package com.benyi.sdk.imageLayout.model;



/**
 * Created by MuFe on 2018/5/30.
 * 响应Model
 * @author MuFe
 */

public class Action<T> {
    private static final String LEFT_ACITON ="LEFT";
    private static final String SELECT_ACITON ="SELECT";
    private static final String TOP_ACITON ="TOP";
    private static final String RIGHT_ACITON ="RIGHT";
    private static final String BOTTOM_ACITON ="BOTTOM";
    public String action;
    public String time;
    public T data;
    public String name;
    public int oldIndex;
    public int newIndex;
    public static Action leftAction(String str,String name){
        Action action=new Action();
        action.action = LEFT_ACITON;
        action.time =str;
        action.name=name;
        return action;
    }
    public static Action selectAction(String str,String name,int oldIndex,int newIndex){
        Action action=new Action();
        action.action = SELECT_ACITON;
        action.time =str;
        action.name=name;
        action.oldIndex =oldIndex;
        action.newIndex =newIndex;
        return action;
    }

    public static Action topAction(String str,String name){
        Action action=new Action();
        action.action = TOP_ACITON;
        action.time =str;
        action.name=name;
        return action;
    }

    public static Action bottomAction(String str,String name){
        Action action=new Action();
        action.action = BOTTOM_ACITON;
        action.time =str;
        action.name =name;
        return action;
    }

    public static Action rightAction(String str,String name){
        Action action=new Action();
        action.action = RIGHT_ACITON;
        action.time =str;
        action.name =name;
        return action;
    }

    public boolean isLeftAction(){
        return this.action.equals(LEFT_ACITON);
    }

    public boolean isRightAction(){
        return this.action.equals(RIGHT_ACITON);
    }
    public boolean isTopAction(){
        return this.action.equals(TOP_ACITON);
    }
    public boolean isSelectAction(){
        return this.action.equals(SELECT_ACITON);
    }
    public boolean isBottomAction(){
        return this.action.equals(BOTTOM_ACITON);
    }

}
