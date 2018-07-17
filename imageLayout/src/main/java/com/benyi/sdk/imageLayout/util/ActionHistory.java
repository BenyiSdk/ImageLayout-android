package com.benyi.sdk.imageLayout.util;

import com.benyi.sdk.imageLayout.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MuFe on 2018/5/31.
 * 响应操作记录
 * @author MuFe
 */
public class ActionHistory<T> {
    private final List<Action> list=new ArrayList<>();
    public void addNextAction(String time,String name){
        Action action=Action.leftAction(time,name);
        list.add(action);
    }

    public void addPrevAction(String time,String name){
        Action action=Action.rightAction(time,name);
        list.add(action);
    }

    public void addTopAction(String time,String name,T data){
        Action action=Action.topAction(time,name);
        action.data=data;
        list.add(action);
    }

    public void addBottomAction(String time,String name,T data){
        Action action=Action.bottomAction(time,name);
        action.data=data;
        list.add(action);
    }

    /**
     * 推出最后一次操作
     * @return 操作记录
     */
    public Action pop(){
        if(list.isEmpty()){
            return null;
        }
        Action action=list.get(list.size()-1);
        list.remove(action);
        return action;
    }


    /**
     *
     * @return 获取全部数据
     */
    public List<Action> getActions(){
        return list;
    }

    /**
     * 获取最后一次操作记录
     * @return 操作记录
     */
    public Action getLast(){
        if(list.isEmpty()){
            return null;
        }
        return list.get(list.size()-1);
    }
    /**
     * 加载历史数据
     * @param lists 历史数据
     */
    public void loadActions(List<Action> lists){
        list.addAll(lists);
    }
}
