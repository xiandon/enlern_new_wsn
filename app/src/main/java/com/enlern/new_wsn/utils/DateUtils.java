package com.enlern.new_wsn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by oc on 2017/6/30.
 */

public class DateUtils {

    public static String stampToDate(String stamp) {
        if (stamp != null) {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
            long lt = new Long(stamp);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        }
        return null;
    }
}
