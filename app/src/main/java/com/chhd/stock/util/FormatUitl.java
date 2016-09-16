package com.chhd.stock.util;

import java.text.SimpleDateFormat;

/**
 * Created by CWQ on 2016/7/14.
 */
public class FormatUitl {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    public static String formatTime(long timeMillis) {
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(timeMillis);
    }
}
