package com.benyi.sdk.imageLayout.util;

import java.util.Date;

/**
 * Created by MuFe on 2018/5/30.
 */

public class StringUtil {
    public static String dateToString(Date date){
        if(date==null){
            return null;
        }
        return date.getTime()+"";
    }
    public static String dateToString(){
        return dateToString(new Date());
    }
}
