package com.cmcc.mypicker.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    /**
     * Date类型转为指定格式的String类型
     *
     * @param source
     * @param pattern
     * @return
     */
    public static String DateToString(Date source, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(source);
    }
}
